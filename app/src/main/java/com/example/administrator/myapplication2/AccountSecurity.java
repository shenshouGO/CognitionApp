package com.example.administrator.myapplication2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountSecurity extends AppCompatActivity implements View.OnClickListener{
    private ImageView back;
    private TextView modify_password;
    private TextView set_pass_question;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_security);

        back = (ImageView)findViewById(R.id.back);
        modify_password = (TextView)findViewById(R.id.modify_password);
        set_pass_question = (TextView)findViewById(R.id.set_pass_question);

        back.setOnClickListener(this);
        modify_password.setOnClickListener(this);
        set_pass_question.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.modify_password:
                intent = new Intent(AccountSecurity.this,ModifyPassword.class);
                startActivity(intent);
                break;
            case R.id.set_pass_question:
                intent = new Intent(AccountSecurity.this,SetPasswordQuestion.class);
                startActivity(intent);
                break;
        }
    }
}
