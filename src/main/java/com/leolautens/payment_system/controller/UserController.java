package com.leolautens.payment_system.controller;

import com.leolautens.payment_system.dto.AuthenticationRequest;
import com.leolautens.payment_system.dto.AuthenticationResponse;
import com.leolautens.payment_system.dto.UserRequest;
import com.leolautens.payment_system.dto.UserResponse;
import com.leolautens.payment_system.entity.User;
import com.leolautens.payment_system.service.TokenService;
import com.leolautens.payment_system.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;


@RestController
@RequestMapping("/api/v1/users")

public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserRequest userRequest) throws MessagingException, UnsupportedEncodingException {
        User user = userRequest.toModel();
        UserResponse userSaved = userService.registerUser(user);
        return ResponseEntity.ok().body(userSaved);
    }

    @GetMapping("/verify")
    public String verifyUser(@RequestParam("code") String verificationCode) {
        boolean verified = userService.verifyUser(verificationCode);
        if(verified) {
            return "User verified successfully";
        } else {
            return "Invalid verification code";
        }
    }

    @GetMapping("/loginhello")
    public String hello() {
        return "Hello World";
    }

}
