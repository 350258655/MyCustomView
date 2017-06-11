package com.example.administrator.mycustomview.spinner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.example.administrator.mycustomview.R;

import java.util.ArrayList;
import java.util.List;

public class SpinnerActivity extends AppCompatActivity {

    List<String> mDatas;
    SpinnerView spinnerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);
        //找到View
        spinnerView = (SpinnerView) findViewById(R.id.spinner);

        //初始化数据
        mDatas = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            mDatas.add(100000+i+"");
        }

        //设置数据
        spinnerView.setAdapter(new SpinnerAdapter(this, mDatas));

        //设置点击的监听事件，监听后的操作交给用户去使用
        spinnerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //获取点击的内容，并且填充到编辑框中
                String data = mDatas.get(position);
                //设置编辑框内容
                spinnerView.setText(data);
                //设置编辑框的光标起点
                spinnerView.setSelection(data.length());

                //隐藏PopupWindow
                spinnerView.dismiss();
            }
        });
    }
}
