
package com.acxiom.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acxiom.crm.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findAllByOrderByCreatedAtDesc();

    List<AuditLog> findByUsernameOrderByCreatedAtDesc(String username);

}
