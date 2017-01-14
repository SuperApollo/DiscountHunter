package com.apollo.discounthunter.retrofit.requestinterface;

import com.apollo.discounthunter.constants.Constants;
import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.GET;

/**
 * 主页列表的服务器接口
 * Created by Apollo on 2017/1/13.
 */

public interface ApiService {
    @GET(Constants.HOME_LIST)
    Call<ResponseBody> loadHomeListRepo();
}
