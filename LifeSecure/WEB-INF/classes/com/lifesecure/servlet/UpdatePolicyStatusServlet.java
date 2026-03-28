package com.lifesecure.servlet;

import com.lifesecure.util.DBConnection;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class UpdatePolicyStatusServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("agentId") == null) {
            response.sendRedirect("agent-login.html");
            return;
        }

        int policyId = Integer.parseInt(request.getParameter("policyId"));
        String action = request.getParameter("action");

        String status = "Pending";
        if (action.equals("accept")) status = "Please Accept / Reject";
        else if (action.equals("reject")) status = "Rejected by Agent";

        try (Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE policies SET status=?, agent_id=? WHERE policy_no=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, (int) session.getAttribute("agentId"));
            ps.setInt(3, policyId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("agentDashboard");
    }
}
