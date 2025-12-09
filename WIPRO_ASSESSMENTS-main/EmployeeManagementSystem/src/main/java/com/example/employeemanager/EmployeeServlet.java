package com.example.employeemanager;

import java.io.IOException;

import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class EmployeeServlet
 */
@WebServlet("/employee")
public class EmployeeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");  // IMPORTANT
        PrintWriter pw = response.getWriter();

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String department = request.getParameter("department");
            double salary = Double.parseDouble(request.getParameter("salary"));

            pw.println("<h2>Received: " + id + " - " + name + "</h2>"); // Debug

            Employee emp = new Employee(id, name, department, salary);
            EmployeeDAO dao = new EmployeeDAO();
            dao.addEmployee(emp);

            pw.println("<h1>Record Added Successfully!</h1>");

        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<h3 style='color:red'>Error: " + e.getMessage() + "</h3>");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
