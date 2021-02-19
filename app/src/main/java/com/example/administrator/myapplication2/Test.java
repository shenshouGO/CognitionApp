package com.example.administrator.myapplication2;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import MyClass.GlideImageLoader;
import MyClass.HttpUtil;
import MyClass.MyStringCallBack;
import MyClass.NineGridLayout;
import MyClass.SelectDialog;
import MyClass.Tools;
import okhttp3.Call;

public class Test extends AppCompatActivity {
    private Button b;
    private TextView text;
    private HttpUtil httpUtil;
    final Handler handler = new MyHandler();
    private Map<String, String> params;
    private ArrayList<String> mData;
    private List<String> urlList;
    private NineGridLayout  layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        b = (Button)findViewById(R.id.b);
        text = (TextView)findViewById(R.id.text);
        httpUtil = new HttpUtil();

        urlList = new ArrayList<String>();//图片url
        urlList.add("http://192.168.154.1:8080/file/1.jpg");

        //加载ImageLoader，不加会报错
        ImageLoader imageLoader;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        layout = (NineGridLayout) findViewById(R.id.layout_nine_grid);
        layout.setIsShowAll(false); //当传入的图片数超过9张时，是否全部显示
        layout.setSpacing(10); //动态设置图片之间的间隔
        layout.setUrlList(urlList); //最后再设置图片url

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlList.add("http://192.168.154.1:8080/file/1.jpg");
                layout.setUrlList(urlList);

                //图片预览
//                mData = new ArrayList<String>();
//                mData.add("http://47.95.197.189:8080/file/1.jpg");
//                Intent intent = new Intent(Test.this, PhotoView.class);
//                intent.putStringArrayListExtra("Urls",mData);
//                intent.putExtra("currentPosition", 0);
//                startActivity(intent);
            }
        });
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 1) {
//                    text.setText((String)msg.obj);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
