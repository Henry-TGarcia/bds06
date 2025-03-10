package com.devsuperior.movieflix.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	private Environment env;
	
	@Autowired
	private JwtTokenStore tokenStore;
	
	//Rota para logar
	private static final String[] PUBLIC = {"/oauth/token" ,"/h2-console/**"  };
	
	private static final String[] MOVIES = { "/movies/**" };
	
	private static final String[] REVIEW = { "/reviews/**" };
		
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore);
	}
	//Com isso o Resource server vai ser capaz de decodificar o token e analisar
	//se o token é valido.

	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		//Liberar o H2
		if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		
		http.authorizeRequests()
		.antMatchers(PUBLIC).permitAll()
		.antMatchers(HttpMethod.GET, MOVIES).authenticated()
		.antMatchers(HttpMethod.GET, REVIEW).authenticated()
		.antMatchers(HttpMethod.POST, REVIEW).hasAnyRole("MEMBER").anyRequest().authenticated();
		
	}
	//Configurar as rotas.
	//Quem pode acessar o que.

	
}
