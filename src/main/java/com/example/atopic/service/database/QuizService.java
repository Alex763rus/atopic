package com.example.atopic.service.database;

import com.example.atopic.enums.QuizExportStatus;
import com.example.atopic.enums.quiz.Answer;
import com.example.atopic.model.jpa.Quiz;
import com.example.atopic.model.jpa.QuizKey;
import com.example.atopic.model.jpa.QuizRepository;
import com.example.atopic.model.jpa.User;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.atopic.enums.QuizExportStatus.NEW_QUEST;

@Slf4j
@Service
public class QuizService {

    private HashMap<User, HashSet<Quiz>> allQuests = new HashMap<>();

    @Autowired
    private QuizRepository quizRepository;

    @PostConstruct
    public void init() {
        reloadQuests();
    }


    public List<Integer> getDays(User user, int year, int month) {
        val quests = allQuests.get(user);
        if (quests == null) {
            return new ArrayList<>();
        }
        return quests.stream()
                .filter(quiz -> quiz.getQuizKey().getCalendarYear() == year
                        && quiz.getQuizKey().getCalendarMonth() == month)
                .mapToInt(e -> e.getQuizKey().getCalendarDay())
                .boxed().collect(Collectors.toList());
    }

    public Quiz get(User user, Calendar calendar) {
        val quests = allQuests.get(user);
        if (quests == null) {
            return null;
        }
        val key = new QuizKey();
        key.setUser(user);
        key.setCalendarYear(calendar.get(Calendar.YEAR));
        key.setCalendarMonth(calendar.get(Calendar.MONTH));
        key.setCalendarDay(calendar.get(Calendar.DAY_OF_MONTH));
        return quests.stream().filter(quiz -> quiz.getQuizKey().equals(key))
                .findAny()
                .orElse(null);
    }

    public void reloadQuests() {
        allQuests = new HashMap<>();
        val quests = quizRepository.findAll();
        for (Quiz quiz : quests) {
            val user = quiz.getQuizKey().getUser();
            val userQuests = allQuests.getOrDefault(user, new HashSet<>());
            userQuests.add(quiz);
            allQuests.put(user, userQuests);
        }
    }

    public void save(User user, Calendar calendar, ArrayList<Answer> answers) {
        val quests = allQuests.getOrDefault(user, new HashSet<>());

        val newQuest = new Quiz();
        val questKey = new QuizKey();
        questKey.setUser(user);
        questKey.setCalendarYear(calendar.get(Calendar.YEAR));
        questKey.setCalendarMonth(calendar.get(Calendar.MONTH));
        questKey.setCalendarDay(calendar.get(Calendar.DAY_OF_MONTH));
        newQuest.setQuizKey(questKey);
        newQuest.setQuizExportStatus(NEW_QUEST);
        newQuest.setAnswer1(answers.get(0));
        newQuest.setAnswer2(answers.get(1));
        newQuest.setAnswer3(answers.get(2));
        newQuest.setAnswer4(answers.get(3));
        newQuest.setAnswer5(answers.get(4));
        newQuest.setAnswer6(answers.get(5));
        newQuest.setAnswer7(answers.get(6));
        newQuest.setAnswer8(answers.get(7));
        newQuest.setAnswer9(answers.get(8));
        newQuest.setAnswer10(answers.get(9));
        newQuest.setAnswer11(answers.get(10));
        newQuest.setQuizDate(new Timestamp(System.currentTimeMillis()));

        quizRepository.save(newQuest);
        quests.remove(newQuest);
        quests.add(newQuest);
        allQuests.put(user, quests);
    }

    public List<Quiz> getAll() {
        return quizRepository.findAll();
    }

    public List<Quiz> getAll(QuizExportStatus quizExportStatus) {
        return quizRepository.findQuestByQuizExportStatus(quizExportStatus);
    }

    public void save(List<Quiz> quizzes) {
        quizRepository.saveAll(quizzes);
    }
}
