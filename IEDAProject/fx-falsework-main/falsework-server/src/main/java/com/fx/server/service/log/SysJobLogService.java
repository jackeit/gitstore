package com.fx.server.service.log;

import com.fx.server.entity.log.SysJobLog;
import com.fx.server.mapper.SysJobLogMapper;
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
public class SysJobLogService {
    @Autowired
    private SysJobLogMapper sysJobLogMapper;

    public void addSysJobLog(SysJobLog sysJobLog) {
        sysJobLogMapper.insert(sysJobLog);
    }

    public TableResultResponse<SysJobLog> getPageList(Map<String, Object> params) {

        Query query = new Query(params);
        Page<Object> page = PageHelper.startPage(query.getPage(), query.getLimit());
        List<SysJobLog> list = sysJobLogMapper.selectPage(params);
        int total = (int) Math.ceil(page.getTotal() / (float) query.getLimit());
        return new TableResultResponse<SysJobLog>(total == 0 ? 1 : total, list);
    }
}
