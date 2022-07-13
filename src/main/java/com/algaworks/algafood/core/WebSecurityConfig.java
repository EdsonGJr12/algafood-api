//package com.algaworks.algafood.core;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http
//			.authorizeRequests()
//			.anyRequest()
//			.authenticated()
//		.and()
//		.oauth2ResourceServer().jwt();
//	}
//	
////	@Bean
////	public JwtDecoder jwtDecoder() {
////		SecretKey secretkey = new SecretKeySpec("ajsdioasufuwefew083724fr2fb42efu2bf2".getBytes(), "HmacSHA256");
////		return NimbusJwtDecoder.withSecretKey(secretkey).build();
////	}
//	
//}
