package com.wx.tiktok.BEAN;

/**
 * @author Xavier.S
 * @date 2019.01.17 18:08
 */
public class Cat {

    private String student_id;
    private String user_name;
    private String image_url;
    private String video_url;
    private String _id;
    private int image_w;
    private int image_h;
    private  String createdAt;
    private  String updatedAt;
    private  int _v;

    public int get_v() {
        return _v;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void set_v(int _v) {
        this._v = _v;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getImage_h() {
        return image_h;
    }

    public int getImage_w() {
        return image_w;
    }

    public String get_id() {
        return _id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getStudent_id() {
        return student_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setCreatedAt(String createAt) {
        this.createdAt = createAt;
    }

    public void setImage_h(int image_h) {
        this.image_h = image_h;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setImage_w(int image_w) {
        this.image_w = image_w;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }


}
