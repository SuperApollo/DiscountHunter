package com.apollo.discounthunter.collection.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Apollo on 2017/8/31 15:37
 */

public class GoodsModel implements IGoods,Parcelable {
    public String id = null;//折扣信息唯一id
    String web_url = null;//折扣信息下单地址
    String app_url = null;//唤起淘宝app的折扣信息下单地址
    String pic = null;//折扣信息图片地址
    String title = null;//折扣信息标题
    String reason = null;//折扣信息推荐理由
    String price = null;//价格
    String soldcount = null;
    String commission = null;
    String item_cat_id = null;
    String num_iid = null;
    String platform_id = null;
    String end_time = null;
    String release_time = null;//商品更新时间
    String eventid = null;
    String addtime = null;
    String seller_id = null;
    String quan_id = null;
    String quan_price = null;
    String quan_link = null;//优惠券链接
    String flag = null;//置顶标志 非0置顶
    String totalCount = null;//优惠券总数
    String appliedCount = null;//优惠券领取总数

    public GoodsModel(String id, String web_url, String app_url, String pic, String title, String reason, String price, String soldcount, String commission, String item_cat_id, String num_iid, String platform_id, String end_time, String release_time, String eventid, String addtime, String seller_id, String quan_id, String quan_price, String quan_link, String flag, String totalCount, String appliedCount) {
        this.id = id;
        this.web_url = web_url;
        this.app_url = app_url;
        this.pic = pic;
        this.title = title;
        this.reason = reason;
        this.price = price;
        this.soldcount = soldcount;
        this.commission = commission;
        this.item_cat_id = item_cat_id;
        this.num_iid = num_iid;
        this.platform_id = platform_id;
        this.end_time = end_time;
        this.release_time = release_time;
        this.eventid = eventid;
        this.addtime = addtime;
        this.seller_id = seller_id;
        this.quan_id = quan_id;
        this.quan_price = quan_price;
        this.quan_link = quan_link;
        this.flag = flag;
        this.totalCount = totalCount;
        this.appliedCount = appliedCount;
    }

    protected GoodsModel(Parcel in) {
        id = in.readString();
        web_url = in.readString();
        app_url = in.readString();
        pic = in.readString();
        title = in.readString();
        reason = in.readString();
        price = in.readString();
        soldcount = in.readString();
        commission = in.readString();
        item_cat_id = in.readString();
        num_iid = in.readString();
        platform_id = in.readString();
        end_time = in.readString();
        release_time = in.readString();
        eventid = in.readString();
        addtime = in.readString();
        seller_id = in.readString();
        quan_id = in.readString();
        quan_price = in.readString();
        quan_link = in.readString();
        flag = in.readString();
        totalCount = in.readString();
        appliedCount = in.readString();
    }

    public static final Creator<GoodsModel> CREATOR = new Creator<GoodsModel>() {
        @Override
        public GoodsModel createFromParcel(Parcel in) {
            return new GoodsModel(in);
        }

        @Override
        public GoodsModel[] newArray(int size) {
            return new GoodsModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public String getApp_url() {
        return app_url;
    }

    public void setApp_url(String app_url) {
        this.app_url = app_url;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSoldcount() {
        return soldcount;
    }

    public void setSoldcount(String soldcount) {
        this.soldcount = soldcount;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getItem_cat_id() {
        return item_cat_id;
    }

    public void setItem_cat_id(String item_cat_id) {
        this.item_cat_id = item_cat_id;
    }

    public String getNum_iid() {
        return num_iid;
    }

    public void setNum_iid(String num_iid) {
        this.num_iid = num_iid;
    }

    public String getPlatform_id() {
        return platform_id;
    }

    public void setPlatform_id(String platform_id) {
        this.platform_id = platform_id;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getRelease_time() {
        return release_time;
    }

    public void setRelease_time(String release_time) {
        this.release_time = release_time;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getQuan_id() {
        return quan_id;
    }

    public void setQuan_id(String quan_id) {
        this.quan_id = quan_id;
    }

    public String getQuan_price() {
        return quan_price;
    }

    public void setQuan_price(String quan_price) {
        this.quan_price = quan_price;
    }

    public String getQuan_link() {
        return quan_link;
    }

    public void setQuan_link(String quan_link) {
        this.quan_link = quan_link;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getAppliedCount() {
        return appliedCount;
    }

    public void setAppliedCount(String appliedCount) {
        this.appliedCount = appliedCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(web_url);
        parcel.writeString(app_url);
        parcel.writeString(pic);
        parcel.writeString(title);
        parcel.writeString(reason);
        parcel.writeString(price);
        parcel.writeString(soldcount);
        parcel.writeString(commission);
        parcel.writeString(item_cat_id);
        parcel.writeString(num_iid);
        parcel.writeString(platform_id);
        parcel.writeString(end_time);
        parcel.writeString(release_time);
        parcel.writeString(eventid);
        parcel.writeString(addtime);
        parcel.writeString(seller_id);
        parcel.writeString(quan_id);
        parcel.writeString(quan_price);
        parcel.writeString(quan_link);
        parcel.writeString(flag);
        parcel.writeString(totalCount);
        parcel.writeString(appliedCount);
    }
}
