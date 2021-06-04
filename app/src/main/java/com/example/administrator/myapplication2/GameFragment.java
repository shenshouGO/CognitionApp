package com.example.administrator.myapplication2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2021/2/26.
 */

public class GameFragment extends Fragment implements View.OnClickListener{
    private TextView pictureStory;
    private TextView describeScene;
    private TextView gameUnit;
    private TextView assessReporter;
    private Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.game_unit, container, false);
        pictureStory = (TextView) view.findViewById(R.id.pictureStory);
        describeScene = (TextView) view.findViewById(R.id.describeScene);
        gameUnit = (TextView) view.findViewById(R.id.gameUnit);
        assessReporter = (TextView) view.findViewById(R.id.assessReporter);

        pictureStory.setOnClickListener(this);
        describeScene.setOnClickListener(this);
        gameUnit.setOnClickListener(this);
        assessReporter.setOnClickListener(this);

        Drawable[] assessD = assessReporter.getCompoundDrawables();
        Drawable[] gameD = gameUnit.getCompoundDrawables();
        Drawable[] pictureD = pictureStory.getCompoundDrawables();
        Drawable[] describeD = describeScene.getCompoundDrawables();
        assessD[1].setBounds(0,10,250,260);
        gameD[1].setBounds(0,10,250,260);
        pictureD[1].setBounds(0,10,250,260);
        describeD[1].setBounds(0,10,250,260);
        assessD[3].setBounds(0,0,450,100);
        gameD[3].setBounds(0,0,450,100);
        pictureD[3].setBounds(0,0,450,100);
        describeD[3].setBounds(0,0,450,100);
        assessReporter.setCompoundDrawables(assessD[0],assessD[1],assessD[2],assessD[3]);
        gameUnit.setCompoundDrawables(gameD[0],gameD[1],gameD[2],gameD[3]);
        pictureStory.setCompoundDrawables(pictureD[0],pictureD[1],pictureD[2],pictureD[3]);
        describeScene.setCompoundDrawables(describeD[0],describeD[1],describeD[2],describeD[3]);

        return view;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.pictureStory:
                intent = new Intent(getActivity(), PictureStory.class);
                startActivity(intent);
                break;
            case R.id.describeScene:
                intent = new Intent(getActivity(), DescribeScene.class);
                startActivity(intent);
                break;
            case R.id.gameUnit:
                intent = new Intent(getActivity(), GameFunction.class);
                startActivity(intent);
//                Toast.makeText(getActivity(),"此功能暂不可用",Toast.LENGTH_SHORT).show();
                break;
            case R.id.assessReporter:
                intent = new Intent(getActivity(), AssessReort.class);
                startActivity(intent);
                break;
        }
    }
}
