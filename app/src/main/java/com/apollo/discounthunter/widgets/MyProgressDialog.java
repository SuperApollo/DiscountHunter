package com.apollo.discounthunter.widgets;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.apollo.discounthunter.R;


/**
 * 自定义方形progressdialog
 * Created by Apollo on 2017/2/8.
 */

public class MyProgressDialog extends ProgressDialog {
    int layoutId = -1;

    public MyProgressDialog(Context context) {
        super(context);
    }

    public MyProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public MyProgressDialog(Context context, int theme, int layoutId) {
        super(context, theme);
        this.layoutId = layoutId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(getContext());
        //添加消失动画
        Window window = getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
    }


    private void init(Context context) {
        //设置不可取消，点击其他区域不能取消，实际中可以抽出去封装供外包设置
//        setCancelable(false);
//        setCanceledOnTouchOutside(false);
        if (layoutId == -1)
            setContentView(R.layout.load_dialog);
        else
            setContentView(layoutId);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }
}
