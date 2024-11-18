package com.oauth.implementation.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.oauth.implementation.dto.UserRegisteredDTO;
import com.oauth.implementation.entities.User;


public interface DefaultUserService extends UserDetailsService{

	User save(UserRegisteredDTO userRegisteredDTO);




	
}
