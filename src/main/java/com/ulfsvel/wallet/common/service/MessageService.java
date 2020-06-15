package com.ulfsvel.wallet.common.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.ulfsvel.wallet.common.entity.PasswordResetToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageService {


    private static final Logger log = LoggerFactory.getLogger(MessageService.class);

    private final AmazonSimpleEmailService client;

    private final String emailFrom;

    public MessageService(AmazonSimpleEmailService client, String emailFrom) {
        this.client = client;
        this.emailFrom = emailFrom;
    }

    public void sendResetTokenMessage(String to, PasswordResetToken passwordResetToken) {
        SendEmailRequest request = new SendEmailRequest()
                .withDestination(
                        new Destination().withToAddresses(to))
                .withMessage(new Message()
                        .withBody(new Body()
                                .withText(new Content()
                                        .withCharset("UTF-8").withData(passwordResetToken.getToken())))
                        .withSubject(new Content()
                                .withCharset("UTF-8").withData("Confirmation Token")))
                .withSource(emailFrom);
        client.sendEmail(request);
        log.info(passwordResetToken.getToken());
    }

}
