package com.youthlive.youthlive.Response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 4/18/2017.
 */
public class PasswordForgetResponse {



    @SerializedName("message")
    private String message;

    @SerializedName("success")
    private int success;

    public int getSuccess() {
        return success;
    }
    public void setSuccess(int success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
