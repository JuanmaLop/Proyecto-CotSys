package com.udeateampro.security;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import com.udeateampro.entity.Usuario;
import com.udeateampro.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuración de seguridad para la aplicación.
 * Define la política de autenticación, filtros y codificador de contraseñas.
 */
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	private final UsuarioRepository usuarioRepository;

	/**
	 * Configura la cadena de filtros de seguridad y las reglas de acceso.
	 *
	 * @param http Configuración HTTP de Spring Security
	 * @return SecurityFilterChain configurada
	 * @throws Exception
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						// Rutas públicas - Swagger/OpenAPI
						.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
						.requestMatchers("/swagger-resources/**", "/webjars/**").permitAll()
						// Rutas públicas - Auth
						.requestMatchers("/auth/**").permitAll()
						// Todas las demás rutas requieren autenticación
						.requestMatchers("/api/**").permitAll() // pruebas
						.anyRequest().authenticated());
		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return username -> {
			final Usuario usuario = usuarioRepository.findByEmail(username)
					.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
			// Normalizar rol: eliminar espacios y pasar a mayúsculas
			String normalizedRole = usuario.getRol() != null ? usuario.getRol().trim().toUpperCase() : "";
			// Asegurar prefijo ROLE_ requerido por Spring Security al usar hasRole(...)
			String roleAuthority = normalizedRole.startsWith("ROLE_") ? normalizedRole : "ROLE_" + normalizedRole;
			List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(roleAuthority));
			return org.springframework.security.core.userdetails.User.builder().username(usuario.getEmail())
					.password(usuario.getPassword()).authorities(authorities).build();
		};
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		org.springframework.security.authentication.dao.DaoAuthenticationProvider authProvider = new org.springframework.security.authentication.dao.DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	/**
	 * Bean para obtener el AuthenticationManager de la configuración.
	 *
	 * @param authConfig Configuración de autenticación
	 * @return AuthenticationManager
	 * @throws Exception
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	/**
	 * Bean para codificar contraseñas usando BCrypt.
	 * 
	 * @return PasswordEncoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}