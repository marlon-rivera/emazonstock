package com.emazon.stock.configuration.jwt;

import com.emazon.stock.domain.model.RoleEnum;
import com.emazon.stock.utils.Constants;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader(Constants.HEADER_AUTH);

        if (authorizationHeader != null && authorizationHeader.startsWith(Constants.TOKEN_PREFIX)) {
            String token = authorizationHeader.substring(Constants.TOKEN_PREFIX.length());
                Claims claims = jwtService.getClaimsFromToken(token);

                String username = claims.getSubject();
                String roleStr = claims.get(Constants.ROLE, String.class);
                RoleEnum role = RoleEnum.valueOf(roleStr);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username, null, List.of(new SimpleGrantedAuthority(Constants.ROLE_ +  role.name())));

                SecurityContextHolder.getContext().setAuthentication(authentication);

        }
        filterChain.doFilter(request, response);
    }
}
