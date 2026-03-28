package com.lifesecure.servlet;

import com.lifesecure.util.DBConnection;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class SubmitSupportServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("customerId") == null) {
            response.sendRedirect("customer-login.html");
            return;
        }

        int customerId = (int) session.getAttribute("customerId");
        String policyNo = request.getParameter("policyNo");
        String name = request.getParameter("name");
        String issue = request.getParameter("issue");

        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO support_tickets (customer_id, policy_no, name, issue) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, customerId);
            ps.setString(2, policyNo);
            ps.setString(3, name);
            ps.setString(4, issue);

            ps.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("support.html?msg=success");
    }
}
