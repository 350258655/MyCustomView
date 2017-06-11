package com.example.administrator.mycustomview.numberaddsub;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.mycustomview.R;


public class NumberAddSubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_add_sub);
        NumberAddSubView numberAddSubView = (NumberAddSubView) findViewById(R.id.numberAddSubView);

        numberAddSubView.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {
                Toast.makeText(NumberAddSubActivity.this, "点击了添加", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                Toast.makeText(NumberAddSubActivity.this, "点击了减少", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
