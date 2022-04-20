package com.eggseller.test.exception;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;

import javax.security.auth.login.CredentialException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		log.info("##### exceptionClassName: {}", exceptionClassName);
		log.info("##### response: {}", response.getStatus());
		log.info("##### exception: {}", exception);
		
		String message = "서버오류입니다. 잠시후 다시 시도해 보세요.";
		
		if (exception instanceof AuthenticationServiceException) {			
			message = "사용자 아이디 또는 비밀번호가 맞지 않습니다.";
		} else if (exception instanceof UsernameNotFoundException) {
			//It should be not working because of getting null of the granted authorities form User class.
			message = "사용자 아이디를 찾을 수 없습니다.";
		} else if (exception instanceof BadCredentialsException) {
			message = "비밀번호가 맞지 않습니다.";
		} else if (exception instanceof DisabledException) {
			message = "계정이 비활성화 되었습니다.";
		} else if (exception instanceof AccountExpiredException) {
			message = "계정이 만료되었습니다.";
		} else if (exception instanceof LockedException) {
			message = "계정이 사용이 중지되었습니다.";
		} else if (exception instanceof CredentialsExpiredException) {
			message = "비밀번화 사용기간이 만료되었습니다.";
		} 
		
//		response.sendRedirect(forwardUrl);
//		request.setAttribute("error", message);
		request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, message);
		request.getRequestDispatcher(FORWARD_URI).forward(request, response);
	}


}
