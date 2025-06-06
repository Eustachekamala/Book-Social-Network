package com.eustache.book_network.auth;

import com.eustache.book_network.email.EmailService;
import com.eustache.book_network.email.EmailTemplateName;
import com.eustache.book_network.repositories.RoleRepository;
import com.eustache.book_network.repositories.TokenRepository;
import com.eustache.book_network.repositories.UserRepository;
import com.eustache.book_network.security.JwtService;
import com.eustache.book_network.user.Token;
import com.eustache.book_network.user.User;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

//@Service
@RequiredArgsConstructor
public class AuthentificationService {
  private final  RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  private final EmailService emailService;
  @Value("${application.mailing.frontend.activation-url}")
  private String activateUrl;


  public void register(RegistrationRequest request) throws MessagingException {
    var userRole = roleRepository.findByName("USER")
      //! todo - better exception handling
    .orElseThrow(() -> new RuntimeException("ROLE USER was not initiated"));

    var user = User.builder()
      .firstname(request.getFirstname())
      .lastname(request.getLastname())
      .email(request.getEmail())
      .password(passwordEncoder.encode(request.getPassword()))
      .accountLocked(false)
      .enabled(false)
      .roles(List.of(userRole))
      .build();

    userRepository.save(user);
    sendValidationEmail(user);
  }

  private void sendValidationEmail(User user) throws MessagingException {
    var newToken = generateAndSaveActivationToken(user);
    //send email
    emailService.sendEmail(
      user.getEmail(),
      user.fullName(),
      EmailTemplateName.ACTIVATE_ACCOUNT,
      activateUrl,
      "Account activation",
      newToken
    );
  }

  private String generateAndSaveActivationToken(User user) {
    //generate a token
    String generatedToken = generateActivationCode(6);
    var token = Token.builder()
      .token(generatedToken)
      .createdAt(LocalDateTime.now())
      .expiresAt(LocalDateTime.now().plusMinutes(15))
      .user(user)
      .build();
    tokenRepository.save(token);
    return generatedToken;
  }

  private String generateActivationCode(int length) {
    String characters = "0123456789";
    StringBuilder codeBuilder = new StringBuilder();
    SecureRandom secureRandom = new SecureRandom();

    for (int i = 0; i < length; i++) {
      int randomIndex = secureRandom.nextInt(characters.length());// 0..9
      codeBuilder.append(characters.charAt(randomIndex));
    }
    return codeBuilder.toString();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
    var auth = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        authenticationRequest.getEmail(),
        authenticationRequest.getPassword()
      )
    );
    var claims = new HashMap<String, Object>();
    var user = (User) auth.getPrincipal();
    claims.put("fullname", user.fullName());
    var jwtToken = jwtService.generateToken(claims, user);
    return AuthenticationResponse.builder()
      .token(jwtToken).build();
  }

  //@Transactional
  public void activateAccount(String token) throws MessagingException {
    Token savedToken = tokenRepository.findByToken(token)
      //! todo exception has to be defined
      .orElseThrow(() -> new RuntimeException("Invalid or expired token: " + token));
    if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
      sendValidationEmail(savedToken.getUser());
      throw new RuntimeException("Activation token has expired. A new token has been sent to the same email");
    }
    var user = userRepository.findById(savedToken.getUser().getId())
      .orElseThrow(() -> new RuntimeException("User not found"));
    user.setEnabled(true);
    userRepository.save(user);
    savedToken.setValidatedAt(LocalDateTime.now());
    tokenRepository.save(savedToken);
  }
}
