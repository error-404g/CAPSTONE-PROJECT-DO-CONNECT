package com.company.service;

import java.util.List;

import com.company.model.Task;

public interface TaskService {
	
	List<Task> getAllTasks();
	
	Task getTaskByID(Long id);
	
	Task saveTask(Task task);
	
	void deleteTask(Long id);

}
