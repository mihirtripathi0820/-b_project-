package com.au.bank.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.au.bank.entity.ApiTask;
import com.au.bank.repository.ApiTaskRepository;
import com.au.bank.service.ApiTaskService;

@RestController
@RequestMapping("/api/tasks")
public class ApiTaskController {
	
	private final ApiTaskService apiTaskService;
	private final ApiTaskRepository apiTaskRepository;

	public ApiTaskController(ApiTaskService apiTaskService, ApiTaskRepository apiTaskRepository) {
		this.apiTaskService = apiTaskService;
		this.apiTaskRepository = apiTaskRepository;
	}

	@GetMapping
	public List<ApiTask> getAllTasks() {
		return apiTaskRepository.findAll();
	}

	@PostMapping
	public ApiTask addTask(@RequestBody ApiTask task) {
		task.setNextExecutionTime(LocalDateTime.now().plusSeconds(task.getIntervalInSeconds()).toString());
		return apiTaskRepository.save(task);
	}

	@PutMapping("/{id}")
	public ApiTask updateTask(@PathVariable Long id, @RequestBody ApiTask updatedTask) {
		updatedTask.setId(id);
		return apiTaskRepository.save(updatedTask);
	}

	@DeleteMapping("/{id}")
	public void deleteTask(@PathVariable Long id) {
		apiTaskRepository.deleteById(id);
	}

	@PostMapping("/{id}/trigger")
	public void triggerApiManually(@PathVariable Long id) {
		ApiTask task = apiTaskRepository.findById(id).orElseThrow();
		apiTaskService.executeApi(task);
	}
}
