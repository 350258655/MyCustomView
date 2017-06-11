package com.example.administrator.mycustomview.youku;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.mycustomview.R;


/**
 * Created by shake on 17-6-3.
 * 优酷菜单。视图都是图片做的，逻辑还是挺简单的
 */
public class YoukuMenu extends RelativeLayout implements View.OnClickListener, Animation.AnimationListener {

    private ImageView mIvHome;
    private ImageView mIvMenu;

    private RelativeLayout mRlLevel1;// 一级菜单的容器
    private RelativeLayout mRlLevel2;// 二级菜单的容器
    private RelativeLayout mRlLevel3;// 三级菜单的容器

    private boolean isLevel1Display = true;// 一级菜单是否显示的标记
    private boolean isLevel2Display = true;// 二级菜单是否显示的标记
    private boolean isLevel3Display = true;// 三级菜单是否显示的标记

    //当前执行动画的数量
    private int mAnimationCount;

    public YoukuMenu(Context context) {
        this(context, null);
    }

    public YoukuMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YoukuMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initViews(context);

    }

    private void initViews(Context context) {
        //加载View
        View.inflate(context, R.layout.youku_view, this);

        mRlLevel1 = (RelativeLayout) findViewById(R.id.rl_level1);
        mRlLevel2 = (RelativeLayout) findViewById(R.id.rl_level2);
        mRlLevel3 = (RelativeLayout) findViewById(R.id.rl_level3);

        mIvHome = (ImageView) findViewById(R.id.level1_iv_home);
        mIvMenu = (ImageView) findViewById(R.id.level2_iv_menu);

        // 设置点击事件
        mIvHome.setOnClickListener(this);
        mIvMenu.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == mIvHome) {
            //点击Home按钮
            clickLevel1Home();
        } else if (v == mIvMenu) {
            //点击优酷菜单按钮
            clickLevel2Menu();
        }
    }


    /**
     * 点击优酷菜单按钮
     */
    private void clickLevel2Menu() {
        /** 假如当前有动画正在执行，则不响应点击事件 */
        if (mAnimationCount > 0) {
            return;
        }

        /** 如果三级菜单可见,隐藏三级菜单;如果三级菜单隐藏,显示三级菜单 */
        if(isLevel3Display){
            //隐藏
            hideLevel(mRlLevel3,0);
            //改变状态
            isLevel3Display = false;
        }else {
            //显示
            showLevel(mRlLevel3,0);
            //改变状态
            isLevel3Display = true;
        }

    }


    /**
     * 点击Home按钮
     */
    private void clickLevel1Home() {
        /** 假如当前有动画正在执行，则不响应点击事件 */
        if (mAnimationCount > 0) {
            return;
        }

        /** 当二级菜单和三级菜单都显示，同时隐藏二级菜单和三级菜单 */
        if (isLevel2Display && isLevel3Display) {
            //隐藏二级菜单，延时
            hideLevel(mRlLevel2, 100);
            //隐藏三级菜单，不延时
            hideLevel(mRlLevel3, 0);

            //改变状态
            isLevel3Display = false;
            isLevel2Display = false;

            return;
        }

        /** 当二级菜单和三级菜单都不显示，显示二级菜单 */
        if (!isLevel2Display && !isLevel3Display) {
            //显示二级菜单
            showLevel(mRlLevel2, 0);

            //改变状态
            isLevel2Display = true;

            return;
        }

        /** 当二级菜单显示，并且三级菜单不显示的情况。要隐藏二级菜单 */
        if(isLevel2Display && !isLevel3Display){
            //隐藏二级菜单
            hideLevel(mRlLevel2,0);

            //改变状态
            isLevel2Display = false;

            return;
        }

    }

    /**
     * 隐藏菜单
     *
     * @param container
     * @param startOffset
     */
    private void hideLevel(RelativeLayout container, long startOffset) {

        /** View消失后，要屏蔽它的点击事件。(其实它还在那里，只是不显示出来) */
        for (int i = 0; i < container.getChildCount(); i++) {
            container.getChildAt(i).setEnabled(false);
        }

        /** 消失的动画 */
        RotateAnimation animation = new RotateAnimation(0, -180,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 1f);
        animation.setDuration(400);
        animation.setFillAfter(true);
        animation.setStartOffset(startOffset);
        animation.setAnimationListener(this);

        /** 开始动画 */
        container.startAnimation(animation);
    }

    /**
     * 显示菜单
     *
     * @param container
     * @param startOffset
     */
    private void showLevel(RelativeLayout container, long startOffset) {
        /** View显示，要设置它可点击 */
        for (int i = 0; i < container.getChildCount(); i++) {
            container.getChildAt(i).setEnabled(true);
        }

        /** 显示的动画 */
        RotateAnimation animation = new RotateAnimation(-180, 0,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 1f);
        animation.setDuration(400);
        animation.setFillAfter(true);
        animation.setStartOffset(startOffset);
        animation.setAnimationListener(this);

        /** 开始动画 */
        container.startAnimation(animation);
    }


    /**
     * 动画开始的回调方法
     *
     * @param animation
     */
    @Override
    public void onAnimationStart(Animation animation) {
        mAnimationCount++;
    }

    /**
     * 动画结束的回调方法
     *
     * @param animation
     */
    @Override
    public void onAnimationEnd(Animation animation) {
        mAnimationCount--;
    }

    /**
     * 动画重复的回调方法
     *
     * @param animation
     */
    @Override
    public void onAnimationRepeat(Animation animation) {

    }

}
