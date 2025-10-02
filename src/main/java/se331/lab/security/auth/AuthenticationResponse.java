package se331.lab.security.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se331.lab.OrganizerAuthDTO;
import se331.lab.security.user.Role;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

  @JsonProperty("access_token")
  private String accessToken;
  @JsonProperty("refresh_token")
  private String refreshToken;
  private SimpleUserDTO user;

  // Alternative simple user DTO for registration
  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class SimpleUserDTO {
    private Long id;
    private String name;
    private List<String> roles;
  }
}
