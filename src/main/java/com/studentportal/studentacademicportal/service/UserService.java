
package com.studentportal.studentacademicportal.service;
import java.util.Optional;

import com.studentportal.studentacademicportal.entity.User;
import com.studentportal.studentacademicportal.exception.EmailAlreadyExistsException;
import com.studentportal.studentacademicportal.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User saveUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already registered");        }

        String encodedPassword =
                passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    public Optional<User> login(String email, String password) {

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent() &&
                passwordEncoder.matches(password, user.get().getPassword())) {

            return user;
        }

        return Optional.empty();
    }
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}