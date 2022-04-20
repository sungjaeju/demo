	package com.eggseller.test.exception;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
	
		log.info("############ authenSuccessHandler: {}", request.getSession().getAttribute("jwtId"));
		
//		response.sendRedirect("/loginResult");
		if (request.getSession().getAttribute("jwtId") != null)
			request.getRequestDispatcher("/loginResult").forward(request, response);
	}
	
	public ResponseEntity<? extends Map<String,String>> onJwtAuthenticationSuccess(String token) {
		Map<String, String> responseMap = new HashMap<>();
		responseMap.put("token", token);
		return new ResponseEntity<>(responseMap, HttpStatus.OK);
	}

}
