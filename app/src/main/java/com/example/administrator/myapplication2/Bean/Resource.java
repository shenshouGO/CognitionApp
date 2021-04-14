package com.example.administrator.myapplication2.Bean;

/**
 * Created by Administrator on 2021/2/2.
 */

public class Resource {
    private String path,title,type,theme;

    public Resource(String path,String title,String type,String theme){
        this.path = path;
        this.title = title;
        this.type = type;
        this.theme = theme;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
