package com.ulfsvel.wallet.common.service;

import com.ulfsvel.wallet.common.entity.PasswordResetToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageService.class);

    public void sendResetTokenMessage(PasswordResetToken passwordResetToken) {
        log.info(passwordResetToken.getToken());
    }

}
