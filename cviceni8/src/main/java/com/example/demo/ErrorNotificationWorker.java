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
public class ErrorNotificationWorker {

    private static final Logger LOG = LoggerFactory.getLogger(ErrorNotificationWorker.class);
    private final String sendGridApiKey = "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"; // Замените на ваш API-ключ SendGrid

    @JobWorker(type = "Send_error_to_user")
    public void sendErrorNotification(
            @Variable(name = "userEmail") String recipientEmail,
            @Variable(name = "validationErrorText") String validationErrorText) {

        try {
            Email from = new Email("bondakos@cvut.cz");
            String subject = "Error in Translation Request";
            Email to = new Email(recipientEmail);

            // HTML-контент письма
            String htmlContent = "<html>" +
                    "<body>" +
                    "<h2>We encountered an error with your translation request</h2>" +
                    "<p><strong>Error Details:</strong></p>" +
                    "<p style='color: red;'>" +
                    "You choose 2 same languages. Try again or didnt insert text at all" +validationErrorText + "</p>" +
                    "<hr>" +
                    "<p>Please correct the errors and try again.<br>Sincerely,<br>Team Bondakos Translate</p>" +
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

            LOG.info("Error email sent! Status Code: {}", response.getStatusCode());
        } catch (IOException ex) {
            LOG.error("Error while sending error email: {}", ex.getMessage());
            throw new RuntimeException("Failed to send error email");
        }
    }
}