package com.example.atopic.model.menu.unregister;

import com.example.atopic.model.jpa.User;
import com.example.atopic.model.menu.base.Menu;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.tgcommons.model.wrapper.SendMessageWrap;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.atopic.constant.Constant.Command.COMMAND_REGISTER;
import static com.example.atopic.enums.State.*;
import static com.example.atopic.enums.UserRole.EMPLOYEE;
import static org.example.tgcommons.constant.Constant.Command.COMMAND_START;
import static org.example.tgcommons.constant.Constant.TextConstants.NEW_LINE;

@Component(COMMAND_REGISTER)
@Slf4j
public class MenuRegister extends Menu {

    @Override
    public String getMenuComand() {
        return COMMAND_REGISTER;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        try {
            return switch (stateService.getState(user)) {
                case FREE -> freelogic(user);
                case REGISTER_WAIT_SURNAME -> registerWaitSurnameLogic(user, update);
                case REGISTER_WAIT_NAME -> registerWaitNameLogic(user, update);
                case REGISTER_WAIT_AGE -> registerWaitAgeLogic(user, update);
                default -> errorMessageDefault(update);
            };
        } catch (Exception ex) {
            log.error(ex.toString());
            return createMessageList(user, ex.toString());
        }
    }

    private List<PartialBotApiMethod> registerWaitAgeLogic(User user, Update update) {
        val age = getInputLong(user, update);
        user.setInputAge(age);
        user.setUserRole(EMPLOYEE);
        userService.saveUser(user);
        stateService.deleteUser(user);
        return createMessageList(user, "Данные успешно сохранены, бот готов к работе!");
    }

    private List<PartialBotApiMethod> registerWaitNameLogic(User user, Update update) {
        if (!update.hasMessage()) {
            return createMessageList(user, "Сообщение должно содержать имя");
        }
        user.setInputName(update.getMessage().getText());
        userService.saveUser(user);
        stateService.setState(user, REGISTER_WAIT_AGE);
        return createMessageList(user, "Шаг 3/3 Укажите, сколько вам лет:");
    }

    private List<PartialBotApiMethod> registerWaitSurnameLogic(User user, Update update) {
        if (!update.hasMessage()) {
            return createMessageList(user, "Сообщение должно содержать фамилию");
        }
        user.setInputSurname(update.getMessage().getText());
        userService.saveUser(user);
        stateService.setState(user, REGISTER_WAIT_NAME);
        return createMessageList(user, "Шаг 2/3 Укажите ваше имя:");
    }

    private List<PartialBotApiMethod> freelogic(User user) {
        stateService.setState(user, REGISTER_WAIT_SURNAME);
        return createMessageList(user, "Шаг 1/3 Укажите вашу фамилию:");
    }


    @Override
    public String getDescription() {
        return getMenuComand();
    }

}
