package cs5200project.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import cs5200project.dal.CharacterDao;
import cs5200project.dal.ConnectionManager;
import cs5200project.model.GameCharacter;

@WebServlet("/characters")
public class CharacterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        String searchName = req.getParameter("searchName");
        
        try (Connection conn = ConnectionManager.getConnection()) {
            List<GameCharacter> characters = new ArrayList<>();
            if (searchName != null && !searchName.trim().isEmpty()) {
                // TODO: Implement search by name functionality
                // For now, return all characters
                characters = CharacterDao.getAllCharacters(conn);
            } else {
                characters = CharacterDao.getAllCharacters(conn);
            }
            req.setAttribute("characters", characters);
            req.getRequestDispatcher("/character-list.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
    
    private List<GameCharacter> getAllCharacters(Connection conn) throws SQLException {
        return CharacterDao.getAllCharacters(conn);
    }
} 