package com.lifesecure.servlet;

import com.lifesecure.util.DBConnection;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class UpdateProfileServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("customer-login.html");
            return;
        }

        int customerId = (int) session.getAttribute("customerId");
        
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "UPDATE customers SET name=?, email=?, phone=?, address=? WHERE id=?"
            );
            
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, address);
            ps.setInt(5, customerId);

            int updated = ps.executeUpdate();
            
            if (updated > 0) {
                // Update session attribute if name changed, so dashboard header shows correct name
                session.setAttribute("customer_name", name);
                response.sendRedirect("viewProfile");
            } else {
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<h3>Error updating profile.</h3>");
                out.println("<a href='editProfile'>Back to Edit</a>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<h3>Database error occurred.</h3>");
            out.println("<a href='editProfile'>Back to Edit</a>");
        }
    }
}
