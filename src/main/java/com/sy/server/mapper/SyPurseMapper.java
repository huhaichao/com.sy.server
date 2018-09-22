package com.sy.server.mapper;

import com.sy.common.bean.SyPurse;
import com.sy.common.bean.SyPurseExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface SyPurseMapper {
    long countByExample(SyPurseExample example);

    int deleteByExample(SyPurseExample example);

    int deleteByPrimaryKey(Integer tSyPurseId);

    int insert(SyPurse record);

    int insertSelective(SyPurse record);

    List<SyPurse> selectByExample(SyPurseExample example);
    
    List<SyPurse> selectPayWay(SyPurse record);

    SyPurse selectByPrimaryKey(Integer tSyPurseId);

    int updateByExampleSelective(@Param("record") SyPurse record, @Param("example") SyPurseExample example);

    int updateByExample(@Param("record") SyPurse record, @Param("example") SyPurseExample example);

    int updateByPrimaryKeySelective(SyPurse record);
    
    int updateByTsypsorderno(SyPurse record);

    int updateByPrimaryKey(SyPurse record);
}