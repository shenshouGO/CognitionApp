package com.example.administrator.myapplication2.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.administrator.myapplication2.HotCognitionFragment;
import com.example.administrator.myapplication2.MainActivity;
import com.example.administrator.myapplication2.MyFragment;
import com.example.administrator.myapplication2.RankList;
import com.example.administrator.myapplication2.ResourceRankFragment;

/**
 * Created by Administrator on 2021/3/6.
 */

public class RankListAdapter extends FragmentPagerAdapter {
    private final int PAGER_COUNT = 4;
    private HotCognitionFragment myFragment1 = null;
    private ResourceRankFragment myFragment2 = null;
    private MyFragment myFragment3 = null;
    private MyFragment myFragment4 = null;

    public RankListAdapter(FragmentManager fm) {
        super(fm);
        myFragment1 = new HotCognitionFragment();
        myFragment2 = new ResourceRankFragment();
        myFragment3 = MyFragment.newInstance("排位榜");
        myFragment4 = MyFragment.newInstance("积分榜");
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
            case RankList.PAGE_ONE:
                fragment = myFragment1;
                break;
            case RankList.PAGE_TWO:
                fragment = myFragment2;
                break;
            case RankList.PAGE_THREE:
                fragment = myFragment3;
                break;
            case RankList.PAGE_FOUR:
                fragment = myFragment4;
                break;
        }
        return fragment;
    }
}
