package com.example.administrator.mycustomview.floatview;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.administrator.mycustomview.R;

public class FloatViewActivity extends AppCompatActivity {

    private FloatViewService mFloatViewService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float_view);

        Button btn_show = (Button) findViewById(R.id.btn_show_floatview);
        Button btn_hide = (Button) findViewById(R.id.btn_hide_floatview);


        // 启动，绑定服务
        Intent intent = new Intent(this,FloatViewService.class);
        startService(intent);
        bindService(intent,mConnection,BIND_AUTO_CREATE);

        // 显示悬浮窗
        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFloatViewService != null){
                    mFloatViewService.showFloatView();
                }
            }
        });


        // 隐藏悬浮框
        btn_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFloatViewService != null){
                    mFloatViewService.hideFloatView();
                }
            }
        });
    }


    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mFloatViewService = ((FloatViewService.FloatViewServiceBinder)service).getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mFloatViewService = null;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mFloatViewService != null){
            mFloatViewService.destroyFloatView();
        }

        // 停止服务
        stopService(new Intent(this,FloatViewService.class));
        unbindService(mConnection);
    }
}
