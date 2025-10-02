package se331.lab.security.auth;



import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se331.lab.security.config.JwtService;
import se331.lab.security.token.Token;
import se331.lab.security.token.TokenRepository;
import se331.lab.security.token.TokenType;
import se331.lab.OrganizerAuthDTO;
import se331.lab.security.user.Role;
import se331.lab.security.user.User;
import se331.lab.security.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    User user = User.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .roles(List.of(Role.ROLE_USER))
            .build();
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    saveRefreshToken(savedUser, refreshToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            )
    );
    // Try username first; if not found, try email
    User user = repository.findByUsername(request.getUsername())
            .or(() -> repository.findByEmail(request.getUsername()))
            .orElseThrow();

    String jwtToken = jwtService.generateToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    saveRefreshToken(user, refreshToken);
    
    // Build organizer DTO manually to avoid mapper issues
    OrganizerAuthDTO organizerDTO = OrganizerAuthDTO.builder()
            .id(user.getOrganizer() != null ? user.getOrganizer().getId() : null)
            .name(user.getOrganizer() != null ? user.getOrganizer().getName() : null)
            .roles(user.getRoles())
            .build();
    
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .user(organizerDTO)
            .build();
  }

  private void saveUserToken(User user, String jwtToken) {
    Token token = Token.builder()
            .user(user)
            .token(jwtToken)
            .tokenType(TokenType.ACCESS)
            .expired(false)
            .revoked(false)
            .build();
    tokenRepository.save(token);
  }

  private void saveRefreshToken(User user, String refreshToken) {
    Token token = Token.builder()
            .user(user)
            .token(refreshToken)
            .tokenType(TokenType.REFRESH)
            .expired(false)
            .revoked(false)
            .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
    final String refreshToken = request.getRefreshToken();
    final String userEmail = jwtService.extractUsername(refreshToken);
    
    if (userEmail != null) {
      User user = this.repository.findByUsername(userEmail)
              .or(() -> repository.findByEmail(userEmail))
              .orElseThrow();
      
      // Check if refresh token is valid and not revoked/expired
      boolean isRefreshTokenValid = tokenRepository.findByToken(refreshToken)
              .map(token -> !token.isExpired() && !token.isRevoked() && token.getTokenType() == TokenType.REFRESH)
              .orElse(false);
      
      if (jwtService.isTokenValid(refreshToken, user) && isRefreshTokenValid) {
        String accessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        
        // Revoke old tokens
        revokeAllUserTokens(user);
        
        // Save new tokens
        saveUserToken(user, accessToken);
        saveRefreshToken(user, newRefreshToken);
        
        // Build organizer DTO
        OrganizerAuthDTO organizerDTO = OrganizerAuthDTO.builder()
                .id(user.getOrganizer() != null ? user.getOrganizer().getId() : null)
                .name(user.getOrganizer() != null ? user.getOrganizer().getName() : null)
                .roles(user.getRoles())
                .build();
        
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .user(organizerDTO)
                .build();
      }
    }
    throw new RuntimeException("Invalid refresh token");
  }
}
