package com.lifesecure.servlet;

import com.lifesecure.util.DBConnection;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class PaymentServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("customerId") == null) {
            response.sendRedirect("customer-login.html");
            return;
        }

        int customerId = (int) session.getAttribute("customerId");
        int policyNo = Integer.parseInt(request.getParameter("policyNo"));
        double amount = Double.parseDouble(request.getParameter("amount"));

        try (Connection conn = DBConnection.getConnection()) {
            // Insert Payment
            String sqlPay = "INSERT INTO payments(policy_no, customer_id, amount) VALUES(?,?,?)";
            PreparedStatement psPay = conn.prepareStatement(sqlPay);
            psPay.setInt(1, policyNo);
            psPay.setInt(2, customerId);
            psPay.setDouble(3, amount);
            psPay.executeUpdate();

            // Update Policy to Active
            String sqlUpdate = "UPDATE policies SET status='Active' WHERE policy_no=?";
            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
            psUpdate.setInt(1, policyNo);
            psUpdate.executeUpdate();

        } catch (Exception e) { e.printStackTrace(); }

        // Confirmation page
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Payment Success | LifeSecure</title>");
        out.println("<link href='https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap' rel='stylesheet'>");
        out.println("<style>");
        out.println(":root { --primary: #0f172a; --secondary: #10b981; --white: #ffffff; --card-bg: rgba(255, 255, 255, 0.15); --transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1); }");
        out.println("* { margin: 0; padding: 0; box-sizing: border-box; }");
        out.println("body { font-family: 'Inter', sans-serif; min-height: 100vh; display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg, rgba(15, 23, 42, 0.85) 0%, rgba(30, 58, 138, 0.85) 100%), url('images/cssbgtopsection.jpeg') center/cover no-repeat fixed; color: var(--white); -webkit-font-smoothing: antialiased; }");
        out.println(".receipt-card { width: 100%; max-width: 450px; padding: 40px; background: var(--card-bg); backdrop-filter: blur(20px); -webkit-backdrop-filter: blur(20px); border-radius: 24px; border: 1px solid rgba(255, 255, 255, 0.2); box-shadow: 0 20px 50px rgba(0, 0, 0, 0.3); text-align: center; animation: slideUp 0.6s ease-out forwards; opacity: 0; transform: translateY(20px); }");
        out.println("@keyframes slideUp { to { opacity: 1; transform: translateY(0); } }");
        out.println(".icon { font-size: 64px; color: var(--secondary); margin-bottom: 20px; filter: drop-shadow(0 0 15px rgba(16, 185, 129, 0.4)); animation: scaleIn 0.5s cubic-bezier(0.175, 0.885, 0.32, 1.275) 0.3s forwards; transform: scale(0); }");
        out.println("@keyframes scaleIn { to { transform: scale(1); } }");
        out.println("h2 { font-size: 1.8rem; font-weight: 700; margin-bottom: 10px; color: #34d399; }");
        out.println("p { font-size: 1.1rem; color: #e2e8f0; margin-bottom: 30px; line-height: 1.5; }");
        out.println(".details { text-align: left; background: rgba(0,0,0,0.2); padding: 20px; border-radius: 16px; margin-bottom: 30px; }");
        out.println(".details-row { display: flex; justify-content: space-between; margin-bottom: 10px; font-size: 1rem; }");
        out.println(".details-row:last-child { margin-bottom: 0; padding-top: 10px; border-top: 1px dashed rgba(255,255,255,0.2); font-weight: 700; font-size: 1.2rem; color: #34d399;}");
        out.println(".btn { display: inline-block; width: 100%; padding: 14px; background: #3b82f6; color: white; text-decoration: none; border-radius: 12px; font-weight: 600; font-size: 1.1rem; transition: var(--transition); border: none; cursor: pointer; }");
        out.println(".btn:hover { background: #2563eb; transform: translateY(-2px); box-shadow: 0 8px 15px rgba(59, 130, 246, 0.4); }");
        out.println("</style></head><body>");
        
        out.println("<div class='receipt-card'>");
        out.println("<div class='icon'>✅</div>");
        out.println("<h2>Payment Successful!</h2>");
        out.println("<p>Your transaction has been processed and your policy is now <strong>Active</strong>.</p>");
        
        out.println("<div class='details'>");
        out.println("<div class='details-row'><span>Policy No:</span> <span>" + policyNo + "</span></div>");
        out.println("<div class='details-row'><span>Customer ID:</span> <span>" + customerId + "</span></div>");
        out.println("<div class='details-row'><span>Amount Paid:</span> <span>₹" + amount + "</span></div>");
        out.println("</div>");
        
        out.println("<a href='customer-dashboard.html' class='btn'>Go to Dashboard</a>");
        out.println("</div>");
        
        out.println("</body></html>");
    }
}
