package com.example.administrator.mycustomview.floatdialog;

import android.view.View;

/**
 * Created by shake on 2017/6/28.
 * 各个菜单的元素
 */
public class MenuItem {

    private int mIcon;
    private String mLabel;
    private int mTextColor = android.R.color.white;
    private int mDiameter = 50; // 这个应该是各个Item的距离
    private View.OnClickListener mOnClickListener;
    private boolean mShowdivider = true; // 显示分割线

    public MenuItem(int textColor, int icon, String label, View.OnClickListener onClickListener, boolean showDivider) {
        mTextColor = textColor;
        mIcon = icon;
        mLabel = label;
        mOnClickListener = onClickListener;
        this.mShowdivider = showDivider;
    }

    public MenuItem(int icon, String label, int textColor, View.OnClickListener onClickListener) {
        mIcon = icon;
        mLabel = label;
        mTextColor = textColor;
        mOnClickListener = onClickListener;
    }

    public MenuItem(int icon, String label, boolean showDivider, int textColor, View.OnClickListener onClickListener) {
        this.mIcon = icon;
        this.mLabel = label;
        this.mShowdivider = showDivider;
        this.mTextColor = textColor;
        this.mOnClickListener = onClickListener;
    }

    public MenuItem( int icon, String label, int textColor, int diameter, View.OnClickListener onClickListener) {
        this.mIcon = icon;
        this.mLabel = label;
        this.mTextColor = textColor;
        this.mDiameter = diameter;
        this.mOnClickListener = onClickListener;
    }


    public MenuItem(int bgColor, int icon, String label) {
        this.mIcon = icon;
        this.mLabel = label;
    }


    public int getIcon() {
        return  mIcon;
    }

    public void setIcon(int icon) {
        mIcon = icon;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public int getDiameter() {
        return mDiameter;
    }

    public void setDiameter(int diameter) {
        mDiameter = diameter;
    }

    public View.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }




}
