package com.wd.news.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wd.news.R;
import com.wd.news.base.BaseActivity;
import com.wd.news.util.ExampleUtil;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.CreateGroupCallback;

public class CreateConversationActivity extends BaseActivity {

    private static String TAG = "CreateConversationActivity";
    private EditText nameEt;
    private Button createBtn;

    private EditText groupNameEt, groupDescEt;
    private Button createGroupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_conversation);
        initView();
    }

    private void initView() {
        nameEt = findViewById(R.id.single_cs_name);
        createBtn = findViewById(R.id.create_single_cs_btn);
        groupNameEt = findViewById(R.id.create_group_name_et);
        groupDescEt = findViewById(R.id.create_group_desc_et);
        createGroupBtn = findViewById(R.id.create_group_btn);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEt.getText().toString();
                if (!ExampleUtil.isEmpty(name)) {
                    Intent intent = new Intent(CreateConversationActivity.this, ConversationDetailActivity.class);
                    intent.putExtra("name", name);
                    startActivity(intent);
                } else {
                    Toast.makeText(CreateConversationActivity.this, "请输入对方名字！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        createGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JMessageClient.createGroup(groupNameEt.getText().toString(), groupDescEt.getText().toString(), new CreateGroupCallback() {
                    @Override
                    public void gotResult(int i, String s, long l) {
                        if (i == 0) {
                            Log.e(TAG, "--创建群成功！id:" + l);
                            finish();
                            Intent intent = new Intent(CreateConversationActivity.this,GroupListActivity.class);
                            startActivity(intent);
                        } else {
                            Log.e(TAG, "--创建群失败！" + s);
                        }

                    }
                });
            }
        });
    }
}
