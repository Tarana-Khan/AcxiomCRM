
package com.acxiom.crm.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acxiom.crm.entity.AuditLog;
import com.acxiom.crm.repository.AuditLogRepository;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public AuditLog logAction(
            String username,
            String action,
            String entityName,
            Long recordId,
            String ipAddress) {

        AuditLog log = new AuditLog();

        log.setUsername(username);
        log.setAction(action);
        log.setEntityName(entityName);
        log.setRecordId(recordId);
        log.setIpAddress(ipAddress);

        return auditLogRepository.save(log);
    }

    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<AuditLog> getLogsByUsername(String username) {
        return auditLogRepository.findByUsernameOrderByCreatedAtDesc(username);
    }
}
