package com.apollo.discounthunter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.greendao.bean.SearchHistory;

import java.util.List;

/**
 * Created by Apollo on 2017/1/18.
 */

public class HistoryAdapter extends BaseAdapter {
    List<SearchHistory> datas;
    Context context;
    LayoutInflater inflater;

    public HistoryAdapter(List<SearchHistory> datas, Context context) {
        this.datas = datas;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HistoryHolder holder;
        SearchHistory searchHistory = datas.get(i);
        if (view == null) {
            holder = new HistoryHolder();
            view = inflater.inflate(R.layout.histtory_item, null);
            holder.tvHistory = (TextView) view.findViewById(R.id.tv_history_item);
            view.setTag(holder);

        } else {
            holder = (HistoryHolder) view.getTag();
        }
        holder.tvHistory.setText(searchHistory.getSearch());

        return view;
    }

    class HistoryHolder {
        TextView tvHistory;
    }
}
