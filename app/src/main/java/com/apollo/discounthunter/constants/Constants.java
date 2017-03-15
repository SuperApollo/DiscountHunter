package com.apollo.discounthunter.constants;

/**
 * Created by Apollo on 2017/1/13.
 */

public interface Constants {
    /**
     * 服务器根地址
     */
    String BASE_URL = "http://www.zhekoulieshou.com/";
    /**
     * 检查更新地址
     */
    String CHECK_UPDATE_URL = "http://app.qq.com/";
//          +"#id=detail&appid=1105879293";
    /**
     * 放在github上的app
     */
    String CHECK_UPDATE_GITHUB_URL = "https://raw.githubusercontent.com/SuperApollo/app-update/master/description";
    /**
     * 首页列表数据地址
     */
    String HOME_LIST = BASE_URL + "?c=API&a=app_items&offset=0&limit=10&eid=0";
    /**
     * 点击商品条目跳转携带的商品信息
     */
    String GOODS_INFO = "goods_info";
    /**
     * bundle传值标志
     */
    String BUNDLE_TAG = "bundle_tag";
}
