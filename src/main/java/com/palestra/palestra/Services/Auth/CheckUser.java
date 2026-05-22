package com.palestra.palestra.Services.Auth;

import com.palestra.palestra.Repositories.UserRepository;
import com.palestra.palestra.pojo.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckUser {
    private final UserRepository userRepository;

    @Autowired
    public CheckUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean checkAndInsert(User u) {
        if (userRepository.userExists(u.getUsername())) {
            return false;
        } else {
            userRepository.addUser(u);
            return true;
        }
    }
}
