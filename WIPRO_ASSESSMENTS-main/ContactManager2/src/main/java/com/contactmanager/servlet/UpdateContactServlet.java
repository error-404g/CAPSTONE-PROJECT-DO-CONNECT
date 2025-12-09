package com.contactmanager.servlet;

import com.contactmanager.model.Contact;
import com.contactmanager.util.ContactRepository;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/updateContact")
public class UpdateContactServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");

        ContactRepository.updateContact(new Contact(id, name, phone));

        response.sendRedirect("viewContacts");
    }
}
