package com.company.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.company.entity.Student;
import com.company.entity.Teacher;
import com.company.entity.base.Person;

@Repository
public interface PersonRepo extends JpaRepository<Person , Long>{
	// use existing findAll methods from JPA to display student and teacher details using person in a polymorphic behaviour
	//Polymorphic Behaviour : To query with Parent  and you will get all childs automatically

	// JPQL
	@Query("select s from Student s")
	List<Student> findAllStudents();
	
	@Query("select t from Teacher t")
	List<Teacher>findAllTeachers();
	

}

