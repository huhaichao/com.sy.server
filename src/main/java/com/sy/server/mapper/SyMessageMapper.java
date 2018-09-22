package com.sy.server.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyMessage;
import com.sy.common.busibean.SyMessageExample;

@Mapper
public interface SyMessageMapper {
    long countByExample(SyMessageExample example);

    int deleteByExample(SyMessageExample example);

    int deleteByPrimaryKey(Integer tSyMessageId);

    int insert(SyMessage record);

    int insertSelective(SyMessage record);

    List<SyMessage> selectByExample(SyMessageExample example);

    SyMessage selectByPrimaryKey(Integer tSyMessageId);

    int updateByExampleSelective(@Param("record") SyMessage record, @Param("example") SyMessageExample example);

    int updateByExample(@Param("record") SyMessage record, @Param("example") SyMessageExample example);

    int updateByPrimaryKeySelective(SyMessage record);

    int updateByPrimaryKey(SyMessage record);
    
    List<SyMessage> selectByItem(@Param("record")MapUtil maputil);
    
    SyMessage selectByMesId(@Param("record")MapUtil maputil);
    
    List<HashMap<String,Object>> selectByItems(@Param("record")MapUtil maputil);
    
    int delMessage(@Param("record")MapUtil maputil);
    /**
     * 获取物的数据列表
     * @param maputil
     * @return
     */
    List<Map> selectByItemMap(MapUtil maputil);
    /**
     * 获取人的数据列表
     * @param maputil
     * @return
     */
    List<Map> selectBySyPeopleInformationsMap(MapUtil maputil);
    
    
    /**
     * 获取人物列表
     */
    
    List<Map> selectByMessages(@Param("record")MapUtil maputil);
    /**
     * 获取收藏列表
     * @param maputil
     * @return
     */
    
    List<Map> selectByBrowse(@Param("record")MapUtil maputil);
    
   List<Map> selectByItemMx(@Param("record")MapUtil maputil);
    

   List<Map> selectByPeopleMx(@Param("record")MapUtil maputil);
   
   Map<String,Object> selectByMessageIdFromView(Integer messageId);
   /**
    * 
    * @param maputil
    * @return
    */
   List<SyMessage> getListMessages(MapUtil maputil);

}