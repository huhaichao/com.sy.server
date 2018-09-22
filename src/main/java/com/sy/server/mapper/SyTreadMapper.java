package com.sy.server.mapper;

import com.sy.common.bean.SyTread;
import com.sy.common.bean.SyTreadExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface SyTreadMapper {
    long countByExample(SyTreadExample example);

    int deleteByExample(SyTreadExample example);

    int deleteByPrimaryKey(Integer tSyTreadId);

    int insert(SyTread record);

    int insertSelective(SyTread record);

    List<SyTread> selectByExample(SyTreadExample example);

    SyTread selectByPrimaryKey(Integer tSyTreadId);

    int updateByExampleSelective(@Param("record") SyTread record, @Param("example") SyTreadExample example);

    int updateByExample(@Param("record") SyTread record, @Param("example") SyTreadExample example);

    int updateByPrimaryKeySelective(SyTread record);

    int updateByPrimaryKey(SyTread record);
}