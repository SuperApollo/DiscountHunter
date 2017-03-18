package com.apollo.discounthunter.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.apollo.discounthunter.R;

/**
 * 意见反馈
 * Created by apollo on 17-3-18.
 */

public class FeedbackActivity extends BaseActivity {

    private EditText etFeedback;
    private Button btnFeedback;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected int getMenuLayoutId() {
        return -1;
    }

    @Override
    protected void initView(View view) {
        etFeedback = queryViewById(view, R.id.et_feedback);
        btnFeedback = queryViewById(view, R.id.btn_feedback);
        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String feedback = etFeedback.getText().toString();
                if (TextUtils.isEmpty(feedback)) {
                    mToastUtils.show(FeedbackActivity.this, "内容不能为空");
                    return;
                }
                doFeedBack(feedback);
                mToastUtils.show(FeedbackActivity.this, "提交成功");
                FeedbackActivity.this.finish();
            }
        });
    }

    private void doFeedBack(String feedback) {

    }
}
