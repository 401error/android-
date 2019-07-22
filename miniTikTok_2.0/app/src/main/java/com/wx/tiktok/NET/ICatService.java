package com.wx.tiktok.NET;



import com.wx.tiktok.BEAN.imageresponse;

import retrofit2.Call;
import retrofit2.http.GET;


/**
 * @author Xavier.S
 * @date 2019.01.15 16:42
 */
public interface ICatService {
    // TODO-C1 (2) Implement your Cat Request here, url: https://api.thecatapi.com/v1/images/search?limit=5
    @GET("mini_douyin/invoke/video")
    Call<imageresponse> fechout();




}
