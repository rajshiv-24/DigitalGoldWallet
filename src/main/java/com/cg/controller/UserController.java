package com.cg.controller;

import com.cg.dto.PaymentResponseDTO;
import com.cg.dto.UserRequestDTO;
import com.cg.dto.UserResponseDTO;
import com.cg.dto.WalletTopUpRequestDTO;
import com.cg.entity.User;
import com.cg.repo.UserRepository;
import com.cg.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("@userAccessService.canAccessUser(authentication, #userId)")
    public UserResponseDTO getUserById(@PathVariable Integer userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("@userAccessService.canAccessUser(authentication, #userId)")
    public UserResponseDTO updateUser(@PathVariable Integer userId, @RequestBody UserRequestDTO request) {
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/wallet/top-up")
    @PreAuthorize("@userAccessService.canAccessUser(authentication, #request.userId)")
    public ResponseEntity<PaymentResponseDTO> topUpWallet(@RequestBody WalletTopUpRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.topUpWallet(request));
    }

    @GetMapping("/{userId}/balance")
    @PreAuthorize("@userAccessService.canAccessUser(authentication, #userId)")
    public Map<String, Object> getWalletBalance(@PathVariable Integer userId) {
        UserResponseDTO user = userService.getUserById(userId);
        return Map.of("userId", user.getUserId(), "balance", user.getBalance());
    }

    @GetMapping("/count")
    public Map<String, Long> countUsers() {
        return Map.of("count", userRepository.count());
    }

    @GetMapping("/{userId}/exists")
    public Map<String, Boolean> userExists(@PathVariable Integer userId) {
        return Map.of("exists", userRepository.existsById(userId));
    }

    @GetMapping("/by-email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        return userRepository.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
