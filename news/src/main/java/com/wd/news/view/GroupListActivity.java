package com.wd.news.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wd.news.R;
import com.wd.news.adapter.GroupListAdapter;
import com.wd.news.base.BaseActivity;
import com.wd.news.bean.MyGroupInfo;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupIDListCallback;
import cn.jpush.im.android.api.callback.GetGroupInfoCallback;
import cn.jpush.im.android.api.model.GroupInfo;

public class GroupListActivity extends BaseActivity {
    private static final String TAG = "GroupListActivity";

    private ListView groupLv;
    private List<MyGroupInfo> groupInfoList;
    private GroupListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        groupLv = findViewById(R.id.group_list_view);
        groupInfoList = new ArrayList<>();
        mAdapter = new GroupListAdapter(this, groupInfoList);
        getGroupList();
        enterGroupConversation();
    }

    /**
     * 点击群 进入会话页面
     */
    private void enterGroupConversation() {
        groupLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyGroupInfo myGroupInfo = (MyGroupInfo) groupLv.getItemAtPosition(position);
                JMessageClient.getGroupInfo(myGroupInfo.getGroupId(), new GetGroupInfoCallback() {
                    @Override
                    public void gotResult(int i, String s, GroupInfo groupInfo) {
                        if (i == 0) {
                            Intent intent = new Intent(GroupListActivity.this, ConversationDetailActivity.class);
                            intent.putExtra("name", groupInfo.getGroupName());
                            intent.putExtra("id", groupInfo.getGroupID());
                            startActivity(intent);
                        }
                    }
                });

            }
        });

    }

    /**
     * 获取我的群列表
     */
    private void getGroupList() {
        JMessageClient.getGroupIDList(new GetGroupIDListCallback() {
            @Override
            public void gotResult(int i, String s, List<Long> list) {
                if (i == 0) {
                    //根据ID获取群信息
                    Log.e(TAG, "----" + list.size() + "个群");
                    for (int j = 0; j < list.size(); j++) {
                        JMessageClient.getGroupInfo(list.get(j), new GetGroupInfoCallback() {
                            @Override
                            public void gotResult(int i, String s, GroupInfo groupInfo) {
                                if (i == 0) {
                                    String groupName = groupInfo.getGroupName();
                                    String groupDesc = groupInfo.getGroupDescription();
                                    long groupID = groupInfo.getGroupID();
                                    int members = groupInfo.getGroupMembers().size();
                                    MyGroupInfo myGroupInfo = new MyGroupInfo(groupName, groupDesc, members, groupID);
                                    groupInfoList.add(myGroupInfo);
                                    setAdapter();
                                } else {
                                    Log.e(TAG, "--获取群信息失败！" + s);
                                }
                            }
                        });
//                        GroupManager.addGroupMembers(list.get(j));
                    }
                } else {
                    Log.e(TAG, "--获取群ID列表失败！" + s);
                }
            }
        });
    }

    private void setAdapter() {
        groupLv.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
