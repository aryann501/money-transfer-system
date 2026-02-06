package com.example.backend.repository;

import com.example.backend.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {
    TransactionLog findByIdempotencyKey(String idempotencyKey);
}
