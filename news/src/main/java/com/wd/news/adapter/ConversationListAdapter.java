package com.wd.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.news.R;

import java.util.List;

import cn.jpush.im.android.api.model.Conversation;

public class ConversationListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Conversation> mDatas;
    private LayoutInflater inflater;

    public ConversationListAdapter(Context mContext, List<Conversation> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Conversation cs = mDatas.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.conversation_list_item, null);
            holder.headIv = convertView.findViewById(R.id.friend_head_iv);
            holder.name = convertView.findViewById(R.id.friend_name_tv);
            holder.content = convertView.findViewById(R.id.content_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        holder.headIv.setImageBitmap();
        holder.name.setText(cs.getTitle());//会话a的名字
        holder.content.setText(cs.getLatestText());//最近的会话内容
        return convertView;
    }

    public class ViewHolder {
        ImageView headIv;
        TextView name, content;
    }
}
