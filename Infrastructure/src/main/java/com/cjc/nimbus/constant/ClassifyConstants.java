package com.cjc.nimbus.constant;

/**
 * @author 周程成
 * @ClassName ClassifyConstants
 * @description: 分类常量
 * @datetime 2024/7/27 14:59
 * @version: 1.0
 */
public class ClassifyConstants {

    /**
     * EBOM视图ID
     */
    public static final Long EBOM = 1001L;
    /**
     * 用户
     */
    public static final Long USER = 1001L;
    /**
     * 用户组
     */
    public static final Long USER_GROUP = 1002L;
    /**
     * 物料
     */
    public static final Long PART = 1003L;
    /**
     * 文件
     */
    public static final Long FILE = 1004L;
    /**
     * 变更
     */
    public static final Long CHANGE = 1005L;
    /**
     * 产品空间
     */
    public static final Long PROJECT_REPOSITORY = 1016L;
    /**
     * 项目
     */
    public static final Long PROJECT = 1101L;
    /**
     * 项目成员
     */
    public static final Long REVIEW_CHECKLIST_ITEM = 1104L;
    /**
     * 项目检查单
     */
    public static final Long CHECK_LIST = 1105L;
    /**
     * 项目变更
     */
    public static final Long PROJECT_CHANGE = 1106L;
    /**
     * 项目状态变更
     */
    public static final Long PROJECT_STATUS_CHANGE = 1107L;
    /**
     * 项目关闭
     */
    public static final Long PROJECT_CLOSURE = 1108L;
    /**
     * 变更记录页签大分类
     */
    public static final Long CHANGE_RECORDS = 2013L;
    /**
     * 团队
     */
    public static final Long TEAM = 2031L;
    /**
     * 文件夹
     */
    public static final Long PROJECT_DIR = 90001L;
    /**
     * 生命周期变更(中分类)
     */
    public static final Long LIFE_CYCLE_CHANGE = 10050101L;
    /**
     * 变更申请(中分类)
     */
    public static final Long CHANGE_REQUEST = 10050201L;
    /**
     * 变更指令(中分类)
     */
    public static final Long CHANGE_ORDER = 10050301L;
    /**
     * 变更通知(中分类)
     */
    public static final Long CHANGE_NOTICE = 10050401L;


    private ClassifyConstants() {
        throw new IllegalStateException("Utility class");
    }
}
