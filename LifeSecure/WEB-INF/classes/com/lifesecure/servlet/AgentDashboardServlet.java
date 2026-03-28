package com.lifesecure.servlet;

import com.lifesecure.util.DBConnection;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class AgentDashboardServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("agentId") == null) {
            response.sendRedirect("agent-login.html");
            return;
        }

        int agentId = (int) session.getAttribute("agentId");
        String agentName = (String) session.getAttribute("agentName");

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Agent Dashboard | LifeSecure</title>");
        out.println("<link href='https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap' rel='stylesheet'>");
        out.println("<style>");
        out.println(":root { --primary: #0f172a; --secondary: #3b82f6; --white: #ffffff; --card-bg: rgba(255, 255, 255, 0.15); --transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1); }");
        out.println("* { margin: 0; padding: 0; box-sizing: border-box; }");
        out.println("body { font-family: 'Inter', sans-serif; min-height: 100vh; background: linear-gradient(135deg, rgba(15, 23, 42, 0.85) 0%, rgba(30, 58, 138, 0.85) 100%), url('images/cssbgtopsection.jpeg') center/cover no-repeat fixed; color: var(--white); -webkit-font-smoothing: antialiased; }");
        
        // Header
        out.println("header { background: rgba(15, 23, 42, 0.95); backdrop-filter: blur(10px); -webkit-backdrop-filter: blur(10px); color: var(--white); padding: 24px; text-align: center; font-size: 1.8rem; font-weight: 700; border-bottom: 1px solid rgba(255, 255, 255, 0.1); box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2); display: flex; justify-content: space-between; align-items: center; }");
        out.println(".logout-btn { background: rgba(239, 68, 68, 0.2); border: 1px solid rgba(239, 68, 68, 0.3); color: white; padding: 8px 16px; border-radius: 8px; text-decoration: none; font-size: 1rem; transition: var(--transition); }");
        out.println(".logout-btn:hover { background: rgba(239, 68, 68, 0.8); }");
        
        // Container & Table
        out.println(".container { max-width: 1100px; margin: 50px auto; padding: 0 20px; }");
        out.println(".table-box { background: var(--card-bg); backdrop-filter: blur(15px); padding: 30px; border-radius: 20px; border: 1px solid rgba(255, 255, 255, 0.2); box-shadow: 0 10px 30px rgba(0,0,0,0.2); overflow-x: auto; }");
        out.println("h2 { margin-bottom: 25px; font-weight: 600; font-size: 1.8rem; }");
        out.println("table { width: 100%; border-collapse: collapse; text-align: left; }");
        out.println("th { padding: 15px; border-bottom: 2px solid rgba(255,255,255,0.3); font-size: 1.1rem; }");
        out.println("td { padding: 15px; border-bottom: 1px solid rgba(255,255,255,0.1); }");
        
        // Buttons
        out.println(".action-form { display: flex; gap: 10px; }");
        out.println(".btn { padding: 8px 16px; border: none; border-radius: 8px; font-weight: 600; cursor: pointer; transition: var(--transition); color: white; }");
        out.println(".btn-accept { background: #10b981; } .btn-accept:hover { background: #059669; transform: translateY(-2px); box-shadow: 0 4px 10px rgba(16,185,129,0.3); }");
        out.println(".btn-reject { background: #ef4444; } .btn-reject:hover { background: #dc2626; transform: translateY(-2px); box-shadow: 0 4px 10px rgba(239,68,68,0.3); }");
        out.println("</style></head><body>");

        out.println("<header>");
        out.println("<div>👨‍💼 Agent Workspace</div>");
        out.println("<a href='logout?role=agent' class='logout-btn'>Logout</a>");
        out.println("</header>");

        out.println("<div class='container'>");
        out.println("<h2>Welcome back, " + agentName + "</h2>");
        out.println("<div class='table-box'>");
        out.println("<h3>⏳ Pending Policy Applications</h3><br>");

        out.println("<table><tr><th>Policy ID</th><th>Customer Name</th><th>Policy Type</th><th>Details</th><th>Actions</th></tr>");

        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT p.policy_no, p.policy_type, p.details, c.name AS customer_name " +
                         "FROM policies p JOIN customers c ON p.customer_id = c.id " +
                         "WHERE p.status = 'Pending'";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            boolean hasRecords = false;
            while (rs.next()) {
                hasRecords = true;
                int policyNo = rs.getInt("policy_no");
                out.println("<tr>");
                out.println("<td>#" + policyNo + "</td>");
                out.println("<td><strong>" + rs.getString("customer_name") + "</strong></td>");
                out.println("<td>" + rs.getString("policy_type") + "</td>");
                out.println("<td>" + rs.getString("details") + "</td>");
                out.println("<td>");
                out.println("<form class='action-form' method='post' action='updatePolicyStatus'>");
                out.println("<input type='hidden' name='policyId' value='" + policyNo + "'>");
                out.println("<button type='submit' name='action' value='accept' class='btn btn-accept'>Approve</button>");
                out.println("<button type='submit' name='action' value='reject' class='btn btn-reject'>Reject</button>");
                out.println("</form>");
                out.println("</td>");
                out.println("</tr>");
            }

            if (!hasRecords) {
                out.println("<tr><td colspan='5' style='text-align:center; padding: 30px; color: #cbd5e1;'>No pending applications at the moment. You're all caught up!</td></tr>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<tr><td colspan='5' style='color:#ef4444;'>Error loading applications.</td></tr>");
        }

        out.println("</table>");
        out.println("</div></div>");
        out.println("</body></html>");
    }
}
