package com.example.administrator.mycustomview.wheel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by shake on 17-6-1.
 */
public class WheelAdapter extends PagerAdapter {

    private List<ImageView> mListDatas;
    private Context mContext;


    public WheelAdapter(Context context, List<ImageView> imageViews) {
        this.mContext = context;
        this.mListDatas = imageViews;
    }


    /**
     * 页面的数量，返回最大值
     *
     * @return
     */
    @Override
    public int getCount() {
        if (mListDatas != null) {
            return Integer.MAX_VALUE;
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 初始化Item
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        //先取余
        position = position % mListDatas.size();

        //获取数据
        ImageView imageView = mListDatas.get(position);
        //添加数据
        container.addView(imageView);
        // 记录缓存标记--return 标记
        return imageView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //先取余
        position = position % mListDatas.size();

        //获取数据
        ImageView imageView = mListDatas.get(position);
        //添加数据
        container.removeView(imageView);
    }
}
