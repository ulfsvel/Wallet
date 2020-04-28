package com.ulfsvel.wallet.common.service;

import com.ulfsvel.wallet.common.entity.PasswordResetToken;
import com.ulfsvel.wallet.common.entity.User;
import com.ulfsvel.wallet.common.repository.PasswordResetTokenRepository;
import com.ulfsvel.wallet.common.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.validation.ValidationException;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserService {

    private final UserRepository userRepository;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordResetTokenRepository passwordResetTokenRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(String email, String plainPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            throw new ValidationException("User already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setHashedPassword(passwordEncoder.encode(plainPassword));

        return userRepository.save(user);
    }

    public PasswordResetToken createResetToken(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException(email);
        }

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(UUID.randomUUID().toString());
        passwordResetToken.setExpiryDate();
        passwordResetToken.setUser(userOptional.get());

        return passwordResetTokenRepository.save(passwordResetToken);
    }

    public User resetUserPassword(String token, String plainPassword) {
        Optional<PasswordResetToken> optionalPasswordResetToken = passwordResetTokenRepository.findByToken(token);
        if (!optionalPasswordResetToken.isPresent()) {
            throw new ValidationException("Token not found.");
        }

        PasswordResetToken passwordResetToken = optionalPasswordResetToken.get();
        Calendar cal = Calendar.getInstance();
        if ((passwordResetToken.getExpiryDate()
                .getTime() - cal.getTime()
                .getTime()) <= 0) {
            throw new ValidationException("Reset token is expired.");
        }

        User user = passwordResetToken.getUser();
        user.setHashedPassword(passwordEncoder.encode(plainPassword));

        return userRepository.save(user);
    }

    public User updateUser(String email, String newEmail, String newPassword) {
        Optional<User> userOptional = userRepository.findByEmail(newEmail);
        if (userOptional.isPresent()) {
            throw new ValidationException("User already exists");
        }

        userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException(email);
        }

        User user = userOptional.get();

        if (!StringUtils.isEmpty(newEmail)) {
            user.setEmail(newEmail);
        }
        if (!StringUtils.isEmpty(newPassword)) {
            user.setHashedPassword(passwordEncoder.encode(newPassword));
        }

        return userRepository.save(user);
    }

    public User getUser(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException(email);
        }

        return userOptional.get();
    }
}
