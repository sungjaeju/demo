package com.eggseller.test.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class RoleInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		System.out.println("##################### Interceptor");
		String param = request.getParameter("param");
		System.out.println("request Header: " + request.getHeader("accept"));
		System.out.println("request.getRequestURL: " + request.getRequestURL());
		System.out.println("request.getRequestURI: " + request.getRequestURI());
		System.out.println("request.getQueryString: " + request.getQueryString());
		//System.out.println(request.getRequestURL().append('?').append(request.getQueryString()));

		String loginId = "eggseller";
		if (StringUtils.isEmpty(loginId)) {
			throw new AccessDeniedException("권한이 없습니다.");
		}

		String uri = request.getRequestURI();
		String last = StringUtils.isNotEmpty(uri) ? uri.substring(uri.lastIndexOf("/")) : "";
		System.out.println("##################### last: " + last);

//		UserRole userRole = new UserRole();
//		userRole.setLoginId(loginId);
//		userRole.setUri(uri);

		//int hasRole = systemService.hasUserRoleOnMenu(userRole);
		int hasRole = 1;
		System.out.println("##################### result: " + hasRole);

		if (1 > hasRole) {
			System.out.println("##################### 권한이 없습니다: ");
			//request.getRequestDispatcher("/error").forward(request, response);
			throw new AccessDeniedException("권한이 없습니다.");
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
	
}
