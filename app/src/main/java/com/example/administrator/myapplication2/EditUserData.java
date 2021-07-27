package com.example.administrator.myapplication2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import MyClass.HttpUtil;
import MyClass.MyStringCallBack;
import MyClass.UserInfo;
import okhttp3.Call;

public class EditUserData extends AppCompatActivity implements View.OnClickListener{
    private ImageView back;
    private EditText nickname_content;
    private RadioButton male;
    private RadioButton female;
    private TextView age_content;
    private TextView birthday_content;
    private TextView job_content;
    private RelativeLayout telephone;
    private EditText telephone_content;
    private EditText email_content;
    private Button preserve;

    private HttpUtil httpUtil;
    private Map<String,String> params;
    private JSONObject results;
    private JSONObject JO;
    private Intent intent;
    private Message message;
    final Handler handler = new MyHandler();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String[] jobs = {"学生","教师","程序员","公务员","医生","律师","其他"};
    private int job_number;

    private JSONObject info;
    private int sign = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_data);

        back = (ImageView) findViewById(R.id.back);
        nickname_content = (EditText) findViewById(R.id.nickname_content);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        age_content = (TextView) findViewById(R.id.age_content);
        birthday_content = (TextView) findViewById(R.id.birthday_content);
        job_content = (TextView) findViewById(R.id.job_content);
        telephone = (RelativeLayout) findViewById(R.id.telephone);
        telephone_content = (EditText) findViewById(R.id.telephone_content);
        email_content = (EditText) findViewById(R.id.email_content);
        preserve = (Button) findViewById(R.id.preserve);

        back.setOnClickListener(this);
        birthday_content.setOnClickListener(this);
        job_content.setOnClickListener(this);
        preserve.setOnClickListener(this);
        telephone.setVisibility(View.GONE);

        httpUtil = new HttpUtil();
        intent = getIntent();
        try {
            info = new JSONObject(intent.getStringExtra("info"));
            nickname_content.setText(info.getString("name"));
            if(info.getString("sex").equals("0")){
                female.setChecked(true);
            }else{
                male.setChecked(true);
            }
            age_content.setText(info.getString("age"));
            birthday_content.setText(info.getString("birthday"));
            job_content.setText(info.getString("job"));
            telephone_content.setText(info.getString("telephone"));
            email_content.setText(info.getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.birthday_content:
                AlertDialog.Builder builder = new AlertDialog.Builder(EditUserData.this);
                View view = (RelativeLayout) getLayoutInflater().inflate(R.layout.date_selector, null);
                DatePicker datePicker = (DatePicker) view.findViewById(R.id.date);
                //初始化当前日期
                Calendar calendar = Calendar.getInstance();
                try {
                    if(info.getString("birthday") == null){
                        calendar.setTime(simpleDateFormat.parse(System.currentTimeMillis()+""));
                    }else {
                        calendar.setTime(simpleDateFormat.parse(info.getString("birthday")));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH), null);
                datePicker.setMaxDate(System.currentTimeMillis());
                //设置date布局
                builder.setView(view);
                builder.setTitle("设置出生日期");
                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //日期格式
                        StringBuffer sb = new StringBuffer();
                        sb.append(String.format("%d-%02d-%02d",
                                datePicker.getYear(),
                                datePicker.getMonth() + 1,
                                datePicker.getDayOfMonth()));
                        message = Message.obtain();
                        message.what = 1;
                        message.obj = datePicker.getYear()+"-"+(datePicker.getMonth() + 1)+"-"+datePicker.getDayOfMonth();
                        handler.sendMessage(message);
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("取  消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog time_alert = builder.create();
                time_alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {                    //
                        Button positiveButton = ((AlertDialog) dialog)
                                .getButton(AlertDialog.BUTTON_POSITIVE);
                        positiveButton.setTextColor(Color.parseColor("#548235"));
                        Button negativeButton = ((AlertDialog) dialog)
                                .getButton(AlertDialog.BUTTON_NEGATIVE);
                        negativeButton.setTextColor(Color.parseColor("#548235"));
                    }
                });
                time_alert.show();
                break;
            case R.id.job_content:
                AlertDialog.Builder job_builder = new AlertDialog.Builder(EditUserData.this);
                View job_view = (RelativeLayout) getLayoutInflater().inflate(R.layout.number_selecter, null);
                NumberPicker  numberPicker = (NumberPicker) job_view.findViewById(R.id.number_picker);
                //设置需要显示的内容数组
                numberPicker.setDisplayedValues(jobs);
                //设置最大最小值
                numberPicker.setMinValue(0);
                numberPicker.setMaxValue(jobs.length-1);
                //设置默认的位置
                numberPicker.setValue(0);
                //这里设置为不循环显示，默认值为true
                numberPicker.setWrapSelectorWheel(false);
                //设置不可编辑
                numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

                job_builder.setView(job_view);
                job_builder.setTitle("选择职业");
                job_builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        job_content.setText(jobs[numberPicker.getValue()]);
                        dialog.cancel();
                    }
                });
                job_builder.setNegativeButton("取  消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog job_alert = job_builder.create();
                job_alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {                    //
                        Button positiveButton = ((AlertDialog) dialog)
                                .getButton(AlertDialog.BUTTON_POSITIVE);
                        positiveButton.setTextColor(Color.parseColor("#548235"));
                        Button negativeButton = ((AlertDialog) dialog)
                                .getButton(AlertDialog.BUTTON_NEGATIVE);
                        negativeButton.setTextColor(Color.parseColor("#548235"));
                    }
                });
                job_alert.show();
                break;
            case R.id.preserve:
                if(isEmail(email_content.getText().toString())){
                    params = new HashMap<String ,String>();
                    params.put("name",nickname_content.getText().toString());
                    httpUtil.postRequest("http://59.110.215.154:8080/CognitionAPP/checkName.do",params,new MyStringCallBack(){
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Toast.makeText(EditUserData.this,"保存失败！",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            boolean sign = true;
                            final UserInfo UI = (UserInfo)getApplication();
                            if (response.equals("Registered")){
                                if (nickname_content.getText().toString().equals(UI.getName())){
                                    sign = true;
                                }else{
                                    sign = false;
                                    Toast.makeText(EditUserData.this, "该昵称已被注册！", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if(sign){
                                params = new HashMap<String ,String>();
                                params.put("u_id",UI.getId());
                                params.put("name",nickname_content.getText().toString());
                                if (male.isChecked()){
                                    params.put("sex","1");
                                }else{
                                    params.put("sex","0");
                                }
                                params.put("age",age_content.getText().toString());
                                params.put("birthday",birthday_content.getText().toString());
                                params.put("job",job_content.getText().toString());
                                params.put("email",email_content.getText().toString());
                                httpUtil.postRequest("http://59.110.215.154:8080/CognitionAPP/modifyData.do",params,new MyStringCallBack() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        Toast.makeText(EditUserData.this, "保存失败！", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        if(response.equals("Modify data successfully")){
                                            Toast.makeText(EditUserData.this, "保存成功！", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }else{
                                            Toast.makeText(EditUserData.this, "保存失败！", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }else{
                    Toast.makeText(EditUserData.this,"请输入正确的邮箱格式！",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what){
                    case 1:
                        int age = getAge(simpleDateFormat.parse((String) msg.obj));
                        info.put("age",age);
                        info.put("birthday",(String)msg.obj);
                        birthday_content.setText((String)msg.obj);
                        age_content.setText(""+age);
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private NumberPicker.OnValueChangeListener mValueChangeListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            job_number = newVal;
        }
    };

    public static int getAge(Date date){
        int age = 0;
        Calendar born = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        born.setTime(date);
        age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
        int nowDayOfYear = now.get(Calendar.DAY_OF_YEAR);
        int bornDayOfYear = born.get(Calendar.DAY_OF_YEAR);
        System.out.println("nowDayOfYear:" + nowDayOfYear + " bornDayOfYear:" + bornDayOfYear);
        if (nowDayOfYear < bornDayOfYear) {
            age -= 1;
        }
        return age;
    }

    public static boolean isEmail(String email) {
        Pattern emailPattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher matcher = emailPattern.matcher(email);
        if(matcher.find()){
            return true;
        }
        return false;
    }
}
