package com.wd.news.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.wd.news.R;
import com.wd.news.adapter.ConversationMassageAdapter;
import com.wd.news.base.BaseMessage;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;

public class ConversationDetailActivity extends Activity implements View.OnClickListener {
    private static String TAG = "ConversationDetailActivity";
    private TextView csTitleTv;
    private EditText messageEt;

    private Button sendBtn;
    private ListView messageListView;
    private List<BaseMessage> messageListData;

    private ConversationMassageAdapter conversationMassageAdapter;
    private Conversation conversation;
    private String name;//单聊会话的对象
    private String memberName;//群聊会话的对象
    private long groupId;//群ID
    private boolean isSingle = true;//单聊or群聊
    private List<Message> historyListData;
    private int offSet = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_detail);
        init();
        JMessageClient.registerEventReceiver(this);
    }

    private void init() {
        csTitleTv = findViewById(R.id.cs_title_name);
        messageListView = findViewById(R.id.list_view_message);
        messageEt = findViewById(R.id.message_et);
        sendBtn = findViewById(R.id.message_send_btn);
        name = getIntent().getStringExtra("name");//单聊或群的名字
        groupId = getIntent().getLongExtra("id", -1);
        csTitleTv.setText(name);
        if (groupId != -1) {
            isSingle = false;
        } else {
            isSingle = true;
        }

        csTitleTv.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
        messageListData = new ArrayList<>();
        conversationMassageAdapter = new ConversationMassageAdapter(ConversationDetailActivity.this, messageListData);
        setDefaultMessage();//历史消息记录
        //已在会话页面，通知栏不显示消息
        if (isSingle) {
            JMessageClient.enterSingleConversation(name);
        } else {
            JMessageClient.enterGroupConversation(groupId);
        }

    }

    /**
     * 打开一条会话，设置显示最多6条历史消息
     */
    private void setDefaultMessage() {
        if (isSingle) {//单聊
            conversation = JMessageClient.getSingleConversation(name);
            if (conversation == null) {
                Log.e(TAG, "---第一次对话,跳过取历史消息步骤！");
                return;
            }
        } else {//群聊
            conversation = JMessageClient.getGroupConversation(groupId);

            if (conversation == null) {
                return;
            }
        }
        historyListData = conversation.getAllMessage();
        offSet = historyListData.size();
        if (offSet == 0) {
            return;
        }
        if (offSet > 6) {//历史消息过多时，显示最近6条
            offSet = offSet - 6;
            for (int i = 0; i < 6; i++) {
                Message message = historyListData.get(offSet);
                memberName = message.getFromUser().getUserName();
                ;
                TextContent textcontent = (TextContent) message.getContent();
//                EventNotificationContent textContent = message.getContent();
                String content = textcontent.getText();

                if (message.getDirect().name().equals("send")) {
                    messageListData.add(new BaseMessage(content, "", 1));
                } else if (message.getDirect().name().equals("receive")) {
                    messageListData.add(new BaseMessage(content, memberName, 0));
                }
                offSet++;
            }
        } else if (offSet <= 6) {
            for (int i = 0; i < offSet; i++) {
                Message message = historyListData.get(i);
                memberName = message.getFromUser().getUserName();
                TextContent textcontent;
                String content;
                if (isSingle) {
                    textcontent = (TextContent) message.getContent();
                    content = textcontent.getText();
                } else {
                    MessageContent mc = message.getContent();
                    if (mc.getContentType().name().equals("text")) {
                        textcontent = (TextContent) message.getContent();
                        content = textcontent.getText();
                    } else {
                        //邀请了xxx 加入群聊 系统语句
                        EventNotificationContent enc = (EventNotificationContent) message.getContent();
                        content = enc.getEventText();
                    }
                }


                if (message.getDirect().name().equals("send")) {
                    messageListData.add(new BaseMessage(content, "", 1));
                } else if (message.getDirect().name().equals("receive")) {
                    messageListData.add(new BaseMessage(content, memberName, 0));
                }
            }
        }
        messageListView.setAdapter(conversationMassageAdapter);
//        messageListView.smoothScrollToPosition(messageListView.getCount());
        messageListView.setSelection(conversationMassageAdapter.getCount() - 1);
        conversationMassageAdapter.notifyDataSetChanged();
    }

    //接收到消息
    public void onEventMainThread(MessageEvent event) {
        //do your own business
        Message msg = event.getMessage();//收到的消息
        switch (msg.getContentType()) {
            case text:
                TextContent textcontent = (TextContent) msg.getContent();
                String content = textcontent.getText();
                memberName = msg.getFromUser().getUserName();
                messageListData.add(new BaseMessage(content, memberName, 0));
                messageListView.setAdapter(conversationMassageAdapter);
                messageListView.setSelection(conversationMassageAdapter.getCount() - 1);
                conversationMassageAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(this);
        JMessageClient.exitConversation();//退出会话页面启用通知栏
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.message_send_btn://发送消息
                String content = messageEt.getText().toString().trim();
                Message message;
                if (isSingle) {
                    message = JMessageClient.createSingleTextMessage(name, content);
                } else {
                    message = JMessageClient.createGroupTextMessage(groupId, content);
                }
                JMessageClient.sendMessage(message);
                messageListData.add(new BaseMessage(content, "", 1));
                messageListView.setAdapter(conversationMassageAdapter);
                messageListView.setSelection(conversationMassageAdapter.getCount() - 1);
                conversationMassageAdapter.notifyDataSetChanged();
                messageEt.setText("");
                break;
            case R.id.cs_title_name:
                if (!isSingle) {
                    Intent intent = new Intent(ConversationDetailActivity.this, GroupInfoActivity.class);
                    intent.putExtra("id", groupId);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }
}
