package com.au.bank.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.au.bank.entity.ApiTask;
import com.au.bank.entity.ExecutionLog;
import com.au.bank.repository.ApiTaskRepository;
import com.au.bank.repository.ExecutionLogRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApiTaskServiceImpl implements  ApiTaskService {

	private final ApiTaskRepository apiTaskRepository;
	private final ExecutionLogRepository executionLogRepository;
	private final RestTemplate restTemplate = new RestTemplate();

	public ApiTaskServiceImpl(ApiTaskRepository apiTaskRepository, ExecutionLogRepository executionLogRepository) {
		this.apiTaskRepository = apiTaskRepository;
		this.executionLogRepository = executionLogRepository;
	}

	@Scheduled(fixedRate = 10000) // 10 seconds
	public void executeScheduledTasks() {
		log.info("executeScheduledTasks started");
		List<ApiTask> tasks = apiTaskRepository.findAll();

		for (ApiTask task : tasks) {
			if (shouldExecute(task)) {
				executeApi(task);
			}
		}
	}

	private boolean shouldExecute(ApiTask task) {
		LocalDateTime nextExecution = LocalDateTime.parse(task.getNextExecutionTime());
		return LocalDateTime.now().isAfter(nextExecution);
	}

	public void executeApi(ApiTask task) {
		boolean success;
		String response = "";
		log.info("executeApi started");

		try {
			response = restTemplate.exchange(task.getUrl(),
					org.springframework.http.HttpMethod.valueOf(task.getHttpMethod()), null, String.class).getBody();
			success = true;
		} catch (Exception e) {
			log.info("Exception occured: {}" + e);
			response = e.getMessage();
			success = false;
		}

		// Log the execution
		executionLogRepository.save(new ExecutionLog(null, task.getId(), LocalDateTime.now(), success, response));

		// Update the task's execution times
		task.setLastExecutionTime(LocalDateTime.now().toString());
		task.setNextExecutionTime(LocalDateTime.now().plusSeconds(task.getIntervalInSeconds()).toString());
		apiTaskRepository.save(task);
	}
	
}
