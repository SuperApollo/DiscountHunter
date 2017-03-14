package com.apollo.discounthunter.ui.activity;

import android.widget.TextView;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.constants.Constants;
import com.apollo.discounthunter.retrofit.requestinterface.ApiService;
import com.apollo.discounthunter.utils.AppUtil;
import com.apollo.discounthunter.widgets.ItemView;
import com.squareup.okhttp.ResponseBody;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by apollo on 17-3-14.
 */

public class AboutActivity extends BaseActivity {
    @BindView(R.id.item_about_update)
    ItemView itemUpdate;
    @BindView(R.id.item_about_suggestion)
    ItemView itemSuggestion;
    @BindView(R.id.tv_about_version)
    TextView tvVersion;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected int getMenuLayoutId() {
        return -1;
    }

    @Override
    protected void initView() {
        String versionName = AppUtil.getAppVersionName(this);
        tvVersion.setText("折扣猎手 V" + versionName);
        itemUpdate.setOnItemClickedListner(new ItemView.onItemClickedListner() {
            @Override
            public void onClick() {
                mToastUtils.show(AboutActivity.this, "检查更新");
                checkUpdate();
            }
        });

        itemSuggestion.setOnItemClickedListner(new ItemView.onItemClickedListner() {
            @Override
            public void onClick() {
                mToastUtils.show(AboutActivity.this, "意见反馈");
            }
        });
    }

    /**
     * 检查更新
     */
    private void checkUpdate() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.CHECK_UPDATE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> call = service.loadCheckUpdateRepo("detail", "1105879293");
        showProgress();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                clearProgress();
            }

            @Override
            public void onFailure(Throwable t) {
                clearProgress();
            }
        });


    }

}
