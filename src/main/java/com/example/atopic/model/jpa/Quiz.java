package com.example.atopic.model.jpa;

import com.example.atopic.enums.QuizExportStatus;
import com.example.atopic.enums.quiz.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity(name = "quiz")
public class Quiz {

    @EmbeddedId
    private QuizKey quizKey;

    @Column(name = "export_status")
    private QuizExportStatus quizExportStatus;

    @Column(name = "answer1")
    private Answer answer1;

    @Column(name = "answer2")
    private Answer answer2;

    @Column(name = "answer3")
    private Answer answer3;

    @Column(name = "answer4")
    private Answer answer4;

    @Column(name = "answer5")
    private Answer answer5;

    @Column(name = "answer6")
    private Answer answer6;

    @Column(name = "answer7")
    private Answer answer7;

    @Column(name = "answer8")
    private Answer answer8;

    @Column(name = "answer9")
    private Answer answer9;

    @Column(name = "answer10")
    private Answer answer10;

    @Column(name = "answer11")
    private Answer answer11;

    @Column(name = "quiz_date")
    private Timestamp quizDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quiz quiz = (Quiz) o;
        return Objects.equals(quizKey, quiz.quizKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quizKey);
    }
}
