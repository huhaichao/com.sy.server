package com.sy.server.mapper;

import com.sy.common.bean.SyAccountBalance;
import com.sy.common.bean.SyAccountBalanceExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SyAccountBalanceMapper {
    long countByExample(SyAccountBalanceExample example);

    int deleteByExample(SyAccountBalanceExample example);

    int deleteByPrimaryKey(Integer tSyBalanceId);

    int insert(SyAccountBalance record);

    int insertSelective(SyAccountBalance record);

    List<SyAccountBalance> selectByExample(SyAccountBalanceExample example);

    SyAccountBalance selectByPrimaryKey(Integer tSyBalanceId);

    int updateByExampleSelective(@Param("record") SyAccountBalance record, @Param("example") SyAccountBalanceExample example);

    int updateByExample(@Param("record") SyAccountBalance record, @Param("example") SyAccountBalanceExample example);

    int updateByPrimaryKeySelective(SyAccountBalance record);
    
    int updateBySyAccountSelective(SyAccountBalance record);

    int updateByPrimaryKey(SyAccountBalance record);
}