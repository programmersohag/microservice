package com.sohag.authserver.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
	
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;

	private final UserDetailsService userDetailsService;
	private final RedisConnectionFactory redisConnectionFactory;

	public AuthorizationServerConfiguration(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, @Qualifier("applicationUserDetailsService") UserDetailsService userDetailsService, RedisConnectionFactory redisConnectionFactory) {
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
		this.userDetailsService = userDetailsService;
		this.redisConnectionFactory = redisConnectionFactory;
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer configurer) {
		// necessary to allow remote tokens from being checked and validated
		configurer.tokenKeyAccess("isAnonymous() || hasAuthority('ROLE_TRUSTED_CLIENT')").checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')");
	}
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients
				.inMemory()
				.withClient("client")
				.authorizedGrantTypes("password", "refresh_token")
				.authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
				.scopes("read", "write", "trust")
				.secret(passwordEncoder.encode("secret"))
				.resourceIds("resource-1", "resource-2")
//				.accessTokenValiditySeconds(5 * 60) // 5 minute //overridden by default token services
//				.refreshTokenValiditySeconds(10 * 60); // 10 minute //overridden by default token services
		;
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		endpoints
				.tokenServices(tokenServices())
				// necessary to configure the token store, otherwise same server resource server can not access it
//				.tokenStore(tokenStore())
				//.tokenServices(tokenServices()).userApprovalHandler(userApprovalHandler)
				.authenticationManager(authenticationManager)
				// necessary to generate
				.userDetailsService(userDetailsService)
//                .tokenEnhancer(tokenEnhancer())
                .accessTokenConverter(accessTokenConverter())
		;
	}
	

	@Bean
	public TokenStore tokenStore() {
		return new ResetTokenStore(redisConnectionFactory);
	}

	@Bean
//	@Primary
	public AuthorizationServerTokenServices tokenServices() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		defaultTokenServices.setReuseRefreshToken(false);
		defaultTokenServices.setSupportRefreshToken(true);
//		defaultTokenServices.setTokenEnhancer(accessTokenConverter());
//        defaultTokenServices.setTokenEnhancer(tokenEnhancer());
		defaultTokenServices.setAccessTokenValiditySeconds(60 * 60 * 30);
		defaultTokenServices.setRefreshTokenValiditySeconds(60 * 60 * 60);
		return defaultTokenServices;
	}

//    @Bean
//    public TokenEnhancer tokenEnhancer() {
//        return new TokenEnhancer();
//    }

    @Bean
    public DefaultAccessTokenConverter accessTokenConverter() {
        return new DefaultAccessTokenConverter();
    }
}
