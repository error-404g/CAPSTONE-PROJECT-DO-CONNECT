package com.contactmanager.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

import com.contactmanager.model.Contact;
import com.contactmanager.util.ContactRepository;

@WebServlet("/viewContacts")
public class ViewContactsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Contact> list = ContactRepository.getAllContacts();
        request.setAttribute("contacts", list);

        request.getRequestDispatcher("viewContacts.jsp").forward(request, response);
    }
}
