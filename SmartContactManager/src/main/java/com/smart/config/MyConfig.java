package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MyConfig {

	@Bean
	public UserDetailsService userDetailsService() {
		
		return new UserDetailsServiceImple();
	}
//	
//	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	
	
	
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		
		DaoAuthenticationProvider daoAuthenticationProvider= new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
	
		return daoAuthenticationProvider;
	}
	
	
	
//	//Configure Method
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
//		
//		auth.authenticationProvider(authenticationProvider());
//		
//	}
	
//	protected void configure(HttpSecurity http) throws Exception{
//		
//		http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN")
//		.antMatchers("/user/**").hasRole("USER")
//		.antMatchers("/**").permitAll().and().formLogin().and().csrf().disable();
//		
//	}
	
	
	
//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

	
//		http.csrf().disable().authorizeHttpRequests()
//		.requestMatchers("/user/**").authenticated().and().authorizeHttpRequests()
//		.requestMatchers("/**").permitAll().and().formLogin();
//		
//		
//		return http.build();
//		
//	}
	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		 http.csrf().disable().authorizeHttpRequests().requestMatchers("/admin/**")
				.hasRole("ADMIN").requestMatchers("/user/**").hasRole("USER")
				.requestMatchers("/**").permitAll().anyRequest().authenticated()
				.and().formLogin().loginPage("/signin").loginProcessingUrl("/dologin")
				.defaultSuccessUrl("/user/index");
		
		 return http.build();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
