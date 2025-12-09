package com.company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long>{

}
