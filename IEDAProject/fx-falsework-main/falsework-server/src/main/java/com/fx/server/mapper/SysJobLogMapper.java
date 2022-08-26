package com.fx.server.mapper;

import com.fx.server.entity.log.SysJobLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysJobLogMapper {
    int deleteByPrimaryKey(Long jobLogId);

    int insert(SysJobLog record);

    int insertSelective(SysJobLog record);

    SysJobLog selectByPrimaryKey(Long jobLogId);

    int updateByPrimaryKeySelective(SysJobLog record);

    int updateByPrimaryKey(SysJobLog record);
    List<SysJobLog> selectPage(@Param("params") Map<String, Object> params);
}