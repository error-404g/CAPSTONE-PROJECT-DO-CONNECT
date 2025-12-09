package com.company.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.dto.StudentDTO;
import com.company.dto.Student_DTO;
import com.company.entity.Student;
import com.company.entity.Teacher;
import com.company.entity.base.Address;
import com.company.entity.base.Person;
import com.company.repo.PersonRepo;
import com.company.repo.TeacherRepo;
import com.company.service.StudentService;

@RestController
@RequestMapping("/api")
public class DemoController {
	
	private final  StudentService studentService;
	private final TeacherRepo teacherRepo;
	private final PersonRepo personRepo;
	
	
	public DemoController(StudentService studentService,TeacherRepo teacherRepo,
			PersonRepo personRepo)
	{
		
		this.studentService = studentService;
		this.teacherRepo = teacherRepo;
		this.personRepo = personRepo;
		
	}
	
	@PostMapping("/add")
	public String add()
	{
		
		Teacher t1 = new Teacher("Niti" , new Address("JK" , "Delhi" , "110018") , "IT");
		teacherRepo.save(t1);
		
		
		Student s1 = new Student("Jiya" , new Address("TK" , "Jaipur" , "30002"));
		s1.addEmail("abc@gmail.com");
		s1.addEmail("xyz@Yahoo.co.in");
		
		
		Student s2 = new Student("Tanu" , new Address("Malviya" , "South" , "3545445"));
		s2.addEmail("tanu@gmail.com");
		s2.addEmail("tanu@Yahoo.co.in");
		
		
		studentService.save(s1);
		studentService.save(s2);
		
		
		
		return "Record Added";
		
	}
		@GetMapping("/studentswithemail")
		public List<StudentDTO> students()
		{
			return studentService.findAllWithEmails().stream()
					.map(s-> new StudentDTO(s.getId(),
							s.getName(),
							s.getAddress().getCity(),
							s.getEmails().stream().map(e-> e.getEmail()).toList())).collect(Collectors.toList());
			
			
		
		}
		
		
		@GetMapping("/people")
		public void findAllPeople()
		{
			// Hibernate will provide the data with one query which Hibernate will generate polymorphic behaviour or a query (which will return both Student + Teacher) 
			List<Person> people =  personRepo.findAll();
			
			for(Person p :people)
				if(p instanceof Student s)
				System.out.println("Student "+  s.getName() + " Course" + s.getAddress());
				else if(p instanceof Teacher t)
				System.out.println("Teacher "+  t.getName() + " Subject" + t.getSubject());
		
		}
		
		
		@GetMapping("/students")
		public void findAllStudents()
		{
			 personRepo.findAllStudents().forEach(s->System.out.println("Students Names "+ s.getName()));
			 
			
		}
		
		
		@GetMapping("/teachers")
		public void findAllTeachers()
		{
			personRepo.findAllTeachers().forEach(t->System.out.println("Teacher Names "+ t.getName()));;
			
		}
		
		@GetMapping("/students_dto")
		public List<Student_DTO> student_dto()
		{
              return studentService.allStudents_DTO();
		}
	

}
