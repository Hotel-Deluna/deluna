package com.hotel.config;

import com.hotel.jwt.JwtAuthenticationEntryPoint;
import com.hotel.jwt.JwtAuthenticationFilter;
import com.hotel.jwt.JwtTokenProvider;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("deprecation")
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtTokenProvider tokenProvider;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/static/css/, /static/js/, *.ico");

		web.httpFirewall(defaultHttpFirewall());

		// swagger
		web.ignoring()
				.antMatchers("/v3/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security",
				"/swagger-ui.html", "/webjars/", "/swagger/");
	}

	@Bean
	public HttpFirewall defaultHttpFirewall() {
		return new DefaultHttpFirewall();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 시큐리티는 기본적으로 세션을 사용 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http
				.cors().configurationSource(corsConfigurationSource()) // CORS 설정 적용
				.and()
					.httpBasic().disable() // httpBasic disable
					.csrf().disable() // CSRF Disable
				.exceptionHandling()
				.authenticationEntryPoint(jwtAuthenticationEntryPoint) // exception Handling
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
					.and()
					.authorizeRequests()
					.antMatchers("/owner/**").permitAll()
					.antMatchers("/hotel/**").permitAll()
					.antMatchers("/reservation/**").permitAll()
					.antMatchers("/common/**").permitAll()
					.antMatchers("/swagger-resources/**").permitAll()
					.antMatchers("/member/sign-up", "/member/sign-in").permitAll()
					.antMatchers("/owner/sign-up").permitAll()
				// JwtSecurityConfig 클래스에서 선언된 필터 적용
				.and()
					.apply(new JwtSecurityConfig(tokenProvider))
				.and()
					.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

	}

	// CORS 설정
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOriginPattern(CorsConfiguration.ALL);
		configuration.setAllowedMethods(List.of(CorsConfiguration.ALL));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "content-type", "x-auth-token"));

		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration.applyPermitDefaultValues()); // 전역적용. 나중에 수정필요
		return source;
	}
	
}
