package com.eggseller.test.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ResponseSchema {
	private int status;
	private String message;
	private Timestamp timestamp;
	private Object payload;
}
