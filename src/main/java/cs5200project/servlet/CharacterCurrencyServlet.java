package cs5200project.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import cs5200project.dal.ConnectionManager;
import cs5200project.dal.CharacterCurrencyDao;
import cs5200project.dal.CharacterDao;
import cs5200project.model.GameCharacter;
import cs5200project.model.CharacterCurrency;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/character-currency")
public class CharacterCurrencyServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        try (Connection connection = ConnectionManager.getConnection()) {
            int characterId = Integer.parseInt(req.getParameter("characterId"));
            
            // Get character details
            GameCharacter character = CharacterDao.getCharacterById(connection, characterId);
            req.setAttribute("character", character);
            
            // Get character's currencies
            List<CharacterCurrency> characterCurrencies = CharacterCurrencyDao.getCharacterCurrencies(connection, characterId);
            req.setAttribute("characterCurrencies", characterCurrencies);
            
            // Forward to JSP
            req.getRequestDispatcher("/character-currency.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error retrieving character currencies", e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        String action = req.getParameter("action");
        int characterId = Integer.parseInt(req.getParameter("characterId"));
        
        try (Connection connection = ConnectionManager.getConnection()) {
            if ("update".equals(action)) {
                int currencyId = Integer.parseInt(req.getParameter("currencyId"));
                int newAmount = Integer.parseInt(req.getParameter("amount"));
                CharacterCurrencyDao.updateCurrencyAmount(connection, currencyId, characterId, newAmount);
            }
            
            // Redirect back to currency page
            resp.sendRedirect("character-currency?characterId=" + characterId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error updating character currency", e);
        }
    }
} 