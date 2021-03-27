package com.example.administrator.myapplication2;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView back;
    private TextView rb_good;
    private TextView rb_focus;
    private TextView rb_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        back = (ImageView)findViewById(R.id.back);
        rb_good = (TextView)findViewById(R.id.rb_good);
        rb_focus = (TextView)findViewById(R.id.rb_focus);
        rb_comment = (TextView)findViewById(R.id.rb_comment);

        Drawable[] goodD = rb_good.getCompoundDrawables();
        Drawable[] focusD = rb_focus.getCompoundDrawables();
        Drawable[] commentD = rb_comment.getCompoundDrawables();
        goodD[1].setBounds(0,0,50,45);
        focusD[1].setBounds(0,0,50,50);
        commentD[1].setBounds(0,0,50,50);
        rb_good.setCompoundDrawables(goodD[0],goodD[1],goodD[2],goodD[3]);
        rb_focus.setCompoundDrawables(focusD[0],focusD[1],focusD[2],focusD[3]);
        rb_comment.setCompoundDrawables(commentD[0],commentD[1],commentD[2],commentD[3]);

        back.setOnClickListener(this);
        rb_good.setOnClickListener(this);
        rb_focus.setOnClickListener(this);
        rb_comment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.rb_good:
                break;
            case R.id.rb_focus:
                break;
            case R.id.rb_comment:
                break;
        }
    }
}
