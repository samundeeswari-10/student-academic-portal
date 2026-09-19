package com.studentportal.studentacademicportal.controller;

import com.studentportal.studentacademicportal.dto.UserResponse;
import com.studentportal.studentacademicportal.entity.User;
import com.studentportal.studentacademicportal.exception.EmailAlreadyExistsException;
import com.studentportal.studentacademicportal.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(
            @Valid @RequestBody User user) {

        try {

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
}