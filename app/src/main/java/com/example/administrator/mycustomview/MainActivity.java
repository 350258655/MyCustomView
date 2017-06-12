package com.example.administrator.mycustomview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.administrator.mycustomview.floatview.FloatViewActivity;
import com.example.administrator.mycustomview.numberaddsub.NumberAddSubActivity;
import com.example.administrator.mycustomview.spinner.SpinnerActivity;
import com.example.administrator.mycustomview.switchview.SwitchActivity;
import com.example.administrator.mycustomview.toggle.ToggleButtonActivity;
import com.example.administrator.mycustomview.wheel.WheelActivity;
import com.example.administrator.mycustomview.youku.YoukuMenuActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnwheel = (Button) findViewById(R.id.btn_wheel);
        Button btnaddsub = (Button) findViewById(R.id.btn_add_sub);
        Button btn_youku = (Button) findViewById(R.id.btn_youku);
        Button btn_spinner = (Button) findViewById(R.id.btn_spinner);
        Button btn_switch = (Button) findViewById(R.id.btn_switch);
        Button btn_cus_switch = (Button) findViewById(R.id.btn_cus_switch);
        Button btn_float = (Button) findViewById(R.id.btn_float);
        btnaddsub.setOnClickListener(this);
        btnwheel.setOnClickListener(this);
        btn_youku.setOnClickListener(this);
        btn_spinner.setOnClickListener(this);
        btn_switch.setOnClickListener(this);
        btn_cus_switch.setOnClickListener(this);
        btn_float.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_add_sub:
                startActivity(new Intent(MainActivity.this,NumberAddSubActivity.class));
                break;
            case R.id.btn_wheel:
                startActivity(new Intent(MainActivity.this,WheelActivity.class));
                break;
            case R.id.btn_youku:
                startActivity(new Intent(MainActivity.this,YoukuMenuActivity.class));
                break;
            case R.id.btn_spinner:
                startActivity(new Intent(MainActivity.this,SpinnerActivity.class));
                break;
            case R.id.btn_switch:
                startActivity(new Intent(MainActivity.this,SwitchActivity.class));
                break;
            case R.id.btn_cus_switch:
                startActivity(new Intent(MainActivity.this,ToggleButtonActivity.class));
                break;
            case R.id.btn_float:
                startActivity(new Intent(MainActivity.this,FloatViewActivity.class));
                break;
        }
    }
}
