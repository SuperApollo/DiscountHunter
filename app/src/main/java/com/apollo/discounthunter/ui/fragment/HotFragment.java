package com.apollo.discounthunter.ui.fragment;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.constants.Constants;
import com.apollo.discounthunter.retrofit.model.HomeModel;
import com.apollo.discounthunter.retrofit.requestinterface.HomeListService;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Apollo on 2017/1/13.
 */

public class HotFragment extends BaseFragment {
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

        HomeListService service = retrofit.create(HomeListService.class);
        Call<HomeModel> modelCall = service.repo("?c=API&a=app_items&offset=0&limit=10&eid=0");
        modelCall.enqueue(new Callback<HomeModel>() {
            @Override
            public void onResponse(Response<HomeModel> response, Retrofit retrofit) {
                mToastUtils.show(mContext,"成功");
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hot;
    }

    @Override
    protected boolean hideBottom() {
        return false;
    }
}
