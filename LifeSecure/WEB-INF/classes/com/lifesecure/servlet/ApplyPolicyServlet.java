package com.lifesecure.servlet;

import com.lifesecure.util.DBConnection;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class ApplyPolicyServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("customerId") == null) {
            response.sendRedirect("customer-login.html");
            return;
        }

        int customerId = (int) session.getAttribute("customerId");
        String policyType = request.getParameter("policyType");
        String premium = request.getParameter("premium");
        String details = request.getParameter("details");

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO policies(customer_id, policy_type, premium, details, status, agent_id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, customerId);
            ps.setString(2, policyType);
            ps.setString(3, premium);
            ps.setString(4, details);
            ps.setString(5, "Pending");  // Initially pending for agent review
            ps.setInt(6, 1);             // Default agent id
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Confirmation page
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Request Submitted | LifeSecure</title>");
        out.println("<link href='https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap' rel='stylesheet'>");
        out.println("<style>");
        out.println(":root { --primary: #0f172a; --secondary: #3b82f6; --white: #ffffff; --card-bg: rgba(255, 255, 255, 0.15); --transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1); }");
        out.println("* { margin: 0; padding: 0; box-sizing: border-box; }");
        out.println("body { font-family: 'Inter', sans-serif; min-height: 100vh; display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg, rgba(15, 23, 42, 0.85) 0%, rgba(30, 58, 138, 0.85) 100%), url('images/cssbgtopsection.jpeg') center/cover no-repeat fixed; color: var(--white); -webkit-font-smoothing: antialiased; }");
        out.println(".receipt-card { width: 100%; max-width: 450px; padding: 40px; background: var(--card-bg); backdrop-filter: blur(20px); -webkit-backdrop-filter: blur(20px); border-radius: 24px; border: 1px solid rgba(255, 255, 255, 0.2); box-shadow: 0 20px 50px rgba(0, 0, 0, 0.3); text-align: center; animation: slideUp 0.6s ease-out forwards; opacity: 0; transform: translateY(20px); }");
        out.println("@keyframes slideUp { to { opacity: 1; transform: translateY(0); } }");
        out.println(".icon { font-size: 64px; color: #f59e0b; margin-bottom: 20px; filter: drop-shadow(0 0 15px rgba(245, 158, 11, 0.4)); animation: scaleIn 0.5s cubic-bezier(0.175, 0.885, 0.32, 1.275) 0.3s forwards; transform: scale(0); }");
        out.println("@keyframes scaleIn { to { transform: scale(1); } }");
        out.println("h2 { font-size: 1.8rem; font-weight: 700; margin-bottom: 10px; color: #fcd34d; }");
        out.println("p { font-size: 1.1rem; color: #e2e8f0; margin-bottom: 30px; line-height: 1.5; }");
        out.println(".btn { display: inline-block; width: 100%; padding: 14px; background: #3b82f6; color: white; text-decoration: none; border-radius: 12px; font-weight: 600; font-size: 1.1rem; transition: var(--transition); border: none; cursor: pointer; }");
        out.println(".btn:hover { background: #2563eb; transform: translateY(-2px); box-shadow: 0 8px 15px rgba(59, 130, 246, 0.4); }");
        out.println("</style></head><body>");
        
        out.println("<div class='receipt-card'>");
        out.println("<div class='icon'>⏳</div>");
        out.println("<h2>Apply Request Sent to Agent!</h2>");
        out.println("<p>Your policy request has been successfully submitted and is now pending agent review. You can track its status in 'View My Policies'.</p>");
        out.println("<a href='customer-dashboard.html' class='btn'>Back to Dashboard</a>");
        out.println("</div>");
        
        out.println("</body></html>");
    }
}
