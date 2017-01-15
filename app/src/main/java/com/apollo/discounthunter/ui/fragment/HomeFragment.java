package com.apollo.discounthunter.ui.fragment;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.adapter.HomeListAdapter;
import com.apollo.discounthunter.constants.Constants;
import com.apollo.discounthunter.retrofit.model.HomeModel;
import com.apollo.discounthunter.retrofit.requestinterface.ApiService;
import com.apollo.discounthunter.utils.LogUtil;
import com.apollo.discounthunter.widgets.XListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Apollo on 2017/1/13.
 */

public class HomeFragment extends BaseFragment {
    @BindView(R.id.xlv_home)
    XListView mXlvHome;
    List<HomeModel> mHomeModels = new ArrayList<>();
    private HomeListAdapter mAdapter;

    @Override
    protected void init() {
        requestData();

    }

    /**
     * 请求主页列表数据
     */
    private void requestData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> modelCall = service.loadHomeListRepo("API", "app_items", "0", "10", "0");
        showProgress();
        modelCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                clearProgress();
                parseData(response);
            }

            @Override
            public void onFailure(Throwable t) {
                clearProgress();
            }
        });

    }

    /**
     * 解析主页列表数据
     *
     * @param response
     */
    private void parseData(Response<ResponseBody> response) {
        String json = "";
        try {
            json = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        mHomeModels = gson.fromJson(json, new TypeToken<List<HomeModel>>() {
        }.getType());
        mAdapter = new HomeListAdapter(mContext,mHomeModels);
        mXlvHome.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected boolean hideBottom() {
        return false;
    }
}
