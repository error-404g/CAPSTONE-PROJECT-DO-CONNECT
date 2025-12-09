package com.contactmanager.servlet;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import com.contactmanager.util.ContactRepository;

public class ViewContactsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("contacts", ContactRepository.getAllContacts());
        RequestDispatcher dispatcher = request.getRequestDispatcher("addContact.jsp");
        dispatcher.forward(request, response);
    }
}
