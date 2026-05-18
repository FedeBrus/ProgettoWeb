package com.palestra.palestra.pojo;

public class PersonalStatEntry {
    private final String program;
    private final int times;

    public PersonalStatEntry(String program, int times) {
        this.program = program;
        this.times = times;
    }

    public String getProgram() {
        return program;
    }

    public int getTimes() {
        return times;
    }
}
