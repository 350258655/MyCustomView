package com.example.administrator.mycustomview.floatdialog;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.mycustomview.R;
import com.example.administrator.mycustomview.exitdialog.ExitDialog;

import java.util.ArrayList;

/**
 * Created by shake on 2017/6/28.
 */
public class FloatDialog extends FrameLayout {

    private ArrayList<MenuItem> mMenuItems; // 菜单对象集合
    private Context mContext;
    private WindowManager mWindowManager; // 当前view的窗口管理器
    private int mScreenWidth;  // 屏幕宽度
    private int mScreenHeight;  // 屏幕高度
    private WindowManager.LayoutParams mWmParams;  // WindowMananger的params，控制这个值可以将自定义的view设置到窗口管理器中
    private ImageView mFloatLogoImv;  // 悬浮球的logo
    private ImageView mFloatLoaderImv;  // 围绕悬浮球的动画背景图，可用于做旋转或其它动画，看具体的设计
    private LinearLayout mFloatMenuLine;  // 悬浮菜单的载体，横向线性布局
    private FrameLayout mFloatLogoFra;  // 悬浮球的logo和动画背景图的载体

    public FloatDialog(Builder builder) {
        super(builder.mContext);
        mMenuItems = builder.mMenuItems;
        mContext = builder.mContext;
        init();
    }


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

    }


    /**
     * 创建悬浮球，先从xml导入悬浮球的资源样板,根据每个菜单item内容创建对应的MenuItemView
     *
     * @return
     */
    private View createView() {
        // 将xml布局导入当前View
        View rootView = View.inflate(mContext, R.layout.layout_hd_float_dialog,null);
        FrameLayout rootFloatView = (FrameLayout) rootView.findViewById(R.id.hd_game_menu);
        mFloatMenuLine = (LinearLayout) rootView.findViewById(R.id.hd_game_line);
        mFloatLogoFra = (FrameLayout) rootView.findViewById(R.id.hd_game_fra);
        mFloatLogoImv = (ImageView) rootView.findViewById(R.id.hd_game_logo);
        mFloatLoaderImv = (ImageView) rootView.findViewById(R.id.hd_game_loader);

        // 创建悬浮菜单Item

        return null;
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
