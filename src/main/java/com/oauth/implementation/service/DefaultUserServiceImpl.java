package com.oauth.implementation.service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.oauth.implementation.repositories.RoleRepository;
import com.oauth.implementation.repositories.UserRepository;
import com.oauth.implementation.dto.UserRegisteredDTO;
import com.oauth.implementation.entities.Role;
import com.oauth.implementation.entities.User;


@Service
public class DefaultUserServiceImpl implements DefaultUserService{
   @Autowired
	private UserRepository userRepo;
	
   @Autowired
  	private RoleRepository roleRepo;

   //cifrar las contraseñas de los usuarios antes de guardarlas
   private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
   //@Autowired private BCryptPasswordEncoder passwordEncoder;




	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	
		User user = userRepo.findByEmail(email);
		if(user == null) {
			throw new UsernameNotFoundException("Usuario o contraseña incorrecta..");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRole()));		
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
		return roles.stream()  // Inicia un flujo de los roles del usuario
				.map(role -> new SimpleGrantedAuthority(role.getRole()))  // Convierte cada rol en una autoridad
				.collect(Collectors.toList());  // Recoge las autoridades en una lista
	}


	@Override
	public User save(UserRegisteredDTO userRegisteredDTO) {

		Role role = roleRepo.findByRole("USER");
		if (role == null) {
			throw new IllegalArgumentException("El rol USER no existe");
		}
		User user = new User();
		user.setEmail(userRegisteredDTO.getEmail_id());
		user.setName(userRegisteredDTO.getName());
		user.setPassword(passwordEncoder.encode(userRegisteredDTO.getPassword()));
		user.setRole(role);
		
		return userRepo.save(user);
	}
}
