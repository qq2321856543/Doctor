package com.wd.news.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wd.news.R;
import com.wd.news.base.BaseActivity;
import com.wd.news.event.MyEvent;

import org.greenrobot.eventbus.EventBus;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class UpdateUinfoActivity extends BaseActivity {
    private static final String TAG = "UpdateUinfoActivity";

    private TextView titleNameTv;
    private EditText infoEt;
    private Button cancelBtn, updateBtn;

    private String name, title;
    private UserInfo userInfo;
    private UserInfo.Field field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_uinfo);
        initView();
    }

    private void initView() {
        titleNameTv = findViewById(R.id.title_name_tv);
        infoEt = findViewById(R.id.info_et);
        cancelBtn = findViewById(R.id.cancel_btn);
        updateBtn = findViewById(R.id.update_btn);
        setTitle();
        userInfo = JMessageClient.getMyInfo();
        clicks();
    }

    private void setTitle() {
        name = getIntent().getStringExtra("name");
        if (name.equals("nickName")) {
            title = "昵称";
        } else if (name.equals("birthday")) {
            title = "生日";
        } else if (name.equals("signature")) {
            title = "个性签名";
        } else if (name.equals("gender")) {
            title = "性别";
        } else if (name.equals("region")) {
            title = "地区";
        } else if (name.equals("address")) {
            title = "地址";
        }
        titleNameTv.setText(title);
    }


    private void clicks() {
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.equals("nickName")) {
                    field = UserInfo.Field.nickname;
                    userInfo.setNickname(infoEt.getText().toString());
                } else if (name.equals("birthday")) {
                    field = UserInfo.Field.birthday;
                    userInfo.setBirthday(Long.parseLong(infoEt.getText().toString()));
                } else if (name.equals("signature")) {
                    field = UserInfo.Field.signature;
                    userInfo.setSignature(infoEt.getText().toString());
                } else if (name.equals("gender")) {
                    field = UserInfo.Field.gender;
                    if (infoEt.getText().toString().equals("男")) {
                        userInfo.setGender(UserInfo.Gender.male);
                    } else {
                        userInfo.setGender(UserInfo.Gender.female);
                    }
                } else if (name.equals("region")) {
                    field = UserInfo.Field.region;
                    userInfo.setRegion(infoEt.getText().toString());
                } else if (name.equals("address")) {
                    field = UserInfo.Field.address;
                    userInfo.setAddress(infoEt.getText().toString());
                }
                JMessageClient.updateMyInfo(field, userInfo, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        if (i == 0) {
                            Log.e(TAG, "----更新\"" + title + "\"成功！");
                            EventBus.getDefault().post(new MyEvent(true));
                            startActivity(new Intent(UpdateUinfoActivity.this,PersonalCenterActivity.class));
                            finish();
                        } else {
                            Log.e(TAG, "----更新\"" + title + "\"失败！");
                        }
                    }
                });
            }
        });
    }
}
