package com.wx.tiktok.NET;

import com.wx.tiktok.BEAN.Post;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface PostVideo {



    String HOST ="http://test.androidcamp.bytedance.com/";
    @Multipart
    @POST("mini_douyin/invoke/video")
    Call<Post> createVideo(
            @Query("student_id") String studentId,
            @Query("user_name") String userName,
            @Part MultipartBody.Part image, @Part MultipartBody.Part video);



}
