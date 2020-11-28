package kr.hs.emirim.seungmin.javaproject_azaz.Model;

import java.util.Date;

public class Comments extends CommentId{
    private String message, user_id;
    private Date timestamp;
    private String comment_id;

    public Comments() {}

    public Comments(String message, String user_id, Date timestamp, String comment_id) {
        this.message = message;
        this.user_id = user_id;
        this.timestamp = timestamp;
        this.comment_id = comment_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }
}

