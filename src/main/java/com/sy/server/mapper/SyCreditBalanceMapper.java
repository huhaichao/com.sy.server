package com.sy.server.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sy.common.busibean.SyCreditBalance;
import com.sy.common.busibean.SyCreditBalanceExample;
@Mapper
public interface SyCreditBalanceMapper {
    long countByExample(SyCreditBalanceExample example);

    int deleteByExample(SyCreditBalanceExample example);

    int deleteByPrimaryKey(Integer tSyCreditBalanceId);

    int insert(SyCreditBalance record);

    int insertSelective(SyCreditBalance record);

    List<SyCreditBalance> selectByExample(SyCreditBalanceExample example);

    SyCreditBalance selectByPrimaryKey(Integer tSyCreditBalanceId);

    int updateByExampleSelective(@Param("record") SyCreditBalance record, @Param("example") SyCreditBalanceExample example);

    int updateByExample(@Param("record") SyCreditBalance record, @Param("example") SyCreditBalanceExample example);

    int updateByPrimaryKeySelective(SyCreditBalance record);

    int updateByPrimaryKey(SyCreditBalance record);
}