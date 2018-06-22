package com.xingfu.aidlservicedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xingfu.aidlservicedemo.service.PersonService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button startServiceBtn;
    private Button closeServiceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startServiceBtn=findViewById(R.id.startServiceBtn);
        closeServiceBtn=findViewById(R.id.closeServiceBtn);

        startServiceBtn.setOnClickListener(this);
        closeServiceBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startServiceBtn:
                /*Intent intent = new Intent(MainActivity.this, MyService.class);
                startService(intent);*/
                Intent intent = new Intent(MainActivity.this, PersonService.class);
                startService(intent);
                ToastUtil.showShort(this,"已经启动服务！");
                break;
            case R.id.closeServiceBtn:
                /*Intent intent2 = new Intent(MainActivity.this, MyService.class);
                stopService(intent2);*/
                Intent intent2 = new Intent(MainActivity.this, PersonService.class);
                stopService(intent2);
                ToastUtil.showShort(this,"已经关闭服务！");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        /*Intent intent = new Intent(MainActivity.this, MyService.class);
        stopService(intent);*/
        Intent intent = new Intent(MainActivity.this, PersonService.class);
        stopService(intent);
        ToastUtil.showShort(this,"已经关闭服务！");
        super.onDestroy();
    }
}
