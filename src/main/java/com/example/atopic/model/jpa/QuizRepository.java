package com.example.atopic.model.jpa;

import com.example.atopic.enums.QuizExportStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuizRepository extends CrudRepository<Quiz, Long> {

    @Override
    List<Quiz> findAll();

    List<Quiz> findQuestByQuizExportStatus(QuizExportStatus quizExportStatus);

}
