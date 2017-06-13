package com.example.administrator.mycustomview.sliding;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by shake on 2017/6/13 0013.
 * 这次自定义的ViewGroup
 */
public class SlidingMenuView extends ViewGroup {

    private View mLeftView; //左侧菜单
    private View mContentView; //右侧内容
    private int mLeftWidth; //左侧菜单的宽度
    private float mDownX; //手指按下点的x坐标
    private float mDownY; //手指按下点的y坐标
    private Scroller mScroller;
    private boolean isShowLeft = false; //左侧菜单是否显示


    public SlidingMenuView(Context context) {
        this(context, null);
    }

    public SlidingMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }


    /**
     * 这个方法，是在父容器 RelativeLayout 的measureChild中调用的，进而调用了View的measure方法，
     * 最终再调用此方法，所以传递过来的两个参数代表父容器 RelativeLayout 对此SlidingMenuView的尺寸的期望值
     * 这里可以去看看笔记
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        /**
         * 第一步 测量孩子
         */
        // UNSPECIFIED 不确定的，随意，自己，大小自己去定，后30位给的值都是0，即自己去定义确定大小
        // EXACTLY 精确的，后面30位给的是一个精确数值，比如200，那么父容器希望子View的大小是200
        // AT_MOST,最大的，后面30位给的是一个精确数值，比如200，那么父容器希望子View的大小最大不超过200

        // 生成左侧菜单的宽度期望值，希望它的大小是固定的
        int leftWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mLeftWidth, MeasureSpec.EXACTLY);
        // 测量左侧。因为其高度和父容器是一样大的
        mLeftView.measure(leftWidthMeasureSpec, heightMeasureSpec);

        // 测量右侧内容，和父容器一样大就好
        mContentView.measure(widthMeasureSpec, heightMeasureSpec);


        /**
         * 第二步 设置自己的宽度和高度，根据父容器对自己的期望值来设置
         */
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(measureWidth, measureHeight);

    }

    /**
     * 布局，需要当前ViewGroup测量子View的大小，才能知道子View的大小
     * <p/>
     * view.getWidth()是布局之后才有的
     * view.getMeasureWidth()是测量之后有的
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 获取左边菜单的测量宽高
        int width = mLeftView.getMeasuredWidth();
        int height = mLeftView.getMeasuredHeight();

        // 给左侧菜单布局。根据与父容器的相对性来进行布局
        int lvLeft = -width;
        //int lvLeft = 0;
        int lvTop = 0;
        int lvRigth = 0;
        //int lvRigth = width;
        int lvBottom = height;
        mLeftView.layout(lvLeft, lvTop, lvRigth, lvBottom);

        // 给右侧菜单布局
        mContentView.layout(l, t, r, b);
    }

    /**
     * xml加载完成时候的回调
     */
    @Override
    protected void onFinishInflate() {
        // 获取两个子View
        mLeftView = getChildAt(0);
        mContentView = getChildAt(1);

        // 获取左侧菜单的宽度
        LayoutParams params = mLeftView.getLayoutParams();
        mLeftWidth = params.width;

        super.onFinishInflate();
    }

    /**
     * 是否要拦截。在左侧菜单中，左右移动的事件，被菜单中的TextView给消费掉了。所以这里要进行拦截。
     * 但不需要全部拦截，判断是左右移动的时候才进行拦截就好了
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                float moveX = ev.getX();
                float moveY = ev.getY();

                // 拦截
                if(Math.abs(moveX - mDownX) > Math.abs(moveY - mDownY)){
                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
        }


        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 手机屏幕事件的处理方法
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
                int distanceX = (int) (mDownX - moveX + 0.5f);
                // 计算View现在需要滑动到哪个位置。根据上次move的 scrollX 加上现在移动的距离就可以了
                int scrollX = getScrollX() + distanceX;
                // 设置左侧极点
                if (scrollX < 0 && scrollX < -mLeftView.getMeasuredWidth()) {
                    scrollTo(-mLeftView.getMeasuredWidth(), 0);
                    // 设置右侧极点
                } else if (scrollX > 0) {
                    scrollTo(0, 0);
                    // 标准滑动
                } else {
                    scrollBy(distanceX, 0);
                }

                // 重置mDownX和mDownY
                mDownX = moveX;
                mDownY = mDownY;
                break;

            case MotionEvent.ACTION_UP:
                // 松开需要判断是要去打开，还是关闭
                // 计算左侧菜单的一半
                int middle = -mLeftView.getMeasuredWidth() / 2;
                // 获取整个控件左上顶点的x左边
                int currentX = getScrollX();

                boolean showLeft = false;
                // 这里要记住，移动的是屏幕。自己根据脑海去想象，或者画图就可以了
                if (currentX <= middle) {
                    Log.i("TAG", "onTouchEvent: 要显示左侧菜单");
                    showLeft = true;
                } else {
                    showLeft = false;
                }

                // 切换视图
                switchMenu(showLeft);
                break;

        }
        // 表示消费了
        return true;
    }

    /**
     * 要打开还是关闭左侧菜单
     *
     * @param showLeft
     */
    private void switchMenu(boolean showLeft) {
        // 获取当前左上角的x坐标
        int currentX = getScrollX();
        // 获取左侧菜单的宽度
        int width = mLeftView.getMeasuredWidth();
        // 更新当前是否显示左侧菜单
        isShowLeft = showLeft;

        if (!showLeft) {
            // 关闭
            // 计算开始的坐标
            int startX = currentX;
            int startY = 0;

            // 计算结束的坐标
            int endX = 0;
            int endY = 0;

            // 计算移动的距离
            int dx = endX - startX;
            int dy = endY - startY;

            // 计算时长，跟移动距离相关
            int duration = Math.abs(dx) * 10;
            // 设置最大时长
            if (duration >= 600) {
                duration = 600;
            }

            // 模拟数据变化，但这里只是数据变化。还不会涉及UI更新
            mScroller.startScroll(startX, startY, dx, dy, duration);
        } else {
            // 打开
            // 计算开始坐标
            int startX = currentX;
            int startY = 0;

            // 计算结束坐标
            int endX = -width;
            int endY = 0;

            // 计算移动距离
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
        }

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
     * 控制左侧按钮开关。当左侧菜单是开状态，触发此方法会关掉菜单。当左侧菜单是关状态，触发此方法会打开菜单
     */
    public void switchMenu(){
        switchMenu(!isShowLeft);
    }


}
