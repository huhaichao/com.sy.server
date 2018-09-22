package com.sy.server.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyPeopleInformation;
import com.sy.common.busibean.SyPeopleInformationExample;
@Mapper
public interface SyPeopleInformationMapper {
	 long countByExample(SyPeopleInformationExample example);

	    int deleteByExample(SyPeopleInformationExample example);

	    int deleteByPrimaryKey(Integer tSyPeopleInformationId);

	    int insert(SyPeopleInformation record);

	    int insertSelective(SyPeopleInformation record);

	    List<SyPeopleInformation> selectByExample(SyPeopleInformationExample example);

	    SyPeopleInformation selectByPrimaryKey(Integer tSyPeopleInformationId);

	    int updateByExampleSelective(@Param("record") SyPeopleInformation record, @Param("example") SyPeopleInformationExample example);

	    int updateByExample(@Param("record") SyPeopleInformation record, @Param("example") SyPeopleInformationExample example);

	    int updateByPrimaryKeySelective(SyPeopleInformation record);

	    int updateByPrimaryKey(SyPeopleInformation record);
    
	    List<SyPeopleInformation> selectByItem(@Param("record")MapUtil maputil);
	    
	    List<SyPeopleInformation> selectByDetailItem(@Param("record")MapUtil maputil);
	    
	    SyPeopleInformation selectByMessageId(@Param("record")MapUtil maputil);
	    
	    SyPeopleInformation selectByIdAndType(@Param("record")MapUtil maputil);
	    /**
	     * 根据实体返回map
	     * @param maputil
	     * @return
	     */
	    
	    public List<Map> selectBySyPeopleInformationsMap(@Param("record")MapUtil maputil);
    
}