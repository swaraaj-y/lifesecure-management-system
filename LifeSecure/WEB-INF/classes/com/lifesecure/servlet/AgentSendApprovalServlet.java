package com.lifesecure.servlet;

import com.lifesecure.util.DBConnection;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AgentSendApprovalServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int policyId = Integer.parseInt(request.getParameter("policyId"));

        try {

            Connection conn = DBConnection.getConnection();

            String sql = "UPDATE policies SET status='Pending Admin Approval' WHERE policy_no=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, policyId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("agentDashboard");
    }
}
