package com.example.administrator.myapplication2.Bean;

/**
 * Created by Administrator on 2021/2/2.
 */

public class Resource {
    private String path;
    private String title;

    public Resource(String path,String title){
        this.path = path;
        this.title = title;
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
}
