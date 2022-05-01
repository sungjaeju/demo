package com.eggseller.test.service.authentication;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.jca.context.SpringContextResourceAdapter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import com.eggseller.test.model.User;
import com.eggseller.test.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.spi.service.contexts.ResponseContext;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenProvider implements AuthenticationProvider {
	
	private final AuthenDetailsService authenDetailsService;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	
//	public AuthenProvider() {
//	}
//	public AuthenProvider(AuthenDetailsService authenDetailsService, PasswordEncoder passwordEncoder) {
//		this.authenDetailsService = authenDetailsService;
//		this.passwordEncoder = passwordEncoder;
//	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//		log.info("####### authentication.getDetails: {}", authentication.getDetails().toString());
//		log.info("####### authentication.getName: {}", authentication.getName().toString());
//		log.info("####### authentication.getCredentials: {}", authentication.getCredentials().toString());
		
		//Get method dynamically
//		Method method;
//		try {
//			method = authentication.getClass().getMethod("getDetails");
//			log.info("dynamic: {}", method.toString());
//		} catch (NoSuchMethodException | SecurityException e) {
//			e.printStackTrace();
//		}
		
		log.info("### AuthenProvider.authentication: {}", authentication.toString());
		String username = authentication.getName();
		String password = (String) authentication.getCredentials();
		
		//Fetch user data from database
		User principal = (User) authenDetailsService.loadUserByUsername(username);
		if (null == principal) {
			log.info("###### UsernameNotFoundException");
			throw new UsernameNotFoundException("사용자 발견못함");
//			throw new DisabledException("디세이블됨");
		} else if (!passwordEncoder.matches(password, principal.getPassword())) {
			throw new BadCredentialsException("비밀번호 틀림");	
		}
		
		//Generate JWTToken if the user get login with JWT
//		String jwtId = passwordEncoder.encode(username + System.currentTimeMillis()).substring(1);
		//String jwtId = UUID.randomUUID().toString();
		//String token = jwtUtil.generateToken(jwtId, username);
		//if (StringUtils.isEmpty(token)) throw new AuthenticationServiceException("JWT 토근 생성 실패");
//		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		//request.getSession().setAttribute("jwtId", jwtId);
		//String sessionJwtId = (String) request.getSession().getAttribute("jwtId");
		//log.info("provider token: {} ", token);
		//log.info("provider jwtId: {} ", jwtId);
		//log.info("provider sessionJwtId: {} ", sessionJwtId);
		
		//principal.setJwt(token);
		
		log.info("###### principal: {}", principal.toString());
		log.info("###### principal,getAuthorities: {}", principal.getAuthorities().toString());
		return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
		
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
