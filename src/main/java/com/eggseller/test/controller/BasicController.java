package com.eggseller.test.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eggseller.test.entity.TaskEntity;
import com.eggseller.test.model.Task;
import com.eggseller.test.model.User;
import com.eggseller.test.repository.eggseller.TaskRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class BasicController {
	
	private final TaskRepository taskRepository;
	
	@GetMapping("/getSessionId")
	@ResponseBody
	public String getSessionId(HttpSession session) {
		return session.getId();
	}


	@GetMapping({"", "/"})
//	@ResponseBody
	public String index(@AuthenticationPrincipal User principal) {
		log.info("#### pirncipal: {}", principal);
		log.info("#### getPassword: {}", principal.getPassword());
		return "index";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@ResponseBody
	@PostMapping("/loginResult")
	public ResponseEntity<?>  loginResult(Authentication authentication,
			@AuthenticationPrincipal User principal) {
		log.info("######## Authentication: {} ", authentication);
		log.info("######## principal: {} ", principal);
		String jwt = principal.getJwt();
		log.info("######## jwt: {} ", jwt);
		
		Map<String, String> responseMap = new HashMap<>();
		responseMap.put("jwt", jwt);
		return new ResponseEntity<>(responseMap, HttpStatus.OK);
	}
	

	@GetMapping("/admin")
	@ResponseBody
	public String admin() {
		return "admin";
	}
	
	@GetMapping("/manager")
	@ResponseBody
	public String manager(
		@AuthenticationPrincipal User principal
	){
		log.info("#### BasicController.pirncipal: {}", principal);
		return "manager";
	}
	
	@ResponseBody
	@GetMapping("/tasks")
	public ResponseEntity<?> getTasks(Pageable pageable, TaskEntity taskEntity) {
		log.info("########### tasks.pageable: {} ", pageable);
		log.info("########### tasks.taskEntity: {} ", taskEntity.toString());

		if (null != taskEntity && taskEntity.getTaskName() != null)
			taskRepository.save(taskEntity);
		
//;		List<TaskEntity> taskList = taskRepository.findAll();
		Page<TaskEntity> taskList = taskRepository.findAll(pageable);

		

		return new ResponseEntity<> (taskList, HttpStatus.OK);
	}
	
	@ResponseBody
	@PostMapping("/tasks/save")
	public ResponseEntity<?> saveTasks(TaskEntity taskEntity) {
		log.info("########### tasks.taskEntity: {} ", taskEntity.toString());

		if (null != taskEntity && taskEntity.getTaskName() != null)
			taskEntity = taskRepository.save(taskEntity);
		
		return new ResponseEntity<> (taskEntity, HttpStatus.OK);
	}
	
	//Have some problem
	@ResponseBody
	@PostMapping("/tasks/update")
	public ResponseEntity<?> updateTasks(Task task) {
		log.info("########### tasks.taskEntity: {} ", task.toString());

		TaskEntity entity = new TaskEntity();
		if (null != task && task.getTaskName() != null) {
			entity.setId(task.getId());
			entity.setTaskName(task.getTaskName());
		}
		log.info("########### tasks.entity: {} ", entity.toString());
		List<TaskEntity> taskList = taskRepository.findAll();
		
		return new ResponseEntity<> (taskList, HttpStatus.OK);
	}
}