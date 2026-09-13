package com.studentportal.studentacademicportal.controller;


import com.studentportal.studentacademicportal.entity.User;
import com.studentportal.studentacademicportal.service.UserService;
import org.springframework.web.bind.annotation.*;
import com.studentportal.studentacademicportal.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;


@RestController
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
    public UserController(UserService userService,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }
    @PostMapping("/api/users")
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }
//
//    POST /api/login
//       ↓
//    LoginRequest
//       ↓
//    UserController
//       ↓
//    UserService
//       ↓
//    findByEmail()
//       ↓
//               BCrypt.matches()
//               ↓
//               ┌───────────────┐
//               │ Password right│ → 200 OK
//               │ Password wrong│ → 401
//               └───────────────┘

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            return ResponseEntity.ok(authentication.getName());

        } catch (Exception e) {

            return ResponseEntity.status(401)
                    .body("Invalid email or password");
        }
    }
}