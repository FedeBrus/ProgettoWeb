package com.palestra.palestra.pojo.Programs;

import java.util.List;

public class Program {
    private String name;
    private String owner;
    private List<Exercise> exercises;

    public Program() {

    }

    public Program(String name, List<Exercise> exercises) {
        this(name, null, exercises);
    }

    public Program(String name, String owner, List<Exercise> exercises) {
        this.name = name;
        this.exercises = exercises;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }
}

