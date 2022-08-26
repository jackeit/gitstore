package com.fx.client.request.feign.admin;

import com.fx.client.request.feign.FeignAPI;
import com.fx.server.entity.log.GateLog;
import com.fx.server.msg.ObjectRestResponse;
import com.fx.server.msg.TableResultResponse;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

import java.util.Map;

/**
 * @description:
 * @className: TestFeign
 * @author: liwen
 * @date: 2020/4/1 17:31
 */
@Headers("Content-Type: application/json")
public interface LogFeign extends FeignAPI {
    @RequestLine("GET /log/page")
    TableResultResponse<GateLog> getPageList(@QueryMap Map<String, Object> map);

    @RequestLine("DELETE /log/{id}")
    ObjectRestResponse<Integer> delete(@Param("id") Integer id);


}
