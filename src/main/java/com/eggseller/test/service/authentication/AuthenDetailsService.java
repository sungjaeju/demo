package com.eggseller.test.service.authentication;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.eggseller.test.model.User;
import com.eggseller.test.repository.test.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenDetailsService implements UserDetailsService {
	
	private final UserMapper userMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		UserDetails user = new User(username, new BCryptPasswordEncoder().encode(username), new ArrayList<>(
//				Arrays.asList(
//					new SimpleGrantedAuthority("ROLE_USER")
//					, new SimpleGrantedAuthority("ROLE_MANAGER"))
//				));
//		log.debug("####### user: {}", user.toString());
//		return user;
		

		return userMapper.getUser(username);
	}

	

}
