package cs5200project.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import cs5200project.dal.ConnectionManager;
import cs5200project.dal.Character_JobDao;
import cs5200project.dal.CharacterDao;
import cs5200project.model.GameCharacter;
import cs5200project.model.Character_Job;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/character-jobs")
public class CharacterJobServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        try (Connection connection = ConnectionManager.getConnection()) {
            int characterId = Integer.parseInt(req.getParameter("characterId"));
            
            // Get character details
            GameCharacter character = CharacterDao.getCharacterById(connection, characterId);
            req.setAttribute("character", character);
            
            // Get character's jobs
            List<Character_Job> characterJobs = Character_JobDao.getCharacterJobs(connection, characterId);
            req.setAttribute("characterJobs", characterJobs);
            
            // Forward to JSP
            req.getRequestDispatcher("/character-jobs.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error retrieving character jobs", e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        String action = req.getParameter("action");
        int characterId = Integer.parseInt(req.getParameter("characterId"));
        
        try (Connection connection = ConnectionManager.getConnection()) {
            switch (action) {
                case "updateXP":
                    int jobId = Integer.parseInt(req.getParameter("jobId"));
                    int newXP = Integer.parseInt(req.getParameter("xp"));
                    Character_JobDao.updateXP(connection, characterId, jobId, newXP);
                    break;
                    
                case "unlock":
                    jobId = Integer.parseInt(req.getParameter("jobId"));
                    Character_JobDao.unlockJob(connection, characterId, jobId);
                    break;
            }
            
            // Redirect back to jobs page
            resp.sendRedirect("character-jobs?characterId=" + characterId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error updating character job", e);
        }
    }
} 