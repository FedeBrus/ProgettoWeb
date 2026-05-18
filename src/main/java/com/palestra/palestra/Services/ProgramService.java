package com.palestra.palestra.Services;

import com.palestra.palestra.OpenFeignClients.TrainingAPIClient;
import com.palestra.palestra.pojo.Exercise;
import com.palestra.palestra.pojo.Program;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class ProgramService {
    private final TrainingAPIClient trainingClient;

    public ProgramService(TrainingAPIClient trainingClient) {
        this.trainingClient = trainingClient;
    }

    public List<Program> getDefaultPrograms() {
        return trainingClient.getDefaultPrograms();
    }

    private boolean isDefaultProgram(String programName) {
        List<String> defaultProgramsNames = getDefaultPrograms().stream().map(Program::getName).toList();
        return defaultProgramsNames.contains(programName);
    }

    public List<Exercise> getProgramExercises(String programName) {
        if (isDefaultProgram(programName)) {
            return trainingClient.getProgramExercises(programName);
        } else {
            // TODO: Query al db interno
            return null;
        }
    }

    private Program getDefaultProgramFromName(String programName) {
        List<Program> defaultPrograms = trainingClient.getDefaultPrograms();
        // Programmazione funzionale my beloved <3
        return defaultPrograms.stream().filter((Program p) -> {
            return (p.getName().equals(programName));
        }).toList().getFirst();
    }

    public int getProgramCalories(String programName) {
        Program p = null;
        if (isDefaultProgram(programName)) {
            p = getDefaultProgramFromName(programName);
        } else {
            // TODO: Costruire il programma p a partire da una query sql al db interno
        }

        return trainingClient.getProgramCalories(p);
    }
}
