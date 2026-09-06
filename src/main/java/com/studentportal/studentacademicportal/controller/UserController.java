package com.studentportal.studentacademicportal.controller;

import com.studentportal.studentacademicportal.entity.User;
import com.studentportal.studentacademicportal.service.UserService;
import org.springframework.web.bind.annotation.*;
import com.studentportal.studentacademicportal.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import com.studentportal.studentacademicportal.dto.LoginResponse;
import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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

        Optional<User> user = userService.login(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        if (user.isPresent()) {

            User loggedInUser = user.get();

            LoginResponse response = new LoginResponse(
                    loggedInUser.getId(),
                    loggedInUser.getName(),
                    loggedInUser.getEmail(),
                    loggedInUser.getRole()
            );

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(401).body("Invalid email or password");
    }
}