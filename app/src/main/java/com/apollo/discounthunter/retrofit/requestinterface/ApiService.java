package com.apollo.discounthunter.retrofit.requestinterface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 主页列表的服务器接口
 * Created by Apollo on 2017/1/13.
 */

public interface ApiService {
    /**
     * 请求列表数据
     *
     * @param c
     * @param a
     * @param offset
     * @param limit
     * @param eid
     * @return
     */
    @GET("/")
    Call<ResponseBody> loadHomeListRepo(@Query("c") String c,
                                        @Query("a") String a,
                                        @Query("offset") String offset,
                                        @Query("limit") String limit,
                                        @Query("eid") String eid);

    /**
     * 请求搜索数据
     *
     * @param c
     * @param a
     * @param offset
     * @param limit
     * @param eid
     * @return
     */
    @GET("/")
    Call<ResponseBody> loadSearchListRepo(@Query("c") String c,
                                          @Query("a") String a,
                                          @Query("offset") String offset,
                                          @Query("limit") String limit,
                                          @Query("eid") String eid,
                                          @Query("q") String q);

    /**
     * 检查更新，数据源应用宝
     *
     * @param id
     * @param appid
     * @return
     */
    @GET("/")
    Call<ResponseBody> loadCheckUpdateRepo(@Query("id") String id,

                                           @Query("appid") String appid);

    /**
     * 检查更新，数据源github
     *
     * @return
     */
    @GET("/")
    Call<ResponseBody> loadCheckUpdateGithubRepo();


}
