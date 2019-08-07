package com.htwk.jseiffer.poll;

public class Party {
    private String name;
    private float percent;

    public Party(String name, float percent) {
        this.name = name;
        this.percent = percent;
    }

    public String getName() {
        return name;
    }

    public float getPercent() {
        return percent;
    }

    @Override
    public String toString() {
        return  "\'" + name + '\'' +
                ", " + percent;
    }
}
