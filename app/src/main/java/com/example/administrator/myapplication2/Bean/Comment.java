package com.example.administrator.myapplication2.Bean;

import org.json.JSONObject;

/**
 * Created by Administrator on 2021/2/9.
 */

public class Comment {
    private String id;
    //评论者
    private String u_id;
    private String u_name;
    private String u_img;
    //回复评论id（不为0则为回复）
    private String c_c_id;
    //回复者
    private String r_u_id;
    private String r_u_name;
    private String r_u_img;
    //回复
    private String comment;
    private String time;
    private String good;

    public Comment(JSONObject JO){
        try{
            id = JO.getString("id");
            u_id = JO.getString("u_id");
            u_name = JO.getString("u_name");
            u_img = JO.getString("u_img");
            if(JO.has("c_c_id")||JO.has("s_c_id")){
                if(JO.has("c_c_id"))
                    c_c_id = JO.getString("c_c_id");
                else
                    c_c_id = JO.getString("s_c_id");
                r_u_id = JO.getString("r_u_id");
                r_u_name = JO.getString("r_u_name");
                r_u_img = JO.getString("r_u_img");
            }else {
                c_c_id = "0";
                r_u_id = "";
                r_u_name = "";
                r_u_img = "";
            }
            comment = JO.getString("comment");
            time = JO.getString("time");
            good = JO.getString("good");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getU_img() {
        return u_img;
    }

    public void setU_img(String u_img) {
        this.u_img = u_img;
    }

    public String getC_c_id() {
        return c_c_id;
    }

    public void setC_c_id(String c_c_id) {
        this.c_c_id = c_c_id;
    }

    public String getR_u_id() {
        return r_u_id;
    }

    public void setR_u_id(String r_u_id) {
        this.r_u_id = r_u_id;
    }

    public String getR_u_name() {
        return r_u_name;
    }

    public void setR_u_name(String r_u_name) {
        this.r_u_name = r_u_name;
    }

    public String getR_u_img() {
        return r_u_img;
    }

    public void setR_u_img(String r_u_img) {
        this.r_u_img = r_u_img;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGood() {
        return good;
    }

    public void setGood(String good) {
        this.good = good;
    }
}
