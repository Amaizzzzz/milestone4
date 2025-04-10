package cs5200project.servlet;

import cs5200project.dal.ConnectionManager;
import cs5200project.dal.JobDao;
import cs5200project.dal.GameCharacterDao;
import cs5200project.dal.GearDao;
import cs5200project.model.Job;
import cs5200project.model.GameCharacter;
import cs5200project.model.Gear;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/job")
public class JobDetailServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        String jobIdStr = req.getParameter("id");
        if (jobIdStr == null || jobIdStr.trim().isEmpty()) {
            resp.sendRedirect("jobs");
            return;
        }

        try (Connection connection = ConnectionManager.getConnection()) {
            int jobId = Integer.parseInt(jobIdStr);
            Job job = JobDao.getInstance().getJobById(connection, jobId);
            if (job == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Job not found");
                return;
            }

            List<GameCharacter> characters = GameCharacterDao.getInstance()
                    .getCharactersByJob(connection, jobId);
            List<Gear> availableGear = GearDao.getInstance()
                    .getGearByJob(connection, jobId);

            req.setAttribute("job", job);
            req.setAttribute("characters", characters);
            req.setAttribute("availableGear", availableGear);
            req.getRequestDispatcher("/job-detail.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error retrieving job details", e);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid job ID format");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        String action = req.getParameter("action");
        String jobIdStr = req.getParameter("id");

        if (jobIdStr == null || jobIdStr.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Job ID is required");
            return;
        }

        try (Connection connection = ConnectionManager.getConnection()) {
            int jobId = Integer.parseInt(jobIdStr);

            if ("delete".equals(action)) {
                JobDao.getInstance().delete(connection, jobId);
                resp.sendRedirect("jobs");
            } else {
                // Handle update
                String jobName = req.getParameter("jobName");
                String description = req.getParameter("description");
                int baseHP = Integer.parseInt(req.getParameter("baseHP"));
                int baseMP = Integer.parseInt(req.getParameter("baseMP"));

                Job job = JobDao.getInstance().update(connection, jobId, jobName, 
                        description, baseHP, baseMP);
                resp.sendRedirect("job?id=" + job.getJobID());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error updating job", e);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number format");
        }
    }
} 