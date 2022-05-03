package com.eggseller.test.configure;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.eggseller.test.exception.AuthenFailureHandler;
import com.eggseller.test.exception.AuthenSuccessHandler;
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
	//private final JwtAuthenFilter jwtAuthenFilter;
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
			.antMatchers("/", "/login", "/logout", "/loginAction", "/loginResult", "/anonymous/**", "/task/**", "/tasks/**").permitAll()
			.anyRequest().authenticated();
		
		http.formLogin()
			.loginPage("/login")
			.loginProcessingUrl("/loginAction")
			//.usernameParameter("userId")
			//.passwordParameter("password")
		    //.defaultSuccessUrl("/manager")
			//.failureUrl("/loginResult")
			.failureHandler(authenFailureHandler)
			.successHandler(authenSuccessHandler);
		
		http.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			//.logoutSuccessHandler(null)
			.clearAuthentication(true)
			.invalidateHttpSession(true)
			.deleteCookies("JSESSIONID");
		
		http.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
		
		//http.addFilterBefore(jwtAuthenFilter, UsernamePasswordAuthenticationFilter.class);
		
		http.exceptionHandling()
			.accessDeniedHandler(accessDeniedHAndler())
			.authenticationEntryPoint(authenticationEntryPoint());
	}
	
	
	@Bean
	protected AccessDeniedHandler accessDeniedHAndler() {
		return new AccessDeniedHandler() {
			
			@Override
			public void handle(HttpServletRequest request, HttpServletResponse response,
					AccessDeniedException accessDeniedException) throws IOException, ServletException {
				// TODO Auto-generated method stub
				log.error("가입되지 않은 사용자 접근");
				response.setContentType("application/json;charset=utf-8");
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().println(accessDeniedException);
				
			}
		};
		
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
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new AuthenticationEntryPoint() {

			@Override
			public void commence(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException authException) throws IOException, ServletException {
				// TODO Auto-generated method stub
				log.info("########### securityConfig.authenticationEntryPoint");
				response.setContentType("application/json;charset=utf-8");
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().println(authException);
				
			}
			
			
			
//			@Override
//			public void commence(
//				HttpServletRequest request, 
//				HttpServletResponse response,
//				AuthenticationException authException
//			) throws IOException, ServletException {
//				String ajaxHeader = request.getHeader("authorization");
//				Enumeration<String> headerNames = request.getHeaderNames();
//
//			    if (headerNames != null) {
//			            while (headerNames.hasMoreElements()) {
//			                    System.out.println("HeaderNames: " + headerNames.nextElement());
////			                    System.out.println("Header: " + request.getHeader(headerNames.nextElement()));
//			            }
//			    }
//
//			    log.info("########## SecurityConfig.ajaxHeader: {}", ajaxHeader);
//				if(ajaxHeader == null) {
////					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED, "인증안됨");
//                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//				}
//                else
//                    super.commence(request, response, authException);
//			}
			
		};		
	}
	
	
	//CORS
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.addAllowedOrigin("*");
		corsConfig.addAllowedHeader("*");
		//corsConfig.addAllowedMethod("*");
		corsConfig.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT"));
		corsConfig.setAllowCredentials(true);
		corsConfig.setMaxAge(6000L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);
		return source;
	}
	
}
