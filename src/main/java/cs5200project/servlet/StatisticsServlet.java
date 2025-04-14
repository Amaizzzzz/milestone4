package cs5200project.servlet;

import cs5200project.dal.CharacterStatsDao;
import cs5200project.model.CharacterStats;
import cs5200project.util.ConnectionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/characterstats")
public class StatisticsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> messages = new HashMap<>();
        req.setAttribute("messages", messages);

        String characterIdStr = req.getParameter("characterId");
        if (characterIdStr == null || characterIdStr.trim().isEmpty()) {
            messages.put("error", "Please provide a character ID.");
            req.getRequestDispatcher("/FindCharacter.jsp").forward(req, resp);
            return;
        }

        try (Connection connection = ConnectionManager.getConnection()) {
            int characterId = Integer.parseInt(characterIdStr);
            List<CharacterStats> stats = CharacterStatsDao.getCharacterStatsByCharacterId(connection, characterId);
            req.setAttribute("characterStats", stats);
            req.setAttribute("characterId", characterId);
            messages.put("success", "Successfully retrieved character statistics.");
        } catch (SQLException e) {
            messages.put("error", "Error retrieving character statistics: " + e.getMessage());
            e.printStackTrace();
        }

        req.getRequestDispatcher("/character-stats.jsp").forward(req, resp);
    }
} 