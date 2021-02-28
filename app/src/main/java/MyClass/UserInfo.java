package MyClass;

import android.app.Application;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Administrator on 2021/2/5.
 */

public class UserInfo extends Application {
    private String id;
    private String name;
    private String img;
    private String telephone;

    @Override
    public void onCreate(){
        super.onCreate();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getImg(){
        return img;
    }

    public void setImg(String img){
        this.img = img;
    }

    public String getTelephone(){
        return telephone;
    }

    public void setTelephone(String telephone){
        this.telephone = telephone;
    }

}
