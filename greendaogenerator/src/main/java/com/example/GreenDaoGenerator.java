package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.apollo.discounthunter.greendao.bean");//前面参数为版本号后一个包名
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
    }

}
