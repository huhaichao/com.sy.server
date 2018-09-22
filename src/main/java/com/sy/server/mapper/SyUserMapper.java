package com.sy.server.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyUser;
import com.sy.common.busibean.SyUserExample;


@Mapper
public interface SyUserMapper {
    long countByExample(SyUserExample example);

    int deleteByExample(SyUserExample example);

    int deleteByPrimaryKey(Integer tSyUserId);

    int insert(SyUser record);

    int insertSelective(SyUser record);

    List<SyUser> selectByExample(SyUserExample example);
    
    List<Map<String,Object>> selectLostAndPickById(Map<String,Integer> record);

    SyUser selectByPrimaryKey(Integer tSyUserId);

    int updateByExampleSelective(@Param("record") SyUser record, @Param("example") SyUserExample example);

    int updateByExample(@Param("record") SyUser record, @Param("example") SyUserExample example);

    int updateByPrimaryKeySelective(SyUser record);

    int updateByPrimaryKey(SyUser record);
    
    List<SyUser> selectByPhone(@Param("record")MapUtil phone);

}