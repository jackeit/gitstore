package com.fx.server.rest;


import com.fx.server.entity.log.GateLog;
import com.fx.server.msg.ObjectRestResponse;
import com.fx.server.msg.TableResultResponse;
import com.fx.server.service.log.GateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @Description:
 * @param:
 * @return:
 * @auther: liwen
 * @date: 2020/9/11 9:28 上午
 */
@RestController
@RequestMapping("log")
public class LogController {
    @Autowired
    private GateLogService gateLogService;


    @GetMapping(value = "/page")
    @ResponseBody
    public TableResultResponse<GateLog> getPageList(@RequestParam Map<String, Object> params) {

        return gateLogService.getPageList(params);
    }


    @DeleteMapping("/{id}")
    @ResponseBody
    public ObjectRestResponse<Integer> add(@PathVariable Integer id) {

        return gateLogService.remove(id);
    }


}
