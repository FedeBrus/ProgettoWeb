package com.palestra.palestra.pojo.Stats;

public class GlobalStatEntry {
    private final String role;
    private final String program;
    private final double value;

    public GlobalStatEntry(String role, String program, double value) {
        this.role = role;
        this.program = program;
        this.value = Math.round(value * 100.0) / 100.0; // Wow :|
    }

    public String getRole() {
        return role;
    }

    public String getProgram() {
        return program;
    }

    public double getValue() {
        return value;
    }
}
