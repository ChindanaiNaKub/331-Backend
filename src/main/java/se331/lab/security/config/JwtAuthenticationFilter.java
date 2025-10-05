package se331.lab.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import se331.lab.security.token.TokenRepository;
import se331.lab.security.token.TokenType;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final TokenRepository tokenRepository;

  @Override
  protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
    String path = request.getRequestURI();
    String method = request.getMethod();

    // Skip auth endpoints and public upload endpoints entirely
    if (path.startsWith("/api/v1/auth/")) {
      return true;
    }
    // Skip upload endpoints entirely - they should be public
    if ((path.equals("/uploadImage") || path.equals("/uploadFile")) &&
        ("POST".equalsIgnoreCase(method) || "GET".equalsIgnoreCase(method))) {
      return true;
    }
    // Always let CORS preflight through
    if ("OPTIONS".equalsIgnoreCase(method)) {
      return true;
    }
    return false;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    // Always let CORS preflight through
    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
      filterChain.doFilter(request, response);
      return;
    }
    // Allow public GET endpoints without requiring JWT auth
    String method = request.getMethod();
    String path = request.getServletPath();
    if ("GET".equalsIgnoreCase(method) && (
        path.startsWith("/events") ||
        path.startsWith("/event") ||
        path.startsWith("/api/v1/events") ||
        path.startsWith("/api/v1/event") ||
        path.startsWith("/organizations") ||
        path.startsWith("/api/v1/organizations") ||
        path.startsWith("/auction-items") ||
        path.startsWith("/api/v1/auction-items") ||
        path.startsWith("/students") ||
        path.startsWith("/api/v1/students") ||
        path.startsWith("/organizers") ||
        path.startsWith("/api/v1/organizers")
    )) {
      if (org.slf4j.LoggerFactory.getLogger(JwtAuthenticationFilter.class).isDebugEnabled()) {
        org.slf4j.LoggerFactory.getLogger(JwtAuthenticationFilter.class)
            .debug("Bypassing JWT filter for public GET path: {}", path);
      }
      filterChain.doFilter(request, response);
      return;
    }
    if (request.getServletPath().contains("/api/v1/auth")) {
      filterChain.doFilter(request, response);
      return;
    }
    
    // Allow upload endpoints without requiring JWT auth, but set anonymous authentication
    if (path.equals("/uploadImage") || path.equals("/uploadFile")) {
      AnonymousAuthenticationToken anonymousAuth = new AnonymousAuthenticationToken(
          "anonymous",
          "anonymous",
          java.util.Collections.singletonList(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))
      );
      SecurityContextHolder.getContext().setAuthentication(anonymousAuth);
      filterChain.doFilter(request, response);
      return;
    }
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      org.slf4j.LoggerFactory.getLogger(JwtAuthenticationFilter.class)
          .warn("No Authorization header or doesn't start with Bearer for path: {}", path);
      filterChain.doFilter(request, response);
      return;
    }
    jwt = authHeader.substring(7);
    org.slf4j.LoggerFactory.getLogger(JwtAuthenticationFilter.class)
        .info("Processing JWT for path: {}, token: {}...", path, jwt.substring(0, Math.min(20, jwt.length())));
    
    try {
      userEmail = jwtService.extractUsername(jwt);
      org.slf4j.LoggerFactory.getLogger(JwtAuthenticationFilter.class)
          .info("Extracted username: {}", userEmail);
      
      if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
        org.slf4j.LoggerFactory.getLogger(JwtAuthenticationFilter.class)
            .info("User details loaded: {}, authorities: {}", userDetails.getUsername(), userDetails.getAuthorities());
        
        boolean isTokenValid = tokenRepository.findByToken(jwt)
                .map(t -> {
                  boolean valid = !t.isExpired() && !t.isRevoked() && t.getTokenType() == TokenType.ACCESS;
                  org.slf4j.LoggerFactory.getLogger(JwtAuthenticationFilter.class)
                      .info("Token validation - expired: {}, revoked: {}, type: {}, valid: {}", 
                          t.isExpired(), t.isRevoked(), t.getTokenType(), valid);
                  return valid;
                })
                .orElse(false);
        
        org.slf4j.LoggerFactory.getLogger(JwtAuthenticationFilter.class)
            .info("Token found in repository: {}, JWT service validation: {}", 
                isTokenValid, jwtService.isTokenValid(jwt, userDetails));
        
        if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
          // Extract roles from JWT token and convert to authorities
          List<String> rolesFromToken = jwtService.extractRoles(jwt);
          List<SimpleGrantedAuthority> authorities = rolesFromToken.stream()
              .map(SimpleGrantedAuthority::new)
              .collect(java.util.stream.Collectors.toList());
          
          org.slf4j.LoggerFactory.getLogger(JwtAuthenticationFilter.class)
              .info("Roles extracted from JWT: {}, converted to authorities: {}", rolesFromToken, authorities);
          
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              userDetails,
              null,
              authorities  // Use authorities extracted from JWT token instead of database
          );
          authToken.setDetails(
              new WebAuthenticationDetailsSource().buildDetails(request)
          );
          SecurityContextHolder.getContext().setAuthentication(authToken);
          org.slf4j.LoggerFactory.getLogger(JwtAuthenticationFilter.class)
              .info("Authentication set successfully with JWT authorities: {}", authorities);
        } else {
          org.slf4j.LoggerFactory.getLogger(JwtAuthenticationFilter.class)
              .warn("Token validation failed - isTokenValid: {}, jwtService.isTokenValid: {}", 
                  isTokenValid, jwtService.isTokenValid(jwt, userDetails));
        }
      }
    } catch (Exception e) {
      org.slf4j.LoggerFactory.getLogger(JwtAuthenticationFilter.class)
          .error("Error processing JWT: {}", e.getMessage(), e);
    }
    filterChain.doFilter(request, response);
  }
}
