package com.cg.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByEmail(String email);
}