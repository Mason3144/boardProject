package com.boardProject.auth.config;

import com.boardProject.auth.handler.MemberAuthenticationFailureHandler;
import com.boardProject.auth.handler.MemberAuthenticationSuccessHandler;
import com.boardProject.auth.handler.OAuth2MemberSuccessHandler;
import com.boardProject.auth.jwt.JwtTokenizer;
import com.boardProject.auth.jwt.filter.JwtUsernamePasswordAuthenticationFilter;
import com.boardProject.auth.jwt.filter.JwtVerificationFilter;
import com.boardProject.auth.utils.CustomAuthorityUtils;
import com.boardProject.member.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfiguration {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;

    public SecurityConfiguration(JwtTokenizer jwtTokenizer, CustomAuthorityUtils authorityUtils) {
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .headers().frameOptions().sameOrigin() // (1)
                .and()
                .csrf().disable() // (2)
                .cors(Customizer.withDefaults()) // (3)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable() // (4)
                .httpBasic().disable() // (5)
                .exceptionHandling()
//                .authenticationEntryPoint(new MemberAuthenticationEntryPoint())  // (1) 추가
//                .accessDeniedHandler(new MemberAccessDeniedHandler())            // (2) 추가
                .and()
                .apply(new CustomFilterConfigurer())
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers(HttpMethod.POST, "/*/members").permitAll()
                        .antMatchers(HttpMethod.PATCH, "/*/members/**").hasRole("MEMBER")
                        .antMatchers(HttpMethod.GET, "/*/members").hasRole("ADMIN")
                        .antMatchers(HttpMethod.GET, "/*/members/**").hasAnyRole("MEMBER", "ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/*/members/**").hasRole("MEMBER")
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth2-> oauth2
                        .successHandler(new OAuth2MemberSuccessHandler(jwtTokenizer,authorityUtils))
                );


        return http.build();
    }

    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {  // (2-1)
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

            JwtUsernamePasswordAuthenticationFilter jwtAuthenticationFilter = new JwtUsernamePasswordAuthenticationFilter(authenticationManager, jwtTokenizer);
            jwtAuthenticationFilter.setFilterProcessesUrl("/v1/auth/login");
            jwtAuthenticationFilter.setAuthenticationSuccessHandler(new MemberAuthenticationSuccessHandler());  // (3) 추가
            jwtAuthenticationFilter.setAuthenticationFailureHandler(new MemberAuthenticationFailureHandler());  // (4) 추가

            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, authorityUtils);

            builder.addFilter(jwtAuthenticationFilter)
                    .addFilterAfter(jwtVerificationFilter, JwtUsernamePasswordAuthenticationFilter.class)
                    .addFilterAfter(jwtVerificationFilter, OAuth2LoginAuthenticationFilter.class);


        }
    }


    // (7)
    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    // (8)
    @Bean
    CorsConfigurationSource corsConfiguration(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("*")); // (8-1)
        corsConfiguration.setAllowedMethods(List.of("GET","POST","PATCH","DELETE")); // (8-2)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // (8-3)
        source.registerCorsConfiguration("/**", corsConfiguration); // (8-4)

        return source;
    }
}