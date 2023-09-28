package com.example.atopic.model.menu.employee;

import com.example.atopic.enums.quiz.Answer;
import com.example.atopic.enums.quiz.Question;
import com.example.atopic.model.jpa.User;
import com.example.atopic.model.menu.base.Menu;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.tgcommons.model.button.Button;
import org.example.tgcommons.model.button.ButtonsDescription;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;

import static com.example.atopic.constant.Constant.Command.COMMAND_CALENDAR;
import static com.example.atopic.constant.Constant.Command.COMMAND_TAKE_QUIZ;
import static com.example.atopic.enums.State.CALENDAR_WAIT_CHOOSE_DATE;
import static com.example.atopic.enums.State.FREE;
import static com.example.atopic.enums.quiz.Question.*;
import static java.util.Collections.emptyList;
import static org.example.tgcommons.constant.Constant.Calendar.*;
import static org.example.tgcommons.constant.Constant.Command.COMMAND_START;
import static org.example.tgcommons.constant.Constant.TextConstants.NEW_LINE;
import static org.example.tgcommons.utils.ButtonUtils.createCalendar;
import static org.example.tgcommons.utils.MessageUtils.prepareBold;
import static org.example.tgcommons.utils.StringUtils.prepareShield;

@Component(COMMAND_CALENDAR)
@Slf4j
public class MenuCalendar extends Menu {

    @Override
    public String getMenuComand() {
        return COMMAND_CALENDAR;
    }


    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        try {
            return switch (stateService.getState(user)) {
                case FREE -> freelogic(user, update);
                case CALENDAR_WAIT_CHOOSE_DATE -> waitChooseDateLogic(user, update);
                case CALENDAR_HAVE_DATE -> haveDateLogic(user);
                default -> createErrorDefaultMessage(user);
            };
        } catch (Exception ex) {
            log.error(ex.toString());
            return createMessageList(user, ex.toString());
        }
    }

    @Override
    public PartialBotApiMethod replaceButton(Update update, User user) {
        return createDeleteMessage(update);
    }


    private List<PartialBotApiMethod> waitChooseDateLogic(User user, Update update) {
        val data = getInputCallback(update);
        val calendar = calendarService.get(user);
        switch (data) {
            case NEXT_MONTH -> calendar.add(Calendar.MONTH, 1);
            case PREV_MONTH -> calendar.add(Calendar.MONTH, -1);
            case MAIN_MENU -> stateService.setState(user, FREE);
            default -> {
                try {
                    val day = Integer.parseInt(data);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    calendarService.add(user, calendar);
                    return haveDateLogic(user);
                } catch (NumberFormatException ignore) {
                    int ignored = 0;
                }
            }
        }
        return stateService.getState(user) == FREE ? emptyList() : createCalendarMessage(user, update, calendar);
    }

    private List<PartialBotApiMethod> haveDateLogic(User user) {
        val calendar = calendarService.get(user);
        val quiz = quizService.get(user, calendar);
        val message = new StringBuilder();
        val buttons = new ArrayList<Button>();
        message.append("Выбрана дата: ").append(prepareBold(new java.sql.Date(calendar.getTimeInMillis()).toString())).append(NEW_LINE);
        if (quiz == null) {
            message.append("В выбранную дату данные отсутствуют").append(NEW_LINE);
            buttons.add(Button.init().setKey(COMMAND_TAKE_QUIZ).setValue("Внести данные").build());
        } else {
            message
                    .append(getQuestDescription(QUEST_1, quiz.getAnswer1()))
                    .append(getQuestDescription(QUEST_2, quiz.getAnswer2()))
                    .append(getQuestDescription(QUEST_3, quiz.getAnswer3()))
                    .append(getQuestDescription(QUEST_4, quiz.getAnswer4()))
                    .append(getQuestDescription(QUEST_5, quiz.getAnswer5()))
                    .append(getQuestDescription(QUEST_6, quiz.getAnswer6()))
                    .append(getQuestDescription(QUEST_7, quiz.getAnswer7()))
                    .append(getQuestDescription(QUEST_8, quiz.getAnswer8()))
                    .append(getQuestDescription(QUEST_9, quiz.getAnswer9()))
                    .append(getQuestDescription(QUEST_10, quiz.getAnswer10()))
                    .append(getQuestDescription(QUEST_11, quiz.getAnswer11()))
                    .append(NEW_LINE)
                    .append(("Изменить показания: ")).append(prepareShield(COMMAND_TAKE_QUIZ)).append(NEW_LINE);
            buttons.add(Button.init().setKey(COMMAND_TAKE_QUIZ).setValue("Изменить показания").build());
        }
        buttons.add(Button.init().setKey(COMMAND_CALENDAR).setValue("Календарь").build());
        buttons.add(Button.init().setKey(COMMAND_START).setValue("Главное меню").build());
        val buttonsDescription = ButtonsDescription.init().setCountColumn(1).setButtons(buttons).build();
        return createMessageList(user, message.toString(), buttonsDescription);
    }

    private String getQuestDescription(Question question, Answer answer) {
        return String.format("%s%s- %s%s", question.getTitle(), NEW_LINE, prepareBold(answer.getTitle()), NEW_LINE);
    }

    private List<PartialBotApiMethod> freelogic(User user, Update update) {
        val calendar = calendarService.getOrCurrent(user);
        stateService.setState(user, CALENDAR_WAIT_CHOOSE_DATE);
        return createCalendarMessage(user, update, calendar);
    }

    private List<PartialBotApiMethod> createCalendarMessage(User user, Update update, Calendar calendar) {
        val days = quizService.getDays(user, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
        val markedDays = new HashMap<Integer, String>();
        days.stream().forEach(day -> markedDays.put(day, day + EmojiParser.parseToUnicode(":anger:")));
        return createMessageList(user, "Выберите дату:", createCalendar(calendar, markedDays));
    }

    @Override
    public String getDescription() {
        return "Работа с календарем";
    }
}
