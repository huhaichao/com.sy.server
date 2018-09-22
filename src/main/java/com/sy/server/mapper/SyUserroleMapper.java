package com.sy.server.mapper;

import com.sy.common.bean.SyUserrole;
import com.sy.common.bean.SyUserroleExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SyUserroleMapper {
    long countByExample(SyUserroleExample example);

    int deleteByExample(SyUserroleExample example);

    int deleteByPrimaryKey(String tSyUserroleId);

    int insert(SyUserrole record);

    int insertSelective(SyUserrole record);

    List<SyUserrole> selectByExample(SyUserroleExample example);

    SyUserrole selectByPrimaryKey(String tSyUserroleId);

    int updateByExampleSelective(@Param("record") SyUserrole record, @Param("example") SyUserroleExample example);

    int updateByExample(@Param("record") SyUserrole record, @Param("example") SyUserroleExample example);

    int updateByPrimaryKeySelective(SyUserrole record);

    int updateByPrimaryKey(SyUserrole record);
}