package com.youthlive.youthlive.INTERFACE;


import com.youthlive.youthlive.loginResponsePOJO.loginResponseBean;

import retrofit2.Call;
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



}
