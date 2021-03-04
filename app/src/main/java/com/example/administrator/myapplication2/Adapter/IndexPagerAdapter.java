package com.example.administrator.myapplication2.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.administrator.myapplication2.GameFragment;
import com.example.administrator.myapplication2.MainActivity;
import com.example.administrator.myapplication2.MyFragment;
import com.example.administrator.myapplication2.MyInfo;
import com.example.administrator.myapplication2.ResourceFragment;

/**
 * Created by Administrator on 2021/2/28.
 */

public class IndexPagerAdapter extends FragmentPagerAdapter {
    private final int PAGER_COUNT = 2;
    private ResourceFragment myFragment1 = null;
    private ResourceFragment myFragment2 = null;


    public IndexPagerAdapter(FragmentManager fm) {
        super(fm);
        myFragment1 = ResourceFragment.newInstance("视频");
        myFragment2 = ResourceFragment.newInstance("文本");
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
            case 0:
                fragment = myFragment1;
                break;
            case 1:
                fragment = myFragment2;
                break;
        }
        return fragment;
    }
}
