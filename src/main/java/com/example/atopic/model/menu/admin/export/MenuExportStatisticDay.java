package com.example.atopic.model.menu.admin.export;

import com.example.atopic.enums.quiz.Answer;
import com.example.atopic.enums.quiz.Question;
import com.example.atopic.model.jpa.Quiz;
import com.example.atopic.model.jpa.User;
import com.example.atopic.model.menu.base.Menu;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.tgcommons.model.wrapper.SendDocumentWrap;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.atopic.constant.Constant.Command.COMMAND_EXPORT_STATISTIC_DAY;
import static com.example.atopic.enums.quiz.Question.*;
import static java.util.stream.Collectors.groupingBy;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;

@Component(COMMAND_EXPORT_STATISTIC_DAY)
@Slf4j
public class MenuExportStatisticDay extends Menu {

    @Override
    public String getMenuComand() {
        return COMMAND_EXPORT_STATISTIC_DAY;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        try {
            return switch (stateService.getState(user)) {
                case FREE -> exportStatisticDayToExcel(user, update);
                default -> createErrorDefaultMessage(user);
            };
        } catch (Exception ex) {
            log.error(ex.toString());
            return createMessageList(user, ex.toString());
        }
    }

    private QuizStatistic prepareQuizStatistic(Quiz quiz) {
        val quizStatistic = new QuizStatistic();
        quizStatistic.setQuizKey(quiz.getQuizKey());
        quizStatistic.setAnswer1(quiz.getAnswer1());
        quizStatistic.setAnswer2(quiz.getAnswer2());
        quizStatistic.setAnswer3(quiz.getAnswer3());
        quizStatistic.setAnswer4(quiz.getAnswer4());
        quizStatistic.setAnswer5(quiz.getAnswer5());
        quizStatistic.setAnswer6(quiz.getAnswer6());
        quizStatistic.setAnswer7(quiz.getAnswer7());
        quizStatistic.setAnswer8(quiz.getAnswer8());
        quizStatistic.setAnswer9(quiz.getAnswer9());
        quizStatistic.setAnswer10(quiz.getAnswer10());
        quizStatistic.setAnswer11(quiz.getAnswer11());
        return quizStatistic;
    }

    private List<String> prepareQuestionHeader(Question question) {
        val result = new ArrayList();
        result.add(question.getTitle());
        val answerSize = quizButtonService.getAnswers(question).size() - 1;
        for (int i = 0; i < answerSize; ++i) {
            result.add(EMPTY/*"ПУСТО"*/);
        }
        return result;
    }

    private List<String> prepareAnswerHeader(Question question) {
        return quizButtonService.getAnswers(question).stream()
                .map(answer -> answer.getTitle())
                .collect(Collectors.toList());
    }

    private List<String> prepareAnswerData(String data, Question question, Map<String, Map<Answer, Long>> statistic) {
        val result = new ArrayList();
        for (Answer answer : quizButtonService.getAnswers(question)) {
            result.add(String.valueOf(statistic.get(data).getOrDefault(answer, 0L)));
        }
        return result;
    }


    private List<PartialBotApiMethod> exportStatisticDayToExcel(User user, Update update) {
        val questList = quizService.getAll();
        val quizStatistic = new ArrayList<QuizStatistic>();
        questList.stream().
                forEach(quiz -> quizStatistic.add(prepareQuizStatistic(quiz)));

        val statisticAnswer1 = quizStatistic.stream().collect(groupingBy(QuizStatistic::statisticKey, groupingBy(QuizStatistic::getAnswer1, Collectors.counting())));
        val statisticAnswer2 = quizStatistic.stream().collect(groupingBy(QuizStatistic::statisticKey, groupingBy(QuizStatistic::getAnswer2, Collectors.counting())));
        val statisticAnswer3 = quizStatistic.stream().collect(groupingBy(QuizStatistic::statisticKey, groupingBy(QuizStatistic::getAnswer3, Collectors.counting())));
        val statisticAnswer4 = quizStatistic.stream().collect(groupingBy(QuizStatistic::statisticKey, groupingBy(QuizStatistic::getAnswer4, Collectors.counting())));
        val statisticAnswer5 = quizStatistic.stream().collect(groupingBy(QuizStatistic::statisticKey, groupingBy(QuizStatistic::getAnswer5, Collectors.counting())));
        val statisticAnswer6 = quizStatistic.stream().collect(groupingBy(QuizStatistic::statisticKey, groupingBy(QuizStatistic::getAnswer6, Collectors.counting())));
        val statisticAnswer7 = quizStatistic.stream().collect(groupingBy(QuizStatistic::statisticKey, groupingBy(QuizStatistic::getAnswer7, Collectors.counting())));
        val statisticAnswer8 = quizStatistic.stream().collect(groupingBy(QuizStatistic::statisticKey, groupingBy(QuizStatistic::getAnswer8, Collectors.counting())));
        val statisticAnswer9 = quizStatistic.stream().collect(groupingBy(QuizStatistic::statisticKey, groupingBy(QuizStatistic::getAnswer9, Collectors.counting())));
        val statisticAnswer10 = quizStatistic.stream().collect(groupingBy(QuizStatistic::statisticKey, groupingBy(QuizStatistic::getAnswer10, Collectors.counting())));
        val statisticAnswer11 = quizStatistic.stream().collect(groupingBy(QuizStatistic::statisticKey, groupingBy(QuizStatistic::getAnswer11, Collectors.counting())));

        List<List<String>> excelData = new ArrayList<>();
        val headersQuestion = new ArrayList<String>(
                List.of(EMPTY, EMPTY)
        );
        val headersAnswer = new ArrayList<String>(
                List.of("Дата", "Опросов")
        );
        for (Question question : Question.values()) {
            headersQuestion.addAll(prepareQuestionHeader(question));
            headersAnswer.addAll(prepareAnswerHeader(question));
        }
        excelData.add(headersQuestion);
        excelData.add(headersAnswer);

        List<String> row = new ArrayList<String>();
        for (val statistic1 : statisticAnswer1.entrySet()) {
            val date = statistic1.getKey();
            row.add(date);
            long sum = 0;
            for (Answer answer : quizButtonService.getAnswers(QUEST_1)) {
                sum = sum + statistic1.getValue().getOrDefault(answer, 0L);
            }
            row.add(String.valueOf(sum));

            row.addAll(prepareAnswerData(date, QUEST_1, statisticAnswer1));
            row.addAll(prepareAnswerData(date, QUEST_2, statisticAnswer2));
            row.addAll(prepareAnswerData(date, QUEST_3, statisticAnswer3));
            row.addAll(prepareAnswerData(date, QUEST_4, statisticAnswer4));
            row.addAll(prepareAnswerData(date, QUEST_5, statisticAnswer5));
            row.addAll(prepareAnswerData(date, QUEST_6, statisticAnswer6));
            row.addAll(prepareAnswerData(date, QUEST_7, statisticAnswer7));
            row.addAll(prepareAnswerData(date, QUEST_8, statisticAnswer8));
            row.addAll(prepareAnswerData(date, QUEST_9, statisticAnswer9));
            row.addAll(prepareAnswerData(date, QUEST_10, statisticAnswer10));
            row.addAll(prepareAnswerData(date, QUEST_11, statisticAnswer11));
            excelData.add(row);
            row = new ArrayList<String>();
        }

        return SendDocumentWrap.init()
                .setChatIdLong(user.getChatId())
                .setDocument(excelService.createExcelDocument("Статистика", excelData, 2))
                .setCaption(getDescription())
                .build().createMessageList();
    }

    @Setter
    @Getter
    class QuizStatistic extends Quiz {

        public String statisticKey() {
            return getQuizKey().toString();
        }
    }

    @Override
    public String getDescription() {
        return "Новые опросы";
    }
}
