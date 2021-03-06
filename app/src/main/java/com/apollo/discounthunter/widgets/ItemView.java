package com.apollo.discounthunter.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apollo.discounthunter.R;
import com.suke.widget.SwitchButton;


/**
 * 自定义itemview
 * Created by apollo on 17-3-14.
 */

public class ItemView extends LinearLayout {
    private boolean topLine;//是否显示顶部分割线
    private boolean bottomLine;//是否显示底部分割线
    private int leftDrawable;//左侧图标
    private String leftText;//左侧文字
    private String rightText;//右侧文字
    private int rightDrawable;//右侧图标
    private boolean redPoint;//右侧小红点
    private int showType = 1;//右箭头显示样式
    private View topLineView;
    private View bottomLineView;
    private TextView tvLef;
    private ImageView ivRight;
    private onItemClickedListner onItemClickedListner;
    private OnToggleButtonChangeListner toggleButtonChangeListner;
    private ConstraintLayout bodyLayout;
    private SwitchButton toggleButton;
    private TextView tvRight;
    private ImageView ivPoint;

    public View getTopLineView() {
        return topLineView;
    }

    public View getBottomLineView() {
        return bottomLineView;
    }

    public TextView getTvLef() {
        return tvLef;
    }

    public ImageView getIvRight() {
        return ivRight;
    }

    public ConstraintLayout getBodyLayout() {
        return bodyLayout;
    }

    public TextView getTvRight() {
        return tvRight;
    }

    public ImageView getIvPoint() {
        return ivPoint;
    }

    public String getRightText() {
        return rightText;
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
    }

    public boolean isRedPoint() {
        return redPoint;
    }

    public void setRedPoint(boolean redPoint) {
        this.redPoint = redPoint;
    }

    public void setToggleButtonChangeListner(OnToggleButtonChangeListner toggleButtonChangeListner) {
        this.toggleButtonChangeListner = toggleButtonChangeListner;
    }

    public void setOnItemClickedListner(ItemView.onItemClickedListner onItemClickedListner) {
        this.onItemClickedListner = onItemClickedListner;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public SwitchButton getToggleButton() {
        return toggleButton;
    }

    public void setToggleButton(SwitchButton toggleButton) {
        this.toggleButton = toggleButton;
    }

    public boolean isTopLine() {
        return topLine;
    }

    public void setTopLine(boolean topLine) {
        this.topLine = topLine;
    }

    public boolean isBottomLine() {
        return bottomLine;
    }

    public void setBottomLine(boolean bottomLine) {
        this.bottomLine = bottomLine;
    }

    public int getLeftDrawable() {
        return leftDrawable;
    }

    public void setLeftDrawable(int leftDrawable) {
        this.leftDrawable = leftDrawable;
    }

    public String getLeftText() {
        return leftText;
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;
    }

    public int getRightDrawable() {
        return rightDrawable;
    }

    public void setRightDrawable(int rightDrawable) {
        this.rightDrawable = rightDrawable;
    }

    public ItemView(Context context) {
        super(context);
    }

    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //获取样式
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemView);
        if (typedArray != null) {
            for (int i = 0; i < typedArray.getIndexCount(); i++) {
                int attr = typedArray.getIndex(i);
                switch (attr) {
                    case R.styleable.ItemView_topLine:
                        topLine = typedArray.getBoolean(attr, false);
                        break;
                    case R.styleable.ItemView_bottomLine:
                        bottomLine = typedArray.getBoolean(attr, false);
                        break;
                    case R.styleable.ItemView_leftDrawable:
                        leftDrawable = typedArray.getResourceId(attr, 0);
                        break;
                    case R.styleable.ItemView_leftText:
                        leftText = typedArray.getString(attr);
                        break;
                    case R.styleable.ItemView_rightText:
                        rightText = typedArray.getString(attr);
                        break;
                    case R.styleable.ItemView_redPoint:
                        redPoint = typedArray.getBoolean(attr, false);
                        break;
                    case R.styleable.ItemView_rightDrawable:
                        rightDrawable = typedArray.getResourceId(attr, 0);
                        break;
                    case R.styleable.ItemView_showType:
                        showType = typedArray.getInt(attr, 1);
                        break;
                }

            }
        }

        typedArray.recycle();
        initView(context);
    }

    private void initView(Context context) {
        View itemView = View.inflate(context, R.layout.itemview_layout, this);//将自定义的布局设置给当前控件
        topLineView = itemView.findViewById(R.id.item_top_line);
        bottomLineView = itemView.findViewById(R.id.item_bottom_line);
        tvLef = (TextView) itemView.findViewById(R.id.item_tv_left);
        tvRight = (TextView) itemView.findViewById(R.id.item_tv_right);
        ivPoint = (ImageView) itemView.findViewById(R.id.item_iv_point);
        ivRight = (ImageView) itemView.findViewById(R.id.item_iv_right);
        bodyLayout = (ConstraintLayout) itemView.findViewById(R.id.item_body_layout);
        toggleButton = (SwitchButton) itemView.findViewById(R.id.item_switch_button);

        if (topLine) {
            topLineView.setVisibility(VISIBLE);
        }
        if (bottomLine) {
            bottomLineView.setVisibility(VISIBLE);
        }

        tvLef.setText(leftText);
        if (!TextUtils.isEmpty(rightText)) {
            tvRight.setText(rightText);
            tvRight.setVisibility(VISIBLE);
        }
        if (redPoint) {
            ivPoint.setVisibility(VISIBLE);
        }

        switch (showType) {
            case 0://普通
                ivRight.setVisibility(GONE);
                toggleButton.setVisibility(GONE);
                break;
            case 1://右箭头
                ivRight.setVisibility(VISIBLE);
                toggleButton.setVisibility(GONE);
                if (this.rightDrawable != 0) {
                    Drawable rightDrawable = getResources().getDrawable(this.rightDrawable);
                    ivRight.setImageDrawable(rightDrawable);
                }
                break;
            case 2://switchbutton
                ivRight.setVisibility(GONE);
                toggleButton.setVisibility(VISIBLE);
                break;
        }
        if (leftDrawable != 0) {
            Drawable leftDrawable = getResources().getDrawable(this.leftDrawable);
            //这一步必须要做，否则不显示
            leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
            //代码动态设置textview的leftDrawable
            tvLef.setCompoundDrawables(leftDrawable, null, null, null);
        }

        bodyLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickedListner != null)
                    onItemClickedListner.onClick();
            }
        });

        toggleButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (toggleButtonChangeListner != null)
                    toggleButtonChangeListner.onToggleChanged(isChecked);
            }
        });

    }

    /**
     * itemview的点击监听
     */
    public interface onItemClickedListner {
        void onClick();
    }

    /**
     * switchbutton状态改变监听
     */
    public interface OnToggleButtonChangeListner {
        void onToggleChanged(boolean on);
    }

}
