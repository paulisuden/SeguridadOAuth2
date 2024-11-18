package com.oauth.implementation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.oauth.implementation.service.DefaultUserService;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
	
	@Autowired
	private  DefaultUserService userDetailsService;
	
	@Autowired
	AuthenticationSuccessHandler successHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) //Cross-site request forgery (falsificación de petición en sitios cruzados)

                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers("/registration/**", "/login/**").permitAll();
                    requests.anyRequest().authenticated();})

                .formLogin(login -> login
                        .loginPage("/login")        //indica q la pagina sde inicio sera la q esta en esa ruta (igual en logout)
                        .successHandler(successHandler))

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login"))


                .oauth2Login(login -> login
                        .loginPage("/login")     //permite el uso de oauth2
                        .successHandler(successHandler));

        return http.build();

    }

    /*
    Define un bean de BCryptPasswordEncoder:
    la usamos para cifrar las contraseñas de los usuarios antes de guardarlas en la base de datos
    y también para verificar la contraseña ingresada durante el inicio de sesión. */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService); //cargar los datos del usuario.
        auth.setPasswordEncoder(passwordEncoder()); //verificar que la contraseña ingresada coincida con la que está guardada en la base de datos
        return auth;
    }

    //responsable de gestionar el proceso de autenticación
    @Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
