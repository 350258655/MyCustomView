package com.example.administrator.mycustomview.floatdialog;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.mycustomview.R;
import com.example.administrator.mycustomview.exitdialog.ExitDialog;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by shake on 2017/6/28.
 */
public class FloatDialog extends FrameLayout implements View.OnTouchListener {

    private ArrayList<MenuItem> mMenuItems; // 菜单对象集合
    private ArrayList<MenuItemView> mMenuItemViews; // 菜单中的item对应的view集合
    private Context mContext;
    private WindowManager mWindowManager; // 当前view的窗口管理器
    private int mScreenWidth;  // 屏幕宽度
    private int mScreenHeight;  // 屏幕高度
    private WindowManager.LayoutParams mWmParams;  // WindowMananger的params，控制这个值可以将自定义的view设置到窗口管理器中
    private ImageView mFloatLogoImv;  // 悬浮球的logo
    private ImageView mFloatLoaderImv;  // 围绕悬浮球的动画背景图，可用于做旋转或其它动画，看具体的设计
    private LinearLayout mFloatMenuLine;  // 悬浮菜单的载体，横向线性布局
    private FrameLayout mFloatLogoFra;  // 悬浮球的logo和动画背景图的载体
    private Timer mTimer;  // 一段时间没有操作 定时隐藏菜单，缩小悬浮球 的定时器，定时器可以设置一个对应的定时任务
    private TimerTask mTimerTask;  // 配合定时器的定时任务，本质是一个runnable,包装了定时相关业务方法
    private float mTouchStartX;  // 记录首次按下的位置x
    private float mTouchStartY;  // 记录首次按下的位置y
    private boolean isInitingLoader;  // 当前悬浮球的动画是否首次加载
    private boolean mDraging;  // 是否拖动中
    private boolean mIsRight;  // 当前悬浮球是否悬停在右边
    private boolean isActionLoading;  // 当前悬浮球动画是否是点击事件触发的动画
    private final int HANDLER_TYPE_HIDE_LOGO = 100;
    private final int HANDLER_TYPE_CANCEL_ANIM = 101;
    private boolean mShowLoader = true;  // 是否显示加载动画

    public FloatDialog(Builder builder) {
        super(builder.mContext);
        mMenuItems = builder.mMenuItems;
        mContext = builder.mContext;
        init();
    }

    /**
     * 用于接收隐藏缩小悬浮球、悬浮菜单，取消悬浮球的加载动画
     */
    Handler mTimerHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == HANDLER_TYPE_HIDE_LOGO){
                if(isInitingLoader){
                    isInitingLoader = false;
                    mShowLoader = false;
                    LayoutParams params = (LayoutParams) mFloatLogoFra.getLayoutParams();
                    int paddint75 = (params.width) / 3;
                    if(mIsRight){
                        if(params.rightMargin <= 0){
                            mFloatLogoFra.setPadding(16,16,0,16);
                            params.setMargins(0,0,-paddint75,0);
                            mFloatLogoFra.setLayoutParams(params);
                        }
                    }else {
                        if(params.leftMargin >= 0){
                            params.setMargins(-paddint75,0,0,0);
                            mFloatLogoFra.setLayoutParams(params);
                            mFloatLogoFra.setPadding(0,16,16,16);
                        }
                    }

                    mWmParams.alpha = 0.7f;
                    mWindowManager.updateViewLayout(FloatDialog.this,mWmParams);
                    refreshFloatDialog(mIsRight);
                    mFloatMenuLine.setVisibility(GONE);

                }
            }else if(msg.what == HANDLER_TYPE_CANCEL_ANIM){
                resetLogoSize();
                mFloatLoaderImv.clearAnimation();
                mFloatLoaderImv.setVisibility(GONE);
                mShowLoader = false;
            }
            super.handleMessage(msg);
        }
    };


    private void init() {
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        // 获取屏幕信息
        DisplayMetrics dm = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        mScreenHeight = dm.heightPixels;
        mScreenWidth = dm.widthPixels;

        // 获取窗口参数
        mWmParams = new WindowManager.LayoutParams();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT > 23) {
                // 在android7.1以上系统需要使用TYPE_PHONE类型 配合运行时权限才能正常显示悬浮窗,需要引导用户开启悬浮窗权限
                mWmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            } else {
                mWmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            }
        } else {
            mWmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        // 当前窗口的像素格式为RGBA_8888,即为最高质量
        mWmParams.format = PixelFormat.RGBA_8888;


        // NOT_FOCUSABLE可以是悬浮控件可以响应事件，LAYOUT_IN_SCREEN可以指定悬浮球指定在屏幕内，部分虚拟按键的手机，虚拟按键隐藏时，虚拟按键的位置则属于屏幕内，此时悬浮球会出现在原虚拟按键的位置
        mWmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        // 默认指定位置在屏幕的左上方，可以根据需要自己修改
        mWmParams.gravity = Gravity.LEFT | Gravity.TOP;

        // 默认指定的横坐标为屏幕最左边
        mWmParams.x = 0;

        // 默认指定的纵坐标为屏幕高度的10分之一，这里只是大概约束，因为上的flags参数限制，悬浮球不会出现在屏幕外
        mWmParams.y = mScreenHeight / 10;

        // 宽度指定为内容自适应
        mWmParams.width = LayoutParams.WRAP_CONTENT;
        mWmParams.height = LayoutParams.WRAP_CONTENT;

        // 在当前的FrameLayout 加一个View
        addView(createView());

        // 将当前View设置为前置窗口
        bringToFront();
        mWindowManager.addView(this, mWmParams);
        mTimer = new Timer();
        mShowLoader = true;
        refreshFloatDialog(mIsRight);
        mFloatMenuLine.setVisibility(GONE);

    }


    /**
     * 创建悬浮球，先从xml导入悬浮球的资源样板,根据每个菜单item内容创建对应的MenuItemView
     *
     * @return
     */
    private View createView() {
        // 将xml布局导入当前View
        View rootView = View.inflate(mContext, R.layout.layout_hd_float_dialog, null);
        FrameLayout rootFloatView = (FrameLayout) rootView.findViewById(R.id.hd_game_menu);
        mFloatMenuLine = (LinearLayout) rootView.findViewById(R.id.hd_game_line);
        mFloatLogoFra = (FrameLayout) rootView.findViewById(R.id.hd_game_fra);
        mFloatLogoImv = (ImageView) rootView.findViewById(R.id.hd_game_logo);
        mFloatLoaderImv = (ImageView) rootView.findViewById(R.id.hd_game_loader);

        // 创建悬浮菜单Item
        mMenuItemViews = generateMenuItemViews();
        // 将创建的menuItemView加入线性布局
        addMenuItemViews();
        // 使悬浮球和加速球可以超出父容器之外
        mFloatLogoFra.setClipChildren(false);
        mFloatLogoFra.setClipToPadding(false);
        rootFloatView.setClipChildren(false);
        rootFloatView.setClipToPadding(false);
        mFloatMenuLine.setClipChildren(false);
        mFloatMenuLine.setClipToPadding(false);

        // TODO 设置当前悬浮球的touchListener事件，此listerner设置后，ontouchevent方法失效 ??
        rootView.setOnTouchListener(this);
        rootView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDraging) {
                    if (mFloatMenuLine.getVisibility() == VISIBLE) {
                        mFloatMenuLine.setVisibility(GONE);
                    } else {
                        mFloatMenuLine.setVisibility(VISIBLE);
                    }
                }
            }
        });

        // 悬浮菜单的父容器设置的测量模式为大小完全不能确定
        rootView.measure(MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED), MeasureSpec
                .makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        return rootView;
    }

    /**
     * TODO 首次弹出菜单时，item会从透明到不透明，从小到大的动画效果 ??
     */
    private void addMenuItemViews() {
        for (MenuItemView menuItemView : mMenuItemViews) {
            menuItemView.setVisibility(VISIBLE);
            mFloatMenuLine.addView(menuItemView);
        }
    }


    /**
     * 创建悬浮菜单Item
     *
     * @return
     */
    private ArrayList<MenuItemView> generateMenuItemViews() {
        int menuSize = mMenuItems.size();
        ArrayList<MenuItemView> menuItemViews = new ArrayList<>();

        // 根据menuItem的个数创建menuItemView
        for (int i = 0; i < menuSize; i++) {
            MenuItem item = mMenuItems.get(i);
            // 最后一个不显示分割线
            if (i != menuSize - 1) {
                item.mShowdivider = true;
            } else {
                item.mShowdivider = false;
            }
            // 创建menuItemView
            MenuItemView menuItemView = new MenuItemView(mContext, item);
            setMenuItemOnClickListener(menuItemView, item.getOnClickListener());
            menuItemViews.add(menuItemView);
        }

        return menuItemViews;
    }


    /**
     * 中间先代理一次menuitem的点击事件，增加每次点击前先隐藏悬浮的菜单
     *
     * @param menuItemView
     * @param onClickListener
     */
    private void setMenuItemOnClickListener(final MenuItemView menuItemView, final OnClickListener onClickListener) {

        menuItemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFloatMenuLine.getVisibility() == VISIBLE) {
                    mFloatMenuLine.setVisibility(GONE);
                }

                onClickListener.onClick(menuItemView);
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 移除定时器
        removeTimerTask();
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                // 已经加载过动画
                isInitingLoader = true;

                resetLogoSize();
                mWmParams.alpha = 1f;
                mWindowManager.updateViewLayout(this, mWmParams);
                mDraging = false;
                break;

            case MotionEvent.ACTION_MOVE:
                float mMoveStartX = event.getX();
                float mMoveStartY = event.getY();

                if (Math.abs(mTouchStartX - mMoveStartX) > 3 && Math.abs(mTouchStartY - mMoveStartY) > 3) {
                    mDraging = true;
                    // TODO ????
                    mWmParams.x = (int) (x - mTouchStartX);
                    mWmParams.y = (int) (y - mTouchStartY);
                    mWindowManager.updateViewLayout(this, mWmParams);
                    mFloatMenuLine.setVisibility(GONE);
                    return false;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mWmParams.x > mScreenWidth / 2) {
                    mWmParams.x = mScreenWidth;
                    mIsRight = true;
                } else if (mWmParams.x < mScreenWidth / 2) {
                    mIsRight = true;
                    mWmParams.x = 0;
                }
                // 刷新位置参数
                refreshFloatDialog(mIsRight);
                // 隐藏view
                timerForHide();
                mWindowManager.updateViewLayout(this, mWmParams);
                mTouchStartX = mTouchStartY = 0;
                break;
        }

        return false;
    }


    /**
     * 隐藏View
     */
    private void timerForHide() {
        if(isActionLoading){
            Log.i("TAG", "timerForHide: 加载动画正在执行,不能启动隐藏悬浮的定时器");
            return;
        }

        isInitingLoader = true;

        // 结束任务
        if(mTimerTask != null){
            mTimerTask.cancel();
            mTimerTask = null;
        }

        // 发送消息的定时任务
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = mTimerHandler.obtainMessage();
                message.what = HANDLER_TYPE_HIDE_LOGO;
                mTimerHandler.sendMessage(message);
            }
        };

        if(isInitingLoader){
            if(mTimer != null){
                mTimer.schedule(mTimerTask,6000,3000);
            }
        }


    }


    /**
     * 刷新布局
     * 左边时：悬浮球logo的大小是50dp，所有当左右切换时，需要将菜单中的第一个item对应悬浮的位置右移50dp，其它则在第一个基础上再移4dp的间距
     *  右侧同理
     */
    private void refreshFloatDialog(boolean right) {
        int padding = Utils.dp2Px(4,mContext);
        int padding52 = Utils.dp2Px(50,mContext);
        int count = mMenuItemViews.size();

        if(right){

            // logo的位置参数
            LayoutParams paramsFloatImage = (LayoutParams) mFloatLoaderImv.getLayoutParams();
            paramsFloatImage.gravity = Gravity.RIGHT;
            mFloatLoaderImv.setLayoutParams(paramsFloatImage);

            // 加载动画的位置参数
            LayoutParams mFloatLoaderImvLayoutParams = (LayoutParams) mFloatLoaderImv.getLayoutParams();
            mFloatLoaderImvLayoutParams.gravity = Gravity.RIGHT;
            mFloatLoaderImv.setLayoutParams(mFloatLoaderImvLayoutParams);

            // logo与加载动画的父布局 的位置参数
            LayoutParams paramsFlFloat = (LayoutParams) mFloatLogoFra.getLayoutParams();
            paramsFlFloat.gravity = Gravity.RIGHT;
            mFloatLogoFra.setLayoutParams(paramsFlFloat);

            // 菜单中各个view的位置参数
            for (int i = 0; i < count; i++) {
                MenuItemView menuItemView = mMenuItemViews.get(i);
                if(i == count - 1){
                    // 这是有一个关闭的按钮
                    LinearLayout.LayoutParams paramsMenuClose = (LinearLayout.LayoutParams) menuItemView.getLayoutParams();
                    paramsMenuClose.rightMargin = padding52;
                    paramsMenuClose.leftMargin = padding;
                    menuItemView.setLayoutParams(paramsMenuClose);
                }else {
                    // TODO ???
                    LinearLayout.LayoutParams paramsMenu = (LinearLayout.LayoutParams) menuItemView.getLayoutParams();
                    paramsMenu.rightMargin = padding;
                    paramsMenu.leftMargin = padding;
                    menuItemView.setLayoutParams(paramsMenu);
                }
            }

        }else {
            // logo的位置参数
            LayoutParams params = (LayoutParams) mFloatLogoImv.getLayoutParams();
            params.gravity = Gravity.LEFT;
            mFloatLogoImv.setLayoutParams(params);

            // 加载动画的位置参数
            LayoutParams mFloatLoaderImvLayoutParams = (LayoutParams) mFloatLoaderImv.getLayoutParams();
            mFloatLoaderImvLayoutParams.gravity = Gravity.RIGHT;
            mFloatLoaderImv.setLayoutParams(mFloatLoaderImvLayoutParams);

            // logo与加载动画的父布局 的位置参数
            LayoutParams paramsFlFloat = (LayoutParams) mFloatLogoFra.getLayoutParams();
            paramsFlFloat.gravity = Gravity.LEFT;
            mFloatLogoFra.setLayoutParams(paramsFlFloat);

            // 菜单中各个view的位置参数,各个动态添加进去
            for (int i = 0; i < count; i++) {
                MenuItemView menuItemView = mMenuItemViews.get(i);
                if(i == 0){
                    LinearLayout.LayoutParams paramsMenuClose = (LinearLayout.LayoutParams) menuItemView.getLayoutParams();
                    paramsMenuClose.rightMargin = padding;
                    paramsMenuClose.leftMargin = padding52;
                    menuItemView.setLayoutParams(paramsMenuClose);
                }else {
                    LinearLayout.LayoutParams paramsMenu = (LinearLayout.LayoutParams) menuItemView.getLayoutParams();
                    paramsMenu.rightMargin = padding;
                    paramsMenu.leftMargin = padding;
                    menuItemView.setLayoutParams(paramsMenu);
                }
            }
        }




    }


    /**
     * 悬浮球缩小后下次恢复原始大小
     */
    private void resetLogoSize() {
        LayoutParams floatLogoLayoutParams = Utils.createLayoutParams(Utils.dp2Px(50, mContext), Utils.dp2Px(50, mContext));
        mFloatLogoFra.setLayoutParams(floatLogoLayoutParams);
        mFloatLogoFra.setPadding(0, 0, 0, 0);
    }


    /**
     * 移除定时器
     */
    private void removeTimerTask() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    /**
     * 移除动画
     */
    public void cancleAnim() {
        mShowLoader = false;
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mTimerHandler.sendEmptyMessage(HANDLER_TYPE_CANCEL_ANIM);
            }
        }, 3000);
    }
    //部分手机，隐藏时当前view的成员变量可能会为空
    public void hide() {
        try {
            setVisibility(View.GONE);
            Message message = mTimerHandler.obtainMessage();
            message.what = HANDLER_TYPE_HIDE_LOGO;
            mTimerHandler.sendMessage(message);
            removeTimerTask();
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
        }
    }

    public void show(){
        setVisibility(VISIBLE);
        isInitingLoader = true;
        resetLogoSize();
        mWmParams.alpha = 1f;
        mWindowManager.updateViewLayout(this,mWmParams);

        // 显示动画
        if(mShowLoader){
            mShowLoader = false;
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(new ScaleAnimation(0.5f,1.0f,0.5f,1.0f, Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,1.0f));
            animationSet.addAnimation(new AlphaAnimation(0.5f,1.0f));
            animationSet.setDuration(500);
            animationSet.setInterpolator(new AccelerateInterpolator());
            animationSet.setFillAfter(false);
            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mFloatLoaderImv.setVisibility(VISIBLE);
                    Animation rotaAnimation = new RotateAnimation(0f, +360f, Animation.RELATIVE_TO_PARENT,
                            0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
                    rotaAnimation.setInterpolator(new LinearInterpolator());
                    rotaAnimation.setRepeatCount(Animation.INFINITE);
                    rotaAnimation.setDuration(800);
                    rotaAnimation.setRepeatMode(Animation.RESTART);
                    mFloatLoaderImv.startAnimation(rotaAnimation);
                    cancleAnim();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            mFloatLoaderImv.startAnimation(animationSet);
            timerForHide();
        }

    }

    /**
     * 部分中兴手机 直接从窗口管理器中移除会产生崩溃，此处try catch结束悬浮球
     */
    private void removeFloatView() {
        try {
            mWindowManager.removeView(this);
        } catch (Exception ex) {
        }
    }

    public void destroy() {
        hide();
        removeFloatView();
        removeTimerTask();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        try {
            mTimerHandler.removeMessages(1);
        } catch (Exception ignored) {
        }
    }


    /**
     * 悬浮框的构造器
     */
    public static class Builder {
        private Context mContext;
        private ArrayList<MenuItem> mMenuItems = new ArrayList<>();  // items集合

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setMenuItems(ArrayList<MenuItem> items) {
            mMenuItems = mMenuItems;
            return this;
        }

        public FloatDialog build() {
            return new FloatDialog(this);
        }


    }
}
