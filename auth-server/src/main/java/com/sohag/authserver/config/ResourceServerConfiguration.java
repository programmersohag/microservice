package com.sohag.authserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
	
	private final TokenStore tokenStore;

	public ResourceServerConfiguration(TokenStore tokenStore) {
		this.tokenStore = tokenStore;
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable().authorizeRequests()
				.antMatchers("/**").hasAnyRole("USER", "ADMIN", "PASS", "LOCK")
		;
	}
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.tokenStore(tokenStore);
		resources.resourceId("resource-1");
	}
}
