package cs5200project.servlet;

import cs5200project.dal.*;
import cs5200project.model.*;
import cs5200project.util.ConnectionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/character-jobs/*")
public class CharacterJobsServlet extends HttpServlet {
    private final GameCharacterDao characterDao = GameCharacterDao.getInstance();
    private final CharacterJobDao characterJobDao = CharacterJobDao.getInstance();
    private final JobDao jobDao = JobDao.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try (Connection connection = ConnectionManager.getConnection()) {
            int characterId = Integer.parseInt(request.getParameter("characterId"));
            
            // Get the character
            GameCharacter character = characterDao.getCharacterById(connection, characterId);
            if (character == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Character not found");
                return;
            }
            request.setAttribute("character", character);

            // Get filter parameters
            String jobNameFilter = request.getParameter("jobNameFilter");
            String minXpStr = request.getParameter("minXp");
            String maxXpStr = request.getParameter("maxXp");

            // Get character jobs
            List<CharacterJob> characterJobs = characterJobDao.getCharacterJobs(connection, characterId);

            // Apply filters
            if (jobNameFilter != null && !jobNameFilter.trim().isEmpty()) {
                characterJobs.removeIf(job -> !job.getJobName().toLowerCase()
                    .contains(jobNameFilter.toLowerCase()));
            }
            
            if (minXpStr != null && !minXpStr.trim().isEmpty()) {
                int minXp = Integer.parseInt(minXpStr);
                characterJobs.removeIf(job -> job.getXp() < minXp);
            }
            
            if (maxXpStr != null && !maxXpStr.trim().isEmpty()) {
                int maxXp = Integer.parseInt(maxXpStr);
                characterJobs.removeIf(job -> job.getXp() > maxXp);
            }

            request.setAttribute("characterJobs", characterJobs);
            request.getRequestDispatcher("/character-jobs.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error accessing the database", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        try (Connection connection = ConnectionManager.getConnection()) {
            int characterId = Integer.parseInt(request.getParameter("characterId"));
            int jobId = Integer.parseInt(request.getParameter("jobId"));

            if ("/update-xp".equals(action)) {
                int xp = Integer.parseInt(request.getParameter("xp"));
                characterJobDao.updateXP(connection, characterId, jobId, xp);
            } else if ("/set-current".equals(action)) {
                characterJobDao.setCurrentJob(connection, characterId, jobId);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/character-jobs?characterId=" + characterId);
        } catch (SQLException e) {
            throw new ServletException("Error accessing the database", e);
        }
    }
} 