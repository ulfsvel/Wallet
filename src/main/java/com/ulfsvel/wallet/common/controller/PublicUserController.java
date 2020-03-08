package com.ulfsvel.wallet.common.controller;

import com.ulfsvel.wallet.common.entity.PasswordResetToken;
import com.ulfsvel.wallet.common.request.CreateUserRequest;
import com.ulfsvel.wallet.common.request.PasswordResetTokenRequest;
import com.ulfsvel.wallet.common.request.ResetPasswordRequest;
import com.ulfsvel.wallet.common.service.MessageService;
import com.ulfsvel.wallet.common.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/public/user")
public class PublicUserController {

    private final UserService userService;

    private final MessageService messageService;

    public PublicUserController(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @PostMapping("create")
    public void createUser(@RequestBody CreateUserRequest createUserRequest) {
        userService.createUser(
                createUserRequest.getEmail(),
                createUserRequest.getPassword()
        );
    }


    @PostMapping("resetToken")
    public void resetPasswordRequest(@RequestBody PasswordResetTokenRequest passwordResetTokenRequest) {
        PasswordResetToken passwordResetToken = userService.createResetToken(
                passwordResetTokenRequest.getEmail()
        );
        messageService.sendResetTokenMessage(
                passwordResetToken
        );
    }

    @PostMapping("reset")
    public void resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        userService.resetUserPassword(
                resetPasswordRequest.getToken(),
                resetPasswordRequest.getPassword()
        );
    }

}
