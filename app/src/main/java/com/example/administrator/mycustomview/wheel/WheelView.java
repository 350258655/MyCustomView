package com.example.administrator.mycustomview.wheel;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.administrator.mycustomview.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by shake on 17-6-1.
 * 自定义轮播图
 */
public class WheelView extends RelativeLayout implements ViewPager.OnPageChangeListener {


    LayoutInflater mInflater;

    private TextView tv_title;
    private LinearLayout point_container;
    private ViewPager mPager;

    private static final int TIMER = 1;
    private static final long DELAY = 3000;

    //ViewPager的自动轮播是否关闭
    private boolean isStop;

    //当前加载项
    private int currentItem;


    //模拟数据
    int[] imgs = {R.mipmap.wheel_firth, R.mipmap.wheel_two, R.mipmap.wheel_three, R.mipmap.wheel_four, R.mipmap.wheel_firth, R.mipmap.wheel_six};
    String[] titles = {"音箱狂欢", "手机国庆礼", "IT生活", "母婴茵宝", "国庆大礼包", "手机大放假"};

    List<ImageView> mListDatas = new ArrayList<>();

    private Context mContext;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == TIMER) {
                //自动轮播
                currentItem++;
                mPager.setCurrentItem(currentItem, true);
                mHandler.sendEmptyMessageDelayed(TIMER, DELAY);
            }
        }
    };


    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        //初始化布局加载器
        mInflater = LayoutInflater.from(mContext);

        //初始化布局
        initViews();

        //初始化数据
        initDatas();

        //设置适配器
        mPager.setAdapter(new WheelAdapter(mContext, mListDatas));

        //给ViewPager设置监听器
        mPager.setOnPageChangeListener(this);

        //默认选中最中间项
        int middle = Integer.MAX_VALUE / 2;
        int extra = middle % mListDatas.size();
        currentItem = middle - extra;
        mPager.setCurrentItem(currentItem);

        //开始轮播
        mHandler.sendEmptyMessageDelayed(TIMER, DELAY);
    }

    /**
     * 初始化数据
     */
    private void initDatas() {

        for (int i = 0; i < imgs.length; i++) {

            //给集合添加imageView
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(imgs[i]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            mListDatas.add(imageView);

            //添加点
            View point = new View(mContext);
            point.setBackgroundResource(R.drawable.point_radius_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15, 15);
            if (i != 0) {
                params.leftMargin = 10;
            } else {
                //默认选中第一个
                point.setBackgroundResource(R.drawable.point_radius_selected);
            }
            point_container.addView(point, params);

            //设置标题
            tv_title.setText(titles[i]);
        }

    }

    /**
     * 初始化布局
     */
    private void initViews() {
        View view = mInflater.inflate(R.layout.wheel_view, this, true);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        point_container = (LinearLayout) view.findViewById(R.id.point_container);
        mPager = (ViewPager) view.findViewById(R.id.pager);
    }


    // 回调方法,当viewpager滚动时的回调
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    // 回调方法,当viewpager的某个页面选中时的回调
    @Override
    public void onPageSelected(int position) {
        //先取余
        position = position % mListDatas.size();
        //设置选中的点的样式
        int childCount = point_container.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = point_container.getChildAt(i);
            view.setBackgroundResource(position == i ? R.drawable.point_radius_selected : R.drawable.point_radius_normal);
        }

        //设置标题
        tv_title.setText(titles[position]);
    }

    // 回调方法, 当viewpager的滑动状态改变时的回调
    // * @see ViewPager#SCROLL_STATE_IDLE : 闲置状态
    // * @see ViewPager#SCROLL_STATE_DRAGGING :拖动状态
    // * @see ViewPager#SCROLL_STATE_SETTLING: 固定状态
    @Override
    public void onPageScrollStateChanged(int state) {

        switch (state){
            //拖动状态下，要关闭自动轮播
            case ViewPager.SCROLL_STATE_DRAGGING:
                mHandler.removeMessages(TIMER);
                isStop = true;
                break;

            //闲置状态下，要重新开启自动轮播
            case ViewPager.SCROLL_STATE_IDLE:
                if(isStop){
                    mHandler.sendEmptyMessageDelayed(TIMER,DELAY);
                    isStop = false;
                }
                break;
        }


    }
}
