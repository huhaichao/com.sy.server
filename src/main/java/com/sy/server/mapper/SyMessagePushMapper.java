package com.sy.server.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyMessagePush;
import com.sy.common.busibean.SyMessagePushExample;

@Mapper
public interface SyMessagePushMapper {
    long countByExample(SyMessagePushExample example);

    int deleteByExample(SyMessagePushExample example);

    int deleteByPrimaryKey(Integer tSyMessagePushId);

    int insert(SyMessagePush record);

    int insertSelective(SyMessagePush record);

    List<SyMessagePush> selectByExample(SyMessagePushExample example);

    SyMessagePush selectByPrimaryKey(Integer tSyMessagePushId);

    int updateByExampleSelective(@Param("record") SyMessagePush record, @Param("example") SyMessagePushExample example);

    int updateByExample(@Param("record") SyMessagePush record, @Param("example") SyMessagePushExample example);

    int updateByPrimaryKeySelective(SyMessagePush record);

    int updateByPrimaryKey(SyMessagePush record);
    
    List<SyMessagePush> selectByItem(MapUtil maputil);
    
    int deleteByItem(MapUtil maputil);
    
}