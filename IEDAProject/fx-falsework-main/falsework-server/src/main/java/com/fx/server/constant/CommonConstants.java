package com.fx.server.constant;

/**
 * @Description:
 * @param:
 * @return:
 * @auther: liwen
 * @date: 2020/8/2 10:55 上午
 */
public class CommonConstants {
    public final static String RESOURCE_TYPE_MENU = "menu";
    public final static String RESOURCE_TYPE_BTN = "button";
    // 用户token异常
    public static final Integer EX_USER_INVALID_CODE = 40101;
    public static final Integer EX_USER_PASS_INVALID_CODE = 40001;
    // 客户端token异常
    public static final Integer EX_CLIENT_INVALID_CODE = 40301;
    public static final Integer EX_CLIENT_FORBIDDEN_CODE = 40331;
    public static final Integer EX_OTHER_CODE = 500;
    public static final String CONTEXT_KEY_USER_ID = "currentUserId";
    public static final String CONTEXT_KEY_USERNAME = "currentUserName";
    public static final String CONTEXT_KEY_USER_NAME = "currentUser";
    public static final String CONTEXT_KEY_USER_TOKEN = "currentUserToken";
    public static final String JWT_KEY_USER_ID = "userId";
    public static final String JWT_KEY_NAME = "name";
    public static final String FAIL = "1";
    public static final String SUCCESS = "0";
    /**
     * 执行目标key
     */
    public static final String TASK_PROPERTIES = "TASK_PROPERTIES";
    /**
     * 执行目标参数
     */
    public static final String TASK_PARAMETERS = "TASK_PARAMETERS";
    public static final String WEB_SOCKET_USER_LIST = "web_socket_user_list";
    public static final String WEB_SOCKET_USER_ID = "web_socket_user_id";
    public static final String WEB_SOCKET_TOKEN = "WEB_SOCKET_TOKEN";


}
