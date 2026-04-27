package com.cg.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cg.entity.TransactionHistory;
import com.cg.enums.TransactionStatus;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Integer> {

    List<TransactionHistory> findByUserUserId(Integer userId);

    List<TransactionHistory> findByTransactionStatus(TransactionStatus status);
}
