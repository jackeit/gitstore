package com.fx.server.service.log;

import com.alibaba.druid.util.StringUtils;
import com.fx.server.entity.log.GateLog;
import com.fx.server.entity.log.LogInfo;
import com.fx.server.mapper.GateLogMapper;
import com.fx.server.msg.ObjectRestResponse;
import com.fx.server.msg.TableResultResponse;
import com.fx.server.util.Query;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @className: GateLogService
 * @author: liwen
 * @date: 2020/12/3 10:33
 */
@Service
public class GateLogService {

    @Autowired
    private GateLogMapper gateLogMapper;
    public void saveLog(LogInfo info){
        GateLog log = new GateLog();
        BeanUtils.copyProperties(info,log);
        log.setCrtTime(new Date(info.getCrtTime()));
        gateLogMapper.insertSelective(log);
    }

    public TableResultResponse<GateLog> getPageList(Map<String, Object> params) {

        Query query = new Query(params);
        Page<Object> page = PageHelper.startPage(query.getPage(), query.getLimit());
        String keyId = (String) params.get("keyId");
        List<GateLog> list = gateLogMapper.selectPage(StringUtils.isEmpty(keyId) ? null : keyId);
        int total = (int) Math.ceil(page.getTotal() / (float) query.getLimit());
        return new TableResultResponse<GateLog>(total == 0 ? 1 : total, list);
    }




    public ObjectRestResponse<Integer> remove(Integer id) {
        gateLogMapper.deleteByPrimaryKey(id);
        return new ObjectRestResponse<Integer>().rel(true);
    }
}
