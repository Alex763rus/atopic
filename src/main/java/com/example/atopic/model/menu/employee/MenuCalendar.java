package com.example.atopic.model.menu.employee;

import com.example.atopic.enums.quest.Answer;
import com.example.atopic.enums.quest.Question;
import com.example.atopic.model.jpa.Quest;
import com.example.atopic.model.jpa.User;
import com.example.atopic.model.menu.base.Menu;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static com.example.atopic.constant.Constant.Command.COMMAND_CALENDAR;
import static com.example.atopic.constant.Constant.Command.COMMAND_QUEST;
import static com.example.atopic.enums.State.CALENDAR_WAIT_CHOOSE_DATE;
import static com.example.atopic.enums.State.FREE;
import static com.example.atopic.enums.quest.Question.*;
import static org.example.tgcommons.constant.Constant.Calendar.*;
import static org.example.tgcommons.constant.Constant.TextConstants.NEW_LINE;
import static org.example.tgcommons.utils.ButtonUtils.createCalendar;
import static org.example.tgcommons.utils.MessageUtils.prepareBold;

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
                    int q = 0;
                }
            }
        }
        return createCalendarMessage(user, update, calendar);
    }

    private List<PartialBotApiMethod> haveDateLogic(User user) {
        val calendar = calendarService.get(user);
        val quest = questService.get(user, calendar);
        val message = new StringBuilder();
        message.append(prepareBold("Выбрана дата: ")).append(new java.sql.Date(calendar.getTimeInMillis())).append(NEW_LINE);
        if (quest == null) {
            message.append("В выбранную дату данные отсутствуют.").append(NEW_LINE)
                    .append("внести данные: ").append(COMMAND_QUEST).append(NEW_LINE);
        } else {
            message
                    .append(getQuestDescription(QUEST_1, quest.getAnswer1()))
                    .append(getQuestDescription(QUEST_2, quest.getAnswer2()))
                    .append(getQuestDescription(QUEST_3, quest.getAnswer3()))
                    .append(getQuestDescription(QUEST_4, quest.getAnswer4()))
                    .append(getQuestDescription(QUEST_5, quest.getAnswer5()))
                    .append(getQuestDescription(QUEST_6, quest.getAnswer6()))
                    .append(getQuestDescription(QUEST_7, quest.getAnswer7()))
                    .append(getQuestDescription(QUEST_8, quest.getAnswer8()))
                    .append(getQuestDescription(QUEST_9, quest.getAnswer9()))
                    .append(getQuestDescription(QUEST_10, quest.getAnswer10()))
                    .append(getQuestDescription(QUEST_11, quest.getAnswer11()))
                    .append(NEW_LINE)
                    .append(("Изменить показания: ")).append(COMMAND_QUEST).append(NEW_LINE);
        }
        message.append("Выбрать другую дату: ").append(COMMAND_CALENDAR).append(NEW_LINE);
        return createMessageList(user, message.toString());
    }

    private String getQuestDescription(Question question, Answer answer) {
        return String.format("%s - %s%s", question.getTitle(), answer.getTitle(), NEW_LINE);
    }

    private List<PartialBotApiMethod> freelogic(User user, Update update) {
        val calendar = Calendar.getInstance();
        calendarService.add(user, calendar);
        stateService.setState(user, CALENDAR_WAIT_CHOOSE_DATE);
        return createCalendarMessage(user, update, calendar);
    }


    private List<PartialBotApiMethod> createCalendarMessage(User user, Update update, Calendar calendar) {
        val days = questService.getDays(user, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
        val markedDays = new HashMap<Integer, String>();
        days.stream().forEach(day -> markedDays.put(day, day + EmojiParser.parseToUnicode(":anger:")));
        return createMessageList(user, "Выберите дату:", createCalendar(calendar, markedDays));
    }

    @Override
    public String getDescription() {
        return "Работа с календарем";
    }
}
