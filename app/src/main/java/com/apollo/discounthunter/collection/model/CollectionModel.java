package com.apollo.discounthunter.collection.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.apollo.discounthunter.greendao.bean.MyCollection;
import com.apollo.discounthunter.greendao.daohelper.MyCollectionDaoHelper;

import java.util.List;

/**
 * Created by Apollo on 2017/8/31 15:37
 */

public class CollectionModel extends MyCollection implements ICollectionModel, Parcelable {


    private MyCollectionDaoHelper myCollectionDaoHelper;

    public CollectionModel() {
        myCollectionDaoHelper = MyCollectionDaoHelper.getMyCollectionDaoHelper();
    }

    protected CollectionModel(Parcel in) {
    }

    public static final Creator<CollectionModel> CREATOR = new Creator<CollectionModel>() {
        @Override
        public CollectionModel createFromParcel(Parcel in) {
            return new CollectionModel(in);
        }

        @Override
        public CollectionModel[] newArray(int size) {
            return new CollectionModel[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    @Override
    public String toBeString() {
        return "id:" + getId() + "title:" + getTitle() + "reason:" + getReason() + "price:" + getPrice();
    }

    @Override
    public List<MyCollection> getAllCollections() {
        return myCollectionDaoHelper.getAllMyCollection();
    }

    @Override
    public void deleteCollection(MyCollection myCollection) {
        myCollectionDaoHelper.deleteMyCollection(myCollection);
    }

    @Override
    public void deleteAllCollection() {
        myCollectionDaoHelper.deleteAllMyCollection();
    }
}
