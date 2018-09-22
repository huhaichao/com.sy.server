package com.sy.server.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sy.common.annotation.MapUtil;
import com.sy.common.bean.SyIntegralExample;
import com.sy.common.busibean.SyIntegral;
@Mapper
public interface SyIntegralMapper {
    long countByExample(SyIntegralExample example);

    int deleteByExample(SyIntegralExample example);

    int deleteByPrimaryKey(Integer tSyIntegralId);

    int insert(SyIntegral record);

    int insertSelective(SyIntegral record);

    List<SyIntegral> selectByExample(SyIntegralExample example);

    SyIntegral selectByPrimaryKey(Integer tSyIntegralId);

    int updateByExampleSelective(@Param("record") SyIntegral record, @Param("example") SyIntegralExample example);

    int updateByExample(@Param("record") SyIntegral record, @Param("example") SyIntegralExample example);

    int updateByPrimaryKeySelective(SyIntegral record);

    int updateByPrimaryKey(SyIntegral record);
    
    List<SyIntegral> selectByItem(@Param("record")MapUtil maputil);

	List<SyIntegral> selectByDate(@Param("record")MapUtil maputil);
}