package com.sportsvista.servlet;

import com.sportsvista.dao.UserDAO;
import com.sportsvista.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");

        if (userDAO.getUserByUsername(username) != null) {
            response.sendRedirect("login.jsp?error=Username already exists");
            return;
        }

        if (userDAO.getUserByEmail(email) != null) {
            response.sendRedirect("login.jsp?error=Email already exists");
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password); // TODO: Hash password before saving
        user.setFullName(fullName);

        if (userDAO.createUser(user)) {
            User authenticatedUser = userDAO.authenticate(username, password);
            HttpSession session = request.getSession();
            session.setAttribute("user", authenticatedUser);
            response.sendRedirect("dashboard");
        } else {
            response.sendRedirect("login.jsp?error=Registration failed");
        }
    }
}
