package com.apollo.discounthunter.collection.presenter;

import com.apollo.discounthunter.collection.model.CollectionModel;
import com.apollo.discounthunter.collection.model.ICollectionModel;
import com.apollo.discounthunter.collection.view.ICollectionView;
import com.apollo.discounthunter.greendao.bean.MyCollection;

import java.util.List;

/**
 * Created by Apollo on 2017/8/31 15:58
 */

public class PresenterImpl implements IPresenter {
    private ICollectionView collectionView;
    private ICollectionModel collectionModel;
    private List<MyCollection> allCollections;

    public PresenterImpl(ICollectionView collectionView) {
        this.collectionView = collectionView;
        collectionModel = new CollectionModel();
    }

    public void deleteCollection(MyCollection myCollection) {
        collectionModel.deleteCollection(myCollection);
        allCollections.remove(myCollection);
        collectionView.refreshView(allCollections);
    }

    public void getAllCollections() {
        allCollections = collectionModel.getAllCollections();
        collectionView.showData(allCollections);
    }
}
