package com.wd.common.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;


import com.wd.common.Base.Apis;
import com.wd.common.Base.BaseApplication;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {

    private Apis apis;

    private RetrofitUtil(){
        init();
    }
    private static class getHttputil{
        private static final RetrofitUtil HTTP_UTIL=new RetrofitUtil();
    }
    public static RetrofitUtil getInstance(){
        return getHttputil.HTTP_UTIL;
    }
    public void init(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .addInterceptor(new Header())
                .addInterceptor(httpLoggingInterceptor);
        OkHttpClient build = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://mobile.bwstudent.com/movieApi/")
                .client(build)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apis = retrofit.create(Apis.class);
    }
    public Apis getApis(){
        return apis;
    }
    public Boolean isWifi(Context context){
        ConnectivityManager cm= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null) {
            return true;
        }
        return false;
    }
    public class Header implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            String userId = SPUtils.getString(BaseApplication.getContext(), SPUtils.USERINFO_NAME, SPUtils.USERINFO_KEY_USER_ID);
            String sessionId = SPUtils.getString(BaseApplication.getContext(), SPUtils.USERINFO_NAME, SPUtils.USERINFO_KEY_SESSION_ID);

            if (TextUtils.isEmpty(userId)||TextUtils.isEmpty(sessionId)){
                Request build = request.newBuilder()
                        .addHeader("ak", "0110010010000")
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .build();
                return chain.proceed(build);
            }
            Request build = request.newBuilder()
                    .addHeader("userId", userId)
                    .addHeader("sessionId", sessionId)
                    .addHeader("ak", "0110010010000")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            return chain.proceed(build);
        }
    }
    public RequestBody getRequsetBody(List<File> files,HashMap<String,String> map){
//        if (map.size() < 1){
//            return null;
//        }
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (Map.Entry<String,String> entry:map.entrySet()){
            Log.i("xxx","key = "+entry.getKey()+"value = "+entry.getValue());
            builder.addFormDataPart(entry.getKey(),entry.getValue()+"");
        }

        for (int i = 0; i <files.size(); i++){
            builder.addFormDataPart("image",files.get(i).getName(),RequestBody.create(MediaType.parse("image/jepg"),files.get(i)));
        }

        return builder.build();
    }



}
