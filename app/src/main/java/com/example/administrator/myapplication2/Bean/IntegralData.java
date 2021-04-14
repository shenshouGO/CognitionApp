package com.example.administrator.myapplication2.Bean;

/**
 * Created by Administrator on 2021/4/9.
 */

public class IntegralData {
    private String integral,source,time;

    public IntegralData(String integral, String source, String time) {
        this.integral = integral;
        this.source = source;
        this.time = time;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
