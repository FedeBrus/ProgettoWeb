package com.palestra.palestra.OpenFeignClients;

import com.palestra.palestra.pojo.Exercise;
import com.palestra.palestra.pojo.Program;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "trainingClient", url="http://localhost:8081/")
public interface TrainingAPIClient {
    @GetMapping("/default")
    List<Program> getDefaultPrograms();

    @GetMapping("/default/{defaultProgramName}")
    List<Exercise> getProgramExercises(@PathVariable String defaultProgramName);

    @PostMapping("/calories")
    int getProgramCalories(@RequestBody Program program);
}
