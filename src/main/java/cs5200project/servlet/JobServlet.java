package cs5200project.servlet;

import cs5200project.dal.ConnectionManager;
import cs5200project.dal.JobDao;
import cs5200project.model.Job;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/jobs")
public class JobServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        try (Connection connection = ConnectionManager.getConnection()) {
            List<Job> jobs = JobDao.getInstance().getAllJobs(connection);
            req.setAttribute("jobs", jobs);
            req.getRequestDispatcher("/job-list.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error retrieving jobs", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        String jobName = req.getParameter("jobName");
        String description = req.getParameter("description");
        int baseHP = Integer.parseInt(req.getParameter("baseHP"));
        int baseMP = Integer.parseInt(req.getParameter("baseMP"));

        try (Connection connection = ConnectionManager.getConnection()) {
            Job job = JobDao.getInstance().create(connection, jobName, description, baseHP, baseMP);
            resp.sendRedirect("job?id=" + job.getJobID());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error creating job", e);
        }
    }
} 