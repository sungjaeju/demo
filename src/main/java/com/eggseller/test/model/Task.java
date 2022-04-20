package com.eggseller.test.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Task {

	private int id;
	private String taskName;
	private String taskContent;
}
