package com.fx.server.entity.log;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * sys_job_log
 * @author 
 */
@Data
public class SysJobLog implements Serializable {
    /**
     * 任务日志ID
     */
    private Long jobLogId;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务组名
     */
    private String jobGroup;

    /**
     * 调用目标字符串
     */
    private String invokeTarget;

    /**
     * 日志信息
     */
    private String jobMessage;

    /**
     * 执行状态（0正常 1失败）
     */
    private String status;

    /**
     * 异常信息
     */
    private String exceptionInfo;

    /**
     * 创建时间
     */
    private Date createTime;

    /** 开始时间 */
    private Date startTime;

    /** 结束时间 */
    private Date endTime;

    private static final long serialVersionUID = 1L;
}