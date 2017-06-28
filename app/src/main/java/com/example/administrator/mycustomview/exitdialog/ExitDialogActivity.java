package com.example.administrator.mycustomview.exitdialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.administrator.mycustomview.R;

public class ExitDialogActivity extends AppCompatActivity {

    private ExitDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit_dialog);

        Button btn_show = (Button) findViewById(R.id.btn_show_exit);
        Button btn_exit = (Button) findViewById(R.id.btn_hide_exit);

         dialog = new ExitDialog.Builder(this)
                .setTitle("退出")
                .setContent("您确定要退出吗？")
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ExitDialogActivity.this, "点击了确定按钮!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ExitDialogActivity.this, "点击了取消按钮!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .build();

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });


        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });


    }

}
