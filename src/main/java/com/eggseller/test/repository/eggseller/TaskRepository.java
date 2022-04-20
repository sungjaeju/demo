package com.eggseller.test.repository.eggseller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eggseller.test.entity.TaskEntity;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

	public List<TaskEntity> findAll();

	public Page<TaskEntity> findAll(Pageable pageable);
	
	public List<TaskEntity> findByIdGreaterThan(int i, Pageable pageable);
	
	public List<TaskEntity> findByIdLike(String keyword);

	public TaskEntity findById(String id);
	
//	public boolean save(Task);
	
}
