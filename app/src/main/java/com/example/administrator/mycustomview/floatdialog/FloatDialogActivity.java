package com.example.administrator.mycustomview.floatdialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.administrator.mycustomview.R;

import java.util.ArrayList;

/**
 * 另外一种悬浮窗
 */
public class FloatDialogActivity extends AppCompatActivity implements View.OnClickListener{

    private FloatDialog mFloatDialog;
    String HOME = "首页";
    String FAVOUR = "收藏";
    String FEEDBACK = "客服";
    String MESSAGE = "消息";
    String CLOSE = "关闭";
    String[] MENU_ITEMS = {HOME, FAVOUR, FEEDBACK, MESSAGE, CLOSE};
    private int[] menuIcons = new int[]{R.mipmap.yw_menu_account, R.mipmap.yw_menu_favour, R.mipmap.yw_menu_fb, R.mipmap.yw_menu_msg, R.mipmap.yw_menu_close};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float_dialog);
        Button btn_show = (Button) findViewById(R.id.btn_show_floatdialog);
        Button btn_hide = (Button) findViewById(R.id.btn_hide_floatdialog);


        ArrayList<MenuItem> mMenuItems = new ArrayList<>();
        for (int i = 0; i < menuIcons.length; i++) {
            mMenuItems.add(new MenuItem(menuIcons[i], MENU_ITEMS[i], android.R.color.black, this));
        }

        mFloatDialog = new FloatDialog.Builder(this).setMenuItems(mMenuItems).build();

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloatDialog.show();
            }
        });


    }

    @Override
    public void onClick(View v) {

    }
}
