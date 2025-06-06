package com.eustache.book_network.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.*;
import static org.springframework.mail.javamail.MimeMessageHelper.*;

@Service
@RequiredArgsConstructor
public class EmailService {
  private final JavaMailSender mailSender;
  private final SpringTemplateEngine templateEngine;


  @Async
  public void sendEmail(
      String to,
      String username,
      EmailTemplateName emailTemplateName,
      String confirmationUrl,
      String subject,
      String activationCode
  ) throws MessagingException {
    String templateName;
    if (emailTemplateName == null) {
      templateName = "confirm-email";
    }else {
      templateName = emailTemplateName.getName();
    }
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper messageHelper = new MimeMessageHelper(
      mimeMessage,
      MULTIPART_MODE_MIXED,
      UTF_8.name()
    );
    Map<String, Object> properties = new HashMap<>();
    properties.put("username", username);
    properties.put("confirmationUrl", confirmationUrl);
    properties.put("activation_code", activationCode);

    Context context = new Context();
    context.setVariables(properties);

    messageHelper.setFrom("eustachekamala9@gmail.com");
    messageHelper.setTo(to);
    messageHelper.setSubject(subject);

    String template = templateEngine.process(templateName, context);
    messageHelper.setText(template, true);
    mailSender.send(mimeMessage);
  }
}
