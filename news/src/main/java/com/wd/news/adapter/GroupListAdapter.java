package com.wd.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wd.news.R;
import com.wd.news.bean.MyGroupInfo;

import java.util.List;

public class GroupListAdapter extends BaseAdapter {
    private Context mContext;
    private List<MyGroupInfo> myGroupInfoList;
    private LayoutInflater inflater;

    public GroupListAdapter(Context mContext, List<MyGroupInfo> myGroupInfoList) {
        this.mContext = mContext;
        this.myGroupInfoList = myGroupInfoList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return myGroupInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return myGroupInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.conversation_list_item, null);
            holder.groupName = convertView.findViewById(R.id.friend_name_tv);
            holder.groupDesc = convertView.findViewById(R.id.content_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MyGroupInfo groupInfo = myGroupInfoList.get(position);
        holder.groupName.setText(groupInfo.getGroupName() + "(" + groupInfo.getMemberCount() + ")");
        holder.groupDesc.setText(groupInfo.getGroupDesc());

        return convertView;
    }

    public class ViewHolder {
        TextView groupName, groupDesc;
    }
}
