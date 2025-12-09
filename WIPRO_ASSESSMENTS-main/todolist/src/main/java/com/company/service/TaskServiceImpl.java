package com.company.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.model.Task;
import com.company.repository.TaskRepository;


@Service
public class TaskServiceImpl implements  TaskService{

	@Autowired
	private TaskRepository taskRepository;
	
	@Override
	public List<Task> getAllTasks() {
		// TODO Auto-generated method stub
		return taskRepository.findAll() ;
	}

	@Override
	public Task getTaskByID(Long id) {
		// TODO Auto-generated method stub
		return taskRepository.findById(id).orElse(null);
	}

	@Override
	public Task saveTask(Task task) {
		// TODO Auto-generated method stub
		return taskRepository.save(task);
	}

	@Override
	public void deleteTask(Long id) {
		// TODO Auto-generated method stub
		taskRepository.deleteById(id);
	}
	
	

}
