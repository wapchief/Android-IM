package com.wapchief.jpushim.framework.network;

import android.util.Log;

import com.wapchief.jpushim.entity.UserStateBean;
import com.wapchief.jpushim.entity.UserStateListBean;
import com.wapchief.jpushim.framework.helper.SharedPrefHelper;
import com.wapchief.jpushim.framework.utils.Base64Utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wapchief on 2017/9/25.
 * 网络访问的基类
 * HTTP 验证
 * 验证采用 HTTP Basic 机制，即 HTTP Header（头）里加一个字段（Key/Value对）：
 * Authorization: Basic base64_auth_string
 * 其中 base64_auth_string 的生成算法为：base64(appKey:masterSecret)
 * 即，对 appKey 加上冒号，加上 masterSecret 拼装起来的字符串，再做 base64 转换。
 */

public class NetWorkManager {
    public static String AppKey="b47a37f342eba5f9fbcd1961";
    public static String masterSecret = "8a664e4e727113addc250b61";
    public static String base64_auth_string = Base64Utils.getBase64(AppKey +":"+masterSecret);
    SharedPrefHelper helper = SharedPrefHelper.getInstance();

    public static OkHttpClient.Builder httpClient;


    /*获取请求头*/
    public static void headers() {
        httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {

            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .addHeader("Authorization", "Basic " + base64_auth_string)
                        .build();
                Log.d("onrequest", "request:" + request.toString());
                Log.d("onrequestHeader", "request headers:" + request.headers().toString());
                return chain.proceed(request);
            }
        });
    }


    /*判断好友在线状态*/
    public static void isFriendState(String userName, Callback<UserStateBean> callback){
        headers();
        Log.e("base64_auth_string", base64_auth_string);
        OkHttpClient client=httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Url.JPUSH_ROOT)
                .client(client)
                .build();
        Api aPi = retrofit.create(Api.class);
        Call<UserStateBean> call = aPi.isFriendState(
                userName);
        call.enqueue(callback);

    }

    /*批量查询好友在线状态*/
    public static void isFriendStateList(String[] list,Callback<UserStateListBean> callback){
        headers();
        OkHttpClient client=httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Url.JPUSH_ROOT)
                .client(client)
                .build();
        Api aPi = retrofit.create(Api.class);
        Call<UserStateListBean> call = aPi.isFriendsStateList(
                list);
        call.enqueue(callback);
    }

    /*获取用户资料*/

    public static void getUserInfo(String username,Callback<ResponseBody> callback){
        headers();
        OkHttpClient client=httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Url.JPUSH_ROOT)
                .client(client)
                .build();
        Api aPi = retrofit.create(Api.class);
        Call<ResponseBody> call = aPi.userInfo(
                username);
        call.enqueue(callback);

    }
}
