package com.palestra.palestra.Services;

import com.palestra.palestra.OpenFeignClients.TrainingAPIClient;
import com.palestra.palestra.Repositories.CustomExerciseRepository;
import com.palestra.palestra.pojo.Exercise;
import com.palestra.palestra.pojo.Program;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.core.exc.InputCoercionException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class ProgramInserterService {
    private final CustomExerciseRepository repo;
    private final TrainingAPIClient restAPI;

    @Autowired
    public ProgramInserterService(CustomExerciseRepository repo, TrainingAPIClient restAPI) {
        this.repo = repo;
        this.restAPI = restAPI;
    }

    public int addProgramToDB(String programName, String exercises) throws JacksonException, IllegalStateException {
        // Converti oggetti in formato JSON da String a oggetti veri
        ObjectMapper mapper = new ObjectMapper();
        List<Exercise> exerciseList = mapper.readValue(exercises, new TypeReference<List<Exercise>>(){});

        try {
            repo.addProgram(programName, exerciseList);
        } catch (RuntimeException e) {
            // Errore durante l'inserimento al DB
            throw new IllegalStateException("Errore durante l'inserimento nel DB");
        }
        return restAPI.getProgramCalories(new Program(programName, exerciseList));
    }
}
