package com.example.administrator.mycustomview.switchview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.administrator.mycustomview.R;

public class SwitchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);
        SwitchView switchview = (SwitchView) findViewById(R.id.switchview);
        switchview.setOnSwitchUpdateListener(new SwitchView.OnSwitchStateUpdateListener() {
            @Override
            public void onStateUpdate(boolean isOpen) {
                Log.i("TAG", "状态更新了，现在是：" + isOpen);
            }
        });
    }
}
