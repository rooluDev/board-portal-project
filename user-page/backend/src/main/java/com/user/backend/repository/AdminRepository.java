package com.user.backend.repository;

import com.user.backend.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Admin Repository
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
}
