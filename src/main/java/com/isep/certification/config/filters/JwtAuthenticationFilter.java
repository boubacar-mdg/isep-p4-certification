package com.isep.certification.config.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.isep.certification.config.security.SecurityConfiguration;
import com.isep.certification.tokens.repositories.TokenRepository;
import com.isep.certification.tokens.services.JwtService;
import com.isep.certification.users.models.entities.User;
import com.isep.certification.users.models.enums.AccountState;
import com.isep.certification.users.repositories.UserRepository;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final TokenRepository tokenRepository;
  private final UserRepository userRepository;

  private static final AntPathMatcher pathMatcher = new AntPathMatcher();

  public boolean isPathMatching(String servletPath, String[] whiteList) {
    for (String path : whiteList) {
        if (pathMatcher.match(path, servletPath)) {
            return true;
        }
    }
    return false;
}

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {

    String[] whiteList =  SecurityConfiguration.WHITE_LIST_URL;
    String path = request.getServletPath();
    if (request.getServletPath().contains("/api/v1/auth") || isPathMatching(path, whiteList)) {
      filterChain.doFilter(request, response);
      return;
    }
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String phoneNumber;

    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    jwt = authHeader.substring(7);
    phoneNumber = jwtService.extractUsername(jwt);

    if(phoneNumber != null){
      User user = userRepository.findByPhoneNumber(phoneNumber).get();
      if(!user.getAccountState().equals(AccountState.ACTIVE)){
        filterChain.doFilter(request, response);
        return;
      }
    }

    if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(phoneNumber);
      var isTokenValid = tokenRepository.findByToken(jwt)
          .map(t -> !t.isExpired() && !t.isRevoked())
          .orElse(false);
      if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );
        authToken.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    filterChain.doFilter(request, response);
  }
}
