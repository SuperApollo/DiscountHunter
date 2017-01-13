package com.apollo.discounthunter.ui.fragment;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.constants.Constants;

import retrofit.GsonConverterFactory;
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
