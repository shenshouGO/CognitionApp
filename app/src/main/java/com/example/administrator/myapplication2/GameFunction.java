package com.example.administrator.myapplication2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class GameFunction extends AppCompatActivity implements View.OnClickListener{
    private TextView quick_join;
    private TextView choose_join;
    private TextView create_room;
    private TextView search_room;
    private TextView follow_room;
    private TextView username;
    private Button btn_OK;
    private Button btn_close;
    private View choose_join_view;
    private View create_room_view;
    private View search_room_view;
    private Button btn_OK_create_room;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private Switch swh_status;
    private EditText pass;
    private Intent intent;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_function);

        quick_join = (TextView) findViewById(R.id.quick_join);
        choose_join = (TextView) findViewById(R.id.choose_join);
        create_room = (TextView) findViewById(R.id.create_room);
        search_room = (TextView) findViewById(R.id.search_room);
        follow_room = (TextView) findViewById(R.id.follow_room);
        username = (TextView) findViewById(R.id.name);
        intent=this.getIntent();
        name = intent.getStringExtra("name");
        username.setText(name);

        final LayoutInflater inflater = GameFunction.this.getLayoutInflater();
        choose_join_view = inflater.inflate(R.layout.choose_join, null,false);
        create_room_view = inflater.inflate(R.layout.create_room, null,false);
        search_room_view = inflater.inflate(R.layout.search_room, null,false);
        btn_OK_create_room = (Button)create_room_view.findViewById(R.id.btn_OK_create_room);
        pass = (EditText)create_room_view.findViewById(R.id.pass);
        pass.setEnabled(false);
        swh_status = (Switch) create_room_view.findViewById(R.id.set);

        quick_join.setOnClickListener(this);
        choose_join.setOnClickListener(this);
        create_room.setOnClickListener(this);
        search_room.setOnClickListener(this);
        swh_status.setOnClickListener(this);
        btn_OK_create_room.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.choose_join:
                setView(choose_join_view);
                break;
            case R.id.create_room:
                setView(create_room_view);
                break;
            case R.id.search_room:
                setView(search_room_view);
                break;
            case R.id.set:
                if(swh_status.isChecked())
                {
                    pass.setEnabled(true);
                    pass.setFocusableInTouchMode(true);
                    pass.setFocusable(true);
                    pass.setBackgroundColor(Color.parseColor("#FFE6E9F5"));
                }
                else
                {
                    pass.setEnabled(false);
                    pass.setBackgroundColor(Color.GRAY);
                }
                break;
            case R.id.btn_OK_create_room:
                intent = new Intent(GameFunction.this, Room.class);
                intent.putExtra("num",0);
                intent.putExtra("name", name);
                startActivity(intent);
                alert.dismiss();
                break;
            case R.id.quick_join:
                intent = new Intent(GameFunction.this, Room.class);
                intent.putExtra("num",1);
                intent.putExtra("name", name);
                startActivity(intent);
                break;
            case R.id.btn_close:
                alert.dismiss();
                break;
        }
    }

    private void setView(View v){
        btn_close = v.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(this);
        builder = new AlertDialog.Builder(this);
        builder.setView(v);
        builder.setCancelable(false);
        if((ViewGroup)v.getParent() != null)
        {
            ((ViewGroup)v.getParent()).removeView(v);
        }
        alert = builder.create();
        alert.show();
    }
}