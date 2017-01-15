package com.apollo.discounthunter.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.retrofit.model.HomeModel;
import com.apollo.discounthunter.utils.ImageLoaderUtils;
import com.apollo.discounthunter.utils.TimeUtils;

import java.util.List;

/**
 * Created by wangpengbo on 2017/1/15.
 */

public class HomeListAdapter extends BaseAdapter {
    Context mContext;
    List<HomeModel> mDatas;
    LayoutInflater mInflater;


    public HomeListAdapter(Context context, List<HomeModel> datas) {
        this.mContext = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HomeModel homeModel = mDatas.get(i);
        ViewHolder holder;
        ImageLoaderUtils imageLoaderUtils = ImageLoaderUtils.getInstance(mContext);
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.home_list_item, null);
            holder.ivIcon = (ImageView) view.findViewById(R.id.iv_home_item_icon);
            holder.tvTitle = (TextView) view.findViewById(R.id.tv_home_item_title);
            holder.tvPrice = (TextView) view.findViewById(R.id.tv_home_item_price);
            holder.tvTime = (TextView) view.findViewById(R.id.tv_home_item_time);
            holder.tvReason = (TextView) view.findViewById(R.id.tv_home_item_reason);
            holder.tvUnusable = (TextView) view.findViewById(R.id.tv_home_item_unusable);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();

        }
        imageLoaderUtils.loadImageView(homeModel.getPic(), holder.ivIcon);
        String title = homeModel.getTitle();
        if (!TextUtils.equals("0", homeModel.getFlag()))
            title = "【置顶】" + title;
        holder.tvTitle.setText(title);
        float p = Float.parseFloat(homeModel.getPrice());
        if (p > 0)
            holder.tvPrice.setText("¥" + homeModel.getPrice());
        else
            holder.tvPrice.setVisibility(View.GONE);
        holder.tvTime.setText(TimeUtils.getReleaseTime(homeModel.getRelease_time()));
        holder.tvReason.setText(homeModel.getReason());

        if (!TextUtils.isEmpty(homeModel.getTotalCount())&&!TextUtils.isEmpty(homeModel.getAppliedCount())){
            float totlaCount = Float.parseFloat(homeModel.getTotalCount());
            float appliedCount = Float.parseFloat(homeModel.getAppliedCount());
            if ((appliedCount / totlaCount) < 1)
                holder.tvUnusable.setVisibility(View.GONE);
            else
                holder.tvUnusable.setVisibility(View.VISIBLE);
        }

        return view;
    }

    class ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvPrice;
        TextView tvTime;
        TextView tvReason;
        TextView tvUnusable;
    }

}
