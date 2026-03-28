package com.lifesecure.servlet;

import com.lifesecure.util.DBConnection;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class CustomerLoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT id, name FROM customers WHERE email=? AND password=?")) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                HttpSession session = request.getSession();
                session.setAttribute("customerId", rs.getInt("id"));
                session.setAttribute("customer_name", rs.getString("name"));

                response.sendRedirect("customer-dashboard.html");

            } else {

                response.sendRedirect("customer-login.html?error=1");

            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Database error occurred.");
        }
    }
}
