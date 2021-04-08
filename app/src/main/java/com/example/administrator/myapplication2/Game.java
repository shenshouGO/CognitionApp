package com.example.administrator.myapplication2;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
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

import MyClass.UserInfo;

public class Game extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {
    private ImageView back;
    private TextView quick_join;
    private TextView create_room;
    private View create_room_view;
    private Spinner type_spin;
    private Spinner mode_spin;
    private Spinner subject_spin;
    private Switch swh_status;
    private EditText pass;
    private Button btn_OK_create_room;
    private Button btn_close;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private Intent intent;

    private String type;
    private String mode;
    private String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        back = (ImageView) findViewById(R.id.back);
        quick_join = (TextView) findViewById(R.id.quick_join);
        create_room = (TextView) findViewById(R.id.create_room);

        final LayoutInflater inflater = Game.this.getLayoutInflater();
        create_room_view = inflater.inflate(R.layout.create_room, null,false);
        btn_OK_create_room = (Button)create_room_view.findViewById(R.id.btn_OK_create_room);
        pass = (EditText)create_room_view.findViewById(R.id.pass);
        pass.setEnabled(false);
        swh_status = (Switch) create_room_view.findViewById(R.id.set);
        type_spin = (Spinner) create_room_view.findViewById(R.id.type_spin);
        mode_spin = (Spinner) create_room_view.findViewById(R.id.mode_spin);
        subject_spin = (Spinner) create_room_view.findViewById(R.id.subject_spin);


        quick_join.setOnClickListener(this);
        create_room.setOnClickListener(this);
        swh_status.setOnClickListener(this);
        btn_OK_create_room.setOnClickListener(this);
        type_spin.setOnItemSelectedListener(this);
        mode_spin.setOnItemSelectedListener(this);
        subject_spin.setOnItemSelectedListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        final UserInfo UI = (UserInfo) getApplication();
        switch (v.getId()) {
            case R.id.create_room:
                setView(create_room_view);
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
                intent = new Intent(Game.this, Room.class);
                intent.putExtra("num",0);
                intent.putExtra("name", UI.getName());
                intent.putExtra("type", type);
                intent.putExtra("mode", mode);
                intent.putExtra("subject", subject);
                startActivity(intent);
                alert.dismiss();
                break;
            case R.id.btn_close:
                alert.dismiss();
                break;
            case R.id.quick_join:
                intent = new Intent(Game.this, Room.class);
                intent.putExtra("num",1);
                intent.putExtra("name", UI.getName());
                startActivity(intent);
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
