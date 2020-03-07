package com.ulfsvel.wallet.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulfsvel.wallet.common.request.AuthenticationRequest;
import com.ulfsvel.wallet.common.response.AuthenticationResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final AuthenticationManager authenticationManager;

    private final JwtConfiguration jwtConfiguration;

    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtConfiguration jwtConfiguration, ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher(jwtConfiguration.getAuthUrl(), "POST"));
        this.authenticationManager = authenticationManager;
        this.jwtConfiguration = jwtConfiguration;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AuthenticationRequest authenticationRequest = objectMapper.readValue(
                request.getInputStream(),
                AuthenticationRequest.class
        );
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(),
                authenticationRequest.getPassword()
        );

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) throws IOException {
        User user = ((User) authentication.getPrincipal());

        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        byte[] signingKey = jwtConfiguration.getSecret().getBytes();

        String token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setHeaderParam(jwtConfiguration.getTypeName(), jwtConfiguration.getTokenType())
                .setIssuer(jwtConfiguration.getIssuer())
                .setAudience(jwtConfiguration.getAudience())
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfiguration.getExpirationTime()))
                .claim(jwtConfiguration.getClaimsName(), roles)
                .compact();

        response.addHeader(jwtConfiguration.getTokenHeader(), jwtConfiguration.getTokenPrefix() + token);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse(
                token,
                jwtConfiguration.getExpirationTime()
        );

        response.getWriter().write(objectMapper.writeValueAsString(authenticationResponse));
        response.getWriter().flush();
        response.getWriter().close();
    }
}