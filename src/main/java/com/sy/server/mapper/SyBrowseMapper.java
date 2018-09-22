package com.sy.server.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sy.common.annotation.MapUtil;
import com.sy.common.bean.SyBrowseExample;
import com.sy.common.busibean.SyBrowse;
@Mapper
public interface SyBrowseMapper {
    long countByExample(SyBrowseExample example);

    int deleteByExample(SyBrowseExample example);

    int deleteByPrimaryKey(Integer tSyBrowseId);

    int insert(SyBrowse record);

    int insertSelective(SyBrowse record);

    List<SyBrowse> selectByExample(SyBrowseExample example);

    SyBrowse selectByPrimaryKey(Integer tSyBrowseId);

    int updateByExampleSelective(@Param("record") SyBrowse record, @Param("example") SyBrowseExample example);

    int updateByExample(@Param("record") SyBrowse record, @Param("example") SyBrowseExample example);

    int updateByPrimaryKeySelective(SyBrowse record);

    int updateByPrimaryKey(SyBrowse record);
    
    int deleteByCollect(SyBrowse record);
}