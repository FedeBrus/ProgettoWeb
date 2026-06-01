package com.palestra.palestra.Services.Programs;

import com.palestra.palestra.OpenFeignClients.TrainingAPIClient;
import com.palestra.palestra.Repositories.UserRepository;
import com.palestra.palestra.Services.Trial.TrialUserManager;
import com.palestra.palestra.pojo.Programs.Exercise;
import com.palestra.palestra.pojo.Programs.Program;
import feign.RetryableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.List;

@Service
public class ProgramService {
    private final TrainingAPIClient trainingClient;
    private final CustomProgramService customProgramService;
    private final UserRepository repo;
    private final TrialUserManager trialManager;

    @Autowired
    public ProgramService(TrainingAPIClient trainingClient, CustomProgramService customProgramService, UserRepository repo, TrialUserManager trialManager) {
        this.trainingClient = trainingClient;
        this.customProgramService = customProgramService;
        this.repo = repo;
        this.trialManager = trialManager;
    }

    public List<Program> getDefaultPrograms() throws RetryableException {
        return trainingClient.getDefaultPrograms();
    }

    private boolean isDefaultProgram(String programName) throws RetryableException {
        List<String> defaultProgramsNames = getDefaultPrograms().stream().map(Program::getName).toList();
        return defaultProgramsNames.contains(programName);
    }

    public List<Exercise> getProgramExercises(String username, String programName) throws ClassNotFoundException, RetryableException {
        if (isDefaultProgram(programName)) {
            return trainingClient.getProgramExercises(programName);
        } else {
            return customProgramService.getCustomProgram(username, programName).getExercises();
        }
    }

    private Program getDefaultProgramFromName(String programName) throws RetryableException {
        List<Program> defaultPrograms = trainingClient.getDefaultPrograms();
        // Programmazione funzionale my beloved <3
        return defaultPrograms.stream().filter((Program p) -> {
            return (p.getName().equals(programName));
        }).toList().getFirst();
    }

    public int getProgramCalories(String username, String programName) throws ClassNotFoundException, RetryableException {
        Program p = null;
        if (isDefaultProgram(programName)) {
            p = getDefaultProgramFromName(programName);
        } else {
            p = customProgramService.getCustomProgram(username, programName);
        }

        return trainingClient.getProgramCalories(p);
    }

    public void completeProgram(User authUser, String programName) throws IllegalAccessException {
        final boolean isTrialUser = authUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER_PROVA"));

        if(isTrialUser && trialManager.getTimesTrained(authUser.getUsername()) > 2) {
            throw new IllegalAccessException();
        }

        repo.completeProgram(authUser.getUsername(), programName);

        // L'utente prova potrebbe finire gli accessi
        if(isTrialUser) {
            if(trialManager.getTimesTrained(authUser.getUsername()) == 3) {
                repo.disableUser(authUser.getUsername());
            }
        }
     }
}
