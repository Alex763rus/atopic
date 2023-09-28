package com.example.atopic.model.menu.admin.export;

import com.example.atopic.enums.quiz.Question;
import com.example.atopic.model.jpa.Quiz;
import com.example.atopic.model.jpa.User;
import com.example.atopic.model.menu.base.Menu;
import lombok.val;
import org.example.tgcommons.model.wrapper.SendDocumentWrap;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Lists.asList;

public abstract class MenuExportQuizBase extends Menu {

    protected List<PartialBotApiMethod> exportToExcel(User user, List<Quiz> data) {
        List<List<String>> excelData = new ArrayList<>();
        val headers = new ArrayList<String>(
                List.of("№", "Чат ИД:", "Логин:", "Фамилия:", "Имя:", "Возраст:", "Дата анкеты:", "Дата опроса:")
        );
        Arrays.stream(Question.values()).forEach(quiz -> headers.add(quiz.getTitle()));
        excelData.add(headers);

        for (int i = 0; i < data.size(); ++i) {
            val quiz = data.get(i);
            val client = quiz.getQuizKey().getUser();
            excelData.add(
                    Arrays.asList(
                            String.valueOf(i + 1)
                            , String.valueOf(client.getChatId())
                            , client.getUserName()
                            , client.getInputSurname()
                            , client.getInputName()
                            , String.valueOf(client.getInputAge())
                            , quiz.getQuizKey().toString()
                            , String.valueOf(quiz.getQuizDate())
                            , quiz.getAnswer1().getTitle()
                            , quiz.getAnswer2().getTitle()
                            , quiz.getAnswer3().getTitle()
                            , quiz.getAnswer4().getTitle()
                            , quiz.getAnswer5().getTitle()
                            , quiz.getAnswer6().getTitle()
                            , quiz.getAnswer7().getTitle()
                            , quiz.getAnswer8().getTitle()
                            , quiz.getAnswer9().getTitle()
                            , quiz.getAnswer10().getTitle()
                            , quiz.getAnswer11().getTitle()
                    )
            );
        }
        stateService.refreshUser(user);
        return SendDocumentWrap.init()
                .setChatIdLong(user.getChatId())
                .setDocument(excelService.createExcelDocument("Опросы", excelData, 8))
                .setCaption(getDescription())
                .build().createMessageList();
    }
}
