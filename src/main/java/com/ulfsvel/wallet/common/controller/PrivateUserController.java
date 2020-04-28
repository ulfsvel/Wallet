package com.ulfsvel.wallet.common.controller;

import com.ulfsvel.wallet.common.entity.Wallet;
import com.ulfsvel.wallet.common.request.UpdateUserRequest;
import com.ulfsvel.wallet.common.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


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

    @GetMapping("getWallets")
    public List<Wallet> getUser(Principal principal) {
        return userService.getUser(
                principal.getName()
        ).getWalletList();
    }

}
