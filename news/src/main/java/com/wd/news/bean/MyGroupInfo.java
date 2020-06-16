package com.wd.news.bean;

public class MyGroupInfo {
    private String groupName;
    private String groupDesc;
    private int memberCount;
    private long groupId;

    public MyGroupInfo(String groupName, String groupDesc, int memberCount, long groupId) {
        this.groupName = groupName;
        this.groupDesc = groupDesc;
        this.memberCount = memberCount;
        this.groupId = groupId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }
}
