package com.cjc.nimbus.result;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局响应码
 * 0-9999 为系统保留
 * 约定：
 * 从左往右，第1位应用编码，第2，3位为主模块编号，第4，5位为子模块编号，第6，7，8位为内部编码
 * 第1位：1 后台 2 前端
 * 第2，3位：101 业务支持 100 全局支撑
 * 第4，5位 10101 国际化 10102 基础设置
 * 10000000-10099999 保留
 * 10001000-10000999 通用【common】，主要包含通用的响应码，infrastructure模块中的响应码
 * 10101000-10101999 国际化【i18n】
 * 10102000-10102999 ObjectFrameWork【基类,类等】
 * 10103000-10103999 RolePrivilege【角色权限】
 * 10104000-10104999 SystemIntegration【系统集成，包含：px、event、ldap、邮件等】
 * 10105000-10105999 Workflow【工作流】
 * 10106000-10106999 criteria【条件】
 * 10107000-10107999 分类设置【分类设置】
 * 10108000-10108008 设置中心【附件格式】
 * 10109000-10109999 智能规则设置
 * 10108200-10108300 文件服务器【文件服务器】
 * 10108400-10108500 公告设置【公告设置】
 * 10108600-10108700 定时任务
 * 10110000-10110999 产品空间
 * 10110100-10110199 基线
 * 10110200-10110299 流程（变更）
 * 10111000-10112999 activity
 * 10112000-10112999 输入输出
 * 10113000-10113999 扩展设置
 *
 * @author CJC
 */
@Slf4j
public enum GlobalResultCode implements ResCode {
    SUCCESS(0, "成功"),
    BAD_REQUEST(400, "异常请求"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "方法不允许"),
    USER_NOT_EXISTS(407, "用户信息不存在，请联系管理员"),
    LOGIN_IN_LOGIN_NAME_EMPTY(411, "登录用户名不能为空"),
    LOGIN_IN_USER_PASSWORD_EMPTY(412, "登录密码不能为空"),
    INTERNAL_SERVER_ERROR(500, "功能异常，请联系管理员"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    JPA_OPTIMISTIC_LOCK_ERROR(601, "乐观锁异常，请刷新重试"),
    REPEAT_SUBMIT_INTERVAL_ERROR(603, "重复提交间隔时间不能小于[1]秒"),
    ID_GENERATE_ERROR(603, "ID生成异常"),
    ACCEPT_LANGUAGE_ERROR(604, "请求头[Accept-Language]参数缺失"),
    UNSUPPORTED_REQUEST_METHOD(605, "不支持%s的请求"),
    MISSING_PATH_VARIABLE(606, "缺失必需的路径变量%s"),
    /**
     * Mybatis执行异常
     */
    MYBATIS_EXECUTE_ERROR(608, "Mybatis执行异常"),
    /**
     * 未知异常
     */
    UNKNOWN_ERROR(609, "程序好像出错了，请联系管理员"),
    /**
     * 请求参数类型不匹配，参数[%s]要求类型为：'%s'，但输入值为：'%s'
     */
    METHOD_ARGUMENT_TYPE_MISMATCH(610, "请求参数类型不匹配，参数[%s]要求类型为：'%s'，但输入值为：'%s'"),
    /**
     * 未找到执行策略的分发器
     */
    STRATEGY_DISPATCHER_NOT_FOUND(611, "程序好像出错了，请联系管理员"),
    CONFIG_ERROR(613, "系统配置错误"),
    /**
     * SQL语法异常
     */
    SQL_SYNTAX_ERROR(612, "程序好像出错了，请联系管理员"),
    PARAM_CHECK_ERROR(1000, "参数错误"),
    DATA_NOT_EXIST(1001, "数据不存在"),
    FILE_EXPORT_ERROR(1002, "文件导出异常"),
    FILE_IMPORT_ERROR(1003, "文件导入异常"),
    API_ID_ERROR(1004, "APIID只能输入大小写字母、数字、英文点和下划线，且长度不超过40"),
    PRIMARY_KEY_ERROR(1005, "主键不能为空"),
    AQUIRE_REDISSON_LOCK_FAIL(1006, "获取redis锁失败"),


    /**
     * 请输入%s~%s之间的文本内容(excel导出单元格长度校验占用)
     */
    EXCEL_LENGTH_NOT_MATCH(1007, "请输入%s~%s之间的文本内容"),
    /**
     * 输入文本长度有误
     */
    EXCEL_LENGTH_ERROR(1008, "输入文本长度有误"),
    /**
     * 分页参数错误
     */
    PAGE_PARAM_ERROR(1009, "分页参数错误"),
    /**
     * 领域对象不能为空
     */
    DOMAIN_OBJECT_NULL(1010, "领域对象不能为空"),
    /**
     * APIID不能为空
     */
    API_ID_EMPTY(1011, "APIID不能为空"),
    /**
     * 上传附件为空
     */
    UPLOAD_FILE_EMPTY(1012, "上传附件为空"),
    /**
     * 排序的序号已超过限制
     */
    RANK_OVER_LIMIT(1013, "排序的序号已超过限制"),

    /**
     * 系统预制数据不可以删除
     */
    SYSTEMDEFAULT_DATA_CANNOT_BE_DELETED(1014, "系统预制数据不可以删除"),
    /**
     * 系统预制数据不可以启用禁用
     */
    SYSTEMDEFAULT_DATA_CANNOT_BE_ENABLED_OR_DISABLED(1015, "系统预制数据不可以启用禁用"),
    SYSTEMDEFAULT_DATA_SETTING_ERROR(1016, "系统预制数据预制错误"),
    /**
     * 系统预制数据不可以修改
     */
    SYSTEMDEFAULT_DATA_CANNOT_BE_MODIFIED(1017, "系统预制数据不可以修改"),
    /**
     * APIID重复
     */
    APIID_UNIQUE_ERROR(1018, "APIID重复"),
    LOGIN_IN_FAIL(1019, "登陆失败"),
    /**
     * {name}创建成功
     */
    CREATE_SUCCESS(1020, "创建成功"),
    /**
     * 创建失败，名称重复
     */
    CREATE_FAIL_NAME_REPEAT(1021, "创建失败，名称重复"),
    /**
     * 创建失败，APIID重复
     */
    CREATE_FAIL_APIID_REPEAT(1022, "创建失败，APIID重复"),
    /**
     * 创建失败，和{name}规则重复
     */
    CREATE_FAIL_RULE_REPEAT(1023, "创建失败，和%s规则重复"),
    /**
     * {name}删除成功
     */
    DELETE_SUCCESS(1024, "删除成功"),
    /**
     * 删除成功，共删除{count}条数据
     */
    DELETE_SUCCESS_COUNT(1025, "删除成功，共删除%s条数据"),
    /**
     * 成功删除{count}条数据，未删除{count}条数据
     */
    DELETE_SUCCESS_COUNT_FAIL_COUNT(1026, "成功删除%s条数据，未删除%s条数据"),
    /**
     * {name}启用成功
     */
    ENABLE_SUCCESS(1027, "启用成功"),
    /**
     * 启用成功，共启用{count}条数据
     */
    ENABLE_SUCCESS_COUNT(1028, "启用成功，共启用%s条数据"),
    /**
     * 成功启用{count}条数据，未启用{count}条数据
     */
    ENABLE_SUCCESS_COUNT_FAIL_COUNT(1029, "成功启用%s条数据，未启用%s条数据"),
    /**
     * {name}禁用成功
     */
    DISABLE_SUCCESS(1030, "禁用成功"),
    /**
     * 禁用成功，共禁用{count}条数据
     */
    DISABLE_SUCCESS_COUNT(1031, "禁用成功，共禁用%s条数据"),
    /**
     * 成功禁用{count}条数据，未禁用{count}条数据
     */
    DISABLE_SUCCESS_COUNT_FAIL_COUNT(1032, "成功禁用%s条数据，未禁用%s条数据"),
    /**
     * {name}替换成功
     */
    REPLACE_SUCCESS(1033, "替换成功"),
    /**
     * 替换成功，共替换{count}条数据
     */
    REPLACE_SUCCESS_COUNT(1034, "替换成功，共替换%s条数据"),
    /**
     * 成功替换{count}条数据，未替换{count}条数据
     */
    REPLACE_SUCCESS_COUNT_FAIL_COUNT(1035, "成功替换%s条数据，未替换%s条数据"),
    /**
     * 导入成功，共导入{count}条数据
     */
    IMPORT_SUCCESS_COUNT(1036, "导入成功，共导入%s条数据"),
    /**
     * 导入成功，共导入{count}条数据，导入失败{count}条数据
     */
    IMPORT_SUCCESS_COUNT_FAIL_COUNT(1037, "导入成功，共导入%s条数据，导入失败%s条数据"),
    /**
     * {name}重置成功
     */
    RESET_SUCCESS(1038, "重置成功"),
    /**
     * 重置成功，共重置{count}条数据
     */
    RESET_SUCCESS_COUNT(1039, "重置成功，共重置%s条数据"),
    /**
     * 重置成功，共重置{count}条数据，重置失败{count}条数据
     */
    RESET_SUCCESS_COUNT_FAIL_COUNT(1040, "重置成功，共重置%s条数据，重置失败%s条数据"),
    /**
     * 删除失败
     */
    DELETE_FAIL(1041, "删除失败"),
    /**
     * 启用失败
     */
    ENABLE_FAIL(1042, "启用失败"),
    /**
     * 禁用失败
     */
    DISABLE_FAIL(1043, "禁用失败"),
    /**
     * 替换失败
     */
    REPLACE_FAIL(1044, "替换失败"),
    /**
     * 重置失败
     */
    RESET_FAIL(1045, "重置失败"),
    /**
     * 调整成功
     */
    ADJUST_SUCCESS(1046, "调整成功"),
    /**
     * 调整成功，共调整%s条数据
     */
    ADJUST_SUCCESS_COUNT(1047, "调整成功，共调整%s条数据"),
    /**
     * 调整成功，共调整%s条数据，调整失败%s条数据
     */
    ADJUST_SUCCESS_COUNT_FAIL_COUNT(1048, "调整成功，共调整%s条数据，调整失败%s条数据"),
    /**
     * 调整失败
     */
    ADJUST_FAIL(1049, "调整失败"),
    /**
     * 添加成功
     */
    ADD_SUCCESS(1050, "添加成功"),
    /**
     * 添加成功，共添加%s条数据
     */
    ADD_SUCCESS_COUNT(1051, "添加成功，共添加%s条数据"),
    /**
     * 添加成功，共添加%s条数据，添加失败%s条数据
     */
    ADD_SUCCESS_COUNT_FAIL_COUNT(1052, "添加成功，共添加%s条数据，添加失败%s条数据"),
    /**
     * 添加失败
     */
    ADD_FAIL(1053, "添加失败"),
    /**
     * 邮箱格式错误
     */
    EMAIL_FORMAT_ERROR(1054, "邮箱格式错误"),
    /**
     * 转入成功
     */
    TRANSFER_SUCCESS(1055, "转入成功"),
    /**
     * 转入成功，共转入%s条数据
     */
    TRANSFER_SUCCESS_COUNT(1056, "转入成功，共转入%s条数据"),
    /**
     * 转入成功，共转入%s条数据，转入失败%s条数据
     */
    TRANSFER_SUCCESS_COUNT_FAIL_COUNT(1057, "转入成功，共转入%s条数据，转入失败%s条数据"),
    /**
     * 转入失败
     */
    TRANSFER_FAIL(1058, "转入失败"),
    /**
     * 转移成功
     */
    MOVE_SUCCESS(1059, "转移成功"),
    /**
     * 转移成功，共转移%s条数据
     */
    MOVE_SUCCESS_COUNT(1060, "转移成功，共转移%s条数据"),
    /**
     * 转移成功，共转移%s条数据，转移失败%s条数据
     */
    MOVE_SUCCESS_COUNT_FAIL_COUNT(1061, "转移成功，共转移%s条数据，转移失败%s条数据"),
    /**
     * 转移失败
     */
    MOVE_FAIL(1062, "转移失败"),
    /**
     * 列表中自定义SQL为空
     */
    CUSTOM_SQL_EMPTY(1063, "列表中自定义SQL为空"),
    /**
     * 只支持SELECT查询语句
     */
    CUSTOM_SQL_TYPE_ERROR(1064, "只支持SELECT查询语句"),
    /**
     * SQL解析出错
     */
    SQL_PARSE_ERROR(1065, "SQL解析出错"),
    /**
     * 创建失败，名称重复
     */
    CREATE_FAIL_NAME_REPEAT_ERROR(1066, "创建失败，名称重复"),
    /**
     * 创建失败，APIID重复
     */
    CREATE_FAIL_APIID_REPEAT_ERROR(1067, "创建失败，APIID重复"),
    /**
     * 更新失败，名称重复
     */
    UPDATE_FAIL_NAME_REPEAT_ERROR(1068, "更新失败，名称重复"),
    /**
     * 更新失败，APIID重复
     */
    UPDATE_FAIL_APIID_REPEAT_ERROR(1069, "更新失败，APIID重复"),

    /**
     * 租户已存在
     */
    TENANT_EXIST(1070, "租户已存在"),
    OPEN_TENANT_ERROR(1071, "开通租户失败"),
    DOMAIN_EXIST(1072, "专属域名不能重复"),
    REPLACE_TENANT_DATA_ERROR(1073, "根据0租户数据重置所有租户预设数据失败"),
    DATE_BETWEEN_PARAM_ERROR(1074, "日期区间参数错误"),
    NOT_ALLOW_NO_PARAM_CONSTRUCTOR(1075, "不允许无参构造函数"),
    UPDATE_ERROR_WHEN_DELETE(1076, "更新失败,数据已被删除"),

    OPERATE_ERROR_WHEN_DELETE(1077, "操作失败,数据已被删除"),
    UPLOAD_FILE_FAIL(1078, "上传文件失败"),
    MPP_FILE_READER_ERROR(1079, "MPP文件读取异常"),
    FILE_NOT_EXIST(1080, "文件不存在"),
    DOWNLOAD_FILE_FAIL(1081, "下载文件失败"),
    INVALID_URL(1082, "无效的URL"),
    API_ID_IS_NULL(1083, "apiId为空"),
    CLASS_NOT_EXIST(1084, "类不存在"),
    //参数校验失败
    PARAM_CHECK_ERROR_MSG(1085, "参数错误：%s"),
    SESSION_NOT_FOUND(1086, "会话异常"),
    API_ID_DUPLICATE(1087, "APIID重复"),
    NAME_DUPLICATE(1088, "名称重复"),
    LOGIN_IN_MAX_ONLINE_USER(1089, "到达最大用户数量限制"),
    BUILD_IN_DATA_CANNOT_DELETE(1090, "系统预设权限不可删除"),
    NOT_AUTH_TO_OPERATION(1091, "无权限操作"),

    DISABLE_ERROR(1092, "%s禁用失败！"),
    //(1093, "保存失败，%s已被禁用"),
    OBJ_DISABLED(1094, "保存失败，%s已被禁用"),
    OBJ_ALREADY_DELETED(1095, "保存失败，%s已被删除"),
    OBJ_NOT_EXIST(1095, "保存失败，%s已被删除"),
    FAVORITE_MAX_LIMIT_1000(1096, "最多收藏1000条数据"),


    // 定时器调度失败
    SCHEDULED_TASK_ERROR(1096, "调度定时任务失败"),
    SCHEDULED_TASK_JOB_CLASS_NOT_FOUND(1097, "调度任务类不存在"),

    LIFE_CYCLE_NOT_EXIST(1098, "生命周期不存在"),
    UTIL_EXECUTE_SCRIPT_ERROR(1099, "脚本执行出错"),

    ;
    /**
     * 响应码
     */
    private final int code;
    /**
     * 描述
     */
    private final String desc;

    GlobalResultCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
