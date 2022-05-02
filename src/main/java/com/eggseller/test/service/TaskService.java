package com.eggseller.test.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eggseller.test.entity.TaskEntity;
import com.eggseller.test.model.Task;
import com.eggseller.test.repository.eggseller.TaskMapper;
import com.eggseller.test.repository.eggseller.TaskRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

	private final TaskRepository taskRepository;
	private final TaskMapper taskMapper;

	
	public Task getTask(String taskName) {
		Task task = taskMapper.selectTask(taskName);
		return task;
	}
	
	public List<TaskEntity> getTaskList() {
		return taskRepository.findAll();
	}
	
	public List<TaskEntity> getTaskList(Pageable pageable) {
		return taskRepository.findByIdGreaterThan(0, pageable);
	}
	
}
