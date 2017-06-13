package com.example.administrator.mycustomview.sliding;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.mycustomview.R;

public class SlidingActivity extends AppCompatActivity {
     SlidingMenuView smSlidingMenuView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding);
         smSlidingMenuView = (SlidingMenuView) findViewById(R.id.sm);


        // 设置返回按钮的点击事件
        ImageView iv_back = (ImageView) smSlidingMenuView.getChildAt(1).findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smSlidingMenuView.switchMenu();
            }
        });


    }


    public void clickTab(View v){
        String text = ((TextView) v).getText().toString();
        Toast.makeText(SlidingActivity.this, "点击了：" + text, Toast.LENGTH_SHORT).show();
        smSlidingMenuView.switchMenu();
    }
}
