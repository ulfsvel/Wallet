package com.ulfsvel.wallet.common.controller;

import com.ulfsvel.wallet.common.request.UpdateUserRequest;
import com.ulfsvel.wallet.common.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RestController
@RequestMapping("api/user/")
public class PrivateUserController {

    private final UserService userService;

    public PrivateUserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("update")
    public void updateUser(@RequestBody UpdateUserRequest updateUserRequest, Principal principal) {
        userService.updateUser(
                principal.getName(),
                updateUserRequest.getEmail(),
                updateUserRequest.getPassword()
        );
    }

}
