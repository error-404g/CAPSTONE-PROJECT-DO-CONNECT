package com.contactmanager.servlet;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import com.contactmanager.model.Contact;
import com.contactmanager.util.ContactRepository;

public class AddContactServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        Contact contact = new Contact(name, email, phone);
        ContactRepository.addContact(contact);

        response.sendRedirect("viewContacts");
    }
}
