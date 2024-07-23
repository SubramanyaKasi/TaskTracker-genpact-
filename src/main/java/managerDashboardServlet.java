import java.io.IOException;
import java.util.List;

import DAO.TaskDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Task;

@WebServlet("/managerDashboardServlet")
public class managerDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TaskDAO taskDAO;

    public managerDashboardServlet() {
        this.taskDAO = new TaskDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Task> pendingTasks = taskDAO.getPendingTasks();
        request.setAttribute("pendingTasks", pendingTasks);
        request.getRequestDispatcher("managerDashboard.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int taskId = Integer.parseInt(request.getParameter("taskId"));
        String action = request.getParameter("action");

        if ("approve".equals(action)) {
            taskDAO.updateTaskStatus(taskId, "Approved");
        } else if ("reject".equals(action)) {
            taskDAO.updateTaskStatus(taskId, "Rejected");
        }

        response.sendRedirect("ManagerDashboardServlet");
    }
}
