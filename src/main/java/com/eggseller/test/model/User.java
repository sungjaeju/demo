package com.eggseller.test.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@ToString(exclude = "password")
@Slf4j
public class User implements UserDetails {

	private int uid;
	private String username;
	private String password;
	private String email;
	private List<GrantedAuthority> roleList;
	private String jwt;
	
//	public User(String username) {
//		this.username = username;
//	}
	
//	public User setAuthorities() {
//		this.roles = null != this.roles ? this.roles : "";
//		roleList = 
//			new ArrayList<>(
//				Arrays.stream(this.roles.replaceAll("[\\s]", "").split(","))
//					.map(SimpleGrantedAuthority::new)
//					.collect(Collectors.toSet())
//			);
//			
//		log.info("###### roleList: {}", roleList);
//		return this;
//	};
	public void setRoleList(String roles) {
		roles = null == roles ? "" : roles;
		this.roleList = 
			new ArrayList<>(
				Arrays.stream(roles.replaceAll("[\\s]", "").split(","))
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toSet())
			);
		
//		this.roleList.forEach(role -> System.out.println("###### role:" + role.getAuthority()));
		log.info("###### user.roleList: {}", this.roleList);
	};
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {	
		return null != this.roleList ? this.roleList : Collections.emptyList();
	}
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
}
