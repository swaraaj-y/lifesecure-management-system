package com.lifesecure.servlet;

import com.lifesecure.util.DBConnection;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class LogoutServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String role = request.getParameter("role");
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        if ("admin".equals(role)) {
            response.sendRedirect("admin-login.html");
        } else if ("agent".equals(role)) {
            response.sendRedirect("agent-login.html");
        } else {
            response.sendRedirect("customer-login.html");
        }
    }
}
