package com.lifesecure.servlet;

import com.lifesecure.util.DBConnection;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class ViewProfileServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("customer-login.html");
            return;
        }

        int customerId = (int) session.getAttribute("customerId");

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>My Profile | LifeSecure</title>");
        out.println("<link href='https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap' rel='stylesheet'>");
        out.println("<style>");
        out.println(":root { --primary: #0f172a; --secondary: #3b82f6; --white: #ffffff; --card-bg: rgba(255, 255, 255, 0.15); --transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1); }");
        out.println("* { margin: 0; padding: 0; box-sizing: border-box; }");
        out.println("body { font-family: 'Inter', sans-serif; min-height: 100vh; background: linear-gradient(135deg, rgba(15, 23, 42, 0.85) 0%, rgba(30, 58, 138, 0.85) 100%), url('images/cssbgtopsection.jpeg') center/cover no-repeat fixed; color: var(--white); -webkit-font-smoothing: antialiased; }");
        out.println(".container { max-width: 600px; margin: 60px auto; padding: 40px; background: var(--card-bg); backdrop-filter: blur(15px); -webkit-backdrop-filter: blur(15px); border-radius: 24px; border: 1px solid rgba(255, 255, 255, 0.2); box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2); text-align: center; }");
        out.println("h2 { font-size: 2.2rem; font-weight: 700; margin-bottom: 30px; letter-spacing: -0.5px; }");
        out.println(".profile-info { text-align: left; margin-bottom: 30px; font-size: 1.1rem; }");
        out.println(".profile-info p { margin-bottom: 15px; padding: 15px; background: rgba(255,255,255,0.05); border-radius: 12px; border: 1px solid rgba(255,255,255,0.1); }");
        out.println(".profile-info strong { color: #93c5fd; display: inline-block; width: 100px; }");
        out.println(".btn { display: inline-block; padding: 12px 24px; background: var(--secondary); color: white; text-decoration: none; border-radius: 12px; font-weight: 600; transition: var(--transition); border: none; cursor: pointer; margin: 10px; }");
        out.println(".btn:hover { background: #2563eb; transform: translateY(-2px); box-shadow: 0 8px 15px rgba(59, 130, 246, 0.4); }");
        out.println(".btn-outline { background: transparent; border: 1px solid rgba(255,255,255,0.3); }");
        out.println(".btn-outline:hover { background: rgba(255,255,255,0.1); border-color: white; transform: translateY(-2px); box-shadow: 0 8px 15px rgba(255,255,255,0.1); }");
        
        out.println("header { background: rgba(15, 23, 42, 0.95); backdrop-filter: blur(10px); -webkit-backdrop-filter: blur(10px); color: var(--white); padding: 24px; text-align: center; font-size: 1.8rem; font-weight: 700; border-bottom: 1px solid rgba(255, 255, 255, 0.1); box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2); display: flex; justify-content: space-between; align-items: center; }");
        out.println(".logout-btn { background: rgba(239, 68, 68, 0.2); border: 1px solid rgba(239, 68, 68, 0.3); color: white; padding: 8px 16px; border-radius: 8px; text-decoration: none; font-size: 1rem; transition: var(--transition); }");
        out.println(".logout-btn:hover { background: rgba(239, 68, 68, 0.8); }");
        
        out.println("</style></head><body>");
        
        out.println("<header>");
        out.println("<div>👤 My Profile</div>");
        out.println("<a href='logout' class='logout-btn'>Logout</a>");
        out.println("</header>");

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM customers WHERE id=?"
            );

            ps.setInt(1, customerId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                out.println("<div class='container' style='max-width: 800px; margin-top: 50px;'>");
                
                out.println("<div style='display: flex; flex-wrap: wrap; gap: 30px; text-align: left;'>");
                
                out.println("<div style='flex: 1; min-width: 300px;'>");
                out.println("<h3 style='margin-bottom:15px; color:#93c5fd;'>Personal Details</h3>");
                out.println("<div class='profile-info'>");
                out.println("<p><strong>Name:</strong> " + rs.getString("name") + "</p>");
                out.println("<p><strong>Email:</strong> " + rs.getString("email") + "</p>");
                out.println("<p><strong>Phone:</strong> " + rs.getString("phone") + "</p>");
                out.println("<p><strong>Address:</strong> " + rs.getString("address") + "</p>");
                out.println("</div>");
                out.println("<a href='editProfile' class='btn'>✏️ Edit Profile</a>");
                out.println("</div>");

                out.println("<div style='flex: 1; min-width: 300px;'>");
                out.println("<h3 style='margin-bottom:15px; color:#10b981;'>Active Policies</h3>");
                out.println("<div class='profile-info'>");
                
                PreparedStatement psPol = con.prepareStatement("SELECT policy_no, policy_type FROM policies WHERE customer_id=? AND status='Active'");
                psPol.setInt(1, customerId);
                ResultSet rsPol = psPol.executeQuery();
                
                boolean hasPolicies = false;
                while (rsPol.next()) {
                    hasPolicies = true;
                    out.println("<p style='border-left: 4px solid #10b981; padding-left: 15px;'>");
                    out.println("<strong style='color:white; width: auto;'>Policy #" + rsPol.getInt("policy_no") + "</strong><br>");
                    out.println("<span style='color: #cbd5e1;'>" + rsPol.getString("policy_type") + "</span>");
                    out.println("</p>");
                }
                
                if (!hasPolicies) {
                    out.println("<p>No active policies found.</p>");
                }
                
                out.println("</div>");
                out.println("</div>");
                
                out.println("</div>");
                
                out.println("<div style='margin-top: 30px;'><a href='customer-dashboard.html' class='btn btn-outline'>⬅ Dashboard</a></div>");
                out.println("</div>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<div class='container'><p>Error loading profile.</p></div>");
        }
        
        out.println("</body></html>");
    }
}
