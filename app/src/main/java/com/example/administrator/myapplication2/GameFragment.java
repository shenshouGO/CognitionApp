package com.example.administrator.myapplication2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2021/2/26.
 */

public class GameFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private JSONObject Info;
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
                Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.assessReporter),Toast.LENGTH_LONG).show();
                break;
        }
    }

    public static final GameFragment newInstance(String info)
    {
        GameFragment fragment = new GameFragment();
        Bundle bundle = new Bundle();
        bundle.putString("info", info);
        fragment.setArguments(bundle);

        return fragment ;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Info = new JSONObject(getArguments().getString("info"));
            context = getContext();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
