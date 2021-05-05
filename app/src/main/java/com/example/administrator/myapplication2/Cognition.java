package com.example.administrator.myapplication2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.myapplication2.Adapter.ImagePickerAdapter;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import MyClass.CustomVideoView;
import MyClass.GlideImageLoader;
import MyClass.HttpUtil;
import MyClass.InternetRequest;
import MyClass.MyStringCallBack;
import MyClass.SelectDialog;
import MyClass.UserInfo;

public class Cognition extends AppCompatActivity implements View.OnClickListener,ImagePickerAdapter.OnRecyclerViewItemClickListener{
    private ImageView back;
    private ImageView send;
    private ImageView pic;
    private TextView text;
    private CustomVideoView video;
    private MediaController mediaController;
    private EditText cognition;
    private RecyclerView image_screen;
    private TextView name;
    private TextView words;
    private TextView draw;
    private Intent intent;
    private JSONObject info;

    private HttpUtil httpUtil;
    private Map<String,String> params;

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 1;               //允许选择图片最大数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cognition);

        back = (ImageView)findViewById(R.id.back);
        send = (ImageView)findViewById(R.id.send);
        pic = (ImageView)findViewById(R.id.pic);
        text = (TextView)findViewById(R.id.text);
        text.setMovementMethod(ScrollingMovementMethod.getInstance());
        video = (CustomVideoView)findViewById(R.id.video);
        cognition = (EditText) findViewById(R.id.cognition);
        image_screen = (RecyclerView) findViewById(R.id.image_screen);
        name = (TextView)findViewById(R.id.name);
        words = (TextView)findViewById(R.id.words);
        draw = (TextView)findViewById(R.id.draw);
        draw.setVisibility(View.INVISIBLE);

        Drawable[] nameD = name.getCompoundDrawables();
        Drawable[] wordsD = words.getCompoundDrawables();
        Drawable[] drawD = draw.getCompoundDrawables();
        nameD[1].setBounds(0,0,50,45);
        wordsD[1].setBounds(0,0,50,50);
        drawD[1].setBounds(0,0,50,50);
        name.setCompoundDrawables(nameD[0],nameD[1],nameD[2],nameD[3]);
        words.setCompoundDrawables(wordsD[0],wordsD[1],wordsD[2],wordsD[3]);
        draw.setCompoundDrawables(drawD[0],drawD[1],drawD[2],drawD[3]);
        back.setOnClickListener(this);
        name.setOnClickListener(this);
        words.setOnClickListener(this);
        pic.setOnClickListener(this);
        draw.setOnClickListener(this);

        //设置图片上传相关配置
        initImagePicker();
        initWidget();

        httpUtil = new HttpUtil();
        intent = this.getIntent();
        final UserInfo UI = (UserInfo)getApplication();
        try{
            info = new JSONObject(intent.getStringExtra("info"));

            if(info.getString("type").equals("视频")){
                text.setVisibility(View.GONE);
                pic.setVisibility(View.GONE);
                video.setVisibility(View.VISIBLE);
                setVideo();
            }else if(info.getString("type").equals("文本")){
                video.setVisibility(View.GONE);
                pic.setVisibility(View.GONE);
                text.setVisibility(View.VISIBLE);
                params = new HashMap<String, String>();
                params.put("file",info.getString("file"));
                httpUtil.postRequest("http://192.168.154.1:8080/CognitionAPP/read.do",params,new MyStringCallBack() {
                    @Override
                    public void onResponse(String response, int id) {
                        super.onResponse(response, id);
                        text.setText(response);
                    }
                });
            }else{
                text.setVisibility(View.GONE);
                video.setVisibility(View.GONE);
                pic.setVisibility(View.VISIBLE);
                draw.setVisibility(View.VISIBLE);
                Glide.with(Cognition.this).load("http://192.168.154.1:8080/file/"+info.getString("file")).into(pic);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(cognition.getText())){
                    Toast.makeText(Cognition.this,"请输入内容再发送！",Toast.LENGTH_SHORT).show();
                }else{
                    final String data = cognition.getText().toString();
                    cognition.setText("");
                    try {
                        params = new HashMap<String, String>();
                        params.put("ID",UI.getId());
                        params.put("u_name",UI.getName());
                        params.put("u_img",UI.getImg());
                        params.put("c_r_id",info.getString("id"));
                        params.put("c_r_file",info.getString("file"));
                        params.put("cognition",data);

                        httpUtil.postRequest("http://192.168.154.1:8080/CognitionAPP/createCognition.do",params,new MyStringCallBack() {
                            @Override
                            public void onResponse(String response, int id) {
                                super.onResponse(response, id);
                                if(response.equals("Create successfully")){
                                    intent = new Intent(Cognition.this, OtherCognition.class);
                                    intent.putExtra("info", info.toString());
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(Cognition.this,"重评失败！",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v){
        try{
            switch (v.getId()) {
                case R.id.name:
                case R.id.words:
                    cognition.setVisibility(View.VISIBLE);
                    break;
                case R.id.draw:
                    image_screen.setVisibility(View.VISIBLE);
                    break;
                case R.id.pic:
                    List<String> imgs = new ArrayList<String>();
                    imgs.add("http://192.168.154.1:8080/file/"+info.getString("file"));
                    intent = new Intent(Cognition.this, PhotoView.class);
                    intent.putStringArrayListExtra("Urls",(ArrayList<String>) imgs);
                    intent.putExtra("currentPosition", 0);
                    startActivity(intent);
                    break;
                case R.id.back:
                    finish();
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setVideo(){
        video.setPlayPauseListener(new CustomVideoView.PlayPauseListener() {
            @Override
            public void onPlay() {
                //开始播放之后消除背景
                video.setBackground(null);
            }
            @Override
            public void onPause() {
                System.out.println("Pause!");//our needed process when the video is paused
            }
        });

        String videoUrl = null;
        try {
            videoUrl = "http://192.168.154.1:8080/file/"+info.getString("file");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = null;
        //video.setVideoURI(Uri.parse("http://192.168.154.1:8080/file/2019跨年.mp4" ));
        video.setVideoPath(videoUrl);
        //设置视频缩略图
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoUrl, new HashMap());
        bitmap = retriever.getFrameAtTime();
        video.setBackground(new BitmapDrawable(getResources(),bitmap));
        mediaController = new MediaController(this);
        video.setMediaController(mediaController);
        mediaController.setMediaPlayer(video);
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                            //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(false);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setMultiMode(true);                      //多选
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    private void initWidget() {
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        image_screen.setLayoutManager(new GridLayoutManager(this, 1));
        image_screen.setHasFixedSize(true);
        image_screen.setAdapter(adapter);
    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style.transparentFrameWindowStyle, listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                Intent intent1 = new Intent(Cognition.this, ImageGridActivity.class);
                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS,true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null){
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null){
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        }
    }
}
