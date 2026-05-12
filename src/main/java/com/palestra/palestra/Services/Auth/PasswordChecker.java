package com.palestra.palestra.Services.Auth;

import org.springframework.stereotype.Service;

@Service
public class PasswordChecker {
    public boolean checkPassword(String password) {
        return password.length() >= 8 && password.contains("id_10");
    }
}
