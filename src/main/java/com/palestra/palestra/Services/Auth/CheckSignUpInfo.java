package com.palestra.palestra.Services.Auth;

import com.palestra.palestra.pojo.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CheckSignUpInfo {
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

        if (u.getPassword().length() < 8 || !u.getPassword().contains("team10")) {
            return false;
        }

        boolean validRole = switch (u.getRole()) {
            case "trial", "basic", "pro" -> true;
            default -> false;
        };

        if (!validRole) {
            return false;
        }

        return true;
    }
}
