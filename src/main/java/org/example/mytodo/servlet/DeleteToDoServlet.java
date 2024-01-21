package org.example.mytodo.servlet;

import org.example.mytodo.manager.ToDoManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/deleteToDo")
public class DeleteToDoServlet extends HttpServlet {

    private ToDoManager todoManager = new ToDoManager();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        todoManager.delete(id);
        resp.sendRedirect("/todos");
    }
}
