package com.studentportal.studentacademicportal.controller;

import com.studentportal.studentacademicportal.dto.LoginRequest;
import com.studentportal.studentacademicportal.dto.StudentProfileResponse;
import com.studentportal.studentacademicportal.dto.UserResponse;
import com.studentportal.studentacademicportal.entity.User;
import com.studentportal.studentacademicportal.exception.EmailAlreadyExistsException;
import com.studentportal.studentacademicportal.service.JwtService;
import com.studentportal.studentacademicportal.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserController(
            UserService userService,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/api/users")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {

        try {

            // Public registration can only create STUDENT accounts
            user.setRole("STUDENT");

            User savedUser = userService.saveUser(user);

            UserResponse response = new UserResponse(
                    savedUser.getId(),
                    savedUser.getName(),
                    savedUser.getEmail(),
                    savedUser.getRole()
            );

            return ResponseEntity.ok(response);

        } catch (EmailAlreadyExistsException e) {

            return ResponseEntity.status(409)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest loginRequest) {

        try {

            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginRequest.getEmail(),
                                    loginRequest.getPassword()
                            )
                    );

            User user = userService
                    .findByEmail(authentication.getName())
                    .orElseThrow();

            String token = jwtService.generateToken(
                    user.getEmail(),
                    user.getRole()
            );

            return ResponseEntity.ok(token);

        } catch (Exception e) {

            return ResponseEntity.status(401)
                    .body("Invalid email or password");
        }
    }

    @GetMapping("/api/profile/{id}")
    public ResponseEntity<StudentProfileResponse> getProfile(
            @PathVariable Long id) {

        User user = userService.findById(id)
                .orElseThrow();

        StudentProfileResponse response =
                new StudentProfileResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole()
                );

        return ResponseEntity.ok(response);
    }
}