package com.youthlive.youthlive.INTERFACE;


import com.youthlive.youthlive.addVideoPOJO.addVideoBean;
import com.youthlive.youthlive.getLivePOJO.getLiveBean;
import com.youthlive.youthlive.loginResponsePOJO.loginResponseBean;
import com.youthlive.youthlive.singleVideoPOJO.singleVideoBean;
import com.youthlive.youthlive.vlogListPOJO.vlogListBean;

import okhttp3.MultipartBody;
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
            @Part("phone") String phone,
            @Part("countryCode") String code
    );

    @Multipart
    @POST("youthlive/api/socialsign_up.php")
    Call<loginResponseBean> socialSignIn(
            @Part("pid") String pid,
            @Part("email") String email
    );

    @Multipart
    @POST("youthlive/api/resend_code.php")
    Call<loginResponseBean> resend(
            @Part("phone") String phone,
            @Part("countryCode") String code
    );

    @Multipart
    @POST("youthlive/api/update_user_info.php")
    Call<loginResponseBean> addUserData(
            @Part MultipartBody.Part file,
            @Part("userName") String userName,
            @Part("gender") String gender,
            @Part("birthday") String birthday,
            @Part("bio") String bio,
            @Part("userId") String userId
    );

    @Multipart
    @POST("youthlive/api/varify_code.php")
    Call<loginResponseBean> verify(
            @Part("userId") String userId,
            @Part("code") String code
    );

    @Multipart
    @POST("youthlive/api/create_password.php")
    Call<loginResponseBean> createPassword(
            @Part("userId") String userId,
            @Part("password") String password
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
    @POST("youthlive/api/all_video.php")
    Call<vlogListBean> getVlogList(
            @Part("userId") String userId
    );

    @Multipart
    @POST("youthlive/api/video_likes.php")
    Call<singleVideoBean> getsingleVideo(
            @Part("userId") String userId,
            @Part("videoId") String videoId
    );

    @Multipart
    @POST("youthlive/api/video_likes.php")
    Call<singleVideoBean> likeVideo(
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


    @Multipart
    @POST("youthlive/api/add_video.php")
    Call<addVideoBean> addVideo(
            @Part("userId") String userId,
            @Part("caption") String caption,
            @Part("tag") String tag,
            @Part MultipartBody.Part file
    );

}
