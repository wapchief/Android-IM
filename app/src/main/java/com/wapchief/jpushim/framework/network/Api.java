package com.wapchief.jpushim.framework.network;

import com.wapchief.jpushim.entity.UserStateBean;
import com.wapchief.jpushim.entity.UserStateListBean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by wapchief on 2017/9/25.
 * 所有接口请求类
 */

public interface Api {

    /*获取好友在线信息*/
    @GET("/v1/users/{username}/userstat")
    Call<UserStateBean> isFriendState(
            @Path("username") String username);

    /*获取好友批量在线状态*/

    @POST("/v1/users/userstat")
    Call<UserStateListBean> isFriendsStateList(@Query("") String[] list
    );


    /*获取用户资料*/
    @GET("/v1/users/{username}")
    Call<ResponseBody> userInfo(
            @Path("username") String username);
}
