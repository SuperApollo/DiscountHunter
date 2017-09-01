package com.apollo.discounthunter.collection.model;

import com.apollo.discounthunter.greendao.bean.MyCollection;

import java.util.List;

/**
 * Created by Apollo on 2017/8/31 15:31
 */

public interface ICollectionModel {
    String toBeString();

    List<MyCollection> getAllCollections();

    void deleteCollection(MyCollection myCollection);

    void deleteAllCollection();
}
