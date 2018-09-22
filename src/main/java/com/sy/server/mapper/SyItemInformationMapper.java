package com.sy.server.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyItemInformation;
import com.sy.common.busibean.SyItemInformationExample;


@Mapper
public interface SyItemInformationMapper {
    long countByExample(SyItemInformationExample example);

    int deleteByExample(SyItemInformationExample example);

    int deleteByPrimaryKey(Integer tSyItemInformationId);

    int insert(SyItemInformation record);

    int insertSelective(SyItemInformation record);

    List<SyItemInformation> selectByExample(SyItemInformationExample example);

    SyItemInformation selectByPrimaryKey(Integer tSyItemInformationId);

    int updateByExampleSelective(@Param("record") SyItemInformation record, @Param("example") SyItemInformationExample example);

    int updateByExample(@Param("record") SyItemInformation record, @Param("example") SyItemInformationExample example);

    int updateByPrimaryKeySelective(SyItemInformation record);

    int updateByPrimaryKey(SyItemInformation record);
    
    List<SyItemInformation> selectByItem(@Param("record")MapUtil maputil);
    
    List<SyItemInformation> selectByDetailItem(@Param("record")MapUtil maputil);
    
    SyItemInformation selectByMessageId (@Param("record")MapUtil maputil);
    /**
     * 通过物品主键id和发布类型获取物品详细信息
     * wangliang
     * @param maputil
     * @return  
     */
    SyItemInformation selectByIdAndType (@Param("record")MapUtil maputil);
    /**
     * 返回Map数据信息
     * @param maputil
     * @return
     */
    public List<Map> selectByItemMap(@Param("record")MapUtil maputil);
    
    /**
     * 删除发布信息接口
     */
    
    public int delSyItemInfomation(MapUtil maputil);

    
}