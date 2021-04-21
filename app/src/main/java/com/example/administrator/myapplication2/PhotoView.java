package com.example.administrator.myapplication2;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myapplication2.Adapter.ImagePreviewAdapter;
import com.example.administrator.myapplication2.Adapter.MixedAdapter;
import com.example.administrator.myapplication2.Bean.OtherCognitions;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import MyClass.FileUtils;
import MyClass.PhotoViewPager;
import cn.hzw.doodle.DoodleActivity;
import cn.hzw.doodle.DoodleParams;

public class PhotoView extends AppCompatActivity {
    public static final String TAG = PhotoView.class.getSimpleName();
    private PhotoViewPager mViewPager;
    private int currentPosition;
    private ImagePreviewAdapter adapter;
    private TextView mTvImageCount;
    private TextView mTvSaveImage;
    private ImageView draw;
    private List<String> Urls;
    private String path;
    private String urlPath;
    private String localPath;
    final Handler handler = new MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        initView();
        initData();
    }

    private void initView() {
        mViewPager = (PhotoViewPager) findViewById(R.id.view_pager_photo);
        mTvImageCount = (TextView) findViewById(R.id.tv_image_count);
        draw = (ImageView) findViewById(R.id.draw);
    }

    private void initData() {
        Intent intent = getIntent();
        currentPosition = intent.getIntExtra("currentPosition",0);
        Urls = intent.getStringArrayListExtra("Urls");
        localPath = Environment.getExternalStorageDirectory().toString() + "/shidoe";

        adapter = new ImagePreviewAdapter(Urls, this);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(currentPosition, false);
        mTvImageCount.setText(currentPosition+1 + "/" + Urls.size());
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition = position;
                mTvImageCount.setText(currentPosition + 1 + "/" + Urls.size());
            }
        });

        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlPath = Urls.get(currentPosition);
                path = System.currentTimeMillis()+"."+urlPath.substring(urlPath.lastIndexOf('.')+1,urlPath.length());
                downLoad(urlPath,path);
                path = localPath+"/" + path;
            }
        });
    }

    public void downLoad(final String path, final String FileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(5000);
                    con.setConnectTimeout(5000);
                    con.setRequestProperty("Charset", "UTF-8");
                    con.setRequestMethod("GET");
                    if (con.getResponseCode() == 200) {
                        InputStream is = con.getInputStream();//获取输入流
                        FileOutputStream fileOutputStream = null;//文件输出流
                        if (is != null) {
                            fileOutputStream = new FileOutputStream(new File(localPath, FileName));//指定文件保存路径，代码看下一步
                            byte[] buf = new byte[1024];
                            int ch;
                            while ((ch = is.read(buf)) != -1) {
                                fileOutputStream.write(buf, 0, ch);//将获取到的流写入文件中
                            }
                            Message message = Message.obtain();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what){
                    case 1://涂鸦界面渲染
                        DoodleParams params = new DoodleParams(); // 涂鸦参数
                        params.mImagePath = path;
                        params.mSavePath = localPath;
                        params.mSavePathIsDir = true;
                        DoodleActivity.startActivityForResult(PhotoView.this, params,0);
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
