package com.sy.server.mapper;

import com.sy.common.bean.SyDictionary;
import com.sy.common.bean.SyDictionaryExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface SyDictionaryMapper {
    long countByExample(SyDictionaryExample example);

    int deleteByExample(SyDictionaryExample example);

    int deleteByPrimaryKey(Integer tSyDictionaryId);

    int insert(SyDictionary record);

    int insertSelective(SyDictionary record);

    List<SyDictionary> selectByExample(SyDictionaryExample example);

    SyDictionary selectByPrimaryKey(Integer tSyDictionaryId);

    int updateByExampleSelective(@Param("record") SyDictionary record, @Param("example") SyDictionaryExample example);

    int updateByExample(@Param("record") SyDictionary record, @Param("example") SyDictionaryExample example);

    int updateByPrimaryKeySelective(SyDictionary record);

    int updateByPrimaryKey(SyDictionary record);
}