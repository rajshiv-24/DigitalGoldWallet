package com.cg.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cg.entity.PhysicalGoldTransaction;

public interface PhysicalGoldTransactionRepository extends JpaRepository<PhysicalGoldTransaction, Integer> {

    List<PhysicalGoldTransaction> findByUserUserId(Integer userId);
}
