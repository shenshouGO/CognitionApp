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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication2.Adapter.ImagePickerAdapter;
import com.example.administrator.myapplication2.Adapter.PictureAdapter;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import MyClass.GlideImageLoader;
import MyClass.HttpUtil;
import MyClass.MyStringCallBack;
import MyClass.SelectDialog;
import MyClass.UserInfo;
import okhttp3.Call;

public class ReleaseScene extends AppCompatActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener{
    private ImageView send;
    private TextView title;
    private EditText scene;
    private RecyclerView image_screen;
    private ArrayList<String> mPicList = new ArrayList<>();
    private PictureAdapter PA;
    private Map<String,String> params;
    private String content;
    final Handler handler = new MyHandler();
    private Intent intent;
    private String type;

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 9;               //允许选择图片最大数

    private HttpUtil httpUtil;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_scene);

        title = (TextView) findViewById(R.id.title);
        send = (ImageView)findViewById(R.id.send);
        scene = (EditText)findViewById(R.id.scene);
        image_screen = (RecyclerView) findViewById(R.id.image_screen);
        httpUtil = new HttpUtil();
        intent = getIntent();
        type = intent.getStringExtra("type");
        if(type.equals("发布动态")){
            title.setText("发布动态");
            scene.setHint("说点什么吧...");
        }
        final UserInfo UI = (UserInfo)getApplication();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = scene.getText().toString();
//                JSONObject JO = new JSONObject();
                String s="";
                try {
                    s = java.net.URLEncoder.encode(content,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
//                try {
//                    JO.put("content",content);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                Log.e("content:",content);
                Log.e("s:",s);

//                params = new HashMap<String, String>();
//                params.put("u_id",""+1);
//                params.put("u_name",UI.getName());
//                params.put("img","img");
//                params.put("scene",s);
//                params.put("time",""+new Date().getTime());

                url="http://192.168.154.1:8080/CognitionAPP/createScene.do?";
                url+="u_id="+UI.getId();
                url+="&u_name="+UI.getName();
                url+="&u_img="+UI.getImg();
                if(type.equals("发布动态")){
                    url+="&type=动态";
                }else {
                    url+="&type=情景";
                }
                url+="&scene="+s;
                url+="&time="+new Date().getTime();
                Log.e("url",url);
                uploadImage(selImageList);
            }
        });

        initImagePicker();
        initWidget();
    }

    private void uploadImage(ArrayList<ImageItem> pathList) {
        httpUtil.postFileRequest(url, null, pathList, new MyStringCallBack() {

            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                Log.e("response:","onError");
                Toast.makeText(ReleaseScene.this,"发送失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                super.onResponse(response, id);
                Log.e("response:",response+" "+id);
                if(response.equals("Create successfully")){
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }
        });
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 1) {
                    Toast.makeText(ReleaseScene.this,"发布成功",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
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

        image_screen.setLayoutManager(new GridLayoutManager(this, 3));
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
                List<String> names = new ArrayList<>();
                names.add("拍照");
                names.add("相册");
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0: // 直接调起相机
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent = new Intent(ReleaseScene.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS,true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(ReleaseScene.this, ImageGridActivity.class);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                break;
                            default:
                                break;
                        }
                    }
                }, names);
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
