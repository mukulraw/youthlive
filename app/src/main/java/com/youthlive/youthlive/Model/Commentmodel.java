package com.youthlive.youthlive.Model;

/**
 * Created by USER on 10/31/2017.
 */

public class Commentmodel {
    String comment;
    String createddate;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreateddate() {
        return createddate;
    }

    public void setCreateddate(String createddate) {
        this.createddate = createddate;
    }

    public String getCommenthash() {
        return commenthash;
    }

    public void setCommenthash(String commenthash) {
        this.commenthash = commenthash;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    String commenthash;
    String userid;
}
