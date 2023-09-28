package com.example.atopic.service.menu;

import com.example.atopic.enums.quiz.Answer;
import com.example.atopic.enums.quiz.Question;
import jakarta.annotation.PostConstruct;
import lombok.val;
import org.example.tgcommons.model.button.Button;
import org.example.tgcommons.model.button.ButtonsDescription;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.atopic.enums.quiz.Answer.*;
import static com.example.atopic.enums.quiz.Question.QUEST_7;
import static com.example.atopic.enums.quiz.Question.QUEST_8;

@Service
public class QuizButtonService {

    private final HashMap<Question, ButtonsDescription> questionButtons = new HashMap<>();
    private ButtonsDescription buttonsDescription5;

    private final HashMap<Question, List<Answer>> questionAnswer = new HashMap<>();
    private List<Answer> questionAnswer5;

    @PostConstruct
    public void init() {
        questionAnswer.put(QUEST_7, List.of(ANSWER_YES, ANSWER_NO));
        questionAnswer.put(QUEST_8, List.of(ANSWER_STRONGLY, ANSWER_SLIGHTLY, ANSWER_NOT_BOTHER));
        questionAnswer5 = List.of(ANSWER_VERY_MUCH, ANSWER_STRONG_ENOUGH, ANSWER_MINOR_WAY, ANSWER_NO);

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

        buttonsDescription5 = ButtonsDescription.init()
                .setCountColumn(1)
                .setButtons(new ArrayList<>(List.of(
                        createButton(ANSWER_VERY_MUCH),
                        createButton(ANSWER_STRONG_ENOUGH),
                        createButton(ANSWER_MINOR_WAY),
                        createButton(ANSWER_NO),
                        createButton(ANSWER_BACK)
                )))
                .build();

        questionButtons.put(QUEST_7, buttonsDescription3);
        questionButtons.put(QUEST_8, buttonsDescription4);
    }

    public ButtonsDescription getButtonsDescription(final Question question) {
        return questionButtons.getOrDefault(question, buttonsDescription5);
    }

    public List<Answer> getAnswers(final Question question) {
        return questionAnswer.getOrDefault(question, questionAnswer5);
    }


    private Button createButton(Answer answer) {
        return Button.init().setKey(answer.name()).setValue(answer.getTitle()).build();
    }
}
