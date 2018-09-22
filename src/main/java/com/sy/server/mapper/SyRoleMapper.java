package com.sy.server.mapper;
import com.sy.common.bean.Page;
import com.sy.common.bean.SyRole;
import com.sy.common.bean.SyRoleExample;


import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface SyRoleMapper {
    long countByExample(SyRoleExample example);

    int deleteByExample(SyRoleExample example);

    int deleteByPrimaryKey(String tSyRoleId);
    
    int delRoles(List<String> array);

    int insert(SyRole record);

    int insertSelective(SyRole record);

    List<SyRole> selectByExample(SyRoleExample example);
    
    
    List<SyRole> fallRoles(Page page);

    SyRole selectByPrimaryKey(String tSyRoleId);
    
    int getCount();

    int updateByExampleSelective(@Param("record") SyRole record, @Param("example") SyRoleExample example);

    int updateByExample(@Param("record") SyRole record, @Param("example") SyRoleExample example);

    int updateByPrimaryKeySelective(SyRole record);

    int updateByPrimaryKey(SyRole record);
    
    
}