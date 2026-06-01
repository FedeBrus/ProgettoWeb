package com.palestra.palestra.OpenFeignClients;

import com.palestra.palestra.pojo.Programs.Exercise;
import com.palestra.palestra.pojo.Programs.Program;
import feign.RetryableException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.net.ConnectException;
import java.util.List;

@FeignClient(name = "trainingClient", url="http://localhost:8081/")
public interface TrainingAPIClient {
    @GetMapping("/default")
    List<Program> getDefaultPrograms() throws RetryableException;

    @GetMapping("/details")
    List<Exercise> getProgramExercises(@RequestParam String defaultProgramName);

    @PostMapping("/calories")
    int getProgramCalories(@RequestBody Program program);
}
