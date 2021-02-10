package com.example.administrator.myapplication2.Bean;

import com.example.administrator.myapplication2.OtherCognition;

/**
 * Created by Administrator on 2021/2/5.
 */

public class OtherCognitions {
    private String img;
    private String name;
    private String text;
    private String time;
    private String good;
    private String comment;

    public OtherCognitions(String img,String name,String text,String time,String good,String comment){
        this.img = img;
        this.name = name;
        this.text = text;
        this.time = time;
        this.good = good;
        this.comment = comment;
    }

    public void setImg(String img){
        this.img = img;
    }

    public String getImg(){return img;}

    public void setName(String name){
        this.name = name;
    }

    public String getName(){return name;}

    public void setText(String text){
        this.text = text;
    }

    public String getText(){return text;}

    public void setTime(String time){
        this.time = time;
    }

    public String getTime(){return time;}

    public void setGood(String good){
        this.good = good;
    }

    public String getGood(){return good;}

    public void setComment(String comment){
        this.comment = comment;
    }

    public String getComment(){return comment;}
}
