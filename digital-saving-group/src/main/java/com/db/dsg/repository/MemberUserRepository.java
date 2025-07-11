package com.db.dsg.repository;

import com.db.dsg.model.MemberUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberUserRepository extends JpaRepository<MemberUser, Long> {
    Optional<MemberUser> findByUsername(String username);
    boolean existsByUsername(String username);
}