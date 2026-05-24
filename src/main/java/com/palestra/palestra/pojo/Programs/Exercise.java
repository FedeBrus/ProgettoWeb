package com.palestra.palestra.pojo.Programs;

public class Exercise {
    private String name;
    private int sets;
    private int reps;
    private int kcal;

    public Exercise() {

    }

    public Exercise(String name, int sets, int reps, int kcal) {
        this.name = name;
        this.sets = sets;
        this.reps = reps;
        this.kcal = kcal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public int getKcal() { return kcal; }
}
