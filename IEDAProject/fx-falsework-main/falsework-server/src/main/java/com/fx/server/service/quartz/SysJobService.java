package com.fx.server.service.quartz;

import com.fx.server.constant.CommonConstants;
import com.fx.server.entity.SysJob;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class SysJobService {
    @Autowired
    private Scheduler scheduler;

    @PostConstruct
    public void startScheduler() {

        try {
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加一个job
     *
     * @param jobClass     任务实现类
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     * @param jobTime      时间表达式 (这是每隔多少秒为一次任务)
     * @param jobTimes     运行的次数 （<0:表示不限次数）
     * @param jobData      参数
     */
    public void addJob(Class<? extends Job> jobClass, String jobName, String jobGroupName, int jobTime,
                       int jobTimes, Map jobData) {
        try {
            // 任务名称和组构成任务key
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName)
                    .build();
            // 设置job参数
            if (jobData != null && jobData.size() > 0) {
                jobDetail.getJobDataMap().putAll(jobData);
            }
            // 使用simpleTrigger规则
            Trigger trigger = null;
            if (jobTimes < 0) {
                trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroupName)
                        .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(1).withIntervalInSeconds(jobTime))
                        .startNow().build();
            } else {
                trigger = TriggerBuilder
                        .newTrigger().withIdentity(jobName, jobGroupName).withSchedule(SimpleScheduleBuilder
                                .repeatSecondlyForever(1).withIntervalInSeconds(jobTime).withRepeatCount(jobTimes))
                        .startNow().build();
            }
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加一个job
     */
    public Integer addJob(SysJob sysJob) {
        try {
            // 创建jobDetail实例，绑定Job实现类
            // 指明job的名称，所在组的名称，以及绑定job类
            // 任务名称和组构成任务key
            JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) Class.forName(sysJob.getJobClass())).withIdentity(sysJob.getJobName(), sysJob.getJobGroupName())
                    .build();
            jobDetail.getJobDataMap().put(CommonConstants.TASK_PROPERTIES, sysJob);

            // 设置job参数
            if (StringUtils.isNotBlank(sysJob.getParames())) {
                jobDetail.getJobDataMap().put(CommonConstants.TASK_PARAMETERS, sysJob.getParames());
            }
            // 定义调度触发规则
            // 使用cornTrigger规则
            // 触发器key
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(sysJob.getJobName(), sysJob.getJobGroupName()).withDescription(sysJob.getDescription())
                    .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                    .withSchedule(CronScheduleBuilder.cronSchedule(sysJob.getJobTime())).startNow().build();
            // 把作业和触发器注册到任务调度中
            scheduler.scheduleJob(jobDetail, trigger);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 修改 一个job的 时间表达式
     */
    public Integer updateJob(SysJob sysJob) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(sysJob.getJobName(), sysJob.getJobGroupName());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(sysJob.getJobTime())).withDescription(sysJob.getDescription()).build();
            JobKey jobKey = JobKey.jobKey(sysJob.getJobName(), sysJob.getJobGroupName());
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);

            // 设置job参数
            if (StringUtils.isNotBlank(sysJob.getParames())) {
                jobDetail.getJobDataMap().put(CommonConstants.TASK_PARAMETERS, sysJob.getParames());
            }

            // 重启触发器
            scheduler.rescheduleJob(triggerKey, trigger);

            return 0;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 删除任务一个job
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     */
    public Integer deleteJob(String jobName, String jobGroupName) {
        try {
            scheduler.deleteJob(new JobKey(jobName, jobGroupName));
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 暂停一个job
     *
     * @param jobName
     * @param jobGroupName
     */
    public Integer pauseJob(String jobName, String jobGroupName) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
            scheduler.pauseJob(jobKey);
            return 0;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 恢复一个job
     *
     * @param jobName
     * @param jobGroupName
     */
    public Integer resumeJob(String jobName, String jobGroupName) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
            scheduler.resumeJob(jobKey);
            return 0;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 立即执行一个job
     *
     * @param jobName
     * @param jobGroupName
     */
    public Integer runAJobNow(String jobName, String jobGroupName) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
            scheduler.triggerJob(jobKey);
            return 0;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取所有计划中的任务列表
     *
     * @return
     */
    public List<SysJob> queryAllJob(String jobName,String jobGroup,String jobStatus) {
        List<SysJob> jobList = null;
        try {
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            jobList = new ArrayList<>();
            for (JobKey jobKey : jobKeys) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {

                    SysJob sysJob = new SysJob();
                    sysJob.setJobName(jobKey.getName());
                    sysJob.setJobClass(scheduler.getJobDetail(trigger.getJobKey()).getJobClass().getName());
                    sysJob.setJobGroupName(jobKey.getGroup());
                    sysJob.setDescription(trigger.getDescription());
                    sysJob.setParames(scheduler.getJobDetail(trigger.getJobKey()).getJobDataMap().getString(CommonConstants.TASK_PARAMETERS));
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    sysJob.setJobStatus(triggerState.name());
                    if (trigger instanceof CronTrigger) {
                        CronTrigger cronTrigger = (CronTrigger) trigger;
                        String cronExpression = cronTrigger.getCronExpression();
                        sysJob.setJobTime(cronExpression);
                    }

                    boolean a = false;
                    boolean b = false;
                    boolean c = false;
                    if (StringUtils.isBlank(jobName)) {
                        a = true;
                    } else {
                        a = jobKey.getName().toLowerCase().contains(jobName);
                    }
                    if (StringUtils.isBlank(jobGroup)) {
                        b = true;
                    } else {
                        b = jobKey.getGroup().equalsIgnoreCase(jobGroup);
                    }
                    if (StringUtils.isBlank(jobStatus)) {
                        c = true;
                    } else {
                        c = triggerState.name().equalsIgnoreCase(jobStatus);
                    }
                    if (a && b && c) {
                        jobList.add(sysJob);

                    }
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return jobList;
    }

    /**
     * 获取所有正在运行的job
     *
     * @return
     */
    public List<SysJob> queryRunJob() {
        List<SysJob> jobList = null;
        try {
            List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
            jobList = new ArrayList<>(executingJobs.size());
            for (JobExecutionContext executingJob : executingJobs) {
                SysJob sysJob = new SysJob();
                JobDetail jobDetail = executingJob.getJobDetail();
                JobKey jobKey = jobDetail.getKey();
                Trigger trigger = executingJob.getTrigger();
                sysJob.setJobName(jobKey.getName());
                sysJob.setJobGroupName(jobKey.getGroup());
                sysJob.setDescription("触发器:" + trigger.getKey());
                sysJob.setJobClass(scheduler.getJobDetail(trigger.getJobKey()).getJobClass().toString());

                sysJob.setParames(scheduler.getJobDetail(trigger.getJobKey()).getJobDataMap().getString(CommonConstants.TASK_PARAMETERS));

                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                sysJob.setJobStatus(triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    sysJob.setJobTime(cronExpression);
                }
                jobList.add(sysJob);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return jobList;
    }
}
