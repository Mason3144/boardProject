package com.boardProject.auth.jwt.filter;

import com.boardProject.auth.jwt.JwtTokenizer;
import com.boardProject.auth.utils.CustomAuthorityUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JwtVerificationFilter extends OncePerRequestFilter {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;

    public JwtVerificationFilter(JwtTokenizer jwtTokenizer, CustomAuthorityUtils authorityUtils) {
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            Map<String, Object> claims = verifyJws(request);
            setAuthenticationToContext(claims);
        } catch (Exception ee) {
            request.setAttribute("exception", ee);
        }

        filterChain.doFilter(request, response);
    }

    // (6)
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String authorization = request.getHeader("Authorization");

        return authorization == null || !authorization.startsWith("Bearer");
    }

    private Map<String, Object> verifyJws(HttpServletRequest request) {
        String jws = request.getHeader("Authorization").replace("Bearer ", "");

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        Map<String, Object> claims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();

        return claims;
    }

    private void setAuthenticationToContext(Map<String, Object> claims) {
        AuthenticatedPrincipal principal =
                AuthenticatedPrincipal.builder()
                        .email((String) claims.get("username"))
                        .memberId((Integer) claims.get("memberId"))
                        .name((String) claims.get("name"))
                        .isLoggedIn(true)
                        .build();

        List<GrantedAuthority> authorities =
                authorityUtils.createAuthorities((List<String>)claims.get("roles"));
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(principal, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    @Getter
    @Builder
    public static class AuthenticatedPrincipal{
        private Integer memberId;
        private String email;
        private String name;
        private boolean isLoggedIn;
        public Map<String,Object> getAuthenticatedPrincipal(){
            Map<String,Object> map = new HashMap<>();
            map.put("memberId", memberId);
            map.put("email", email);
            map.put("name", name);
            map.put("loggedIn", isLoggedIn);
            return map;
        }
    }
}
