package com.fx.server.service.log;

import com.fx.server.entity.log.SysLoginInfor;
import com.fx.server.mapper.SysLoginInforMapper;
import com.fx.server.msg.TableResultResponse;
import com.fx.server.util.Query;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @className: SysJobLogServer
 * @author: liwen
 * @date: 2020/12/28 16:37
 */
@Service
public class SysLoginInfoService {
    @Autowired
    private SysLoginInforMapper sysLoginInforMapper;

    public int addSysJobLog(SysLoginInfor sysLoginInfor) {
        return sysLoginInforMapper.insertSelective(sysLoginInfor);
    }

    public TableResultResponse<SysLoginInfor> getPageList(Map<String, Object> params) {

        Query query = new Query(params);
        Page<Object> page = PageHelper.startPage(query.getPage(), query.getLimit());
        List<SysLoginInfor> list = sysLoginInforMapper.selectPage(params);
        int total = (int) Math.ceil(page.getTotal() / (float) query.getLimit());
        return new TableResultResponse<SysLoginInfor>(total == 0 ? 1 : total, list);
    }
}
