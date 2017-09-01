package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGenerator {
    public static void main(String[] args) throws Exception {
        //======================重要提示=======================
        //  运行完了此方法后要及时修改./greendao.dao.DaoMaster 里面的 onUpgrade方法,有字段变化需要更新数据库时，把版本号加1
        //  修改为  if (newVersion > oldVersion) {
        //               MigrationHelper.getInstance().migrate(db,ContactDao.class,MsgListDao.class,PriHisMsgDao.class.......);新增表时不用动新加的表dao
        //          } else {
        //              dropAllTables(db, true);
        //              onCreate(db);
        //          }
        //=====================================================
        Schema schema = new Schema(2, "com.apollo.discounthunter.greendao.bean");//前面参数为版本号后一个包名
        schema.setDefaultJavaPackageDao("com.apollo.discounthunter.greendao.dao");
        addBean(schema);//添加bean数据
        new DaoGenerator().generateAll(schema, "../DiscountHunter/app/src/main/java-gen");
    }

    private static void addBean(Schema schema) {
        Entity contact = schema.addEntity("SearchHistory");//一个实体类对应一张数据库表，此处表名为 SearchHistory （即类名）
        // 也可以重新命名表名
        // person.setTableName("People");
//        contact.addIdProperty().primaryKey().autoincrement();//主键自增长
        contact.addStringProperty("search").notNull();//搜索关键字，非空
        contact.addStringProperty("time").notNull();//时间，非空

        Entity collection = schema.addEntity("MyCollection");//我的收藏
        collection.addStringProperty("id").notNull().primaryKey().javaDocField("折扣信息唯一id");
        collection.addStringProperty("web_url").javaDocField("折扣信息下单地址");
        collection.addStringProperty("app_url").javaDocField("唤起淘宝app的折扣信息下单地址");
        collection.addStringProperty("pic").javaDocField("折扣信息图片地址");
        collection.addStringProperty("title").javaDocField("折扣信息标题");
        collection.addStringProperty("reason").javaDocField("折扣信息推荐理由");
        collection.addStringProperty("price").javaDocField("价格");
        collection.addStringProperty("soldcount").javaDocField("");
        collection.addStringProperty("commission").javaDocField("");
        collection.addStringProperty("item_cat_id").javaDocField("");
        collection.addStringProperty("num_iid").javaDocField("");
        collection.addStringProperty("platform_id").javaDocField("");
        collection.addStringProperty("end_time").javaDocField("");
        collection.addStringProperty("release_time").javaDocField("商品更新时间");
        collection.addStringProperty("eventid").javaDocField("");
        collection.addStringProperty("addtime").javaDocField("");
        collection.addStringProperty("seller_id").javaDocField("");
        collection.addStringProperty("quan_id").javaDocField("");
        collection.addStringProperty("quan_price").javaDocField("");
        collection.addStringProperty("quan_link").javaDocField("优惠券链接");
        collection.addStringProperty("flag").javaDocField("置顶标志 非0置顶");
        collection.addStringProperty("totalCount").javaDocField("优惠券总数");
        collection.addStringProperty("appliedCount").javaDocField("优惠券领取总数");

    }


}
