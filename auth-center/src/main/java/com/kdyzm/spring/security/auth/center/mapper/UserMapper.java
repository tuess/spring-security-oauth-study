package com.kdyzm.spring.security.auth.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kdyzm.spring.security.auth.center.entity.TUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author kdyzm
 */
public interface UserMapper extends BaseMapper<TUser> {

    @Select("SELECT DISTINCT tp.`url` FROM `t_user_role` tur \n" +
            "INNER JOIN `t_role_permission` trp ON tur.`role_id` = trp.`role_id`\n" +
            "INNER JOIN `t_permission` tp ON trp.`permission_id` = tp.`id`\n" +
            "WHERE tur.`user_id` = #{userId};")
    List<String> findAllPermissions(@Param("userId") Integer userId);


    @Select("SELECT NAME \n" +
            "FROM\n" +
            "\tt_role c\n" +
            "\tINNER JOIN ( SELECT a.role_id FROM t_role_permission a INNER JOIN t_permission b ON a.permission_id = b.id AND b.url = #{url} ) d ON c.id = d.role_id")
    List<String> findRoleByUrl(@Param("url") String url);

    @Insert("insert into  `t_role` (id,name) VALUES (#{id},#{data});")
    int insertOne(@Param("id") int id, @Param("data") String data);

    @Insert("insert into  `t_user_role` (id,user_id,role_id) VALUES (#{id},#{userId},#{roleId})")
    int insertException(@Param("id") int id, @Param("userId") int userId, @Param("roleId") int roleId);

}
