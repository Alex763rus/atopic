package com.example.atopic.model.menu.employee;

import com.example.atopic.enums.quiz.Answer;
import com.example.atopic.enums.quiz.Question;
import com.example.atopic.model.jpa.User;
import com.example.atopic.model.menu.base.Menu;
import com.example.atopic.service.menu.QuizButtonService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.atopic.constant.Constant.Command.COMMAND_TAKE_QUIZ;
import static com.example.atopic.enums.State.FREE;
import static com.example.atopic.enums.State.WAIT_QUIZ;
import static com.example.atopic.enums.quiz.Answer.ANSWER_BACK;
import static java.util.Collections.emptyList;

@Component(COMMAND_TAKE_QUIZ)
@Slf4j
public class MenuTakeQuiz extends Menu {


    private final HashMap<User, Integer> questTmp = new HashMap<>();
    private final HashMap<User, ArrayList<Answer>> answerTmp = new HashMap<>();

    @Override
    public String getMenuComand() {
        return COMMAND_TAKE_QUIZ;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        try {
            return switch (stateService.getState(user)) {
                case FREE -> freeLogic(user);
                case WAIT_QUIZ -> questLogic(user, update);
                default -> errorMessageDefault(update);
            };
        } catch (Exception ex) {
            log.error(ex.toString());
            return createMessageList(user, ex.toString());
        }
    }


    private List<PartialBotApiMethod> freeLogic(User user) {
        stateService.setState(user, WAIT_QUIZ);
        val questNumber = 0;
        val question = Question.values()[questNumber];
        questTmp.put(user, questNumber);
        answerTmp.put(user, new ArrayList<>());

        val answer = new ArrayList<PartialBotApiMethod>();
        answer.add(createMessage(user, "Анкетирование состоит из 11 вопросов"));
        answer.addAll(calcBtnProcess(user, question));
        return answer;
    }

    private List<PartialBotApiMethod> questLogic(User user, Update update) {
        if (!update.hasCallbackQuery()) {
            return createErrorDefaultMessage(user);
        }
        val answer = Answer.valueOf(update.getCallbackQuery().getData());
        int questNumber = questTmp.get(user);
        if (answer == ANSWER_BACK) {
            questNumber = questNumber - 1;
        } else {
            val answers = answerTmp.getOrDefault(user, new ArrayList<>());
            answers.add(questNumber, answer);
            questNumber = questNumber + 1;
        }
        if (questNumber >= Question.values().length) {
            stateService.setState(user, FREE);
            quizService.save(user, calendarService.get(user), answerTmp.get(user));
            return createMessageList(user, "Анкета успешно сохранена, дата в календаре отмечена!");
        }
        if (questNumber < 0) {
            stateService.setState(user, FREE);
            return emptyList();
        }
        val question = Question.values()[questNumber];
        questTmp.put(user, questNumber);
        return calcBtnProcess(user, question);
    }

    private List<PartialBotApiMethod> calcBtnProcess(User user, Question question) {
        return createMessageList(user, question.getTitle(), quizButtonService.getButtonsDescription(question));
    }

    @Override
    public String getDescription() {
        return "Посмотреть календарь";
    }
}
