package com.lifesecure.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AdminLoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
        String user = request.getParameter("username");
        String pass = request.getParameter("password");
        
        // Hardcoded admin check
        boolean isValidAdmin = ("yash".equals(user) && "1234".equals(pass)) || 
                               ("swaraj".equals(user) && "1234".equals(pass));
                               
        if (isValidAdmin) {
            HttpSession session = request.getSession();
            session.setAttribute("adminUser", user);
            response.sendRedirect("adminDashboard");
        } else {
            response.sendRedirect("admin-login.html?error=invalid");
        }
    }
}
