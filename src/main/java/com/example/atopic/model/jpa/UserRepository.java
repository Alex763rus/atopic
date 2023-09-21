package com.example.atopic.model.jpa;

import com.example.atopic.enums.QuestExportStatus;
import com.example.atopic.enums.UserRole;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findUserByUserRole(UserRole userRole);

    User findUserByChatId(Long chatId);

    List<User> findAll();

}
