package com.example.administrator.mycustomview.loadingdialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.administrator.mycustomview.R;

public class LoadingDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_dialog);

        Button btn_open = (Button) findViewById(R.id.btn_show_dialog);
        Button btn_close = (Button) findViewById(R.id.btn_close_dialog);


        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.show(LoadingDialogActivity.this,"加载中...");
            }
        });


        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.close();
            }
        });


    }
}
