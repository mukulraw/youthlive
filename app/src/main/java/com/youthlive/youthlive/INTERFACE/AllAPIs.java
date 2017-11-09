package com.youthlive.youthlive.INTERFACE;


import com.youthlive.youthlive.getLivePOJO.getLiveBean;
import com.youthlive.youthlive.loginResponsePOJO.loginResponseBean;
import com.youthlive.youthlive.singleVideoPOJO.singleVideoBean;
import com.youthlive.youthlive.vlogListPOJO.vlogListBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AllAPIs {

    @Multipart
    @POST("youthlive/api/sign_up.php")
    Call<loginResponseBean> signUp(
            @Part("name") String name,
            @Part("email") String email,
            @Part("password") String password,
            @Part("phone") String phone
    );

    @Multipart
    @POST("youthlive/api/mobile_signin.php")
    Call<loginResponseBean> signIn(
            @Part("phone") String phone,
            @Part("password") String password
    );

    @Headers({
            "Accept: application/vnd.bambuser.v1+json",
            "Content-Type: application/json",
            "Authorization: Bearer 374rnkqn332isfldjc8a3oki8"
    })
    @GET("broadcasts")
    Call<getLiveBean> getLiveList();

    @Multipart
    @POST("youthlive/api/get_video.php")
    Call<vlogListBean> getVlogList(
            @Part("userId") String userId
    );

    @Multipart
    @POST("youthlive/api/get_single_video.php")
    Call<singleVideoBean> getsingleVideo(
            @Part("userId") String userId,
            @Part("videoId") String videoId
    );

    @Multipart
    @POST("youthlive/api/video_comment.php")
    Call<vlogListBean> comment(
            @Part("userId") String userId,
            @Part("videoId") String videoId,
            @Part("comment") String comment
    );

}
