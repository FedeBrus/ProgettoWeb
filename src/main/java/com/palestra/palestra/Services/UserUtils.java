package com.palestra.palestra.Services;

import com.palestra.palestra.Repositories.UserRepository;
import com.palestra.palestra.Services.Auth.PasswordChecker;
import com.palestra.palestra.pojo.SecurityUser;
import com.palestra.palestra.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserUtils {
    private final UserRepository repo;
    private final PasswordChecker pwChecker;

    @Autowired
    public UserUtils(UserRepository repo, PasswordChecker pwChecker) {
        this.repo = repo;
        this.pwChecker = pwChecker;
    }

    public boolean changePassword(String username, String newPassword) {
        if(pwChecker.checkPassword(newPassword)) {
            repo.changePassword(username, newPassword);
            return true;
        }else {
            return false;
        }
    }
}
