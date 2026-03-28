package com.lifesecure.servlet;

import com.lifesecure.util.DBConnection;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class CustomerRegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String dob = request.getParameter("dob");
        String password = request.getParameter("password");

        try (Connection con = DBConnection.getConnection()) {

            String sql = "INSERT INTO customers(name, phone, email, address, dob, password) VALUES(?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setString(4, address);
            ps.setDate(5, Date.valueOf(dob));
            ps.setString(6, password);

            ps.executeUpdate();

            // Get customer id
            PreparedStatement ps2 = con.prepareStatement(
                    "SELECT id FROM customers WHERE email=?"
            );
            ps2.setString(1, email);

            ResultSet rs = ps2.executeQuery();

            if (rs.next()) {

                HttpSession session = request.getSession(true);

                session.setAttribute("customer_id", rs.getInt("id"));
                session.setAttribute("customer_name", name);

                response.sendRedirect("customer-dashboard.html");
            } else {
                response.sendRedirect("customer-register.html?error=1");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("customer-register.html?error=1");
        }
    }
}
