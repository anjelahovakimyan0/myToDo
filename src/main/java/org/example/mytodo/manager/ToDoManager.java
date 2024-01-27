package org.example.mytodo.manager;

import org.example.mytodo.db.DBConnectionProvider;
import org.example.mytodo.model.ToDo;
import org.example.mytodo.model.ToDoStatus;
import org.example.mytodo.util.DateUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ToDoManager {

    private Connection connection = DBConnectionProvider.getInstance().getConnection();
    private UserManager userManager = new UserManager();

    public void add(ToDo todo) {
        String sql = "INSERT INTO todo(title, created_date, finish_date, user_id, status) VALUES(?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, todo.getTitle());
            preparedStatement.setString(2, DateUtil.dateToString(todo.getCreatedDate()));
            preparedStatement.setString(3, DateUtil.dateToString(todo.getFinishDate()));
            preparedStatement.setInt(4, todo.getUser().getId());
            preparedStatement.setString(5, String.valueOf(todo.getStatus()));
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                todo.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ToDo getToDoById(int id) {
        String sql = "SELECT * FROM todo WHERE id =" + id;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return ToDo.builder()
                        .id(id)
                        .title(resultSet.getString("title"))
                        .createdDate(DateUtil.stringToDate(resultSet.getString("created_date")))
                        .finishDate(DateUtil.stringToDate(resultSet.getString("finish_date")))
                        .user(userManager.getUserById(resultSet.getInt("user_id")))
                        .status(ToDoStatus.valueOf(resultSet.getString("status")))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ToDo> getAllToDos(int id) {
        String sql = "SELECT * FROM todo WHERE user_id = " + id;
        List<ToDo> todos = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                todos.add(ToDo.builder()
                        .id(resultSet.getInt("id"))
                        .title(resultSet.getString("title"))
                        .createdDate(DateUtil.stringToDate(resultSet.getString("created_date")))
                        .finishDate(DateUtil.stringToDate(resultSet.getString("finish_date")))
                        .user(userManager.getUserById(resultSet.getInt("user_id")))
                        .status(ToDoStatus.valueOf(resultSet.getString("status")))
                        .build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todos;
    }

    public void update(ToDo todo) {
        String sql = "UPDATE todo SET title = ?, created_date = ?, finish_date = ?, user_id = ?, status = ? WHERE id = " + todo.getId();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, todo.getTitle());
            preparedStatement.setString(2, DateUtil.dateToString(todo.getCreatedDate()));
            preparedStatement.setString(3, DateUtil.dateToString(todo.getFinishDate()));
            preparedStatement.setInt(4, todo.getUser().getId());
            preparedStatement.setString(5, String.valueOf(todo.getStatus()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM todo WHERE id = " + id;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
