package com.fx.server.vo;

import lombok.Data;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.Serializable;
import java.util.Map;

/**
 * @description:
 * @className: SysJob
 * @author: liwen
 * @date: 2020/12/25 09:06
 */
@Data
public class SysJobVO implements Serializable {

    /**
     * 任务实现类
     */
    private Class<? extends QuartzJobBean> jobClass;
    /**
     * 任务名称(建议唯一)
     */
    private String jobName;
    /**
     * 任务组名
     */
    private String jobGroupName;
    /**
     * 时间表达式 （如：0/5 * * * * ? ）
     */
    private String jobTime;
    /**
     * 参数
     */
    private Map jobData;

    private String description;
    private String jobStatus;



    public SysJobVO(Class<? extends QuartzJobBean> jobClass, String jobName, String jobGroupName, String jobTime, Map jobData) {
        this.jobClass = jobClass;
        this.jobName = jobName;
        this.jobGroupName = jobGroupName;
        this.jobTime = jobTime;
        this.jobData = jobData;
    }

    public SysJobVO() {

    }

}
