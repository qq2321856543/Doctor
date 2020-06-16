package com.wd.news.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.wd.news.R;
import com.wd.news.adapter.ConversationListAdapter;
import com.wd.news.base.BaseActivity;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;

public class ConversationListActivity extends BaseActivity implements OnClickListener {
    public static String TAG = "ConversationListActivity";
    private Button createCsBtn;
    private Button myGroupBtn;
    private TextView myNameTv;
    private Button exitBtn;
    private ListView conversationListView;
    private List<Conversation> conversationListDatas;
    private ConversationListAdapter mAdapter;

    private UserInfo myInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);
        init();
        getHistoryData();
    }

    private void init() {
        createCsBtn = findViewById(R.id.create_conversation_btn);
        myGroupBtn = findViewById(R.id.my_group_btn);
        myNameTv = findViewById(R.id.my_name_tv);
        exitBtn = findViewById(R.id.exit_btn);
        conversationListView = findViewById(R.id.cv_list_view);
        createCsBtn.setOnClickListener(this);
        myGroupBtn.setOnClickListener(this);
        myNameTv.setOnClickListener(this);
        exitBtn.setOnClickListener(this);
    }

    private void getHistoryData() {
        myInfo = JMessageClient.getMyInfo();
        myNameTv.setText(myInfo.getUserName());
        conversationListDatas = JMessageClient.getConversationList();
        mAdapter = new ConversationListAdapter(ConversationListActivity.this, conversationListDatas);
        conversationListView.setAdapter(mAdapter);
        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Conversation cs = conversationListDatas.get(position);
                Intent intent = new Intent(ConversationListActivity.this, ConversationDetailActivity.class);
                intent.putExtra("name", cs.getTitle());
                if (cs.getType().name().equals("single")) {
                } else if (cs.getType().name().equals("group")) {//点击群则增加一个群ID
//                    Log.e(TAG,"--id:"+cs.getId());
                    intent.putExtra("id", Long.parseLong(cs.getTargetId()));
                }
//                Log.e(TAG,"--id"+cs.getType().name());
                startActivity(intent);
//                Toast.makeText(ConversationListActivity.this, "name:" + cs.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.create_conversation_btn://创建会话
                intent = new Intent(ConversationListActivity.this, CreateConversationActivity.class);
                break;
            case R.id.my_group_btn://我的群列表
                intent = new Intent(ConversationListActivity.this, GroupListActivity.class);
                break;
            case R.id.my_name_tv://个人中心
                intent = new Intent(ConversationListActivity.this, PersonalCenterActivity.class);
                intent.putExtra("name", myInfo.getUserName());
                break;
            case R.id.exit_btn:
                JMessageClient.logout();//退出登录
                intent = new Intent(ConversationListActivity.this, MainActivity.class);
                break;
            default:
                break;
        }
        startActivity(intent);
    }
}
