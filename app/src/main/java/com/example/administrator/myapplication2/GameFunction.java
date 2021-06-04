package com.example.administrator.myapplication2;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import MyClass.UserInfo;

public class GameFunction extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{
    private ImageView img;
    private TextView username;
    private TextView account;
    private TextView task;
    private TextView notice;
    private TextView rank;
    private TextView course;
    private TextView quick_join;
    private TextView choose_join;
    private TextView create_room;
    private TextView search_room;
    private TextView follow_room;

    private Button btn_OK;
    private Button btn_close;
    private View choose_join_view;
    private View create_room_view;
    private View search_room_view;
    private Button btn_OK_create_room;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private Spinner type_spin;
    private Spinner mode_spin;
    private Spinner subject_spin;
    private Switch swh_status;
    private EditText pass;
    private Intent intent;
    private String name;
    private String telephone;
    private ImageView back;

    private String type;
    private String mode;
    private String subject;
    private Random random;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_function);

        task = (TextView) findViewById(R.id.task);
        notice = (TextView) findViewById(R.id.notice);
        rank = (TextView) findViewById(R.id.rank);
        course = (TextView) findViewById(R.id.course);
        quick_join = (TextView) findViewById(R.id.quick_join);
        choose_join = (TextView) findViewById(R.id.choose_join);
        create_room = (TextView) findViewById(R.id.create_room);
        search_room = (TextView) findViewById(R.id.search_room);
        follow_room = (TextView) findViewById(R.id.follow_room);
        username = (TextView) findViewById(R.id.name);
        account = (TextView) findViewById(R.id.account);
        back = (ImageView) findViewById(R.id.back);
        intent=this.getIntent();
        final UserInfo UI = (UserInfo) getApplication();
        name = UI.getName();
        telephone = UI.getTelephone();
        username.setText(name);
        account.setText(telephone);

        Drawable[] taskD = task.getCompoundDrawables();
        Drawable[] noticeD = notice.getCompoundDrawables();
        Drawable[] rankD = rank.getCompoundDrawables();
        Drawable[] courseD = course.getCompoundDrawables();
        Drawable[] quickD = quick_join.getCompoundDrawables();
        Drawable[] chooseD = choose_join.getCompoundDrawables();
        Drawable[] createD = create_room.getCompoundDrawables();
        Drawable[] searchD = search_room.getCompoundDrawables();
        Drawable[] followD = follow_room.getCompoundDrawables();
        taskD[1].setBounds(0,0,80,80);
        noticeD[1].setBounds(0,0,80,80);
        rankD[1].setBounds(0,0,80,80);
        courseD[1].setBounds(0,0,80,80);
        quickD[1].setBounds(0,0,400,400);
        chooseD[1].setBounds(0,0,400,400);
        createD[1].setBounds(0,0,100,100);
        searchD[1].setBounds(0,0,100,100);
        followD[1].setBounds(0,0,100,100);
        task.setCompoundDrawables(taskD[0],taskD[1],taskD[2],taskD[3]);
        notice.setCompoundDrawables(noticeD[0],noticeD[1],noticeD[2],noticeD[3]);
        rank.setCompoundDrawables(rankD[0],rankD[1],rankD[2],rankD[3]);
        course.setCompoundDrawables(courseD[0],courseD[1],courseD[2],courseD[3]);
        quick_join.setCompoundDrawables(quickD[0],quickD[1],quickD[2],quickD[3]);
        choose_join.setCompoundDrawables(chooseD[0],chooseD[1],chooseD[2],chooseD[3]);
        create_room.setCompoundDrawables(createD[0],createD[1],createD[2],createD[3]);
        search_room.setCompoundDrawables(searchD[0],searchD[1],searchD[2],searchD[3]);
        follow_room.setCompoundDrawables(followD[0],followD[1],followD[2],followD[3]);

        final LayoutInflater inflater = GameFunction.this.getLayoutInflater();
        choose_join_view = inflater.inflate(R.layout.choose_join, null,false);
        create_room_view = inflater.inflate(R.layout.create_room, null,false);
        search_room_view = inflater.inflate(R.layout.search_room, null,false);
        btn_OK_create_room = (Button)create_room_view.findViewById(R.id.btn_OK_create_room);
        pass = (EditText)create_room_view.findViewById(R.id.pass);
        pass.setEnabled(false);
        swh_status = (Switch) create_room_view.findViewById(R.id.set);
        type_spin = (Spinner) create_room_view.findViewById(R.id.type_spin);
        mode_spin = (Spinner) create_room_view.findViewById(R.id.mode_spin);
        subject_spin = (Spinner) create_room_view.findViewById(R.id.subject_spin);
        random = new Random();

        quick_join.setOnClickListener(this);
        choose_join.setOnClickListener(this);
        create_room.setOnClickListener(this);
        search_room.setOnClickListener(this);
        swh_status.setOnClickListener(this);
        type_spin.setOnItemSelectedListener(this);
        mode_spin.setOnItemSelectedListener(this);
        subject_spin.setOnItemSelectedListener(this);
        btn_OK_create_room.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.choose_join:
//                setView(choose_join_view);
                break;
            case R.id.create_room:
                type_spin.setSelection(0,true);
                mode_spin.setSelection(0,true);
                subject_spin.setSelection(0,true);
                setView(create_room_view);
                break;
            case R.id.search_room:
//                setView(search_room_view);
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
//                intent = new Intent(GameFunction.this, Room.class);
//                intent.putExtra("num",0);
//                intent.putExtra("name", name);
//                if(type.equals("随机")){
//                    type_spin.setSelection(random.nextInt(3)+1,true);
//                    type = type_spin.getSelectedItem().toString();
//                }
//                intent.putExtra("type", type);
//                if(mode.equals("随机")){
//                    mode_spin.setSelection(random.nextInt(2)+1,true);
//                    mode = mode_spin.getSelectedItem().toString();
//                }
//                intent.putExtra("mode", mode);
//                if(subject.equals("随机")){
//                    subject_spin.setSelection(random.nextInt(3)+1,true);
//                    subject = subject_spin.getSelectedItem().toString();
//                }
//                intent.putExtra("subject", subject);
//                startActivity(intent);
                alert.dismiss();
                Toast.makeText(GameFunction.this,"创建房间",Toast.LENGTH_SHORT).show();
                break;
            case R.id.quick_join:
//                intent = new Intent(GameFunction.this, Room.class);
//                intent.putExtra("num",1);
//                intent.putExtra("name", name);
//                startActivity(intent);
                Toast.makeText(GameFunction.this,"快速加入",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_close:
                alert.dismiss();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.type_spin:
                type = adapterView.getSelectedItem().toString();
                Log.e("Type",type);
                break;
            case R.id.mode_spin:
                mode = adapterView.getSelectedItem().toString();
                Log.e("Mode",mode);
                break;
            case R.id.subject_spin:
                subject = adapterView.getSelectedItem().toString();
                Log.e("Subject",subject);
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

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}