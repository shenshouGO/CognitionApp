package com.example.administrator.myapplication2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.myapplication2.Adapter.HorizontalListViewAdapter;
import com.example.administrator.myapplication2.Adapter.ScreenAdapter;
import com.example.administrator.myapplication2.Bean.Resource;

import java.util.LinkedList;
import java.util.List;

import MyClass.FileUtils;
import MyClass.HorizontalListView;

public class Test extends AppCompatActivity{
    private Button b;
    private ImageView iv;
    private String path;
    private Context context;
    private Dialog dialog;
    private Bitmap bmp;
    private ImageView imageView;
    private boolean big;
    private Intent intent;
    private Bundle bundle;
    private HorizontalListView resourceList;
    private HorizontalListViewAdapter hListViewAdapter;
    private List<Resource> resources;
    private ListView lv;
    private ScreenAdapter sa;
    private List<Resource> screens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        resourceList = (HorizontalListView)findViewById(R.id.resourceList);
        lv = (ListView) findViewById(R.id.resourceScreen);
        resources = new LinkedList<Resource>() ;
        screens = new LinkedList<Resource>() ;
        path = "http://47.95.197.189:8080/file/";

        resources.add(new Resource(path+"4.jpeg","TOP1"));
        resources.add(new Resource(path+"2.jpg","TOP2"));
        resources.add(new Resource(path+"timg.jpg","TOP3"));
        resources.add(new Resource(path+"5.jpeg","TOP4"));

        screens.add(new Resource(path+"4.jpeg","学习|文字|最新"));
        screens.add(new Resource(path+"2.jpg","政治|图片|最新"));
        screens.add(new Resource(path+"timg.jpg","学习|文字|最新"));
        screens.add(new Resource(path+"5.jpeg","政治|图片|最新"));

        hListViewAdapter = new HorizontalListViewAdapter(Test.this,(List<Resource>)resources);
        resourceList.setAdapter(hListViewAdapter);
        sa = new ScreenAdapter(Test.this,(List<Resource>)screens);
        lv.setAdapter(sa);

        resourceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(Test.this,"你点击了第" + i + "项",Toast.LENGTH_SHORT).show();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(Test.this,"你点击了第" + i + "项",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
