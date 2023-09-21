package com.example.atopic.model.menu.employee;

import com.example.atopic.enums.quest.Answer;
import com.example.atopic.enums.quest.Question;
import com.example.atopic.model.jpa.User;
import com.example.atopic.model.menu.base.Menu;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.tgcommons.model.button.Button;
import org.example.tgcommons.model.button.ButtonsDescription;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.atopic.constant.Constant.Command.COMMAND_QUEST;
import static com.example.atopic.enums.State.FREE;
import static com.example.atopic.enums.State.WAIT_QUEST;
import static com.example.atopic.enums.quest.Answer.*;
import static com.example.atopic.enums.quest.Question.*;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;
import static org.example.tgcommons.constant.Constant.TextConstants.NEW_LINE;

@Component(COMMAND_QUEST)
@Slf4j
public class MenuQuest extends Menu {


    private final HashMap<Question, ButtonsDescription> questionButtons = new HashMap<>();
    private final HashMap<User, Integer> questTmp = new HashMap<>();
    private final HashMap<User, ArrayList<Answer>> answerTmp = new HashMap<>();
    private final ButtonsDescription buttonsDescription5 = ButtonsDescription.init()
            .setCountColumn(1)
            .setButtons(new ArrayList<>(List.of(
                    createButton(ANSWER_VERY_MUCH),
                    createButton(ANSWER_STRONG_ENOUGH),
                    createButton(ANSWER_MINOR_WAY),
                    createButton(ANSWER_NO),
                    createButton(ANSWER_BACK)
            )))
            .build();


    @Override
    public String getMenuComand() {
        return COMMAND_QUEST;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        try {
            return switch (stateService.getState(user)) {
                case FREE -> freeLogic(user);
                case WAIT_QUEST -> questLogic(user, update);
                default -> errorMessageDefault(update);
            };
        } catch (Exception ex) {
            log.error(ex.toString());
            return createMessageList(user, ex.toString());
        }
    }

    @PostConstruct
    public void init() {
        val buttonsDescription4 = ButtonsDescription.init()
                .setCountColumn(1)
                .setButtons(new ArrayList<>(List.of(
                        createButton(ANSWER_STRONGLY),
                        createButton(ANSWER_SLIGHTLY),
                        createButton(ANSWER_NOT_BOTHER),
                        createButton(ANSWER_BACK))))
                .build();
        val buttonsDescription3 = ButtonsDescription.init()
                .setCountColumn(1)
                .setButtons(new ArrayList<>(List.of(
                        createButton(ANSWER_YES),
                        createButton(ANSWER_NO),
                        createButton(ANSWER_BACK))))
                .build();

        questionButtons.put(QUEST_7, buttonsDescription3);
        questionButtons.put(QUEST_8, buttonsDescription4);
    }

    private Button createButton(Answer answer) {
        return Button.init().setKey(answer.name()).setValue(answer.getTitle()).build();
    }

    private List<PartialBotApiMethod> freeLogic(User user) {
        stateService.setState(user, WAIT_QUEST);
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
            questService.save(user, calendarService.get(user), answerTmp.get(user));
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
        return createMessageList(user, question.getTitle(), questionButtons.getOrDefault(question, buttonsDescription5));
    }

    @Override
    public String getDescription() {
        return "Посмотреть календарь";
    }
}
