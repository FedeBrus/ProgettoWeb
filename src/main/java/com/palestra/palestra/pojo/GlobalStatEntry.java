package com.palestra.palestra.pojo;

public class GlobalStatEntry {
    private final String role;
    private final String program;
    private final int times;

    public GlobalStatEntry(String role, String program, int times) {
        this.role = role;
        this.program = program;
        this.times = times;
    }

    public String getRole() {
        return role;
    }

    public String getProgram() {
        return program;
    }

    public int getTimes() {
        return times;
    }
}
