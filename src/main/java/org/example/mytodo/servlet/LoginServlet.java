package org.example.mytodo.servlet;

import org.example.mytodo.manager.UserManager;
import org.example.mytodo.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    private UserManager userManager = new UserManager();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        User userByEmail = userManager.getByEmail(email);
        if (userByEmail != null && userByEmail.getPassword().equals(password)) {
            req.getSession().setAttribute("user", userByEmail);
            resp.sendRedirect("/addToDo");
        } else {
            req.getSession().setAttribute("msg", "Invalid email or password!");
            resp.sendRedirect("/login");
        }
    }
}
