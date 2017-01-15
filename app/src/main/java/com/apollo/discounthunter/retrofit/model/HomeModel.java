package com.apollo.discounthunter.retrofit.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 首页列表实体类
 * Created by Apollo on 2017/1/13.
 */

public class HomeModel implements Parcelable {
    private String id;//折扣信息唯一id
    private String web_url;//折扣信息下单地址
    private String app_url;//唤起淘宝app的折扣信息下单地址
    private String pic;//折扣信息图片地址
    private String title;//折扣信息标题
    private String reason;//折扣信息推荐理由
    private String price;//价格
    private String soldcount;
    private String commission;
    private String item_cat_id;
    private String num_iid;
    private String platform_id;
    private String end_time;
    private String release_time;//商品更新时间
    private String eventid;
    private String addtime;
    private String seller_id;
    private String quan_id;
    private String quan_price;
    private String quan_link;//优惠券链接
    private String flag;//置顶标志 非0置顶
    private String totalCount;//优惠券总数
    private String appliedCount;//优惠券领取总数

    protected HomeModel(Parcel in) {
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

    public static final Creator<HomeModel> CREATOR = new Creator<HomeModel>() {
        @Override
        public HomeModel createFromParcel(Parcel in) {
            return new HomeModel(in);
        }

        @Override
        public HomeModel[] newArray(int size) {
            return new HomeModel[size];
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
