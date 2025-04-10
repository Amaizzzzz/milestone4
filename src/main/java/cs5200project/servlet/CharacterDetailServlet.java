package cs5200project.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs5200project.dal.*;
import cs5200project.model.*;

@WebServlet("/character")
public class CharacterDetailServlet extends HttpServlet {
    private CharacterDao characterDao;
    private CharacterStatsDao characterStatsDao;
    private CharacterJobDao characterJobDao;
    private GearInstanceDao gearInstanceDao;

    @Override
    public void init() throws ServletException {
        characterDao = CharacterDao.getInstance();
        characterStatsDao = CharacterStatsDao.getInstance();
        characterJobDao = CharacterJobDao.getInstance();
        gearInstanceDao = GearInstanceDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect("characters");
            return;
        }

        try {
            int characterId = Integer.parseInt(idStr);
            try (Connection conn = ConnectionManager.getConnection()) {
                // Get character details
                GameCharacter character = characterDao.getCharacterById(conn, characterId);
                if (character == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Character not found");
                    return;
                }

                // Get character stats
                List<CharacterStats> stats = characterStatsDao.getByCharacterID(conn, character);

                // Get character jobs
                List<CharacterJob> jobs = characterJobDao.getByCharacterID(conn, character);

                // Get character equipment
                List<GearInstance> equipment = gearInstanceDao.getByCharacterID(conn, character);

                // Set attributes for JSP
                req.setAttribute("character", character);
                req.setAttribute("characterStats", stats);
                req.setAttribute("characterJobs", jobs);
                req.setAttribute("equipment", equipment);

                // Forward to JSP
                req.getRequestDispatcher("/character-detail.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid character ID");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
} 