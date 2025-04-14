package cs5200project.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import cs5200project.dal.JobDao;
import cs5200project.model.Job;
import cs5200project.util.ConnectionManager;

@WebServlet("/job-detail/*")
public class JobDetailServlet extends HttpServlet {
    private final JobDao jobDao = JobDao.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            int jobId = Integer.parseInt(pathInfo.substring(1));
            try (Connection connection = ConnectionManager.getConnection()) {
                Job job = jobDao.getJobById(connection, jobId);
                if (job == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                request.setAttribute("job", job);
                request.getRequestDispatcher("/job-detail.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException e) {
            throw new ServletException("Error accessing the database", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            int jobId = Integer.parseInt(request.getParameter("jobId"));
            try (Connection connection = ConnectionManager.getConnection()) {
                if ("delete".equals(action)) {
                    Job job = jobDao.getJobById(connection, jobId);
                    if (job != null) {
                        jobDao.delete(connection, job);
                    }
                    response.sendRedirect(request.getContextPath() + "/jobs");
                } else if ("update".equals(action)) {
                    String jobName = request.getParameter("jobName");
                    String description = request.getParameter("description");
                    int minLevel = Integer.parseInt(request.getParameter("minLevel"));
                    int maxLevel = Integer.parseInt(request.getParameter("maxLevel"));
                    
                    jobDao.update(connection, jobId, jobName, description, minLevel, maxLevel);
                    response.sendRedirect(request.getContextPath() + "/job-detail/" + jobId);
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException e) {
            throw new ServletException("Error accessing the database", e);
        }
    }
} 