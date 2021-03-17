package com.example.administrator.myapplication2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Join extends AppCompatActivity {
    private EditText etName;
    private Button btn;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        etName = (EditText) findViewById(R.id.name);
        btn = (Button) findViewById(R.id.join_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etName.getText().toString();
                Intent intent = new Intent(Join.this, GameFunction.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
    }
}
