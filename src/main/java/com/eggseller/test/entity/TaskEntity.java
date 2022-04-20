package com.eggseller.test.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;


@Data
@Entity
@ToString
@Table(name = "task_entity")
public class TaskEntity {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int id;
	
	private String taskName;
}
