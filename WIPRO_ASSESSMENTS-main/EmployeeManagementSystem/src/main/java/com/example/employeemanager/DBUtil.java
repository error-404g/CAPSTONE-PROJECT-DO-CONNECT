package com.example.employeemanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil{

	
	// info of my database (url(localhost) , username and password of your database
	 private static final String url = "jdbc:mysql://localhost:3306/employee_db";
     private static   final String user = "root";
      private static   final String password = "12345";     

      static
      {
     
        try {
            // Load the MySQL JDBC driver (optional for newer JDBC versions, but good practice)
            Class.forName("com.mysql.cj.jdbc.Driver"); 

            // Establish the connection
            
        
        }
            catch (ClassNotFoundException e1) {
            System.err.println("MySQL JDBC Driver not found: " + e1.getMessage());
        } 
      }
        
        public static Connection getConnection() throws SQLException
        {
        	
             Connection connection =  DriverManager.getConnection(url, user, password);
             if (connection != null) {
             	
                 System.out.println("Connected to the database successfully!");
              
           
        }
			 return connection;   
        }
}

