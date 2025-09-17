package com.example.qualitycontrolproject.WebController;

import com.example.qualitycontrolproject.Repository.UserRepository;
import com.example.qualitycontrolproject.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;  // encode et vérifie les mots de passe de manière sécurisée avec BCrypt.



/*    @PostMapping("/signup")
    public String signUp(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return "Email already exists";
        }
        user.setPassword(user.getPassword());
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("AUDITOR"); // Par défaut
        }
        userRepository.save(user);
        return "User registered successfully";
    }*/

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("AUDITOR"); // par défaut
        }
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }


  /*  @PostMapping("/login")
    public String login(@RequestBody User user) {
        User existingUser = userRepository.findByEmail(user.getEmail())
                .orElse(null);

        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
            return "Login successful as " + existingUser.getRole();
        } else {
            return "Invalid email or password";
        }
    }*/

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User existingUser = userRepository.findByEmail(user.getEmail()).orElse(null);

        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
            return ResponseEntity.ok(existingUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }
}