package com.example.administrator.myapplication2.Bean;

/**
 * Created by Administrator on 2021/1/25.
 */

public class DoubleScore {
    private String name,validity,novelty,average;

    public DoubleScore(String name, String validity, String novelty, String average){
        this.name = name;
        this.validity = validity;
        this.novelty = novelty;
        this.average = average;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getNovelty() {
        return novelty;
    }

    public void setNovelty(String novelty) {
        this.novelty = novelty;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }
}
