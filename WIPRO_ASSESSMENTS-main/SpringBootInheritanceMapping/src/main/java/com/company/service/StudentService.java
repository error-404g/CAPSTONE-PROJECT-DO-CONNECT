package com.company.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.dto.Student_DTO;
import com.company.entity.Student;
import com.company.repo.StudentRepo;

@Service
public class StudentService {
	

	private final StudentRepo students;

	public StudentService(StudentRepo students) {
		super();
		this.students = students;
	}
	
	public Student save(Student s ) {
		
		return students.save(s);
	}
	
	public List<Student> findAllWithEmails()
	{
		
		return students.findAll();
	}
	
	
		
		public List<Student_DTO> allStudents_DTO()
	{
			return (List<Student_DTO>) students.findAllStudents().stream()
					.map(s-> new Student_DTO(s.getId(),
							s.getName(),
							s.getAddress().getCity()))
						.collect(Collectors.toList());
	}

}
