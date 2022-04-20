package com.eggseller.test.repository.test;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.eggseller.test.model.User;

@Mapper
public interface UserMapper {

	@Select("SELECT * FROM users")
    List<User> getUsers();
	
	List<User> getUsersXml();
	
	User getUserByUsername(String username);

	User getUser(String username);
	
	@Insert("INSERT INTO users(username, password) VALUES(#{username}, #{password})")
    @Options(useGeneratedKeys = true, keyProperty = "uid")
    int insertUser(@Param("user") final User user);
	
	int insertUserXml(@Param("user") final User user);
}
