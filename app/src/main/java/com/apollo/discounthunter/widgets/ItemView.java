package com.apollo.discounthunter.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
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
    private int rightDrawable;//右侧图标
    private int showType = 1;//右箭头
    private View topLineView;
    private View bottomLineView;
    private TextView tvLef;
    private ImageView ivRight;
    private onItemClickedListner onItemClickedListner;
    private OnToggleButtonChangeListner toggleButtonChangeListner;
    private RelativeLayout bodyLayout;
    private SwitchButton toggleButton;

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
        this(context, null);
    }

    public ItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        ivRight = (ImageView) itemView.findViewById(R.id.item_iv_right);
        bodyLayout = (RelativeLayout) itemView.findViewById(R.id.item_body_layout);
        toggleButton = (SwitchButton) itemView.findViewById(R.id.item_switch_button);

        if (topLine) {
            topLineView.setVisibility(VISIBLE);
        }
        if (bottomLine) {
            bottomLineView.setVisibility(VISIBLE);
        }

        tvLef.setText(leftText);

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
