package com.fx.server.entity;

import lombok.Data;
import org.quartz.Job;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @className: SysJob
 * @author: liwen
 * @date: 2020/12/25 09:06
 */
@Data
public class SysJob implements Serializable {

    /**
     * 任务实现类
     */
    private String jobClass;
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
    private String parames;

    private String description;
    private String jobStatus;


    public SysJob(String jobClass, String jobName, String jobGroupName, String jobTime, String parames) {
        this.jobClass = jobClass;
        this.jobName = jobName;
        this.jobGroupName = jobGroupName;
        this.jobTime = jobTime;
        this.parames = parames;
    }

    public SysJob() {

    }

}
