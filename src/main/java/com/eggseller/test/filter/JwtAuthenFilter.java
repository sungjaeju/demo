package com.eggseller.test.filter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.eggseller.test.model.User;
import com.eggseller.test.service.authentication.AuthenDetailsService;
import com.eggseller.test.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenFilter<R> extends OncePerRequestFilter {
	
	private final JwtUtil jwtUtil;
	private final AuthenDetailsService authenDetailsService;
	private final static  List<String> EXCLUDE_URL = Collections.unmodifiableList(
					Arrays.asList("/login", "/logout", "/loginAction", "/loginResult", "/login", "/favicon.ico")
				); 
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
//		Enumeration<String> headerNames = request.getHeaderNames();
//	    if (headerNames != null) {
//            while (headerNames.hasMoreElements()) {
//            	String name = headerNames.nextElement();
//                log.info("header: {}: {} " , name, request.getHeader(name));
//            }
//	    }

	    //If a client has jwt and the Spring security has not it's authentication.
		//setJwtSecurityAuthentication(request);
		
		log.info("##### filter.response.status: {}", response.getStatus());
		
		filterChain.doFilter(request, response);
	}
	

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//		return super.shouldNotFilter(request);
		log.info("########request.getServletPath: {}", request.getServletPath());
		return EXCLUDE_URL.stream().anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));
	}
	
	private void setJwtSecurityAuthentication(HttpServletRequest request) {
		String certificate = request.getHeader("certificate");
		String token = getJwtToken(certificate);
		log.info("filter jwt token: {}", token);
		if (StringUtils.isEmpty(token)) return;
		
		HttpSession session = request.getSession();
		String jwtId = (String) session.getAttribute("jwtId");
		log.info("filter session jwtId: {}", jwtId);
		if (null == jwtId || !jwtUtil.validateToken(token, jwtId)) return;
		
//		String username = "jsj"; 
		String username = jwtUtil.getSubject(token);
		log.info("filter jwt.username: {}", username);
		
		//Generate and set Security Authentication
		Optional.ofNullable(getPrincipal(session, username)).ifPresent(principal -> {
			Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
			log.info("####### last authentication: {}", SecurityContextHolder.getContext().getAuthentication());
			
			session.setAttribute("principal", principal);
			
			if (isTimeRebuidToken(10, token)) {
				jwtUtil.generateToken(jwtId, username);
			}
			
//			setJwtId("jwtId", value -> {
//				session.setAttribute("jwtId", value);
//				return true;
//			});
		});			
	}
	
	private <T> void setJwtId(String sess, Function<String, T> resolver) {
		resolver.apply(sess);
	}
	
	private boolean isTimeRebuidToken(int limtTime, String token) {
		
		Date issuedDate = jwtUtil.getIssuedDate(token);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String stringIssuedDate = sdf.format(issuedDate);
		//Date parsedIssuedDate = sdf.parse(stringIssuedDate);
		long startTime = issuedDate.getTime();
		long endTime = new Date().getTime();
		long elepsedTime = endTime - startTime;


//		LocalDateTime startDate = issuedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//		long btwTime = ChronoUnit.HOURS.between(startDate, new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		
		log.info("####### issuedDate: {} ", issuedDate);
//		log.info("####### stringIssuedDate: {} ", stringIssuedDate);
//		log.info("####### parsedIssuedDate: {} ", parsedIssuedDate);
		log.info("elepseTime: {}", elepsedTime);

		return elepsedTime > 1000 * 60 * limtTime;
	}
	
	private String getJwtToken(String certificate) {
		return null != certificate && certificate.startsWith("Bearer ") 
				? certificate.replace("Bearer ", "") : null;
	}
	
	private User getPrincipal(HttpSession session, String username) {
//		Authentication authen = SecurityContextHolder.getContext().getAuthentication();
//		User storedUser = null != authen ? (User) authen.getPrincipal() : null;

		User storedUser = (User) session.getAttribute("principal");
		log.info("####### storedUser: {}", storedUser);
		
		return null != storedUser ? storedUser : (User) authenDetailsService.loadUserByUsername(username);
	}

}
