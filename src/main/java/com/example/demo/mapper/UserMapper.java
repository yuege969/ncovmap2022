package com.example.demo.mapper;

import com.example.demo.domain.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;

@Repository
@Mapper
public interface UserMapper {

    @Insert("insert into user(uname,password) values(#{uname},#{password})")
    int saveUser(@Param("uname") String uname,@Param("password") String password);

    @Select("select * from user where uname=#{uname}")
    User selectUname(@Param("uname") String uname);

    @Select("select * from user where uname=#{uname} and password=#{password}")
    User selectUser(@Param("uname") String uname,@Param("password") String password);

    @Update("update user set password=#{password} where uname=#{uname}")
    int updatePassword(@Param("uname") String uname,@Param("password") String password);

    @Update("update user set uname=#{newUname} where uname=#{uname} and password=#{password}")
    int updateUser(@Param("uname") String uname,@Param("password") String password,@Param("newUname") String newUname);

    @Update("update user set actived=#{actived} where uname=#{uname} and password=#{password}")
    int updateActived(@Param("uname") String uname,@Param("password") String password,@Param("actived") int actived);

    @Update("update user set securityCode=#{securityCode} where uname=#{uname} and password=#{password}")
    int updateSecurity(@Param("uname") String uname,@Param("password") String password,@Param("securityCode") String securityCode);

    @Update("update user set phone=#{phone} where uname=#{uname} and password=#{password}")
    int updatePhone(@Param("uname") String uname,@Param("password") String password,@Param("phone") String phone);
    @Update("update user set mail=#{mail} where uname=#{uname} and password=#{password}")
    int updateMail(@Param("uname") String uname,@Param("password") String password,@Param("mail") String mail);

    @Update("update user set timeOfLastLogin=#{timeOfLastLogin} where uname=#{uname} and password=#{password}")
    int updateTimeOfLastLogin(@Param("uname") String uname,@Param("password") String password,@Param("timeOfLastLogin") Timestamp tate);

    @Update("update user set identity=#{identity} where uname=#{uname} and password = #{password}")
    int updateIdentity(@Param("uname") String uname,@Param("password") String password,@Param("timeOfLastLogin") String identity);
}