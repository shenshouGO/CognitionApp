package MyClass;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.getuitest.demo.DemoApplication;
import com.example.administrator.getuitest.demo.config.Config;
import com.example.administrator.getuitest.demo.ui.activity.GetuiSdkDemoActivity;
import com.example.administrator.getuitest.demo.ui.presenter.AuthInteractor;
import com.example.administrator.myapplication2.R;
import com.igexin.sdk.IUserLoggerInterface;
import com.igexin.sdk.PushManager;

import java.lang.ref.WeakReference;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Administrator on 2021/2/5.
 */

public class UserInfo extends Application implements AuthInteractor.IAuthFinished{
    private String id;
    private String name;
    private String img;
    private String telephone;

    private static final String TAG = "GetuiSdkDemo";
    public static WeakReference<GetuiSdkDemoActivity> demoActivity;
    public static String isCIDOnLine = "";
    public static String cid = "";
    public static Context appContext;
    public static boolean isSignError = false;

    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
     */
    public static StringBuilder payloadData = new StringBuilder();
    private static DemoHandler handler;

    public static void sendMessage(Message msg) {
        handler.sendMessage(msg);
    }

    @Override
    public void onCreate(){
        super.onCreate();
        appContext = this;
        Log.e(TAG, "DemoApplication onCreate");

        Config.init(this);
        if (handler == null) {
            handler = new DemoHandler();
        }
        startAuth();
        initSdk();
    }

    private void initSdk() {
        Log.e(TAG, "initializing sdk...");
        PushManager.getInstance().initialize(this);
        if (true) {
            //切勿在 release 版本上开启调试日志
            PushManager.getInstance().setDebugLogger(this, new IUserLoggerInterface() {

                @Override
                public void log(String s) {

                }
            });
        }
    }

    private void startAuth() {
        AuthInteractor interactor = new AuthInteractor();
        interactor.checkTime(this);
        interactor.fetchAuthToken(this);
    }

    @Override
    public void onAuthFinished(String token) {
        Config.authToken = token;
        Log.e(TAG, "鉴权成功,token = " + token);
    }

    @Override
    public void onAuthFailed(String msg) {
        if (msg.equals("sign_error")) {
            Log.e(TAG, "鉴权失败,请检查签名参数");
            isSignError = true;
        }
        Log.e(TAG, "onAuthFailed = " + msg);
    }


    public static class DemoHandler extends Handler {
        public static final int RECEIVE_MESSAGE_DATA = 0; //接收到消息
        public static final int RECEIVE_CLIENT_ID = 1; //cid通知
        public static final int RECEIVE_ONLINE_STATE = 2; //cid在线状态通知消息
        public static final int SHOW_TOAST = 3; //Toast消息


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RECEIVE_MESSAGE_DATA:  //接收到消息
                    Log.e(TAG,"message:"+(String)msg.obj);
                    break;

                case RECEIVE_CLIENT_ID:  //cid通知
                    Log.e(TAG,"cid:"+(String)msg.obj);
                    break;
                case RECEIVE_ONLINE_STATE:  //cid在线状态通知
                    Log.e(TAG,"isCIDOnLine:"+(String)msg.obj);
                    break;
                case SHOW_TOAST:  //需要弹出Toast
                    Toast.makeText(DemoApplication.appContext, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }

        }

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
