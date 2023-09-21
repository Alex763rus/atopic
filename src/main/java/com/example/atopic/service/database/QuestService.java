package com.example.atopic.service.database;

import com.example.atopic.enums.QuestExportStatus;
import com.example.atopic.enums.quest.Answer;
import com.example.atopic.model.jpa.Quest;
import com.example.atopic.model.jpa.QuestKey;
import com.example.atopic.model.jpa.QuestRepository;
import com.example.atopic.model.jpa.User;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.atopic.enums.QuestExportStatus.NEW_QUEST;

@Slf4j
@Service
public class QuestService {

    private HashMap<User, HashSet<Quest>> allQuests = new HashMap<>();

    @Autowired
    private QuestRepository questRepository;

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
                .filter(quest -> quest.getQuestKey().getCalendarYear() == year
                        && quest.getQuestKey().getCalendarMonth() == month)
                .mapToInt(e -> e.getQuestKey().getCalendarDay())
                .boxed().collect(Collectors.toList());
    }

    public Quest get(User user, Calendar calendar) {
        val quests = allQuests.get(user);
        if (quests == null) {
            return null;
        }
        val key = new QuestKey();
        key.setUser(user);
        key.setCalendarYear(calendar.get(Calendar.YEAR));
        key.setCalendarMonth(calendar.get(Calendar.MONTH));
        key.setCalendarDay(calendar.get(Calendar.DAY_OF_MONTH));
        return quests.stream().filter(quest -> quest.getQuestKey().equals(key))
                .findAny()
                .orElse(null);
    }

    public void reloadQuests() {
        allQuests = new HashMap<>();
        val quests = questRepository.findAll();
        for (Quest quest : quests) {
            val user = quest.getQuestKey().getUser();
            val userQuests = allQuests.getOrDefault(user, new HashSet<>());
            userQuests.add(quest);
            allQuests.put(user, userQuests);
        }
    }

    public void save(User user, Calendar calendar, ArrayList<Answer> answers) {
        val quests = allQuests.getOrDefault(user, new HashSet<>());

        val newQuest = new Quest();
        val questKey = new QuestKey();
        questKey.setUser(user);
        questKey.setCalendarYear(calendar.get(Calendar.YEAR));
        questKey.setCalendarMonth(calendar.get(Calendar.MONTH));
        questKey.setCalendarDay(calendar.get(Calendar.DAY_OF_MONTH));
        newQuest.setQuestKey(questKey);
        newQuest.setQuestExportStatus(NEW_QUEST);
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
        newQuest.setQuestDate(new Timestamp(System.currentTimeMillis()));

        questRepository.save(newQuest);
        quests.remove(newQuest);
        quests.add(newQuest);
        allQuests.put(user, quests);
    }

    public List<Quest> getAll() {
        return questRepository.findAll();
    }

    public List<Quest> getAll(QuestExportStatus questExportStatus) {
        return questRepository.findQuestByQuestExportStatus(questExportStatus);
    }

    public void save(List<Quest> quests) {
        questRepository.saveAll(quests);
    }
}
