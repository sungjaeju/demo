package com.eggseller.test.model;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

public class Response {
	public static ResponseEntity<?> build(HttpStatus status, String message, Object payload) {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("status", status);
		body.put("message", message);
		body.put("timestamp", new Timestamp(System.currentTimeMillis()));
        body.put("payload", payload);
        
        return new ResponseEntity<Object>(body, status);
	}
	
	public static ResponseEntity<?> build(HttpStatus status, String message) {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("status", status);
		body.put("message", message);
		body.put("timestamp", new Timestamp(System.currentTimeMillis()));
        body.put("payload", null);
        
        return new ResponseEntity<Object>(body, status);
	}

	public static ResponseEntity<?> ok(Object payload) {
		HttpStatus status = HttpStatus.OK;
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("status", status);
		body.put("message", "success");
		body.put("timestamp", new Timestamp(System.currentTimeMillis()));
        body.put("payload", payload);
        
        return new ResponseEntity<Object>(body, status);
	}

}
