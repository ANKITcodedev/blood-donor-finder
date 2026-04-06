package com.ankit.BloodDonorFinder.security;

import com.ankit.BloodDonorFinder.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication
        .UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context
        .SecurityContextHolder;
import org.springframework.security.core.userdetails
        .User;
import org.springframework.security.core.userdetails
        .UserDetails;
import org.springframework.security.web.authentication
        .WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.ArrayList;


@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // get authorisation header
        String authHeader = request.getHeader("Authorization");

        // check header exists and start with bearer
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // extract from header  "bearer uetfdkji..." -> "uetfdkji..."
        String token = authHeader.substring(7);

        // check token is valid
        if(!jwtService.isValidToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // extract email form token
        String email = jwtService.extractEmail(token);

        // check user exists in database
        com.ankit.BloodDonorFinder.model.User user = userRepository.findByEmail(email).orElse(null);

        if(user == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // set authentication in spring security
        UserDetails userDetails = new User(user.getEmail(), user.getPassword(), new ArrayList<>());

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);

        // continue to next filter
        filterChain.doFilter(request,response);
    }

}
