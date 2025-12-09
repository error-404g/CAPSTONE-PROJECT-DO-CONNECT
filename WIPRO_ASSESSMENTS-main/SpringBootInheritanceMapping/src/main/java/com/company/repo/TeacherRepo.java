package com.company.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.company.entity.Teacher;

@Repository
public interface TeacherRepo extends JpaRepository<Teacher , Long>{

	@Query("select t from Teacher t")
	List<Teacher>findAllTeachers();

}
