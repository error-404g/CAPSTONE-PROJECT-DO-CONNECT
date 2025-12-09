package com.example.employeemanager;

//Bean Class  // Entity  //  POJO Class // Model we have create userBean class
public class Employee {

	
	//encapsulated properties so giving access we have created getter and setter 
	private int id;
	private String name,department;
	private double salary;

	
	
		public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}


		
		public Employee(int id, String name, String department, double salary) {
			super();
			this.id = id;
			this.name = name;
			this.department = department;
			this.salary = salary;
		}




		public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDepartment() {
		return department;
	}


	public void setDepartment(String department) {
		this.department = department;
	}


	public double getSalary() {
		return salary;
	}


	public void setSalary(double salary) {
		this.salary = salary;
	}

	
	
	
	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", department=" + department + ", salary=" + salary + "]";
	}




	// validate method is to validate the user name and password
	public boolean validate(int id1, String name1)
	{
		if((id1 == id) && name.equals(name))
		return true;
	else
		return false;
	}
}