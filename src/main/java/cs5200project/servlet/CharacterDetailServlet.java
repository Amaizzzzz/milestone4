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

import cs5200project.dal.CharacterDao;
import cs5200project.dal.CharacterStatsDao;
import cs5200project.dal.CharacterJobDao;
import cs5200project.dal.ConnectionManager;
import cs5200project.dal.GearInstanceDao;
import cs5200project.model.Character;
import cs5200project.model.CharacterStats;
import cs5200project.model.CharacterJob;
import cs5200project.model.GearInstance;

@WebServlet("/character")
public class CharacterDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Character ID is required");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            try (Connection conn = ConnectionManager.getConnection()) {
                Character character = CharacterDao.getCharacterById(conn, id);
                if (character == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Character not found");
                    return;
                }
                
                List<CharacterStats> stats = CharacterStatsDao.getStatsByCharacterId(conn, id);
                List<CharacterJob> jobs = CharacterJobDao.getJobsByCharacterId(conn, id);
                List<GearInstance> gear = GearInstanceDao.getGearByCharacterId(conn, id);
                
                req.setAttribute("character", character);
                req.setAttribute("stats", stats);
                req.setAttribute("jobs", jobs);
                req.setAttribute("gear", gear);
                req.getRequestDispatcher("/character-detail.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid character ID format");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
} 