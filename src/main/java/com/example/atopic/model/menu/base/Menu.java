package com.example.atopic.model.menu.base;

import com.example.atopic.config.BotConfig;
import com.example.atopic.exception.InputCallbackException;
import com.example.atopic.exception.InputLongException;
import com.example.atopic.model.jpa.User;
import com.example.atopic.model.menu.MenuActivity;
import com.example.atopic.service.database.QuizService;
import com.example.atopic.service.database.UserService;
import com.example.atopic.service.excel.ExcelService;
import com.example.atopic.service.menu.CalendarService;
import com.example.atopic.service.menu.QuizButtonService;
import com.example.atopic.service.menu.StateService;
import jakarta.persistence.MappedSuperclass;
import lombok.val;
import org.example.tgcommons.model.button.ButtonsDescription;
import org.example.tgcommons.model.wrapper.DeleteMessageWrap;
import org.example.tgcommons.model.wrapper.SendMessageWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.utils.ButtonUtils.createVerticalColumnMenu;

@MappedSuperclass
public abstract class Menu implements MenuActivity {

    @Autowired
    protected BotConfig botConfig;

    @Autowired
    protected StateService stateService;

    @Autowired
    protected ExcelService excelService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected QuizService quizService;

    @Autowired
    protected CalendarService calendarService;

    @Autowired
    protected QuizButtonService quizButtonService;

    private static final String DEFAULT_TEXT_ERROR = "Ошибка! Команда не найдена";


    protected List<PartialBotApiMethod> errorMessageDefault(Update update) {
        return SendMessageWrap.init()
                .setChatIdLong(update.getMessage().getChatId())
                .setText(DEFAULT_TEXT_ERROR)
                .build().createMessageList();
    }

    protected String getInputCallback(Update update) {
        if (!update.hasCallbackQuery()) {
            throw new InputCallbackException();
        }
        return update.getCallbackQuery().getData();
    }


    protected Integer getInputInteger(User user, Update update) {
        if (!update.hasMessage()) {
            throw new InputLongException("Отсутствует сообщение", 10);
        }
        val message = update.getMessage().getText();
        if (message == null || message.equals(EMPTY) || message.trim().length() > 10 || !checkLong(message)) {
            throw new InputLongException(message, 10);
        }
        return Integer.parseInt(message);
    }

    protected Long getInputLong(User user, Update update) {
        if (!update.hasMessage()) {
            throw new InputLongException("Отсутствует сообщение", 18);
        }
        val message = update.getMessage().getText();
        if (message == null || message.equals(EMPTY) || message.trim().length() > 18 || !checkLong(message)) {
            throw new InputLongException(message, 18);
        }
        return Long.parseLong(message);
    }

    protected Long getInputLong(User user, Update update, int maxRank, long minValue, long maxValue) {
        if (!update.hasMessage()) {
            throw new InputLongException("Отсутствует сообщение", maxRank);
        }
        val message = update.getMessage().getText();
        if (message == null || message.equals(EMPTY) || message.trim().length() > maxRank || !checkLong(message)) {
            throw new InputLongException(message, maxRank);
        }
        val result = Long.parseLong(message);
        if (result < minValue || result > maxValue) {
            throw new InputLongException(message, maxRank, minValue, maxValue);
        }
        return result;
    }

    private boolean checkLong(String value) {
        try {
            Long.parseLong(value);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    protected List<PartialBotApiMethod> createErrorDefaultMessage(User user) {
        return createMessageList(user, DEFAULT_TEXT_ERROR);
    }

    protected List<PartialBotApiMethod> createMessageList(User user, String message) {
        return List.of(this.createMessage(user, message));
    }

    protected List<PartialBotApiMethod> createMessageList(User user, String message, ButtonsDescription buttonsDescription) {
        return List.of(this.createMessage(user, message, buttonsDescription));
    }

    protected PartialBotApiMethod createMessage(User user, String message) {
        return SendMessageWrap.init()
                .setChatIdLong(user.getChatId())
                .setText(message)
                .build().createMessage();
    }

    protected PartialBotApiMethod createDeleteMessage(Update update) {
        if (!update.hasCallbackQuery()) {
            return null;
        }
        val message = update.getCallbackQuery().getMessage();
        return DeleteMessageWrap.init()
                .setChatIdLong(message.getChatId())
                .setMessageId(message.getMessageId())
                .build().createMessage();
    }

    protected PartialBotApiMethod createMessage(User user, String message, ButtonsDescription buttonsDescription) {
        return createMessage(user, message, createVerticalColumnMenu(buttonsDescription));
    }

    protected PartialBotApiMethod createMessage(User user, String message, InlineKeyboardMarkup inlineKeyboardMarkup) {
        return SendMessageWrap.init()
                .setChatIdLong(user.getChatId())
                .setText(message)
                .setInlineKeyboardMarkup(inlineKeyboardMarkup)
                .build().createMessage();
    }

    protected List<PartialBotApiMethod> createMessageList(User user, String message, InlineKeyboardMarkup inlineKeyboardMarkup) {
        return List.of(this.createMessage(user, message, inlineKeyboardMarkup));
    }
}
