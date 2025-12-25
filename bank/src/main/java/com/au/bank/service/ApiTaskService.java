package com.au.bank.service;

import org.springframework.scheduling.annotation.Scheduled;

import com.au.bank.entity.ApiTask;

public interface ApiTaskService {
	
	@Scheduled(fixedRate = 5000)
	public void executeScheduledTasks();
	
	
	public void executeApi(ApiTask task);

}
