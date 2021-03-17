package com.example.administrator.myapplication2;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.administrator.myapplication2.Adapter.RankListAdapter;

public class RankList extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener,View.OnClickListener {
    private RadioGroup rg_tab_bar;
    private RadioButton hot_list;
    private RadioButton resource_list;
    private RadioButton rank_list;
    private RadioButton integral_list;
    private ViewPager vpager;

    private RankListAdapter mAdapter;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_list);

        mAdapter = new RankListAdapter(getSupportFragmentManager());
        bindViews();
        hot_list.setChecked(true);
    }

    private void bindViews() {
        rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        hot_list = (RadioButton) findViewById(R.id.hot_list);
        resource_list = (RadioButton) findViewById(R.id.resource_list);
        rank_list = (RadioButton) findViewById(R.id.rank_list);
        integral_list = (RadioButton) findViewById(R.id.integral_list);
        rg_tab_bar.setOnCheckedChangeListener(this);

        vpager = (ViewPager) findViewById(R.id.vpager);
        vpager.setAdapter(mAdapter);
        vpager.setCurrentItem(0);
        vpager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v){

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.hot_list:
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.resource_list:
                vpager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.rank_list:
                vpager.setCurrentItem(PAGE_THREE);
                break;
            case R.id.integral_list:
                vpager.setCurrentItem(PAGE_FOUR);
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
                case PAGE_ONE:
                    hot_list.setChecked(true);
                    break;
                case PAGE_TWO:
                    resource_list.setChecked(true);
                    break;
                case PAGE_THREE:
                    rank_list.setChecked(true);
                    break;
                case PAGE_FOUR:
                    integral_list.setChecked(true);
                    break;
            }
        }
    }
}
