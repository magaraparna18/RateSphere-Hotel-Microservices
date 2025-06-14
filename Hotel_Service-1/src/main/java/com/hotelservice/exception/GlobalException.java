package com.hotelservice.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@SuppressWarnings("unused")
@RestControllerAdvice
public class GlobalException {
	
	@SuppressWarnings("unchecked")
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, Object>> notFoundHandler(ResourceNotFoundException ex){
		@SuppressWarnings("rawtypes")
		Map map=new HashMap<>();
		map.put("message", ex.getMessage());
		map.put("success", false);
		map.put("status",HttpStatus.NOT_FOUND);
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
	}

}
