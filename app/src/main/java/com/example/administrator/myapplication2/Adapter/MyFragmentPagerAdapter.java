package com.example.administrator.myapplication2.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.administrator.myapplication2.DynamicFragment;
import com.example.administrator.myapplication2.GameFragment;
import com.example.administrator.myapplication2.IndexFragment;
import com.example.administrator.myapplication2.MainActivity;
import com.example.administrator.myapplication2.MyInfo;

/**
 * Created by Administrator on 2021/2/25.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 4;
    private IndexFragment myFragment1 = null;
    private GameFragment myFragment2 = null;
    private DynamicFragment myFragment3 = null;
    private MyInfo myFragment4 = null;


    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        myFragment1 = new IndexFragment();
        myFragment2 = GameFragment.newInstance("{}");
        myFragment3 = new DynamicFragment();
        myFragment4 = MyInfo.newInstance("{}");
    }


    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case MainActivity.PAGE_ONE:
                fragment = myFragment1;
                break;
            case MainActivity.PAGE_TWO:
                fragment = myFragment2;
                break;
            case MainActivity.PAGE_THREE:
                fragment = myFragment3;
                break;
            case MainActivity.PAGE_FOUR:
                fragment = myFragment4;
                break;
        }
        return fragment;
    }

}
