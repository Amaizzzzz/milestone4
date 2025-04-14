package cs5200project.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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
    private static final long serialVersionUID = 1L;
    private final CharacterDao characterDao = CharacterDao.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String nameSearch = request.getParameter("nameSearch");
        String raceIdStr = request.getParameter("raceId");
        String sortBy = request.getParameter("sortBy");
        
        try (Connection connection = ConnectionManager.getConnection()) {
            List<GameCharacter> characters;
            if (nameSearch != null || raceIdStr != null || sortBy != null) {
                Integer raceId = raceIdStr != null ? Integer.parseInt(raceIdStr) : null;
                characters = characterDao.getFilteredCharacters(connection, nameSearch, raceId, sortBy);
            } else {
                characters = characterDao.getAllCharacters(connection);
            }
            request.setAttribute("characters", characters);
            request.getRequestDispatcher("/character.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error retrieving characters", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Handle character creation if needed
        response.sendRedirect(request.getContextPath() + "/characters");
    }
} 