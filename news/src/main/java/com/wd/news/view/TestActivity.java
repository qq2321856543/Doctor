package com.wd.news.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


import com.wd.news.R;

import cn.jpush.android.api.JPushInterface;

public class TestActivity extends AppCompatActivity {

    public static final String TAG = "TestActivity";
    private TextView dataTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        dataTv = findViewById(R.id.data_tv);

        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = getIntent().getExtras();
            //通知标题
            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            //通知内容
            String message = bundle.getString(JPushInterface.EXTRA_ALERT);
            String extraJson = "";
            if (!message.equals("")) {
                //附带的json数据
                extraJson = bundle.getString(JPushInterface.EXTRA_EXTRA);
            }
            dataTv.setText("title:" + title + "\nmessage:" + message + "\nextraJson:" + extraJson);
        }
    }
}
