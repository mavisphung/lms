package com.lmsapp.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.lmsapp.project.handler.CustomAuthSuccessHandler;
import com.lmsapp.project.user.service.CustomUserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	// config cho đăng nhập user từ database

	// Service giải quyết vụ loadByUsername
	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsServiceImpl();
	}

	// Sử dụng loại password encoder nào
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Provider cung cấp cho authentication
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new CustomAuthSuccessHandler();
	}
	
	// -------------------------------------------------------------
	// Config cho trang
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.authorizeRequests()
					.antMatchers("/css/**", "/js/**", "/fonts/**", 
								 "/images/**", "/register/**").permitAll()
					//.antMatchers("/resources/**", "/register/**").permitAll()
					.antMatchers("/instructor/**").hasRole("INSTUCTOR")
					.antMatchers("/admin/**","/instructor/**").hasRole("ADMIN")
					.anyRequest().authenticated()
				.and()
				.formLogin()
				// Nếu có lỗi thì xoá những dòng dưới
					.loginPage("/login")
					.loginProcessingUrl("/process-login")
//					.defaultSuccessUrl("/")
					.successHandler(authenticationSuccessHandler())
					.failureUrl("/login?error")
					.usernameParameter("username")
					.passwordParameter("password").permitAll()
				.and()
				.logout()
					.logoutUrl("/logout")
					.logoutSuccessUrl("/login?logout")
					.permitAll();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
	}
	
	
	
	
}
