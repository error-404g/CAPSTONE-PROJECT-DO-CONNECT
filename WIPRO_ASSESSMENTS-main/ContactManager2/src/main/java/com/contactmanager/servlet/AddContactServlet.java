package com.contactmanager.servlet;

import com.contactmanager.model.Contact;
import com.contactmanager.util.ContactRepository;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/addContact")
public class AddContactServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");

        ContactRepository.addContact(new Contact(id, name, phone));

        request.setAttribute("message", "Contact Added Successfully!");
        request.getRequestDispatcher("addContact.jsp").forward(request, response);
    }
}
