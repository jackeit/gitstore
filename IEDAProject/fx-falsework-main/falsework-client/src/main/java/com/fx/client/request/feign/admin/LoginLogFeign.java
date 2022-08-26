package com.fx.client.request.feign.admin;

import com.fx.client.request.feign.FeignAPI;
import com.fx.server.entity.log.SysLoginInfor;
import com.fx.server.msg.TableResultResponse;
import feign.Headers;
import feign.QueryMap;
import feign.RequestLine;

import java.util.Map;

/**
 * @description:
 * @className: SysJobController
 * @author: liwen
 * @date: 2020/12/25 09:23
 */
@Headers("Content-Type: application/json")
public interface LoginLogFeign extends FeignAPI {

    @RequestLine("GET /log/login/list")
    TableResultResponse<SysLoginInfor> getSysLoginInfor(@QueryMap Map<String, Object> map);

}
