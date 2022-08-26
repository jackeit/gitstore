package com.fx.server.mapper;

import com.fx.server.entity.log.SysLoginInfor;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysLoginInforMapper {
    int deleteByPrimaryKey(Long infoId);

    int insert(SysLoginInfor record);

    int insertSelective(SysLoginInfor record);

    SysLoginInfor selectByPrimaryKey(Long infoId);

    int updateByPrimaryKeySelective(SysLoginInfor record);

    int updateByPrimaryKey(SysLoginInfor record);
    List<SysLoginInfor> selectPage(@Param("params")Map<String,Object> params);
}