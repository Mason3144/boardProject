package com.codestates.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomAuthorityUtils {
    @Value("${mail.address.admin}")
    private String adminMailAddress;
    private final List<String> ADMIN_ROLES_STRING = List.of("ADMIN","MEMBER");
    private final List<String> MEMBER_ROLES_STRING = List.of("MEMBER");

    public List<GrantedAuthority> createAuthorities(List<String> roles){
        return roles.stream().map(role-> new SimpleGrantedAuthority("ROLE_"+role)).collect(Collectors.toList());
    }
    public List<String> createRoles(String email){
        if(email.equals(adminMailAddress)) return ADMIN_ROLES_STRING;
        return MEMBER_ROLES_STRING;
    }
}
