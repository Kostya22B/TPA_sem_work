package com.example.demo;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EmailSenderWorker {

    private static final Logger LOG = LoggerFactory.getLogger(EmailSenderWorker.class);
    private final String sendGridApiKey = "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"; // Замените на ваш API-ключ SendGrid

    @JobWorker(type = "Send_text_to_user")
    public void sendEmail(
            @Variable(name = "userEmail") String recipientEmail,
            @Variable(name = "translatedText") String translatedText,
            @Variable(name = "translationText") String translationText,
            @Variable(name = "translationLanguageInput") String sourceLanguage,
            @Variable(name = "translationLanguageOutput") String targetLanguage,
            @Variable(name = "translationFormality") String translationFormality) {

        try {
            Email from = new Email("bondakos@cvut.cz");
            String subject = "Your Translated Text";
            Email to = new Email(recipientEmail);

            // HTML-контент письма
            String htmlContent = "<html>" +
                    "<body style='font-family: Arial, sans-serif; line-height: 1.6;'>" +
                    "<h2 style='color: #4CAF50;'>Your text was successfully translated!</h2>" +
                    "<p><strong>Original Text:</strong></p>" +
                    "<p style='background-color: #f9f9f9; padding: 10px; border-radius: 5px;'>" + translationText + "</p>" +
                    "<p><strong>Translated Text:</strong></p>" +
                    "<p style='background-color: #f4f4f4; padding: 10px; border-radius: 5px;'>" + translatedText + "</p>" +
                    "<hr>" +
                    "<h3 style='color: #2196F3;'>Translation Details</h3>" +
                    "<ul>" +
                    "<li><strong>Source Language:</strong> " + sourceLanguage + "</li>" +
                    "<li><strong>Target Language:</strong> " + targetLanguage + "</li>" +
                    "<li><strong>Formality:</strong> " + translationFormality + "</li>" +
                    "</ul>" +
                    "<hr>" +
                    "<p>Thank you for using Bondakos Translate!<br>Sincerely,<br>Team Bondakos Translate</p>" +
                    "</body>" +
                    "</html>";

            Content content = new Content("text/html", htmlContent);
            Mail mail = new Mail(from, subject, to, content);

            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            LOG.info("Email sent! Status Code: {}", response.getStatusCode());
        } catch (IOException ex) {
            LOG.error("Error while sending email: {}", ex.getMessage());
            throw new RuntimeException("Failed to send email");
        }
    }
}

