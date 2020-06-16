package com.wd.news.view;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wd.news.R;
import com.wd.news.base.BaseActivity;
import com.wd.news.event.MyEvent;
import com.wd.news.util.FileUtil;
import com.wd.news.util.TimeUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.api.model.UserInfo.Field;
import cn.jpush.im.api.BasicCallback;

public class PersonalCenterActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "PersonalCenterActivity";
    private ImageView headIv;
    private TextView nickNameTv, birthdayTv, signatureTv, genderTv, regionTv, addressTv;

    private UserInfo myInfo;
    private Field field;

    private static final int REQUEST_EXTERNAL_STORAGE = 0;
    private static String[] PERMISSIONS_STORAGE = new String[]{
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.read_EXTERNAL_STORAGE"};// 读取权限
    private static final int PHOTO_REQUEST_GALLERY = 1;// 从相册中选择

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        EventBus.getDefault().register(this);
        initView();
        myInfo = JMessageClient.getMyInfo();
        showInfo();
    }

    private void showInfo() {
        Log.e(TAG, "--" + myInfo.toString());
        nickNameTv.setText(myInfo.getNickname());
        birthdayTv.setText(TimeUtil.getDateFromMillisecond(myInfo.getBirthday()));
        signatureTv.setText(myInfo.getSignature());
        if (myInfo.getGender().name().equals("male")) {
            genderTv.setText("男");
        } else if (myInfo.getGender().name().equals("female")) {
            genderTv.setText("女");
        } else {
            genderTv.setText("未知");
        }
        regionTv.setText(myInfo.getRegion());
        addressTv.setText(myInfo.getAddress());
        //获取头像
        //从本地获取用户头像的缩略头像bitmap，如果本地存在头像缩略图文件，直接返回；若不存在，会异步从服务器拉取。 下载完成后 会将头像保存至本地并返回。当用户未设置头像，或者下载失败时回调返回Null。
        //所有的缩略头像bitmap在sdk内都会缓存， 并且会有清理机制，所以上层不需要对缩略头像bitmap做缓存。
        if (myInfo.getAvatar() != null) {
            myInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                @Override
                public void gotResult(int i, String s, Bitmap bitmap) {
                    if (i == 0) {
                        Log.e(TAG, "--gotResult：获取缩略头像成功！");
                        headIv.setImageBitmap(bitmap);
                    } else {
                        Log.e(TAG, "--gotResult：获取缩略头像失败！");
                    }
                }
            });

        }
    }

    private void initView() {
        headIv = findViewById(R.id.head_iv);
        nickNameTv = findViewById(R.id.nick_name_tv);
        birthdayTv = findViewById(R.id.birthday_tv);
        signatureTv = findViewById(R.id.signature_tv);
        genderTv = findViewById(R.id.gender_tv);
        regionTv = findViewById(R.id.region_tv);
        addressTv = findViewById(R.id.address_tv);
        clicks();
    }

    private void clicks() {
        nickNameTv.setOnClickListener(this);
        birthdayTv.setOnClickListener(this);
        signatureTv.setOnClickListener(this);
        genderTv.setOnClickListener(this);
        regionTv.setOnClickListener(this);
        addressTv.setOnClickListener(this);
        //更新头像
        headIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取权限
                checkPermission();
            }
        });
        //更新生日
        birthdayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(PersonalCenterActivity.this, UpdateUinfoActivity.class);
        switch (v.getId()) {
            case R.id.nick_name_tv:
                intent.putExtra("name", "nickName");
                break;
            case R.id.signature_tv:
                intent.putExtra("name", "signature");
                break;
            case R.id.gender_tv:
                intent.putExtra("name", "gender");
                break;
            case R.id.region_tv:
                intent.putExtra("name", "region");
                break;
            case R.id.address_tv:
                intent.putExtra("name", "address");
                break;
            default:
                break;
        }
        startActivity(intent);
    }

    //默认显示日期
    int year = 2018;
    int month = 2;
    int day = 29;

    private void showDatePicker() {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int pickerYear, int monthOfYear, int dayOfMonth) {
                year = pickerYear;
                month = monthOfYear;
                day = dayOfMonth;
                String time = pickerYear + "-" + (monthOfYear + 1) + "-" + dayOfMonth + " 0:0:0";
                Log.e(TAG, "onDateSet:" + time);
                field = Field.birthday;
                myInfo.setBirthday(TimeUtil.timeStrToSecond(time));
                JMessageClient.updateMyInfo(field, myInfo, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        if (i == 0) {
                            birthdayTv.setText(month + 1 + "月" + day + "日");
                            Toast.makeText(PersonalCenterActivity.this, "更新生日信息成功！", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "--updateMyInfo:更新生日信息成功！");
                        } else {
                            Toast.makeText(PersonalCenterActivity.this, "更新生日信息失败！", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "--updateMyInfo:更新生日信息失败！");
                        }
                    }
                });
            }
        }, year, month, day).show();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            } else {
                pickPicture();
            }
        } else {
            pickPicture();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickPicture();
            } else {
                Log.e(TAG, "---没有读写权限");
            }
        }
    }

    private void pickPicture() {
        //从图库选择图片
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Log.e(TAG, "---onActivityResult resultCode:error");
            return;
        }
        //外界的程序访问ContentProvider所提供数据  通过ContentResolver接口
        ContentResolver resolver = getContentResolver();
        Bitmap bm = null;
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                try {
                    Uri uri = data.getData();//图片uri
                    bm = MediaStore.Images.Media.getBitmap(resolver, uri);
                    headIv.setImageBitmap(bm);//设置头像

                    File file = FileUtil.getFileByUri(this, uri);
                    Log.e(TAG, "---file path:" + file.getAbsolutePath());
                    JMessageClient.updateUserAvatar(file, new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0) {
                                Toast.makeText(PersonalCenterActivity.this, "头像更新成功！", Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "----头像更新成功!");
                            } else {
                                Toast.makeText(PersonalCenterActivity.this, "头像更新失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "----头像更新失败!--" + s);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 更新信息后刷新信息
     *
     * @param event
     */
    @Subscribe
    public void refreshInfo(MyEvent event) {
        if (event.refresh == true) {
            myInfo = JMessageClient.getMyInfo();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
