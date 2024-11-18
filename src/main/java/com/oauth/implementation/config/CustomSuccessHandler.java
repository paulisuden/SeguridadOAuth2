package com.oauth.implementation.config;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.oauth.implementation.repositories.UserRepository;
import com.oauth.implementation.dto.UserRegisteredDTO;
import com.oauth.implementation.service.DefaultUserService;


/*verifica si el usuario autenticado con OAuth2 ya existe en la base de datos
y si no lo registra automáticamente. */

@Component //componente gestionado por Spring
//Spring Security puede detectarlo como un manejador de éxito de autenticación
public class CustomSuccessHandler implements AuthenticationSuccessHandler{

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	DefaultUserService userService;
		
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		//los http contienen info de la solicitud y rta
		//authentication contiene info del usuario autenticado

		String redirectUrl = null;
		//se comprueba si se inicio sesion con google o github
		if(authentication.getPrincipal() instanceof DefaultOAuth2User) { ////si el mail no esta disponible crea uno usando el nombre de usuario
		DefaultOAuth2User  userDetails = (DefaultOAuth2User ) authentication.getPrincipal();
         String username = userDetails.getAttribute("email") !=null?userDetails.getAttribute("email"):userDetails.getAttribute("login")+"@gmail.com" ;
          if(userRepo.findByEmail(username) == null) {
        	  UserRegisteredDTO user = new UserRegisteredDTO(); //crea un nuevo UserRegisteredDTO con los datos básicos del usuario.
        	  user.setEmail_id(username);
        	  user.setName(userDetails.getAttribute("email") !=null?userDetails.getAttribute("email"):userDetails.getAttribute("login"));
			  //contraseña genérica porque la contraseña no es relevante para OAuth2
        	  user.setPassword(("pauli"));
        	  user.setRole("USER");
        	  userService.save(user);
          }
		}
		redirectUrl = "/inicio";
		//redirige al usuario a la url indicada
		new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
	}

}
