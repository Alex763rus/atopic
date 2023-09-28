package com.example.atopic.model.menu.base;

import com.example.atopic.model.jpa.User;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.tgcommons.model.button.Button;
import org.example.tgcommons.model.button.ButtonsDescription;
import org.example.tgcommons.model.wrapper.SendMessageWrap;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

import static com.example.atopic.constant.Constant.Command.*;
import static org.example.tgcommons.constant.Constant.Command.COMMAND_START;
import static org.example.tgcommons.constant.Constant.TextConstants.NEW_LINE;
import static org.example.tgcommons.utils.StringUtils.prepareShield;

@Component(COMMAND_START)
@Slf4j
public class MenuStart extends Menu {

    @Override
    public String getMenuComand() {
        return COMMAND_START;
    }

    @Override
    public PartialBotApiMethod replaceButton(Update update, User user) {
        return createDeleteMessage(update);
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        try {
            return switch (user.getUserRole()) {
                case BLOCKED -> createMessageList(user, "Доступ запрещен");
                case UNREGISTERED -> getUnregisteredMenuText(user, update);
                case EMPLOYEE -> getEmployeeMenuText(user);
                case ADMIN -> getAdminMenuText(user);
                default -> createErrorDefaultMessage(user);
            };
        } catch (Exception ex) {
            log.error(ex.toString());
            return createMessageList(user, ex.toString());
        }
    }

    private List<PartialBotApiMethod> getUnregisteredMenuText(User user, Update update) {
        String messageText = "Для продолжения работы, укажите, пожалуйста, свои анкетные данные:";
        val buttons = new ArrayList<Button>(List.of(
                Button.init().setKey(COMMAND_REGISTER).setValue("Зарегистрироваться").build()
        ));
        val buttonsDescription = ButtonsDescription.init().setCountColumn(1).setButtons(buttons).build();
        return createMessageList(user, messageText, buttonsDescription);
    }

    private List<PartialBotApiMethod> getAdminMenuText(User user) {
        val buttons = new ArrayList<Button>(List.of(
                Button.init().setKey(COMMAND_CALENDAR).setValue("Календарь").build(),
                Button.init().setKey(COMMAND_EXPORT_NEW_QUIZZES).setValue("Выгрузить новые").build(),
                Button.init().setKey(COMMAND_EXPORT_ALL_QUIZZES).setValue("Выгрузить все").build(),
                Button.init().setKey(COMMAND_EXPORT_STATISTIC_DAY).setValue("Выгрузить cтатистику").build()
        ));
        val buttonsDescription = ButtonsDescription.init().setCountColumn(1).setButtons(buttons).build();
        return createMessageList(user, "Меню администратора:", buttonsDescription);
    }

    private List<PartialBotApiMethod> getEmployeeMenuText(User user) {
        val buttons = new ArrayList<Button>(List.of(
                Button.init().setKey(COMMAND_CALENDAR).setValue("Календарь").build()
        ));
        val buttonsDescription = ButtonsDescription.init().setCountColumn(1).setButtons(buttons).build();
        return createMessageList(user, "Главное меню:", buttonsDescription);
    }

    @Override
    public String getDescription() {
        return " Начало работы";
    }
}
