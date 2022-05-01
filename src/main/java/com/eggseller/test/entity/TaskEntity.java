package com.eggseller.test.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.ToString;


@Data
@Entity
@ToString
@Table(name = "tasks")
public class TaskEntity {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int id;
	
	private String taskName;
	
	@Column(name = "created_at")
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	@UpdateTimestamp
	private LocalDateTime updatedAt;
}
