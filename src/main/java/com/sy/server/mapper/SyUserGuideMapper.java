package com.sy.server.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyUserGuide;
import com.sy.common.busibean.SyUserGuideExample;
@Mapper
public interface SyUserGuideMapper {
    long countByExample(SyUserGuideExample example);

    int deleteByExample(SyUserGuideExample example);

    int deleteByPrimaryKey(Integer tSyUserguideId);

    int insert(SyUserGuide record);

    int insertSelective(SyUserGuide record);

    List<SyUserGuide> selectByExample(SyUserGuideExample example);

    SyUserGuide selectByPrimaryKey(Integer tSyUserguideId);

    int updateByExampleSelective(@Param("record") SyUserGuide record, @Param("example") SyUserGuideExample example);

    int updateByExample(@Param("record") SyUserGuide record, @Param("example") SyUserGuideExample example);

    int updateByPrimaryKeySelective(SyUserGuide record);

    int updateByPrimaryKey(SyUserGuide record);
    
    List<SyUserGuide> selectByItem(MapUtil maputil);

}