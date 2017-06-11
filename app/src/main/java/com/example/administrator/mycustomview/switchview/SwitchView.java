package com.example.administrator.mycustomview.switchview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.administrator.mycustomview.R;


/**
 * Created by shake on 17-6-9.
 * 自定义开关
 * Android 的界面绘制流程
 * 测量			 摆放		绘制
 * measure	->	layout	->	draw
 * | 		  |			 |
 * onMeasure -> onLayout -> onDraw 重写这些方法, 实现自定义控件
 * <p/>
 * onResume()之后执行
 * <p/>
 * View
 * onMeasure() (在这个方法里指定自己的宽高) -> onDraw() (绘制自己的内容)
 * <p/>
 * ViewGroup
 * onMeasure() (指定自己的宽高, 所有子View的宽高)-> onLayout() (摆放所有子View) -> onDraw() (绘制内容)
 */
public class SwitchView extends View {

    private Bitmap mSwitchBackground;// 背景的图片
    private Bitmap mSwitchSlide;// 滑块的图片

    private Paint mPaint;

    private boolean isOpen; // 开关状态。true表示开，false表示关

    /**
     * 标识现在是否是触摸状态
     */
    private boolean isTouch;

    private float mCurrentX;

    private OnSwitchStateUpdateListener mListener;

    public SwitchView(Context context) {
        this(context, null);
    }

    public SwitchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 加载背景图片和滑块，用加载图片的方式加载进来
     */
    private void init() {
        mPaint = new Paint();
        mSwitchBackground = BitmapFactory.decodeResource(getResources(), R.drawable.switch_background);
        mSwitchSlide = BitmapFactory.decodeResource(getResources(), R.drawable.slide_button_background);
    }


    /**
     * 测量大小，这个控件的大小，就是背景图片有多大，这个控件就是多大
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (mSwitchBackground != null) {
            int width = mSwitchBackground.getWidth();
            int height = mSwitchBackground.getHeight();
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }


    @Override
    protected void onDraw(Canvas canvas) {

        // 绘制背景的显示
        if (mSwitchBackground != null) {
            canvas.drawBitmap(mSwitchBackground, 0, 0, mPaint);
        }

        // 绘制滑块
        if (mSwitchSlide != null) {

            //假如是触摸状态
            if (isTouch) {
                //要减去滑块的一半
                float left = mCurrentX - mSwitchSlide.getWidth() / 2.0f;
                //限制左右边界
                if (left < 0) {
                    left = 0;
                } else if (left > mSwitchBackground.getWidth() - mSwitchSlide.getWidth()) {
                    left = mSwitchBackground.getWidth() - mSwitchSlide.getWidth();
                }

                canvas.drawBitmap(mSwitchSlide, left, 0, mPaint);
            } else {
                // 根据开关状态boolean, 直接设置滑块位置
                if (isOpen) {
                    canvas.drawBitmap(mSwitchSlide, mSwitchBackground.getWidth() - mSwitchSlide.getWidth(), 0, mPaint);
                } else {
                    canvas.drawBitmap(mSwitchSlide, 0, 0, mPaint);
                }

            }


        }

    }

    /**
     * 重写触摸事件, 响应用户的触摸.
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                isTouch = true;
                mCurrentX = event.getX();
                break;

            case MotionEvent.ACTION_MOVE:
                mCurrentX = event.getX();
                break;

            case MotionEvent.ACTION_UP:
                isTouch = false;
                mCurrentX = event.getX();

                boolean state;
                // 手指离开屏幕的时候，判断现在的状态
                if (mCurrentX > mSwitchBackground.getWidth() / 2.0f) {
                    state = true;
                } else {
                    state = false;
                }

                //假如状态更新，那么就回调给外面。这里state要和isOpen做对比，就是避免这种情况：原本是开，用户触摸之后，没有关掉
                //却又再次回调 开的状态给用户
                if (state != isOpen && mListener != null) {
                    mListener.onStateUpdate(state);
                }
                //更新状态
                isOpen = state;
                break;
        }


        //TODO 这是重新绘制界面的方法，非常重要！！！
        invalidate();
        // 消费了用户的触摸事件, 才可以收到其他的事件.
        return true;
    }


    /**
     * 设置更新接口
     *
     * @param listener
     */
    public void setOnSwitchUpdateListener(OnSwitchStateUpdateListener listener) {
        this.mListener = listener;
    }

    /**
     * 状态更新的接口
     */
    public interface OnSwitchStateUpdateListener {
        void onStateUpdate(boolean isOpen);
    }


}
