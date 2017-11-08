package com.youthlive.youthlive.Response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 4/28/2017.
 */
public class FollowerResponse {

    @SerializedName("follow_status")
    private String follow_status;

    @SerializedName("success")
    private int success;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public void setFollow_status(String follow_status) {
        this.follow_status = follow_status;
    }
    public String getFollow_status() {
        return follow_status;
    }

}
