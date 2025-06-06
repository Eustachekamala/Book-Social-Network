package com.eustache.book_network.auth;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@RestController
//@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentification")
public class AuthentificationController {
  private final AuthentificationService authentificationService;

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<?> register(
    @RequestBody @Valid RegistrationRequest request
  ) throws MessagingException {
      authentificationService.register(request);
      return ResponseEntity.accepted().build();
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
    @RequestBody @Valid AuthenticationRequest authenticationRequest
  ){
    return ResponseEntity.ok(authentificationService.authenticate(authenticationRequest));
  }

  @GetMapping("/activate-account")
  public void confirm(
    @RequestParam String token
  ) throws MessagingException {
    authentificationService.activateAccount(token);
  }
}
