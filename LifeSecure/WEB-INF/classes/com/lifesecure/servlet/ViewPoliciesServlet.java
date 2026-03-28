package com.lifesecure.servlet;

import com.lifesecure.util.DBConnection;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class ViewPoliciesServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("customer-login.html");
            return;
        }

        Integer customerId = (Integer) session.getAttribute("customerId");

        if (customerId == null) {
            response.sendRedirect("customer-login.html");
            return;
        }

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>View Policies</title>");

        out.println("<style>");
        out.println("body{font-family:'Segoe UI',sans-serif;margin:0;");
        out.println("background:linear-gradient(rgba(10,61,98,0.85),rgba(10,61,98,0.85)),url('images/cssbgtopsection.jpeg');");
        out.println("background-size:cover;color:white;text-align:center;}");

        out.println(".container{margin-top:60px; max-width: 1100px; margin-left: auto; margin-right: auto; padding: 0 20px;}");

        out.println(".table-box{margin:auto;width:100%;");
        out.println("background:rgba(255,255,255,0.15);backdrop-filter:blur(10px);");
        out.println("padding:25px;border-radius:15px; overflow-x: auto;}");

        out.println("table{width:100%;border-collapse:collapse;color:white;font-size:16px;}");

        out.println("th{font-size:18px;padding:15px;text-align:left;}");
        out.println("td{padding:14px;text-align:left;}");

        out.println("th,td{border-bottom:1px solid rgba(255,255,255,0.3);}");


        out.println(".pay-btn{background:#27ae60;color:white;padding:6px 12px;border:none;border-radius:6px;cursor:pointer;}");
        out.println(".pay-btn:hover{background:#1e8449;}");

        out.println(".back-btn{display:inline-block;padding:10px 20px;");
        out.println("border-radius:8px;background:rgba(255,255,255,0.2);");
        out.println("color:white;text-decoration:none;transition: all 0.3s ease;}");

        out.println(".back-btn:hover{background:rgba(255,255,255,0.35);}");
        
        out.println("header { background: rgba(15, 23, 42, 0.95); backdrop-filter: blur(10px); -webkit-backdrop-filter: blur(10px); color: white; padding: 24px; text-align: center; font-size: 1.8rem; font-weight: 700; border-bottom: 1px solid rgba(255, 255, 255, 0.1); box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2); display: flex; justify-content: space-between; align-items: center; }");
        out.println(".logout-btn { background: rgba(239, 68, 68, 0.2); border: 1px solid rgba(239, 68, 68, 0.3); color: white; padding: 8px 16px; border-radius: 8px; text-decoration: none; font-size: 1rem; transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1); }");
        out.println(".logout-btn:hover { background: rgba(239, 68, 68, 0.8); }");

        out.println("</style></head><body>");
        
        out.println("<header>");
        out.println("<div>📄 Your Policies</div>");
        out.println("<a href='logout' class='logout-btn'>Logout</a>");
        out.println("</header>");

        out.println("<div class='container'>");
        
        out.println("<div style='display: flex; justify-content: flex-end; align-items: center; margin-bottom: 20px;'>");
        out.println("<a href='customer-dashboard.html' class='back-btn'>⬅ Back to Dashboard</a>");
        out.println("</div>");
        
        out.println("<div class='table-box'>");

        out.println("<table>");
        out.println("<tr>");
        out.println("<th>Policy No</th>");
        out.println("<th>Policy Type</th>");
        out.println("<th>Details</th>");
        out.println("<th>Premium (₹)</th>");
        out.println("<th>Status</th>");
        out.println("<th>Agent Name</th>");
        out.println("<th>Action</th>");
        out.println("</tr>");

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT p.policy_no, p.policy_type, p.premium, p.details, p.status, a.name AS agent_name, c.name AS customer_name " +
                    "FROM policies p " +
                    "LEFT JOIN agents a ON p.agent_id = a.id " +
                    "JOIN customers c ON p.customer_id = c.id " +
                    "WHERE p.customer_id=?"
            );

            ps.setInt(1, customerId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int policyNo = rs.getInt("policy_no");
                String policyType = rs.getString("policy_type");
                String premium = rs.getString("premium");
                String details = rs.getString("details");
                String status = rs.getString("status");
                String agent = rs.getString("agent_name");
                String customerName = rs.getString("customer_name");

                out.println("<tr>");

                out.println("<td>" + policyNo + "</td>");
                out.println("<td>" + policyType + "</td>");
                out.println("<td>" + (details != null ? details : "-") + "</td>");
                out.println("<td>₹" + premium + "</td>");

                String color = "white";

                if ("Active".equalsIgnoreCase(status))
                    color = "lightgreen";
                else if ("Pending".equalsIgnoreCase(status) || "Under Review".equalsIgnoreCase(status))
                    color = "gold";
                else if ("Rejected".equalsIgnoreCase(status) || "Rejected by Agent".equalsIgnoreCase(status))
                    color = "lightcoral";
                else if ("Approved / Pending Payment".equalsIgnoreCase(status) || "Please Accept / Reject".equalsIgnoreCase(status))
                    color = "#00e6e6";

                out.println("<td style='color:" + color + ";font-weight:bold;'>" + status + "</td>");

                if (agent != null)
                    out.println("<td>" + agent + "</td>");
                else
                    out.println("<td>Agent Not Assigned Yet</td>");

                out.println("<td>");

                if ("Approved / Pending Payment".equalsIgnoreCase(status)) {

                    out.println("<form action='pay-payment.html' method='get'>");

                    out.println("<input type='hidden' name='policyNo' value='" + policyNo + "'>");
                    out.println("<input type='hidden' name='amount' value='" + premium + "'>");
                    out.println("<input type='hidden' name='holderName' value='" + customerName + "'>");

                    out.println("<button type='submit' class='pay-btn'>Pay Now</button>");

                    out.println("</form>");

                } else {

                    out.println("-");

                }

                out.println("</td>");

                out.println("</tr>");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        out.println("</table>");
        out.println("</div>");

        out.println("</div>");
        out.println("</body></html>");

    }
}
