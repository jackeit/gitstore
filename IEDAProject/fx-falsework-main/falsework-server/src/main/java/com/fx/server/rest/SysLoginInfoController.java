package com.fx.server.rest;

import com.fx.server.entity.log.SysLoginInfor;
import com.fx.server.msg.TableResultResponse;
import com.fx.server.service.log.SysLoginInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @description:
 * @className: SysLoginInfoController
 * @author: liwen
 * @date: 2021/1/4 13:21
 */
@RestController()
@RequestMapping("log/login")
public class SysLoginInfoController {

    @Autowired
    private SysLoginInfoService sysLoginInfoService;

    @GetMapping("/list")
    @ResponseBody
    private TableResultResponse<SysLoginInfor> getSysloginInforList(@RequestParam Map<String, Object> params) {
        return sysLoginInfoService.getPageList(params);
    }
}
