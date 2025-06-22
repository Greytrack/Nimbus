package com.cjc.nimbus.constant;

/**
 * @author xianggw
 * @ClassName AiredgeConstant
 * @description: 系统常量
 * @datetime 2022/7/27 14:59
 * @version: 1.0
 */
public class AirEdgeConstants {

    public static final String PARM_DYNAMIC_SUB_CLASS_LISTABLE_CLASS_ID = "PARM_DYNAMIC_SUB_CLASS_LISTABLE_CLASS_ID";
    public static final String PARM_DYNAMIC_SUB_CLASS_LISTABLE_BASE_CLASS_ID = "PARM_DYNAMIC_SUB_CLASS_LISTABLE_BASE_CLASS_ID";
    public static final String PARM_DYNAMIC_GET_ALL = "PARM_DYNAMIC_GET_ALL";

    public static final String SUCCESS = "success";

    public static final String ORIGIN_ID = "originId";

    public static final String USER_DEFAULT_ROLE = "UserDefaultRole";
    /**
     * 产品2.0前缀
     */
    public static final String AIR_EDGE2_PREFIX = "ae2-";
    /**
     * ROOT
     */
    public static final Long ROOT_ID = 0L;
    /**
     * ROOT APIID
     */
    public static final String ROOT_API_ID = "ROOT";
    /**
     * 外部协作组织ROOT
     */
    public static final String EXTERNAL_API_ID = "EXTERNAL_ROOT";
    /**
     * 外部协作组织名称
     */
    public static final String EXTERNAL_DEPARTMENT_NAME = "外部协作组织";
    /**
     * 外部协作组织编码
     */
    public static final String EXTERNAL_DEPARTMENT_CODE = "EXTERNAL_DEPARTMENT";
    /**
     * 初始化租户id
     */
    public static final Long DEFAULT_TENANT_ID = 1L;
    /**
     * 中文语言APIID
     */
    public static final String ZH_CN_API_ID = "zh_CN";
    /**
     * 英文语言APIID
     */
    public static final String EN_US_API_ID = "en_US";
    /**
     * 请求头设置的语言类型
     */
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    /**
     * 主键
     */
    public static final String ID = "id";
    /**
     * 租户ID
     */
    public static final String TENANT_ID = "tenantId";
    /**
     * APIID
     */
    public static final String API_ID = "APIID";
    /**
     * 未找到数据源异常
     */
    public static final String CAN_NOT_FIND_DATA_SOURCE_EXCEPTION = "CannotFindDataSourceException";
    /**
     * 上海时区常量
     */
    public static final String SHANGHAI_TIME_ZONE = "Asia/Shanghai";
    /**
     * 2.0登录
     */
    public static final String AIR_EDGE2_LOGIN_URI = "/api/login";
    /**
     * 2.0退出
     */
    public static final String AIR_EDGE2_LOGOUT_URI = "/api/login/logout";
    /**
     * 2.0用户默认角色API ID
     */
    public static final String AIR_EDGE2_DEFAULT_ROLE_API_ID = "UserDefaultRole";
    /**
     * 过滤启用状态 参数
     */
    public static final String FILTER_ENABLED = "filterEnabled";
    public static final String LIMIT_ONE_SQL = " limit 1";
    /**
     * 默认开始节点APIID
     */
    public static final String DEFAULT_START_NODE_API_ID = "DefaultStart";
    /**
     * 默认开始节点（第一个审批节点）APIID
     */
    public static final String DEFAULT_FIRST_APPROVAL_NODE_API_ID = "Start";
    /**
     * 流程编号规则ID
     */
    public static final Long WORKFLOW_NUMBER_RULE_ID = 494109050720608L;
    /**
     * 其他分支APIID
     */
    public static final String OTHER_BRANCH_API_ID = "OtherBranch";
    /**
     * 工作流节点名称
     */
    public static final String WORKFLOW_NODE_NAME = "workflowNodeName";
    /**
     * 旧用户名称
     */
    public static final String OLD_USER_NAME = "oldUserName";
    /**
     * 工作流实例ID
     */
    public static final String WORKFLOW_INSTANCE_ID = "workflowInstanceId";
    /**
     * 用户名称
     */
    public static final String USER_NAME = "userName";
    /**
     * 发现权限
     */
    public static final String DISCOVERY = "Discovery";
    /**
     * 读取权限
     */
    public static final String READ = "Read";
    /**
     * 修改权限
     */
    public static final String MODIFY = "Modify";
    /**
     * 创建权限
     */
    public static final String CREATE = "Create";
    /**
     * delete权限
     */
    public static final String DELETE = "Delete";
    /**
     * 主键别名
     */
    public static final String ALIAS_PRIVILEGE_NAME = "privilege_main";
    /**
     * 是否开启权限
     */
    public static final boolean PRIVILEGE_ENABLE = true;
    public static final boolean REDIS_CACHE = false;
    /**
     * 语言ID
     */
    public static final String LANGUAGE_ID = "languageId";
    /**
     * 过滤ID列表
     */
    public static final String FILTER_ID_LIST = "FILTER_ID_LIST";
    /**
     * 是否关联物料或文件
     */
    public static final String RELATED_MATERIAL_OR_FILE = "relatedMaterialOrFile";
    /**
     * 源ID
     */
    public static final String SOURCE_ID = "sourceId";
    /**
     * 源ID
     */
    public static final String SOURCE_FROM = "sourceFrom";
    /**
     * 变更实例ID
     */
    public static final String CHANGE_INSTANCE_ID = "changeInstanceId";
    /**
     * 2099年，永久有效时间戳
     */
    public static final Long PERMANENT_TIME = 4102415999000L;
    /**
     * 名称
     */
    public static final String NAME = "name";
    /**
     * 审批动作
     */
    public static final String APPROVAL_ACTION = "approvalAction";

    /**
     * 管理员用户 ID
     */
    public static final Long ADMIN_USER_ID = 419436436619264L;

    public static final String SCHEDULED_TASK_ID = "SCHEDULED_TASK_ID";
    public static final String SCHEDULED_TASK_NODE_ID = "SCHEDULED_NODE_ID";
    public static final String ONE_SHOOT_EXECUTOR_ID = "ONE_SHOOT_EXECUTOR_ID";
    public static final String EXEC_DATA = "EXEC_DATA";
    public static final String PLAN_EXEC_TIME = "PLAN_EXEC_TIME";

    public static final String WORKFLOW_TASK_KEY_PATTERN = "%s:%s:%s";

    protected AirEdgeConstants() {
    }


}
