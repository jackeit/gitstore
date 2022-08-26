package com.fx.client.request.feign.admin;

import com.fx.client.request.feign.FeignAPI;
import com.fx.server.entity.SysJob;
import com.fx.server.entity.log.SysJobLog;
import com.fx.server.msg.TableResultResponse;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @className: SysJobController
 * @author: liwen
 * @date: 2020/12/25 09:23
 */
public interface SysJobFeign extends FeignAPI {

    @RequestLine("GET /job/list")
    List<SysJob> getSysJobAll(@QueryMap Map<String,String> map);
    @RequestLine("GET /job/log/list")
    TableResultResponse<SysJobLog> getSysJobLogList(@QueryMap Map<String,Object> map);

    @RequestLine("POST /job")
    Integer addJob(SysJob sysJob);

    @RequestLine("PUT /job")
    Integer updateJob(SysJob sysJob);

    @RequestLine("DELETE /job/{jobName}/{jobGroupName}")
    Integer deleteJob(@Param("jobName") String jobName,@Param("jobGroupName")  String jobGroupName);

    @RequestLine("PUT /job/pause/{jobName}/{jobGroupName}")
    Integer pauseJob(@Param("jobName") String jobName,@Param("jobGroupName")  String jobGroupName);

    @RequestLine("PUT /job/resume/{jobName}/{jobGroupName}")
    Integer resumeJob(@Param("jobName") String jobName,@Param("jobGroupName")  String jobGroupName);

    @RequestLine("PUT /job/run/{jobName}/{jobGroupName}")
    Integer runJob(@Param("jobName") String jobName,@Param("jobGroupName")  String jobGroupName);
}
