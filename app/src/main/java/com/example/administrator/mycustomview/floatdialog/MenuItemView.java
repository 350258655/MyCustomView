package com.example.administrator.mycustomview.floatdialog;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.mycustomview.R;

/**
 * Created by shake on 2017/6/28.
 */
public class MenuItemView extends LinearLayout{

    private Context mContext;
    private MenuItem mMenuItem;
    private ImageView mMenuLogo;
    private TextView mMenuTitle;
    private View mMenuDivider;

    public MenuItemView(Context context,MenuItem item) {
        super(context);
        mMenuItem = item;
        mContext = context;
        init();
    }

    private void init() {
        // 加载布局
        View view = View.inflate(mContext, R.layout.layout_menu_item,this);
        mMenuLogo = (ImageView) view.findViewById(R.id.menu_logo_img);
        mMenuTitle = (TextView) view.findViewById(R.id.menu_item_txt);
        mMenuDivider = view.findViewById(R.id.menu_item_divider);

        // 设置背景
        setBackgroundColor(Color.TRANSPARENT);
        setLayoutParams(Utils.createWrapParams());
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);

        // 设置数据
        mMenuLogo.setImageResource(mMenuItem.getIcon());
        mMenuLogo.setClickable(false);
        mMenuTitle.setText(mMenuItem.getLabel());
        mMenuTitle.setTextColor(mContext.getResources().getColor(mMenuItem.getTextColor()));
        if(mMenuItem.mShowdivider){
            mMenuDivider.setVisibility(VISIBLE);
        }else {
            mMenuDivider.setVisibility(INVISIBLE);
        }

        setOnClickListener(mMenuItem.getOnClickListener());

    }


    public void setImageView(int drawableRes){
        mMenuLogo.setImageResource(drawableRes);
    }


}
