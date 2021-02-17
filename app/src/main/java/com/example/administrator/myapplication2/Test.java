package com.example.administrator.myapplication2;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.myapplication2.Adapter.HorizontalListViewAdapter;
import com.example.administrator.myapplication2.Adapter.ImagePickerAdapter;
import com.example.administrator.myapplication2.Adapter.ScreenAdapter;
import com.example.administrator.myapplication2.Bean.Resource;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import MyClass.GlideImageLoader;
import MyClass.HttpUtil;
import MyClass.MyStringCallBack;
import MyClass.SelectDialog;
import MyClass.Tools;
import okhttp3.Call;

public class Test extends AppCompatActivity {
    private Button b;
    private TextView text;
    private HttpUtil httpUtil;
    final Handler handler = new MyHandler();
    private Map<String, String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        b = (Button)findViewById(R.id.b);
        text = (TextView)findViewById(R.id.text);
        httpUtil = new HttpUtil();
        Long l = new Date().getTime();
//        httpUtil.download("http://47.95.197.189:8080/file/1_1613390055558.txt","download", new HttpUtil.OnDownloadListener() {
//            @Override
//            public void onDownloadSuccess() {
//                Log.e("S","XXX");
//                String s = Tools.readTxt("/storage/emulated/0/Download/1_1613390055558.txt");
//                Message message = new Message();
//                message.what = 1;
//                message.obj = s;
//                handler.sendMessage(message);
//            }
//            @Override
//            public void onDownloading(int progress) {
//
//            }
//            @Override
//            public void onDownloadFailed() {
//            }
//        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params = new HashMap<String, String>();
                params.put("name","name");
                params.put("img","img");
                httpUtil.postRequest("http://192.168.154.1:8080/CognitionAPP/hello.do",params,new MyStringCallBack());
            }
        });
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 1) {
                    text.setText((String)msg.obj);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
