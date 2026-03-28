package com.lifesecure.servlet;

import com.lifesecure.util.DBConnection;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class EditProfileServlet extends HttpServlet {

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
        out.println("<title>Edit Profile | LifeSecure</title>");
        out.println("<link href='https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap' rel='stylesheet'>");
        out.println("<style>");
        out.println(":root { --primary: #0f172a; --secondary: #3b82f6; --white: #ffffff; --card-bg: rgba(255, 255, 255, 0.15); --transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1); }");
        out.println("* { margin: 0; padding: 0; box-sizing: border-box; }");
        out.println("body { font-family: 'Inter', sans-serif; min-height: 100vh; background: linear-gradient(135deg, rgba(15, 23, 42, 0.85) 0%, rgba(30, 58, 138, 0.85) 100%), url('images/cssbgtopsection.jpeg') center/cover no-repeat fixed; color: var(--white); -webkit-font-smoothing: antialiased; }");
        out.println(".container { max-width: 500px; margin: 60px auto; padding: 40px; background: var(--card-bg); backdrop-filter: blur(15px); -webkit-backdrop-filter: blur(15px); border-radius: 24px; border: 1px solid rgba(255, 255, 255, 0.2); box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2); text-align: center; }");
        out.println("h2 { font-size: 2.2rem; font-weight: 700; margin-bottom: 30px; letter-spacing: -0.5px; }");
        out.println(".form-group { text-align: left; margin-bottom: 20px; }");
        out.println("label { display: block; margin-bottom: 8px; font-weight: 500; color: #e2e8f0; }");
        out.println("input, textarea { width: 100%; padding: 12px 16px; background: rgba(15, 23, 42, 0.6); border: 1px solid rgba(255,255,255,0.2); border-radius: 12px; color: white; font-family: 'Inter', sans-serif; font-size: 1rem; transition: var(--transition); }");
        out.println("input:focus, textarea:focus { outline: none; border-color: var(--secondary); box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.3); }");
        out.println(".btn { display: inline-block; width: 100%; padding: 14px; background: var(--secondary); color: white; text-decoration: none; border-radius: 12px; font-weight: 600; font-size: 1.1rem; transition: var(--transition); border: none; cursor: pointer; margin-top: 10px; }");
        out.println(".btn:hover { background: #2563eb; transform: translateY(-2px); box-shadow: 0 8px 15px rgba(59, 130, 246, 0.4); }");
        out.println(".btn-cancel { display: inline-block; width: 100%; padding: 14px; background: transparent; color: white; text-decoration: none; border-radius: 12px; font-weight: 600; font-size: 1.1rem; transition: var(--transition); border: 1px solid rgba(255,255,255,0.3); text-align: center; margin-top: 15px; }");
        out.println(".btn-cancel:hover { background: rgba(255,255,255,0.1); border-color: white; }");
        
        out.println("header { background: rgba(15, 23, 42, 0.95); backdrop-filter: blur(10px); -webkit-backdrop-filter: blur(10px); color: white; padding: 24px; text-align: center; font-size: 1.8rem; font-weight: 700; border-bottom: 1px solid rgba(255, 255, 255, 0.1); box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2); display: flex; justify-content: space-between; align-items: center; }");
        out.println(".logout-btn { background: rgba(239, 68, 68, 0.2); border: 1px solid rgba(239, 68, 68, 0.3); color: white; padding: 8px 16px; border-radius: 8px; text-decoration: none; font-size: 1rem; transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1); }");
        out.println(".logout-btn:hover { background: rgba(239, 68, 68, 0.8); }");
        
        out.println("</style></head><body>");
        
        out.println("<header>");
        out.println("<div>✏️ Edit Profile</div>");
        out.println("<a href='logout' class='logout-btn'>Logout</a>");
        out.println("</header>");

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM customers WHERE id=?");
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                out.println("<div class='container'>");
                out.println("<form action='updateProfile' method='post'>");
                
                out.println("<div class='form-group'>");
                out.println("<label for='name'>Full Name</label>");
                out.println("<input type='text' id='name' name='name' value='" + rs.getString("name") + "' required>");
                out.println("</div>");

                out.println("<div class='form-group'>");
                out.println("<label for='email'>Email Address</label>");
                out.println("<input type='email' id='email' name='email' value='" + rs.getString("email") + "' required>");
                out.println("</div>");

                out.println("<div class='form-group'>");
                out.println("<label for='phone'>Phone Number</label>");
                out.println("<input type='text' id='phone' name='phone' value='" + rs.getString("phone") + "' required>");
                out.println("</div>");

                out.println("<div class='form-group'>");
                out.println("<label for='address'>Address</label>");
                out.println("<textarea id='address' name='address' rows='3' required>" + rs.getString("address") + "</textarea>");
                out.println("</div>");

                out.println("<button type='submit' class='btn'>Save Changes</button>");
                out.println("</form>");
                out.println("<a href='viewProfile' class='btn-cancel'>Cancel</a>");
                out.println("</div>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<div class='container'><p>Error loading profile data.</p></div>");
        }
        
        out.println("</body></html>");
    }
}
