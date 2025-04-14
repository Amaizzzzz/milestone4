package cs5200project.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import cs5200project.dal.CharacterDao;
import cs5200project.dal.ConnectionManager;
import cs5200project.model.GameCharacter;

@WebServlet("/character")
public class CharacterDetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final CharacterDao characterDao = CharacterDao.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String characterIdStr = request.getParameter("id");
        
        if (characterIdStr == null || characterIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/characters");
            return;
        }
        
        try (Connection connection = ConnectionManager.getConnection()) {
            int characterId = Integer.parseInt(characterIdStr);
            GameCharacter character = characterDao.getCharacterById(connection, characterId);
            
            if (character == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Character not found");
                return;
            }
            
            request.setAttribute("character", character);
            request.getRequestDispatcher("/character-detail.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error retrieving character details", e);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid character ID format");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String characterIdStr = request.getParameter("id");
        
        if (characterIdStr == null || characterIdStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Character ID is required");
            return;
        }
        
        try (Connection connection = ConnectionManager.getConnection()) {
            int characterId = Integer.parseInt(characterIdStr);
            
            if ("delete".equals(action)) {
                characterDao.deleteCharacter(connection, characterId);
                response.sendRedirect(request.getContextPath() + "/characters");
            } else {
                // Handle other actions if needed
                response.sendRedirect(request.getContextPath() + "/character?id=" + characterId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error processing character action", e);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid character ID format");
        }
    }
} 