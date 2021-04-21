package com.example.administrator.myapplication2.Bean;

/**
 * Created by Administrator on 2021/1/15.
 */

public class User {
    private String name;
    private String img;
    private int num;
    private String status;

    public User(String name,String img,int num,String status){
        this.name = name;
        this.img = img;
        this.num = num;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getImg() {
        return img;
    }

    public int getNum() {
        return num;
    }

    public String getStatus() {
        return status;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setImg(String img){
        this.img = img;
    }

    public void setNum(int num){
        this.num = num;
    }

    public void setStatus(String status){
        this.status = status;
    }
}
