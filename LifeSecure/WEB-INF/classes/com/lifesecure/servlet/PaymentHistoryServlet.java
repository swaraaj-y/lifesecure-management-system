package com.lifesecure.servlet;

import com.lifesecure.util.DBConnection;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.math.BigDecimal;

public class PaymentHistoryServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("customerId") == null) {
            response.sendRedirect("customer-login.html");
            return;
        }

        int customerId = (int) session.getAttribute("customerId");

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Payment History</title>");
        out.println("<link href='https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap' rel='stylesheet'>");
        out.println("<style>");
        out.println("body { font-family: 'Inter', sans-serif; margin:0;");
        out.println("background: linear-gradient(rgba(10,61,98,0.85), rgba(10,61,98,0.85)), url('images/cssbgtopsection.jpeg');");
        out.println("background-size: cover; color: white; text-align: center; }");
        out.println(".container { margin-top: 60px; max-width: 900px; margin-left: auto; margin-right: auto;}");
        out.println(".table-box { width:100%;");
        out.println("background: rgba(255,255,255,0.15); backdrop-filter: blur(10px);");
        out.println("padding:25px; border-radius:15px; border: 1px solid rgba(255,255,255,0.2); box-shadow: 0 10px 30px rgba(0,0,0,0.1);}");
        out.println("table { width:100%; border-collapse: collapse; color:white; font-size:16px; text-align:left;}");
        out.println("th { font-size:18px; padding:15px; border-bottom: 2px solid rgba(255,255,255,0.3);}");
        out.println("td { padding:14px; border-bottom: 1px solid rgba(255,255,255,0.1);}");
        out.println(".back-btn { display:inline-block; margin-top:25px; padding:12px 24px;");
        out.println("border-radius:8px; background: rgba(255,255,255,0.2); border: 1px solid rgba(255,255,255,0.3);");
        out.println("color:white; text-decoration:none; font-weight:600;}");
        out.println(".back-btn:hover { background: rgba(255,255,255,0.35); }");
        
        out.println("header { background: rgba(15, 23, 42, 0.95); backdrop-filter: blur(10px); -webkit-backdrop-filter: blur(10px); color: white; padding: 24px; text-align: center; font-size: 1.8rem; font-weight: 700; border-bottom: 1px solid rgba(255, 255, 255, 0.1); box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2); display: flex; justify-content: space-between; align-items: center; }");
        out.println(".logout-btn { background: rgba(239, 68, 68, 0.2); border: 1px solid rgba(239, 68, 68, 0.3); color: white; padding: 8px 16px; border-radius: 8px; text-decoration: none; font-size: 1rem; transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1); }");
        out.println(".logout-btn:hover { background: rgba(239, 68, 68, 0.8); }");
        
        out.println("</style></head><body>");
        
        out.println("<header>");
        out.println("<div>🧾 Payment History</div>");
        out.println("<a href='logout' class='logout-btn'>Logout</a>");
        out.println("</header>");

        out.println("<div class='container'>");
        
        out.println("<div style='display: flex; justify-content: flex-end; align-items: center; margin-bottom: 20px;'>");
        out.println("<a href='customer-dashboard.html' class='back-btn' style='margin-top:0;'>⬅ Back to Dashboard</a>");
        out.println("</div>");
        
        out.println("<div class='table-box'>");
        out.println("<table>");
        out.println("<tr><th>Payment ID</th><th>Policy No</th><th>Amount (₹)</th><th>Date & Time</th></tr>");

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT id, policy_no, amount, payment_date FROM payments WHERE customer_id=? ORDER BY payment_date DESC"
            );
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            boolean hasRecords = false;
            while (rs.next()) {
                hasRecords = true;
                out.println("<tr>");
                out.println("<td>txn_" + rs.getInt("id") + "</td>");
                out.println("<td>" + rs.getInt("policy_no") + "</td>");
                out.println("<td style='color:lightgreen; font-weight:bold;'>₹" + rs.getBigDecimal("amount") + "</td>");
                out.println("<td>" + rs.getTimestamp("payment_date") + "</td>");
                out.println("</tr>");
            }

            if(!hasRecords) {
                out.println("<tr><td colspan='4'>You have no successful payments on record.</td></tr>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<tr><td colspan='4'>Error retrieving payments.</td></tr>");
        }

        out.println("</table>");
        out.println("</div>");
        out.println("</div>");
        out.println("</body></html>");
    }
}
