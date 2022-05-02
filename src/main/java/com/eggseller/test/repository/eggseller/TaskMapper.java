package com.eggseller.test.repository.eggseller;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Qualifier;

import com.eggseller.test.model.Task;
import com.eggseller.test.model.User;

@Mapper
public interface TaskMapper {
	
	Task selectTask(String taskName);

	@Select("SELECT * FROM tasks")
    List<Task> getTasks();
	
	List<Task> getTasksXml();
	
	String getTask();
	
	@Insert("INSERT INTO task(taskName) VALUES('springboot')")
    @Options(useGeneratedKeys = true, keyProperty = "uid")
    int insertUser(@Param("taks") final Task task);
	
	int insertUserXml(@Param("task") final Task task);
}
