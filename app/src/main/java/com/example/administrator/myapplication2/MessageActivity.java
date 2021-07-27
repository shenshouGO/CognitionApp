package com.example.administrator.myapplication2;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import MyClass.UserInfo;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView back;
    private ImageView img;
    private TextView name;
    private TextView rb_good;
    private TextView rb_focus;
    private TextView rb_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        back = (ImageView)findViewById(R.id.back);
        img = (ImageView) findViewById(R.id.img);
        name = (TextView) findViewById(R.id.name);
        rb_good = (TextView)findViewById(R.id.rb_good);
        rb_focus = (TextView)findViewById(R.id.rb_focus);
        rb_comment = (TextView)findViewById(R.id.rb_comment);

        Drawable[] goodD = rb_good.getCompoundDrawables();
        Drawable[] focusD = rb_focus.getCompoundDrawables();
        Drawable[] commentD = rb_comment.getCompoundDrawables();
        goodD[1].setBounds(0,0,130,120);
        focusD[1].setBounds(0,0,130,120);
        commentD[1].setBounds(0,0,130,120);
        rb_good.setCompoundDrawables(goodD[0],goodD[1],goodD[2],goodD[3]);
        rb_focus.setCompoundDrawables(focusD[0],focusD[1],focusD[2],focusD[3]);
        rb_comment.setCompoundDrawables(commentD[0],commentD[1],commentD[2],commentD[3]);

        back.setOnClickListener(this);
        rb_good.setOnClickListener(this);
        rb_focus.setOnClickListener(this);
        rb_comment.setOnClickListener(this);

        final UserInfo UI = (UserInfo)getApplication();
        name.setText(UI.getName());
        Glide.with(MessageActivity.this).load("http://59.110.215.154:8080/resource/"+UI.getImg()).into(img);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.rb_good:
                Toast.makeText(MessageActivity.this,"此功能暂不可用",Toast.LENGTH_SHORT).show();
                break;
            case R.id.rb_focus:
                Toast.makeText(MessageActivity.this,"此功能暂不可用",Toast.LENGTH_SHORT).show();
                break;
            case R.id.rb_comment:
                Toast.makeText(MessageActivity.this,"此功能暂不可用",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
