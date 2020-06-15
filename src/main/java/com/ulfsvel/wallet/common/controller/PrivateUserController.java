package com.ulfsvel.wallet.common.controller;

import com.ulfsvel.wallet.common.entity.PasswordResetToken;
import com.ulfsvel.wallet.common.entity.Wallet;
import com.ulfsvel.wallet.common.request.UpdateUserRequest;
import com.ulfsvel.wallet.common.service.MessageService;
import com.ulfsvel.wallet.common.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("api/user/")
public class PrivateUserController {

    private final UserService userService;

    private final MessageService messageService;

    public PrivateUserController(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @PostMapping("update")
    public void updateUser(@RequestBody UpdateUserRequest updateUserRequest, Principal principal) {
        userService.updateUser(
                principal.getName(),
                updateUserRequest.getEmail(),
                updateUserRequest.getPassword()
        );
    }

    @PostMapping("requestConfirmToken")
    public void requestConfirmToken(Principal principal) {
        PasswordResetToken passwordResetToken = userService.createResetToken(
                principal.getName()
        );
        messageService.sendResetTokenMessage(
                principal.getName(),
                passwordResetToken
        );
    }

    @GetMapping("getWallets")
    public List<Wallet> getUser(Principal principal) {
        return userService.getUser(
                principal.getName()
        ).getWalletList();
    }

}
