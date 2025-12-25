package com.au.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.au.bank.entity.ExecutionLog;

public interface ExecutionLogRepository extends JpaRepository<ExecutionLog, Long> {
}
