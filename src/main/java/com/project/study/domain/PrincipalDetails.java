package com.project.study.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Data;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User{

	private Users users;
	private Map<String, Object> attributes;
	
//	일반 로그인 할 때 사용하는 생성자 
	public PrincipalDetails(Users users) {
		this.users = users;
	}
	
//	OAuth 로그인 할 때 사용하는 생성자
	public PrincipalDetails(Users users, Map<String, Object> attributes) {
		this.users = users;
		this.attributes = attributes;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return users.getRole();
			}
		});
		return collect;
	}

	@Override
	public String getPassword() {
		return users.getUserpw();
	}

	@Override
	public String getUsername() {
		return users.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return null;
	}

}
