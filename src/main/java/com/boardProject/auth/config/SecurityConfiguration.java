package com.boardProject.auth.config;

import com.boardProject.auth.handler.*;
import com.boardProject.auth.jwt.JwtTokenizer;
import com.boardProject.auth.jwt.filter.JwtUsernamePasswordAuthenticationFilter;
import com.boardProject.auth.jwt.filter.JwtVerificationFilter;
import com.boardProject.auth.utils.CustomAuthorityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfiguration {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;
    private final MemberAuthenticationSuccessHandler memberAuthenticationSuccessHandler;

    public SecurityConfiguration(JwtTokenizer jwtTokenizer, CustomAuthorityUtils authorityUtils, MemberAuthenticationSuccessHandler memberAuthenticationSuccessHandler) {
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
        this.memberAuthenticationSuccessHandler = memberAuthenticationSuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable()
                .cors(Customizer.withDefaults())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new MemberAuthenticationEntryPoint())
                .accessDeniedHandler(new MemberAccessDeniedHandler())
                .and()
                .apply(new CustomFilterConfigurer())
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers(HttpMethod.POST, "/*/members").permitAll()
                        .antMatchers(HttpMethod.PATCH, "/*/members/**").hasRole("MEMBER")
                        .antMatchers(HttpMethod.GET, "/*/members").hasRole("ADMIN")
                        .antMatchers(HttpMethod.GET, "/*/members/**").hasAnyRole("MEMBER", "ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/*/members/**").hasRole("MEMBER")

                        .antMatchers(HttpMethod.POST, "/*/posts").hasAnyRole("MEMBER", "ADMIN")
                        .antMatchers(HttpMethod.PATCH, "/*/posts/**").hasAnyRole("MEMBER", "ADMIN")
                        .antMatchers(HttpMethod.GET, "/*/posts").permitAll()
                        .antMatchers(HttpMethod.GET, "/*/posts/**").permitAll()
                        .antMatchers(HttpMethod.DELETE, "/*/posts/**").hasAnyRole("MEMBER", "ADMIN")

                        .antMatchers(HttpMethod.POST, "/*/like/**").hasAnyRole("MEMBER", "ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/*/like/**").hasAnyRole("MEMBER", "ADMIN")

                        .antMatchers(HttpMethod.POST, "/*/comment").hasAnyRole("MEMBER", "ADMIN")
                        .antMatchers(HttpMethod.PATCH, "/*/comment/**").hasAnyRole("MEMBER", "ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/*/comment/**").hasAnyRole("MEMBER", "ADMIN")

                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth2-> oauth2
                        .successHandler(memberAuthenticationSuccessHandler)
                );


        return http.build();
    }

    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

            JwtUsernamePasswordAuthenticationFilter jwtAuthenticationFilter = new JwtUsernamePasswordAuthenticationFilter(authenticationManager, jwtTokenizer);
            jwtAuthenticationFilter.setFilterProcessesUrl("/v1/auth/login");
            jwtAuthenticationFilter.setAuthenticationSuccessHandler(memberAuthenticationSuccessHandler);
            jwtAuthenticationFilter.setAuthenticationFailureHandler(new MemberAuthenticationFailureHandler());

            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, authorityUtils);

            builder.addFilter(jwtAuthenticationFilter)
                    .addFilterAfter(jwtVerificationFilter, JwtUsernamePasswordAuthenticationFilter.class)
                    .addFilterAfter(jwtVerificationFilter, OAuth2LoginAuthenticationFilter.class);


        }
    }
    @Bean
    CorsConfigurationSource corsConfiguration(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("*")); // (8-1)
        corsConfiguration.setAllowedMethods(List.of("GET","POST","PATCH","DELETE"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}