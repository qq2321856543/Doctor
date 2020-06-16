package com.wd.news.manager;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupInfoCallback;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.api.BasicCallback;

public class GroupManager {
    private static Context mContext;

    public GroupManager(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 添加群成员
     *
     * @param id      群ID
     * @param members 要添加的用户名集合
     */
    public static void addGroupMembers(long id, List<String> members) {
        JMessageClient.addGroupMembers(id, members, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                } else {
                    Log.e("addMembers", "--群添加成员失败！" + s);
                }
            }
        });
    }

    /**
     * 移除群成员
     *
     * @param groupId 群ID
     * @param names   多个成员集合
     */
    public static void removeGroupMembers(long groupId, List<String> names) {
        JMessageClient.removeGroupMembers(groupId, names, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    Toast.makeText(mContext, "您移除了xxx", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "解散群失败:" + s, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * 移交群主
     *
     * @param groupId  群ID
     * @param userName 新的群主名
     */
    public static void changGroupAdmin(long groupId, final String userName) {
        JMessageClient.getGroupInfo(groupId, new GetGroupInfoCallback() {
            @Override
            public void gotResult(int i, String s, GroupInfo groupInfo) {
                if (i == 0) {
                    groupInfo.changeGroupAdmin(userName, "", new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0) {
                                Toast.makeText(mContext, "群主移交给：" + userName, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(mContext, "移交群主失败:" + s, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 解散指定的群组，只有群的群主有权限解散。
     * 群组解散后会以message的形式通知到群内所有成员，类型为{@link cn.jpush.im.android.api.content.EventNotificationContent.EventNotificationType#group_dissolved}
     *
     * @param groupID 群组id
     * @since 2.5.0
     */
    public static void dissoveGroup(long groupID) {

        JMessageClient.adminDissolveGroup(groupID, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    Toast.makeText(mContext, "您解散了群", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "解散群失败:" + s, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * 退出群
     *
     * @param groupId
     */
    public static void exitGroup(long groupId) {
        JMessageClient.exitGroup(groupId, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    Toast.makeText(mContext, "您退出了群", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "退群失败:" + s, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * 屏蔽群消息
     */
    public static void blockGroupMesage(long groupId) {
        JMessageClient.getGroupInfo(groupId, new GetGroupInfoCallback() {
            @Override
            public void gotResult(int i, String s, GroupInfo groupInfo) {
                if (i == 0) {
                    groupInfo.setBlockGroupMessage(1, new BasicCallback() {//1屏蔽，0取消屏蔽
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0) {
                                Toast.makeText(mContext, "您屏蔽了群消息", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(mContext, "屏蔽群消息失败:" + s, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
