package com.wd.news.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.wd.news.R;
import com.wd.news.base.BaseActivity;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupInfoCallback;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class GroupInfoActivity extends BaseActivity implements View.OnClickListener {
    private static String TAG = "GroupInfoActivity";

    private TextView groupIdTv, groupDescTv;
    private Switch aSwitch;
    private int isBlock;
    private Button exitGroupBtn, changeGroupAdminBtn;
    private long groupId;
    private UserInfo mInfo, groupOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        init();
    }

    private void init() {
        groupIdTv = findViewById(R.id.group_id_tv);
        groupDescTv = findViewById(R.id.group_desc_tv);
        aSwitch = findViewById(R.id.group_message_switch_btn);
        exitGroupBtn = findViewById(R.id.exit_group_btn);
        changeGroupAdminBtn = findViewById(R.id.change_group_admin);
        mInfo = JMessageClient.getMyInfo();
        getGroupInfo();
        clicks();
    }

    /**
     * 获取群信息并显示
     */
    private void getGroupInfo() {
        groupId = getIntent().getLongExtra("id", -1);
        groupIdTv.setText(String.valueOf(groupId));
        JMessageClient.getGroupInfo(groupId, new GetGroupInfoCallback() {
            @Override
            public void gotResult(int i, String s, GroupInfo groupInfo) {
                groupOwner = groupInfo.getGroupMemberInfo(groupInfo.getGroupOwner());
                if (i == 0) {
                    groupDescTv.setText(groupInfo.getGroupDescription());
                    if (groupInfo.isGroupBlocked() == 0) {//0接收消息 1屏蔽消息
                        aSwitch.setChecked(false);
                    } else {
                        aSwitch.setChecked(true);
                    }
                }
            }
        });
    }

    /**
     * 群消息设置按钮开关
     */
    private void clicks() {
        exitGroupBtn.setOnClickListener(this);
        changeGroupAdminBtn.setOnClickListener(this);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    aSwitch.setChecked(true);
                    isBlock = 1;
                    Log.e(TAG, "--1屏蔽群消息");
                } else {
                    aSwitch.setChecked(false);
                    isBlock = 0;
                    Log.e(TAG, "--接收群消息");
                }

                JMessageClient.getGroupInfo(groupId, new GetGroupInfoCallback() {
                    @Override
                    public void gotResult(int i, String s, GroupInfo groupInfo) {
                        if (i == 0) {
                            groupInfo.setBlockGroupMessage(isBlock, new BasicCallback() {
                                @Override
                                public void gotResult(int i, String s) {
                                    if (i == 0) {
                                        if (isBlock == 0) {
                                            Log.e(TAG, "--已设置接收群消息");
                                        } else {
                                            Log.e(TAG, "--设置屏蔽群消息失败：" + s);
                                        }
                                    } else {
                                        Log.e(TAG, "--设置群消息失败：" + s);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit_group_btn://退群
                GroupManager.exitGroup(groupId);
                break;
            case R.id.change_group_admin://移交群主
                if (mInfo.getUserID() == groupOwner.getUserID()) {
                    GroupManager.changGroupAdmin(groupId, "du002");
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
