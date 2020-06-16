package com.wd.news.base;

import cn.jpush.im.android.api.callback.GetReceiptDetailsCallback;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.callback.ProgressUpdateCallback;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class BaseMessage extends Message {
    private String cont;//消息内容
    private String name;//发消息的人
    private int type;//0收到消息，1发出消息

    public BaseMessage(String cont, String name, int type) {
        this.cont = cont;
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void getAtUserList(GetUserInfoListCallback getUserInfoListCallback) {

    }

    @Override
    public boolean isAtMe() {
        return false;
    }

    @Override
    public boolean isAtAll() {
        return false;
    }

    @Override
    public String getFromAppKey() {
        return null;
    }

    @Override
    public UserInfo getFromUser() {
        return null;
    }

    @Override
    public String getTargetName() {
        return null;
    }

    @Override
    public String getTargetID() {
        return null;
    }

    @Override
    public String getTargetAppKey() {
        return null;
    }

    @Override
    public void setOnContentUploadProgressCallback(ProgressUpdateCallback progressUpdateCallback) {

    }

    @Override
    public boolean isContentUploadProgressCallbackExists() {
        return false;
    }

    @Override
    public void setOnContentDownloadProgressCallback(ProgressUpdateCallback progressUpdateCallback) {

    }

    @Override
    public boolean isContentDownloadProgressCallbackExists() {
        return false;
    }

    @Override
    public void setOnSendCompleteCallback(BasicCallback basicCallback) {

    }

    @Override
    public boolean isSendCompleteCallbackExists() {
        return false;
    }

    @Override
    public boolean haveRead() {
        return false;
    }

    @Override
    public void setHaveRead(BasicCallback basicCallback) {

    }

    @Override
    public int getUnreceiptCnt() {
        return 0;
    }

    @Override
    public long getUnreceiptMtime() {
        return 0;
    }

    @Override
    public void setUnreceiptCnt(int i) {

    }

    @Override
    public void setUnreceiptMtime(long l) {

    }

    @Override
    public void getReceiptDetails(GetReceiptDetailsCallback getReceiptDetailsCallback) {

    }
}
