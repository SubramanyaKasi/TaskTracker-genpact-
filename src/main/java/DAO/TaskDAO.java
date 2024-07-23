package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Task;

public class TaskDAO {
    private String jdbcURL = "jdbc:mysql://localhost:3306/timetracker";
    private String jdbcUsername = "root";
    private String jdbcPassword = "Kasi@9563";
    private static final String INSERT_TASK_SQL = "INSERT INTO tasks (title, description, start_time, end_time, approval_status) VALUES (?, ?, ?, ?, 'Pending');";
    private static final String UPDATE_TASK_STATUS_SQL = "UPDATE tasks SET approval_status = ? WHERE id = ?;";
    private static final String SELECT_PENDING_TASKS_SQL = "SELECT * FROM tasks WHERE approval_status = 'Pending';";

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void addTask(Task task) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TASK_SQL)) {
            preparedStatement.setString(1, task.getTitle());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setString(3, task.getStartTime());
            preparedStatement.setString(4, task.getEndTime());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTaskStatus(int taskId, String status) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TASK_STATUS_SQL)) {
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, taskId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Task> getPendingTasks() {
        List<Task> tasks = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PENDING_TASKS_SQL);
             ResultSet rs = preparedStatement.executeQuery()) {
             
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String startTime = rs.getString("start_time");
                String endTime = rs.getString("end_time");
                String approvalStatus = rs.getString("approval_status");
                
                // Assuming Task class has the following constructor
                Task task = new Task(id, title, description, startTime, endTime, approvalStatus);
                tasks.add(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks;
    }
}
