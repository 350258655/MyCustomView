package com.example.administrator.mycustomview.sweep;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by xk on 2017/6/16.
 */
public class SweepView extends ViewGroup {


    private View mContentView; // 内容区域
    private View mDeleteView; // 删除区域
    private int mDeleteWidth; // 删除区域的宽度
    private float mDownX; // 手指按下的x坐标
    private float mDownY; // 手指按下的y坐标
    private Scroller mScroller;
    private OnSweepListener mListener; // 回调接口实例
    private boolean isOpen; // 现在是打开还是关闭


    public SweepView(Context context) {
        this(context, null);
    }

    public SweepView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 测量内容区域的宽高
        mContentView.measure(widthMeasureSpec, heightMeasureSpec);

        // 测量删除区域的宽高
        int deleteWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mDeleteWidth, MeasureSpec.EXACTLY);
        mDeleteView.measure(deleteWidthMeasureSpec, heightMeasureSpec);

        // 设置自己的宽高
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        // 布局内容区域
        mContentView.layout(0, 0, mContentView.getMeasuredWidth(), mContentView.getMeasuredHeight());

        // 布局删除区域
        mDeleteView.layout(mContentView.getMeasuredWidth(), 0,
                mContentView.getMeasuredWidth() + mDeleteWidth, mDeleteView.getMeasuredHeight());

    }


    /**
     * 记住，这是屏幕的分发事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();

                // 计算移动的距离
                int distanceX = (int) (mDownX - moveX);
                //int distanceX = (int) (moveX - mDownX);
                // 计算View现在需要滑动到哪个位置。根据上次move的 scrollX 加上现在移动的距离就可以了
                int scrollX = getScrollX() + distanceX;
                // 设置左侧极点
                if (scrollX >= mDeleteWidth) {
                    scrollTo(mDeleteWidth, 0);
                    // 设置右侧极点
                } else if (scrollX <= 0) {
                    scrollTo(0, 0);
                    // 标准滑动
                } else {
                    scrollBy(distanceX, 0);
                }

                // 重置mDownX和mDownY
                mDownX = moveX;
                mDownY = moveY;
                break;
            case MotionEvent.ACTION_UP:
                // 松开需要判断是要去打开，还是关闭
                // 计算删除区域宽度的一半
                int middle = mDeleteWidth / 2;
                //  获取整个控件左上顶点的x左边
                int currentX = getScrollX();
                if (currentX >= middle) {
                    // 打开
                    //scrollTo(mDeleteWidth, 0);
                    open();
                } else {
                    // 关闭
                    //scrollTo(0, 0);
                    close();
                }


                break;
        }
        return true;
    }

    /**
     * 关闭删除区域
     */
    public void close() {
        // 回调数据
        isOpen = false;
        if (mListener != null) {
            mListener.onSweepChanged(this, isOpen);
        }
        // 计算结束的坐标
        int endX = 0;
        // 模拟数据变化
        dataScroller(endX);
    }


    /**
     * 打开删除区域
     */
    private void open() {
        // 回调数据
        isOpen = true;
        if (mListener != null) {
            mListener.onSweepChanged(this, isOpen);
        }
        // 计算结束的坐标
        int endX = mDeleteWidth;
        // 模拟数据变化
        dataScroller(endX);
    }

    /**
     * 模拟数据变化
     *
     * @param endX
     */
    private void dataScroller(int endX) {
        // 获取当前左上角的x坐标
        int startX = getScrollX();
        int startY = getScrollY();

        // 计算结束的坐标
        int endY = getScrollY();

        // 计算移动的距离
        int dx = endX - startX;
        int dy = endY - startY;

        // 计算时长
        int duration = Math.abs(dx) * 10;
        // 设置最大时长
        if (duration >= 600) {
            duration = 600;
        }

        // 模拟数据变化，但这里只是数据变化。还不会涉及UI更新
        mScroller.startScroll(startX, startY, dx, dy, duration);
        // 重新绘制界面。即UI更新，它接下来会调用如下方法 ---> draw() -->drawChild() --> computeScroll()
        invalidate();
    }


    /**
     * 更新位置的UI回调方法
     */
    @Override
    public void computeScroll() {
        // 获取当前是否还在模拟数据变化
        boolean isMoveing = mScroller.computeScrollOffset();
        if (isMoveing) {
            // 更新位置，调用 mScroller.getCurrX() 可知道当前进度
            scrollTo(mScroller.getCurrX(), 0);
            // 再次调用更新UI的方法
            invalidate();
        }
    }

    /**
     * XML加载完成的时候调用
     */
    @Override
    protected void onFinishInflate() {
        mContentView = getChildAt(0);
        mDeleteView = getChildAt(1);
        LayoutParams params = mDeleteView.getLayoutParams();
        mDeleteWidth = params.width;
        super.onFinishInflate();
    }


    /**
     * 设置回调接口
     *
     * @param listener
     */
    public void setOnSweepListener(OnSweepListener listener) {
        this.mListener = listener;
    }

    /**
     * 定义回调接口
     */
    public interface OnSweepListener {
        void onSweepChanged(SweepView sweepView, boolean isOpen);
    }

}
