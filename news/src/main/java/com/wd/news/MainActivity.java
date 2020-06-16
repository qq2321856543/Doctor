package com.wd.news;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.wd.news.base.BaseActivity;
import com.wd.news.constant.Const;
import com.wd.news.event.MyEvent;
import com.wd.news.register.Register;
import com.wd.news.util.ExampleUtil;
import com.wd.news.view.ConversationListActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static String TAG = "MainActivity";
    public static boolean isForeground = false;
    public static final String MESSAGE_RECEIVED_ACTION = "com.wd.doctor.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    private EditText nameEt, pwEt;
    private Button loginBtn, registerBtn;
    private String userName = "";
    private String psw = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        registerMessageReceiver();//极光推送相关
        EventBus.getDefault().register(MainActivity.this);
    }

    private void initView() {
        nameEt = findViewById(R.id.name_et);
        pwEt = findViewById(R.id.pw_et);
        loginBtn = findViewById(R.id.login_btn);
        registerBtn = findViewById(R.id.register_btn);
        clicks();
    }

    private void clicks() {
        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
    }

    MessageReceiver mMessageReceiver;

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    //登录成功后。。。
    @Subscribe
    public void getLoginResponseCode(MyEvent event) {
        Log.e(TAG, "----code:" + event.code);
        if (event.code == Const.REGISTER_SUCCESS) {
            Log.e(TAG, "----进入历史会话列表");
            Intent intent = new Intent(MainActivity.this, ConversationListActivity.class);
            MainActivity.this.startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        userName = nameEt.getText().toString();
        psw = pwEt.getText().toString();
        switch (v.getId()) {
            case R.id.login_btn:
                if (ExampleUtil.isEmpty(userName) || ExampleUtil.isEmpty(psw)) {
                    Toast.makeText(MainActivity.this, "请先输入用户名和密码！", Toast.LENGTH_SHORT).show();
                } else {
                    //登录
                    Register.login(userName, psw);
                }
                break;
            case R.id.register_btn:
                if (ExampleUtil.isEmpty(userName) || ExampleUtil.isEmpty(psw)) {
                    Toast.makeText(MainActivity.this, "请先输入用户名和密码！", Toast.LENGTH_SHORT).show();
                } else {
                    //注册用户并登录
                    Register.registerUserAndLogin(userName, psw);
                }
                break;
            default:
                break;
        }
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
//                    Log.e(TAG,"[MainActivity]---message:"+messge);
                    if (!ExampleUtil.isEmpty(extras)) {
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
        EventBus.getDefault().unregister(MainActivity.this);
    }
}
