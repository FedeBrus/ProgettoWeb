package com.palestra.palestra.Services.Auth;

import com.palestra.palestra.pojo.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CheckSignUpInfo {
    final PasswordChecker pwChecker;

    @Autowired
    public CheckSignUpInfo(PasswordChecker pwChecker) {
        this.pwChecker = pwChecker;
    }

    public boolean checkUserInfo(User u) {
        if (
            u.getName().isEmpty() ||
            u.getSurname().isEmpty() ||
            u.getUsername().isEmpty() ||
            u.getEmail().isEmpty()
        ) {
           return false;
        }

        LocalDate today = LocalDate.now();
        LocalDate earliestAcceptableDate = today.minusYears(18);
        if (u.getDate_of_birth().isAfter(earliestAcceptableDate)) {
            return false;
        }

        if (!pwChecker.checkPassword(u.getPassword())) {
            return false;
        }

        boolean validRole = switch (u.getRole()) {
            case "ROLE_USER_PROVA", "ROLE_USER_BASIC", "ROLE_USER_PRO" -> true;
            default -> false;
        };

        if (!validRole) {
            return false;
        }

        return true;
    }
}
