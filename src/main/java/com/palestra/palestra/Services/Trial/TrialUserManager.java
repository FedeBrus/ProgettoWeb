package com.palestra.palestra.Services.Trial;

import com.palestra.palestra.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrialUserManager {
    private final UserRepository repo;

    @Autowired
    public TrialUserManager(UserRepository repo) {
        this.repo = repo;
    }

    public int getTimesTrained(String username) {
        return repo.getTotalTrainingProgramsUsed(username);
    }
}
