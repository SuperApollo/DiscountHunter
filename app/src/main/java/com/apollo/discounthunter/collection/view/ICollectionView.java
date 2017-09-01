package com.apollo.discounthunter.collection.view;

import com.apollo.discounthunter.greendao.bean.MyCollection;

import java.util.List;

/**
 * Created by Apollo on 2017/8/31 15:59
 */

public interface ICollectionView {
    void showData(List<MyCollection> datas);
    void refreshView(List<MyCollection> datas);
}
