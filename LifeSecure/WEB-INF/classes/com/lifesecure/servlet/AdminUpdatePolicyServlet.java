package com.lifesecure.servlet;

import com.lifesecure.util.DBConnection;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class AdminUpdatePolicyServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("adminUser") == null) {
            response.sendRedirect("admin-login.html");
            return;
        }

        int policyId = Integer.parseInt(request.getParameter("policyId"));
        String action = request.getParameter("action");
        String status = action.equals("approve") ? "Approved / Pending Payment" : "Rejected";

        try (Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE policies SET status=? WHERE policy_no=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, policyId);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }

        response.sendRedirect("adminDashboard");
    }
}
