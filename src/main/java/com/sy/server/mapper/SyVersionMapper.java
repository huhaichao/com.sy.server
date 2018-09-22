package com.sy.server.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sy.common.annotation.MapUtil;
import com.sy.common.bean.VersionPage;
import com.sy.common.busibean.SyVersion;
import com.sy.common.busibean.SyVersionExample;

/**
 * 系统自动生成接口
 * @author zbb
 *
 */
@Mapper
public interface SyVersionMapper {
    long countByExample(SyVersionExample example);

    int deleteByExample(SyVersionExample example);

    int deleteByPrimaryKey(Integer tSyVersionId);

    int insert(SyVersion record);

    int insertSelective(SyVersion record);

    List<SyVersion> selectByExample(SyVersionExample example);
    
    List<SyVersion> fallVersions(VersionPage page); //手工新增获取数据方法
    
    int getCount();  //手工新增获取数据方法
    
    int delVersion(List<String> array); //手工新增批量删除方法

    SyVersion selectByPrimaryKey(Integer tSyVersionId);

    int updateByExampleSelective(@Param("record") SyVersion record, @Param("example") SyVersionExample example);

    int updateByExample(@Param("record") SyVersion record, @Param("example") SyVersionExample example);

    int updateByPrimaryKeySelective(SyVersion record);

    int updateByPrimaryKey(SyVersion record);
    
    List<SyVersion> selectByItem(@Param("record")MapUtil maputil);

}