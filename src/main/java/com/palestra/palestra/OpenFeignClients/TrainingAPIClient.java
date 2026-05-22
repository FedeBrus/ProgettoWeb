package com.palestra.palestra.OpenFeignClients;

import com.palestra.palestra.pojo.Programs.Exercise;
import com.palestra.palestra.pojo.Programs.Program;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "trainingClient", url="http://localhost:8081/")
public interface TrainingAPIClient {
    @GetMapping("/default")
    List<Program> getDefaultPrograms();

    @GetMapping("/details")
    List<Exercise> getProgramExercises(@RequestParam String defaultProgramName);

    @PostMapping("/calories")
    int getProgramCalories(@RequestBody Program program);
}
