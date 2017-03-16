package com.apollo.discounthunter.retrofit.requestinterface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by apollo on 17-3-16.
 */

public interface TestDownloadService {
    @Streaming
    @GET
    Call<ResponseBody> testDownload(@Url String testUrl);
}
