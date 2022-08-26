package com.fx.server.entity.log;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * sys_logininfor
 * @author 
 */
@Data
public class SysLoginInfor implements Serializable {
    /**
     * 访问ID
     */
    private Long infoId;

    /**
     * 登录账号
     */
    private String loginName;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 登录状态（0成功 1失败）
     */
    private String status;

    /**
     * 提示消息
     */
    private String msg;

    /**
     * 访问时间
     */
    private Date loginTime;

    private static final long serialVersionUID = 1L;
}