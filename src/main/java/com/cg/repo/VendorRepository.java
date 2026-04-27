package com.cg.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cg.entity.Vendor;

public interface VendorRepository extends JpaRepository<Vendor, Integer> {
}