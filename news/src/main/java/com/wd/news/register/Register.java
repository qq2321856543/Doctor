package com.wd.news.register;

import android.util.Log;

import com.wd.news.constant.Const;
import com.wd.news.event.MyEvent;

import org.greenrobot.eventbus.EventBus;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

public class Register {
    public static final String TAG = "Register";

    public static void registerUserAndLogin(final String userName, final String psw) {
        JMessageClient.register(userName, psw, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                Log.e(TAG, "userName:" + userName + ",psw:" + psw);
                if (i == 0) {
                    Log.e(TAG, "注册用户成功（单信息）！！" + s);
                    login(userName, psw);
                } else {
                    Log.e(TAG, "code:" + i + "注册用户失败（单信息）！！" + s);
                    EventBus.getDefault().post(new MyEvent(Const.REGISTER_FAILED));
                }
            }
        });

//        RegisterOptionalUserInfo userInfo = new RegisterOptionalUserInfo();
////        userInfo.setAvatar("001");
//        userInfo.setAddress("光谷");
//        userInfo.setBirthday(2018-03-28);
//        userInfo.setGender(UserInfo.Gender.male);//male男性，female女性
//        userInfo.setNickname("这是昵称呢");
//        userInfo.setRegion("湖北");
//        userInfo.setSignature("个性签名个性签名个性签名");
//        JMessageClient.register(userName, psw, userInfo, new BasicCallback() {
//            @Override
//            public void gotResult(int i, String s) {
//                if (i == 0) {
//                    Log.e(TAG, "注册用户成功（多信息）！！");
////                        login();
//                } else {
//                    Log.e(TAG, "code:" + i + "注册用户失败（多信息）！！" + s);
//                }
//            }
//        });
    }

    public static void login(final String userName, String psw) {
        JMessageClient.login(userName, psw, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    Log.e(TAG, "login:" + userName + "----登录成功!!");
                    EventBus.getDefault().post(new MyEvent(Const.REGISTER_SUCCESS));
                } else {
                    Log.e(TAG, "code:" + i + "login:" + userName + "----登录失败");
                    EventBus.getDefault().post(new MyEvent(Const.LOGIN_FAILED));
                }
            }
        });
    }
}
