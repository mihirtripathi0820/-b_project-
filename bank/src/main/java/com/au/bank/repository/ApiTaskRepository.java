package com.au.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.au.bank.entity.ApiTask;

public interface ApiTaskRepository extends JpaRepository<ApiTask, Long> {
}
