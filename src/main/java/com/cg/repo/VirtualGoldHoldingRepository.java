package com.cg.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cg.entity.VirtualGoldHolding;

public interface VirtualGoldHoldingRepository extends JpaRepository<VirtualGoldHolding, Integer> {

    List<VirtualGoldHolding> findByUserUserId(Integer userId);
}