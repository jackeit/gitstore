package com.fx.server.rest;

import com.fx.server.entity.SysJob;
import com.fx.server.entity.log.SysJobLog;
import com.fx.server.msg.TableResultResponse;
import com.fx.server.service.log.SysJobLogService;
import com.fx.server.service.quartz.SysJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @className: QuartzController
 * @author: liwen
 * @date: 2020/12/25 08:53
 */
@RestController
@RequestMapping("job")
public class SysJobController {
    @Autowired
    private SysJobService sysJobService;
    @Autowired
    private SysJobLogService sysJobLogService;

    @GetMapping("/list")
    @ResponseBody
    public List<SysJob> getAllJob(String jobName, String jobGroup, String jobStatus) {
        SysJob queryJob = new SysJob();
        queryJob.setJobName(jobName);
        queryJob.setJobGroupName(jobGroup);
        queryJob.setJobStatus(jobStatus);
        return sysJobService.queryAllJob(jobName, jobGroup, jobStatus);
    }

    @GetMapping("/log/list")
    @ResponseBody
    public TableResultResponse<SysJobLog> getAllJob(@RequestParam Map<String, Object> params) {

        return sysJobLogService.getPageList(params);
    }

    @PostMapping("")
    @ResponseBody
    public Integer addJob(@RequestBody SysJob sysJob) {

        return sysJobService.addJob(sysJob);
    }

    @PutMapping("")
    @ResponseBody
    public Integer updateJob(@RequestBody SysJob sysJob) {

        return sysJobService.updateJob(sysJob);
    }

    @DeleteMapping("/{jobName}/{jobGroupName}")
    @ResponseBody
    public Integer deleteJob(@PathVariable("jobName") String jobName, @PathVariable("jobGroupName") String jobGroupName) {

        return sysJobService.deleteJob(jobName, jobGroupName);
    }

    @PutMapping("pause/{jobName}/{jobGroupName}")
    @ResponseBody
    public Integer pauseJob(@PathVariable("jobName") String jobName, @PathVariable("jobGroupName") String jobGroupName) {

        return sysJobService.pauseJob(jobName, jobGroupName);
    }

    @PutMapping("resume/{jobName}/{jobGroupName}")
    @ResponseBody
    public Integer resumeJob(@PathVariable("jobName") String jobName, @PathVariable("jobGroupName") String jobGroupName) {

        return sysJobService.resumeJob(jobName, jobGroupName);
    }

    @PutMapping("run/{jobName}/{jobGroupName}")
    @ResponseBody
    public Integer runAJobNow(@PathVariable("jobName") String jobName, @PathVariable("jobGroupName") String jobGroupName) {

        return sysJobService.runAJobNow(jobName, jobGroupName);
    }
}
