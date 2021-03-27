package com.example.administrator.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
                break;
            case R.id.assessReporter:
                intent = new Intent(getActivity(), AssessReort.class);
                startActivity(intent);
                break;
        }
    }
}
