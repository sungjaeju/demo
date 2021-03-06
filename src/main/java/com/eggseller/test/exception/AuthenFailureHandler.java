package com.eggseller.test.exception;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.CredentialException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
//public class AuthenFailureHandler extends SimpleUrlAuthenticationFailureHandler {
public class AuthenFailureHandler implements AuthenticationFailureHandler {

	private final String FORWARD_URI = "/login?error";

	/**
	 * @param forwardUrl
	 */
//	public AuthFailureHandler(String forwardUrl) {
//		Assert.isTrue(UrlUtils.isValidRedirectUrl(forwardUrl), () -> "'" + forwardUrl + "' is not a valid forward URL");
//		this.forwardUrl = forwardUrl;
//	}
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
//		Enumeration<String> attrNames = request.getAttributeNames();
//		while (attrNames.hasMoreElements()) {
//			String attrName = attrNames.nextElement();
//			Object attrValue = request.getAttribute(attrName);
//			System.out.println("###### " + attrName + " : " + attrValue);
//		}
//		request.getAttributeNames().asIterator().forEachRemaining(System.out::println);

		String exceptionClassName = exception.getClass().getSimpleName();
		log.info("##### AuthenFailureHandler.exceptionClassName: {}", exceptionClassName);
		log.info("##### AuthenFailureHandler.response: {}", response.getStatus());
		log.info("##### AuthenFailureHandler.exception: {}", exception);
		
		String message = "?????????????????????. ????????? ?????? ????????? ?????????.";
		
		if (exception instanceof AuthenticationServiceException) {			
			message = "????????? ????????? ?????? ??????????????? ?????? ????????????.";
		} else if (exception instanceof UsernameNotFoundException) {
			//It should be not working because of getting null of the granted authorities form User class.
			message = "????????? ???????????? ?????? ??? ????????????.";
		} else if (exception instanceof BadCredentialsException) {
			message = "??????????????? ?????? ????????????.";
		} else if (exception instanceof DisabledException) {
			message = "????????? ???????????? ???????????????.";
		} else if (exception instanceof AccountExpiredException) {
			message = "????????? ?????????????????????.";
		} else if (exception instanceof LockedException) {
			message = "????????? ????????? ?????????????????????.";
		} else if (exception instanceof CredentialsExpiredException) {
			message = "???????????? ??????????????? ?????????????????????.";
		} 
		
		
		
//		response.sendRedirect(forwardUrl);
//		request.setAttribute("error", message);
//		request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, message);
//		request.getRequestDispatcher(FORWARD_URI).forward(request, response);
		
		//response.setContentType("application/json");
		//response.setStatus(HttpStatus.UNAUTHORIZED.value());
		//response.sendError(HttpStatus.UNAUTHORIZED.value());
//		try {
//			response.getWriter().println(new JSONObject().put("exception", "not allow login"));
//		} catch (IOException | JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		Map<String, Object> body = new HashMap<>();
        body.put("status", response.getStatus());
        body.put("message", message);
        
		response.setContentType("application/json;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().println(body);
		return;
	}


}
