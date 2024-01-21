package org.example.mytodo.servlet;

import org.example.mytodo.manager.ToDoManager;
import org.example.mytodo.manager.UserManager;
import org.example.mytodo.model.ToDo;
import org.example.mytodo.model.ToDoStatus;
import org.example.mytodo.model.User;
import org.example.mytodo.util.DateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebServlet(urlPatterns = "/editToDo")
public class EditToDoServlet extends HttpServlet {

    private ToDoManager todoManager = new ToDoManager();
    private UserManager userManager = new UserManager();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        req.setAttribute("todo", todoManager.getToDoById(id));
        req.getRequestDispatcher("/WEB-INF/editToDo.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String title = req.getParameter("title");
        Date createdDate = DateUtil.stringToDate(req.getParameter("createdDate"));
        Date finishDate = DateUtil.stringToDate(req.getParameter("finishDate"));
        int userId = Integer.parseInt(req.getParameter("userId"));
        ToDoStatus status = ToDoStatus.valueOf(req.getParameter("status"));

        User user = userManager.getUserById(userId);

        todoManager.update(ToDo.builder()
                .id(id)
                .title(title)
                .createdDate(createdDate)
                .finishDate(finishDate)
                .user(user)
                .status(status)
                .build());

        resp.sendRedirect("/todos");
    }
}
