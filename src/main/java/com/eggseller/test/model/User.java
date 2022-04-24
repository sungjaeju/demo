package com.eggseller.test.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@ToString(exclude = "password")
@Slf4j
@Schema(description = "사용자")
public class User implements UserDetails {

	private int uid;
	
	@Schema(description = "아이디")
	@Pattern(regexp = "[a-z0-9]")
	private String username;
	
	@Schema(description = "비밀번호")
	private String password;

	@Schema(description = "이메일", nullable = false, example = "abc@kbstar.com")
	@Email
	@NotBlank
	private String email;
	
	private List<GrantedAuthority> roleList;
	
	@DateTimeFormat(pattern = "yyyyMMdd hh:mm:ss")
	@Schema(description = "가입일", example = "yyyyMMdd hh:mm:ss", maxLength = 6)
	private String regDate;
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
		this.roleList = new ArrayList<>(
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
