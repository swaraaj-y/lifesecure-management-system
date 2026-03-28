package com.lifesecure.servlet;

import com.lifesecure.util.DBConnection;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AgentLoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM agents WHERE email=? AND password=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, email);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                HttpSession session = request.getSession();
                session.setAttribute("agentId", rs.getInt("id"));
                session.setAttribute("agentName", rs.getString("name"));
                response.sendRedirect("agentDashboard");
            } else {
                response.sendRedirect("agent-login.html?error=invalid");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
