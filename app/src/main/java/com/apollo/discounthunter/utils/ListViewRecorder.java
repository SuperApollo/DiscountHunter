package com.apollo.discounthunter.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.apollo.discounthunter.base.BaseApplication;

/**
 * listview位置记录恢复工具类,适用于每个item高度相等的listview
 * Created by Apollo on 2017/2/15.
 */

public class ListViewRecorder {
    private ListView mListView;

    private int scrolledY;
    private int offSetY;
    private int listPos;
    private int lastY;
    private int firstVisiblePosition;

    public ListViewRecorder(ListView listView) {
        mListView = listView;
    }

    /**
     * 设置listView的滑动监听
     */
    public void initEvent() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 不滚动时记录当前滚动到的位置
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    record();
                    //第二种方法
                    ViewGroup item = (ViewGroup) mListView.getChildAt(1);//此处必须为0,因为listview有头布局，所以在此为1
                    offSetY = item.getTop();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                listPos = firstVisibleItem;
            }
        });
    }

    /**
     * 记录位置
     */
    public void record() {
        scrolledY = getScrollY();
    }

    /**
     * 获取ListView的ScrollY
     *
     * @return
     */
    public int getScrollY() {
        View c = mListView.getChildAt(1);
        if (c == null) {
            return 0;
        }
        firstVisiblePosition = mListView.getFirstVisiblePosition();
        int top = c.getTop();
        //因为有下拉刷新头，所以计算的有偏差，要减去头布局高度
        int total = Math.abs(top) + firstVisiblePosition * c.getHeight() + ViewUtil.dp2px(BaseApplication.getContext(), 60);
        return total;
    }

    /**
     * 恢复位置
     */
    public void restore() {
        if (scrolledY != lastY) {//没有滑动则不改变位置，防止闪动
            mListView.post(new Runnable() {//防止卡顿
                @Override
                public void run() {
                    mListView.smoothScrollBy(scrolledY, 0);
                }
            });
        }
        lastY = scrolledY;
    }

    /**
     * 恢复位置和上面对比哪个好？
     */
    public void reCover() {
        mListView.setSelectionFromTop(listPos, offSetY);
    }

    /**
     * 恢复到记录的item
     */
    public void reCoverTo() {
        mListView.setSelection(firstVisiblePosition);

    }


}
