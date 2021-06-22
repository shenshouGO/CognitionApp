package com.example.administrator.myapplication2;

import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.administrator.myapplication2.Adapter.MyFragmentPagerAdapter;

//import static android.os.Build.VERSION_CODES.R;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener,View.OnClickListener {
    //UI Objects
    private RadioGroup rg_tab_bar;
    private RadioButton rb_home;
    private RadioButton rb_game;
    private RadioButton rb_dynamic;
    private RadioButton rb_me;
    private ViewPager vpager;

    private MyFragmentPagerAdapter mAdapter;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        bindViews();
        rb_home.setChecked(true);
    }

    private void bindViews() {
        rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rb_home = (RadioButton) findViewById(R.id.rb_home);
        rb_game = (RadioButton) findViewById(R.id.rb_game);
        rb_dynamic = (RadioButton) findViewById(R.id.rb_dynamic);
        rb_me = (RadioButton) findViewById(R.id.rb_me);
        rg_tab_bar.setOnCheckedChangeListener(this);

        Drawable[] homeD = rb_home.getCompoundDrawables();
        Drawable[] gameD = rb_game.getCompoundDrawables();
        Drawable[] dynamicD = rb_dynamic.getCompoundDrawables();
        Drawable[] meD = rb_me.getCompoundDrawables();
        homeD[1].setBounds(0,0,100,100);
        gameD[1].setBounds(0,0,100,100);
        dynamicD[1].setBounds(0,10,100,100);
        meD[1].setBounds(0,10,100,100);
        rb_home.setCompoundDrawables(homeD[0],homeD[1],homeD[2],homeD[3]);
        rb_game.setCompoundDrawables(gameD[0],gameD[1],gameD[2],gameD[3]);
        rb_dynamic.setCompoundDrawables(dynamicD[0],dynamicD[1],dynamicD[2],dynamicD[3]);
        rb_me.setCompoundDrawables(meD[0],meD[1],meD[2],meD[3]);

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
            case R.id.rb_home:
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.rb_game:
                vpager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.rb_dynamic:
                vpager.setCurrentItem(PAGE_THREE);
                break;
            case R.id.rb_me:
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
                    rb_home.setChecked(true);
                    break;
                case PAGE_TWO:
                    rb_game.setChecked(true);
                    break;
                case PAGE_THREE:
                    rb_dynamic.setChecked(true);
                    break;
                case PAGE_FOUR:
                    rb_me.setChecked(true);
                    break;
            }
        }
    }
}