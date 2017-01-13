package com.apollo.discounthunter.retrofit.requestinterface;

import com.apollo.discounthunter.retrofit.model.HomeModel;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * 主页列表的服务器接口
 * Created by Apollo on 2017/1/13.
 */

public interface HomeListService {
    @GET("append")
    Call<HomeModel> repo(@Path("append") String append);
}
