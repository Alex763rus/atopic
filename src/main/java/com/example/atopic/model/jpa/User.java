package com.example.atopic.model.jpa;

import com.example.atopic.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity(name = "user_tbl")
public class User {

    @Id
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "input_surname")
    private String inputSurname;

    @Column(name = "input_name")
    private String inputName;

    @Column(name = "input_age")
    private Long inputAge;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_role")
    private UserRole userRole;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Override
    public String toString() {
        return "User{" +
                "chatId=" + chatId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", userRole=" + userRole.getTitle() +
                ", registeredAt=" + registeredAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getChatId().equals(user.getChatId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChatId());
    }

}
