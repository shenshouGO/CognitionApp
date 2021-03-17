package com.example.administrator.myapplication2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.administrator.myapplication2.Adapter.IndexPagerAdapter;

/**
 * Created by Administrator on 2021/2/26.
 */

public class IndexFragment extends Fragment implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener{
    private Activity activity;
    private Context context;
    private IndexPagerAdapter mAdapter;
    private RadioGroup rg_tab_bar;
    private RadioButton rb_vedio;
    private RadioButton rb_text;
    private ViewPager vpager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.index, container, false);
        rg_tab_bar = (RadioGroup) view.findViewById(R.id.rg_tab_bar);
        rb_vedio = (RadioButton) view.findViewById(R.id.rb_vedio);
        rb_text = (RadioButton) view.findViewById(R.id.rb_text);
        rg_tab_bar.setOnCheckedChangeListener(this);
        mAdapter = new IndexPagerAdapter(getChildFragmentManager());

        vpager = (ViewPager) view.findViewById(R.id.vpager);
        vpager.setAdapter(mAdapter);
        vpager.setCurrentItem(0);
        vpager.addOnPageChangeListener(this);
        rb_vedio.setChecked(true);

        return view;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_vedio:
                vpager.setCurrentItem(0);
                break;
            case R.id.rb_text:
                vpager.setCurrentItem(1);
                break;
        }
    }

    //重写ViewPager页面切换的处理方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (vpager.getCurrentItem()) {
                case 0:
                    rb_vedio.setChecked(true);
                    break;
                case 1:
                    rb_text.setChecked(true);
                    break;
            }
        }
    }
}
