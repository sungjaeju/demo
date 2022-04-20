package com.eggseller.test.configure;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

import com.eggseller.test.exception.AuthenFailureHandler;
import com.eggseller.test.exception.AuthenSuccessHandler;
import com.eggseller.test.filter.JwtAuthenFilter;
import com.eggseller.test.service.authentication.AuthenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final AuthenProvider authenProvider;
	private final AuthenFailureHandler authenFailureHandler;
	private final AuthenSuccessHandler authenSuccessHandler;
	private final JwtAuthenFilter jwtAuthenFilter;
//	private final UserAuthService userAuthService;
//	private final UserMapper userMapper;

//	@Bean
//	public UserDetailsService userDetailsService() {
//		return new UserDetailsService() {
//			
//			@Override
//			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//				User user = userMapper.getUser(new User(username));
//				log.info("principal: {}", user);
//				if (null == user) throw new UsernameNotFoundException("");
//				return user;
//			}
//		};
//		
//	}
	
//	@Bean
//	protected PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//	@Bean AuthenticationProvider authenticationProvider() {
//		return new MyAuthenticationProvider(userDetailsService(), passwordEncoder());
//	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(userAuthService);
		
		auth.authenticationProvider(authenProvider);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/js/**", "/css/**", "/images/**", "/favicon.ico");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		
		http.authorizeRequests()
			.antMatchers("/admin").hasRole("ADMIN")
			.antMatchers("/manager").hasAnyRole("ADMIN", "MANAGER")
			.antMatchers("/login", "/logout", "/loginAction", "/loginResult", "/anonymous/**", "/guest/**", "/tasks/**").permitAll()
			.anyRequest().authenticated();
		
		http.formLogin()
			.loginPage("/login")
			.loginProcessingUrl("/loginAction")
			//.usernameParameter("userId")
			//.passwordParameter("password")
//		    .defaultSuccessUrl("/manager")
//			.failureUrl("/login?error")
			.successHandler(authenSuccessHandler)
			.failureHandler(authenFailureHandler);
		
		http.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			//.logoutSuccessHandler(null)
			.clearAuthentication(true)
			.invalidateHttpSession(true)
			.deleteCookies("JSESSIONID");
		
//		http.sessionManagement()
//			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.addFilterBefore(jwtAuthenFilter, UsernamePasswordAuthenticationFilter.class);
		
//		http.exceptionHandling()
//			.authenticationEntryPoint(authenticationEntryPoint());
	}
	
	
	
//	@Bean
//	public AuthenticationSuccessHandler authenSuccessHandler() {
//		log.info("######### authenSuccessHandler");
//		return new AuthenticationSuccessHandler() {
//			final String forwardUrl = "/manager";
//
//			@Override
//			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//					Authentication authentication) throws IOException, ServletException {
//				log.info("######### authenSuccessHandler: {}",forwardUrl);
////				request.getRequestDispatcher(forwardUrl).forward(request, response);
//				response.sendRedirect(forwardUrl);
//			}
//		};
//	}

	//For Ajax response
	@Bean
	protected AuthenticationEntryPoint authenticationEntryPoint() {
		return new AuthenticationEntryPoint() {

//			@Override
//			public void commence(HttpServletRequest request, HttpServletResponse response,
//					AuthenticationException authException) throws IOException, ServletException {
//				// TODO Auto-generated method stub
//				
//			}
			
			@Override
			public void commence(
				HttpServletRequest request, 
				HttpServletResponse response,
				AuthenticationException authException
			) throws IOException, ServletException {
				String ajaxHeader = request.getHeader("authorization");
				Enumeration<String> headerNames = request.getHeaderNames();

			    if (headerNames != null) {
			            while (headerNames.hasMoreElements()) {
			                    System.out.println("HeaderNames: " + headerNames.nextElement());
//			                    System.out.println("Header: " + request.getHeader(headerNames.nextElement()));
			            }
			    }

			    log.info("########## SecurityConfig.ajaxHeader: {}", ajaxHeader);
				if(ajaxHeader != null) {
//					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED, "인증안됨");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
				}
//                else
//                    super.commence(request, response, authException);
			}
			
		};		
	}
}
