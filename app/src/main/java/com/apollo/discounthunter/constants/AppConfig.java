package com.apollo.discounthunter.constants;

import android.os.Environment;

import java.io.File;

/**
 * Created by ${Apollo} on 2017/1/13.
 */

public class AppConfig {
    /**
     * UI设计的基准宽度.
     */
    public static int UI_WIDTH = 720;

    /**
     * UI设计的基准高度.
     */
    public static int UI_HEIGHT = 1280;

    /**
     * UI设计的密度.
     */
    public static int UI_DENSITY = 2;

    /**
     * 默认 SharePreferences文件名.
     */
    public static String SHARED_PATH = "app_share";
    public static String SYN_SHARED_PATH = "syn_app_share";

    /**
     * app下载位置
     */
    public static final String FILE_DOWNLOAD = Environment.getExternalStorageDirectory() + File.separator + "discount_hunter/";

    /**
     * 数据库名称
     */
    public static final String DB_NAME = "discount_hunter.db";
    /**
     * 是否有新版本
     */
    public static final String HAS_UPDATE = "has_update";
}
