package com.eggseller.test.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class TaskReq {

	@NotBlank(message = "Not blank")
	@Min(value = 3)
	private String taskName;
	
	private int id;
}
