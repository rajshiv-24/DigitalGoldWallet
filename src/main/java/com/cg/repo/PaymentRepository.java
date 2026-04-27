package com.cg.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cg.entity.Payment;
import com.cg.enums.PaymentStatus;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> findByUserUserId(Integer userId);

    List<Payment> findByPaymentStatus(PaymentStatus status);
}
