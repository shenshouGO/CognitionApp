package com.example.administrator.myapplication2.Bean;

import org.json.JSONObject;

/**
 * Created by Administrator on 2021/3/12.
 */

public class JsonBean {
    private JSONObject JO;

    public JsonBean(JSONObject JO) {
        this.JO = JO;
    }

    public JSONObject getJO() {
        return JO;
    }

    public void setJO(JSONObject JO) {
        this.JO = JO;
    }
}
