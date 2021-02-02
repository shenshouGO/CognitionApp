package com.example.administrator.myapplication2.Bean;

/**
 * Created by Administrator on 2021/1/22.
 */

public class Answer {
    private String answer,name;

    public Answer(String name,String answer){
        this.name = name;
        this.answer = answer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
