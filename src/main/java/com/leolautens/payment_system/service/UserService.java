package com.leolautens.payment_system.service;

import com.leolautens.payment_system.dto.UserResponse;
import com.leolautens.payment_system.entity.User;
import com.leolautens.payment_system.repository.UserRepository;
import com.leolautens.payment_system.util.RandomString;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

    public UserResponse registerUser(User user) throws MessagingException, UnsupportedEncodingException {
        if(userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("User already exists");
        } else {
            String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
            user.setPassword(encodedPassword);

            String randomCode = RandomString.generateRandomString(64);
            user.setVerificationCode(randomCode);
            user.setEnabled(false);

            User savedUser = userRepository.save(user);

            mailService.sendVerificationEmail(user);

            return new UserResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getPassword());
        }
    }

    public boolean verifyUser(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);
        if(user != null) {
            user.setEnabled(true);
            user.setVerificationCode(null);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }
}
