package com.palestra.palestra.Services;

import com.palestra.palestra.OpenFeignClients.TrainingAPIClient;
import com.palestra.palestra.pojo.Exercise;
import com.palestra.palestra.pojo.Program;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class ProgramService {
    private final TrainingAPIClient trainingClient;
    private final CustomProgramService customProgramService;

    @Autowired
    public ProgramService(TrainingAPIClient trainingClient, CustomProgramService customProgramService) {
        this.trainingClient = trainingClient;
        this.customProgramService = customProgramService;
    }

    public List<Program> getDefaultPrograms() {
        return trainingClient.getDefaultPrograms();
    }

    private boolean isDefaultProgram(String programName) {
        List<String> defaultProgramsNames = getDefaultPrograms().stream().map(Program::getName).toList();
        return defaultProgramsNames.contains(programName);
    }

    public List<Exercise> getProgramExercises(String username, String programName) throws ClassNotFoundException {
        if (isDefaultProgram(programName)) {
            return trainingClient.getProgramExercises(programName);
        } else {
            return customProgramService.getCustomProgram(username, programName).getExercises();
        }
    }

    private Program getDefaultProgramFromName(String programName) {
        List<Program> defaultPrograms = trainingClient.getDefaultPrograms();
        // Programmazione funzionale my beloved <3
        return defaultPrograms.stream().filter((Program p) -> {
            return (p.getName().equals(programName));
        }).toList().getFirst();
    }

    public int getProgramCalories(String username, String programName) throws ClassNotFoundException {
        Program p = null;
        if (isDefaultProgram(programName)) {
            p = getDefaultProgramFromName(programName);
        } else {
            p = customProgramService.getCustomProgram(username, programName);
        }

        return trainingClient.getProgramCalories(p);
    }
}
