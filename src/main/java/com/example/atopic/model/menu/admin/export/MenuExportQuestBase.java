package com.example.atopic.model.menu.admin.export;

import com.example.atopic.enums.quest.Question;
import com.example.atopic.model.jpa.Quest;
import com.example.atopic.model.jpa.User;
import com.example.atopic.model.menu.base.Menu;
import lombok.val;
import org.example.tgcommons.model.wrapper.SendDocumentWrap;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.google.common.collect.Lists.asList;

public abstract class MenuExportQuestBase extends Menu {

    protected List<PartialBotApiMethod> exportToExcel(User user, List<Quest> data) {
        List<List<String>> excelData = new ArrayList<>();
        val headers = new ArrayList<String>(
                List.of("№", "Чат ИД:", "Логин:", "Фамилия:", "Имя:", "Возраст:", "Дата анкеты:", "Дата опроса:")
                );
        Arrays.stream(Question.values())
                .forEach(quest -> headers.add(quest.getTitle()));
        excelData.add(headers);

        for (int i = 0; i < data.size(); ++i) {
            val quest = data.get(i);
            val client = quest.getQuestKey().getUser();
            excelData.add(
                    Arrays.asList(
                            String.valueOf(i + 1)
                            , String.valueOf(client.getChatId())
                            , client.getUserName()
                            , client.getInputSurname()
                            , client.getInputName()
                            , String.valueOf(client.getInputAge())
                            , quest.getQuestKey().toString()
                            , String.valueOf(quest.getQuestDate())
                            , quest.getAnswer1().getTitle()
                            , quest.getAnswer2().getTitle()
                            , quest.getAnswer3().getTitle()
                            , quest.getAnswer4().getTitle()
                            , quest.getAnswer5().getTitle()
                            , quest.getAnswer6().getTitle()
                            , quest.getAnswer7().getTitle()
                            , quest.getAnswer8().getTitle()
                            , quest.getAnswer9().getTitle()
                            , quest.getAnswer10().getTitle()
                            , quest.getAnswer11().getTitle()
                    )
            );
        }
        stateService.refreshUser(user);
        return Arrays.asList(
                SendDocumentWrap.init()
                        .setChatIdLong(user.getChatId())
                        .setDocument(excelService.createExcelDocument("Все лиды", excelData))
                        .setCaption(getDescription())
                        .build().createMessage());
    }
}
