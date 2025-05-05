package com.eustache.book_network.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationRequest {
  @Email(message = "Email is not well formatted")
  @NotEmpty(message = "Email should not be empty")
  @NotBlank(message = "Email should not be empty")
  private String email;
  @NotEmpty(message = "Password should not be empty")
  @NotBlank(message = "Password should not be empty")
  @Size(min = 8, message = "Password should be 8 characters long minimum")
  private String password;
}
