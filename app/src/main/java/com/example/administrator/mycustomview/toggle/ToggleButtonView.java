package com.example.administrator.mycustomview.toggle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.CompoundButton;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.CompoundButton;

import com.example.administrator.mycustomview.R;


/**
 * Created by shake on 17-6-9.
 * 完全自定义的开关，不用任何图片。CompoundButton是CheckBox、ToggleButton之类切换开关的父类
 */
public class ToggleButtonView extends CompoundButton {

    //下面背景打开时候的背景颜色
    private int onBackgourndColor;
    //下面背景关闭时候的背景颜色
    private int offBackgroundColor;
    //上面小圆打开时候的颜色
    private int onToggleColor;
    //上面小圆关闭时候的颜色
    private int offToggleColor;

    /**
     * 画笔Paint
     */
    //背景画笔（画下面的背景）
    private Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //开关画笔（画上面的小圆）
    private Paint togglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    /**
     * 路径Path
     */
    //背景路径
    private Path backgroundPath = new Path();
    //打开时上面圆的路径
    private Path togglePath_on = new Path();
    //关闭时上面圆的路径
    private Path togglePath_off = new Path();
    //上面圆和底部背景之间的间距
    private float padding = 1;
    //onDraw时的路径
    private Path toggleDrawPath = new Path();

    public ToggleButtonView(Context context) {
        this(context, null);
    }

    public ToggleButtonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToggleButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setClickable(true);
        initAttrs(attrs);

    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ToggleButton);
        //下面背景打开时候的背景颜色
        onBackgourndColor = a.getColor(R.styleable.ToggleButton_onBackgroundColor, Color.parseColor("#FFB97A8F"));
        //下面背景关闭时候的背景颜色
        offBackgroundColor = a.getColor(R.styleable.ToggleButton_offBackgroundColor, Color.LTGRAY);
        //上面小圆打开时候的颜色
        onToggleColor = a.getColor(R.styleable.ToggleButton_onToggleColor, Color.parseColor("#FF4081"));
        //上面小圆关闭时候的颜色
        offToggleColor = a.getColor(R.styleable.ToggleButton_offToggleColor, Color.WHITE);

        a.recycle();
    }


    /**
     * 测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        if (widthMode != MeasureSpec.EXACTLY) {
            widthSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, displayMetrics);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        }

        if (heightMode != MeasureSpec.EXACTLY) {
            heightSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, displayMetrics);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initPath();

    }




    /**
     * 绘制
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //设置背景的背景颜色
        backgroundPaint.setColor(isChecked() ? onBackgourndColor : offBackgroundColor);
        //设置小球的背景颜色
        togglePaint.setColor(isChecked() ? onToggleColor : offToggleColor);

        //开始画
        canvas.drawPath(backgroundPath, backgroundPaint);
        canvas.drawPath(toggleDrawPath, togglePaint);
    }

    /**
     * 初始化Path。相当于两个圆球大一点，而中间那段就收窄一点！！！！！知道了参数的意义，再对应去画个图就明白了
     */
    private void initPath() {
        int height = getHeight();
        int width = getWidth();

        if (height > 0 && width > 0) {
            //圆的半径取宽和高中较小的一个的一半
            int radius = Math.min(width, height) / 2;

            //重新设置路径
            backgroundPath.reset();
            togglePath_off.reset();
            togglePath_on.reset();


            //画左右两边的矩形，待会再根据矩形画出圆
            //参数： left=矩形左边的x坐标 ，top=矩形顶部的y坐标, right=矩形右边的x坐标，bottom=矩形底部的y坐标
            RectF left = new RectF(0, radius / 2, radius, 3 * radius / 2);
            RectF right = new RectF(width - radius, radius / 2, width, 3 * radius / 2);

            //根据左边矩形画出左边圆弧
            //第二个参数表示开始起始位置的角度值，第三个参数表示要旋转的角度值
            backgroundPath.addArc(left, 90, 180);

            //画中间的矩形
            // backgroundPath.addRect(radius, 0, width - radius, height, Path.Direction.CCW);
            backgroundPath.addRect(radius / 2, radius / 2, width - radius / 2, 3 * radius / 2, Path.Direction.CCW);

            //根据右边矩形画右边圆弧
            //第二个参数表示开始起始位置的角度值，第三个参数表示要旋转的角度值
            backgroundPath.addArc(right, 270, 180);


            //根据矩形画开状态的小圆球，圆球在右边
            RectF on = new RectF(width - 2 * radius, 0, width, height);
            togglePath_on.addArc(on,90,360);

            //根据矩形画关状态的小圆球，圆球在左边
            RectF off = new RectF(0, 0, 2 * radius, 2 * radius);
            togglePath_off.addArc(off,90,360);


            //画开关的圆球
            toggleDrawPath.set(isChecked() ? togglePath_on : togglePath_off);

        }

    }


    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);

        if (toggleDrawPath != null) {
            toggleDrawPath.reset();
            toggleDrawPath.set(isChecked() ? togglePath_on : togglePath_off);
        }
        invalidate();
    }

    /**
     * 暴露给外界使用
     *
     * @return
     */
    public boolean getChecked() {
        return isChecked();
    }

}
