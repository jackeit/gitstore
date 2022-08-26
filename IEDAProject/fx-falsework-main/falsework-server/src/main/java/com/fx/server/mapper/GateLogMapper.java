package com.fx.server.mapper;

import com.fx.server.entity.log.GateLog;

import java.util.List;

public interface GateLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GateLog record);

    int insertSelective(GateLog record);

    GateLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GateLog record);

    int updateByPrimaryKey(GateLog record);
    List<GateLog> selectPage(String keyId);


}