package com.sy.server.mapper;

import com.sy.common.bean.SyCashAccount;
import com.sy.common.bean.SyCashAccountExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface SyCashAccountMapper {
    long countByExample(SyCashAccountExample example);

    int deleteByExample(SyCashAccountExample example);

    int deleteByPrimaryKey(Integer tSyCashAccountId);

    int insert(SyCashAccount record);

    int insertSelective(SyCashAccount record);

    List<SyCashAccount> selectByExample(SyCashAccountExample example);

    SyCashAccount selectByPrimaryKey(Integer tSyCashAccountId);

    int updateByExampleSelective(@Param("record") SyCashAccount record, @Param("example") SyCashAccountExample example);

    int updateByExample(@Param("record") SyCashAccount record, @Param("example") SyCashAccountExample example);

    int updateByPrimaryKeySelective(SyCashAccount record);

    int updateByPrimaryKey(SyCashAccount record);
}