package com.cg.service;

import com.cg.dto.*;
import com.cg.entity.*;
import com.cg.enums.PaymentStatus;
import com.cg.enums.Role;
import com.cg.enums.TransactionType;
import com.cg.exception.DuplicateEmailException;
import com.cg.exception.InsufficientBalanceException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AddressRepository addressRepo;

    @Autowired
    private PaymentRepository paymentRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ──────────────────────────────────────────────────
    // CREATE USER
    // ──────────────────────────────────────────────────
    @Override
    public UserResponseDTO createUser(UserRequestDTO request) {
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        // Check for duplicate email (users.email has unique constraint)
        userRepo.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new DuplicateEmailException(
                    "Email already registered: " + request.getEmail());
        });

        Address address = addressRepo.findById(request.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Address not found with id: " + request.getAddressId()));

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : Role.USER);
        user.setAddress(address);
        user.setBalance(BigDecimal.ZERO);  // new users start with zero balance
        user.setCreatedAt(LocalDateTime.now());

        return toResponseDTO(userRepo.save(user));
    }

    // ──────────────────────────────────────────────────
    // GET USER BY ID
    // ──────────────────────────────────────────────────
    @Override
    public UserResponseDTO getUserById(Integer userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        return toResponseDTO(user);
    }

    // ──────────────────────────────────────────────────
    // GET ALL USERS
    // ──────────────────────────────────────────────────
    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ──────────────────────────────────────────────────
    // UPDATE USER
    // ──────────────────────────────────────────────────
    @Override
    public UserResponseDTO updateUser(Integer userId, UserRequestDTO request) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        if (request.getAddressId() != null) {
            Address address = addressRepo.findById(request.getAddressId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Address not found with id: " + request.getAddressId()));
            user.setAddress(address);
        }

        return toResponseDTO(userRepo.save(user));
    }

    // ──────────────────────────────────────────────────
    // DELETE USER
    // ──────────────────────────────────────────────────
    @Override
    public void deleteUser(Integer userId) {
        userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        userRepo.deleteById(userId);
    }

    // ──────────────────────────────────────────────────
    // WALLET TOP UP
    // Adds INR to user.balance and records in payments table
    // TransactionType.CREDIT = money coming into wallet
    // ──────────────────────────────────────────────────
    @Override
    public PaymentResponseDTO topUpWallet(WalletTopUpRequestDTO request) {
        User user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + request.getUserId()));

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InsufficientBalanceException("Top-up amount must be greater than zero");
        }

        // Credit the wallet
        user.setBalance(user.getBalance().add(request.getAmount()));
        userRepo.save(user);

        // Record payment entry
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setTransactionType(TransactionType.CREDITED_TO_WALLET);  // money credited to wallet
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setCreatedAt(LocalDateTime.now());
        Payment saved = paymentRepo.save(payment);

        return toPaymentResponseDTO(saved);
    }

    // ──────────────────────────────────────────────────
    // MAPPER: User entity → UserResponseDTO
    // ──────────────────────────────────────────────────
    private UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setUserId(user.getUserId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole() != null ? user.getRole() : Role.USER);
        dto.setBalance(user.getBalance());
        dto.setCreatedAt(user.getCreatedAt());

        if (user.getAddress() != null) {
            dto.setStreet(user.getAddress().getStreet());
            dto.setCity(user.getAddress().getCity());
            dto.setState(user.getAddress().getState());
            dto.setPostalCode(user.getAddress().getPostalCode());
            dto.setCountry(user.getAddress().getCountry());
        }
        return dto;
    }

    // ──────────────────────────────────────────────────
    // MAPPER: Payment entity → PaymentResponseDTO
    // ──────────────────────────────────────────────────
    private PaymentResponseDTO toPaymentResponseDTO(Payment payment) {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setPaymentId(payment.getPaymentId());
        dto.setUserId(payment.getUser().getUserId());
        dto.setUserName(payment.getUser().getName());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setTransactionType(payment.getTransactionType());
        dto.setPaymentStatus(payment.getPaymentStatus());
        dto.setCreatedAt(payment.getCreatedAt());
        return dto;
    }
}