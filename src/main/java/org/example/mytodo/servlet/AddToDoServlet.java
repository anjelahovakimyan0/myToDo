package org.example.mytodo.servlet;

import org.example.mytodo.manager.ToDoManager;
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
import java.util.List;

@WebServlet(urlPatterns = "/addToDo")
public class AddToDoServlet extends HttpServlet {

    private ToDoManager todoManager = new ToDoManager();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        List<ToDo> todos = todoManager.getAllToDos(user.getId());
        req.setAttribute("todos", todos);
        req.getRequestDispatcher("/WEB-INF/addToDo.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        Date finishDate = DateUtil.stringToDate(req.getParameter("finishDate"));

        User user = (User) req.getSession().getAttribute("user");

        todoManager.add(ToDo.builder()
                .title(title)
                .createdDate(new Date())
                .finishDate(finishDate)
                .user(user)
                .status(ToDoStatus.NEW)
                .build());

        resp.sendRedirect("/todos");
    }
}
