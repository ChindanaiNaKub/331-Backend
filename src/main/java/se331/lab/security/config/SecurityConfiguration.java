package se331.lab.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;
  private final LogoutHandler logoutHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.headers((headers) -> {
      headers.frameOptions((frameOptions) -> frameOptions.disable());
    });
    http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf((crsf) -> crsf.disable())
            .authorizeHttpRequests((authorize) -> {
              authorize
                  .requestMatchers("/api/v1/auth/**").permitAll()
                  .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                  .requestMatchers(
                      org.springframework.http.HttpMethod.GET,
                      "/events",
                      "/event",
                      "/events/*",
                      "/event/*",
                      "/events/**",
                      "/event/**",
                      "/api/v1/events",
                      "/api/v1/event",
                      "/api/v1/events/*",
                      "/api/v1/event/*",
                      "/api/v1/events/**",
                      "/api/v1/event/**",
                      "/organizations",
                      "/organizations/*",
                      "/organizations/**",
                      "/api/v1/organizations",
                      "/api/v1/organizations/*",
                      "/api/v1/organizations/**",
                      "/auction-items",
                      "/auction-items/*",
                      "/auction-items/**",
                      "/api/v1/auction-items",
                      "/api/v1/auction-items/*",
                      "/api/v1/auction-items/**",
                      "/students",
                      "/students/*",
                      "/students/**",
                      "/api/v1/students",
                      "/api/v1/students/*",
                      "/api/v1/students/**",
                      "/organizers",
                      "/organizers/*",
                      "/organizers/**",
                      "/api/v1/organizers",
                      "/api/v1/organizers/*",
                      "/api/v1/organizers/**"
                  ).permitAll()
                  .requestMatchers("/actuator/**").permitAll()
                  .requestMatchers(org.springframework.http.HttpMethod.POST, "/uploadFile", "/uploadImage").permitAll()
                  .requestMatchers(org.springframework.http.HttpMethod.POST, "/events").hasRole("ADMIN")
                  .anyRequest().authenticated();
            })

            .sessionManagement((session) ->{
              session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })
            .anonymous(anonymous -> {})
            


            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .logout((logout) -> {
              logout.logoutUrl("/api/v1/auth/logout");
              logout.addLogoutHandler(logoutHandler);
              logout.logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());
            })
    ;

    return http.build();

  }

  @Bean

  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOriginPatterns(java.util.List.of("http://localhost:5173"));
    config.setAllowedMethods(java.util.List.of("GET","POST","PUT","DELETE","OPTIONS"));
    config.setAllowedHeaders(java.util.List.of("*"));
    config.setExposedHeaders(java.util.List.of("x-total-count"));
    config.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  @Bean
  public FilterRegistrationBean<CorsFilter> corsFilterBean() {
    FilterRegistrationBean<CorsFilter> bean =
            new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return bean;
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring()
        .requestMatchers(new AntPathRequestMatcher("/uploadImage", "POST"))
        .requestMatchers(new AntPathRequestMatcher("/uploadFile", "POST"));
  }
}
