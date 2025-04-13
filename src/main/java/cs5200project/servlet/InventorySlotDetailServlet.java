package cs5200project.servlet;

import cs5200project.dal.*;
import cs5200project.model.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/inventory-slot-detail")
public class InventorySlotDetailServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String slotNumberStr = req.getParameter("slotNumber");
        String characterIdStr = req.getParameter("characterId");
        
        if (slotNumberStr == null || characterIdStr == null || 
            slotNumberStr.trim().isEmpty() || characterIdStr.trim().isEmpty()) {
            resp.sendRedirect("inventory-slots");
            return;
        }
        
        try {
            int slotNumber = Integer.parseInt(slotNumberStr);
            int characterId = Integer.parseInt(characterIdStr);
            
            try (Connection connection = ConnectionManager.getConnection()) {
                GameCharacter character = CharacterDao.getInstance().getCharacterById(connection, characterId);
                if (character == null) {
                    resp.sendRedirect("inventory-slots");
                    return;
                }
                
                InventorySlot inventorySlot = InventorySlotDao.getInventorySlotByCharacterIdAndSlotNumber(
                    connection, character, slotNumber);
                    
                if (inventorySlot == null) {
                    resp.sendRedirect("inventory-slots");
                    return;
                }
                
                req.setAttribute("inventorySlot", inventorySlot);
                req.setAttribute("character", character);
                req.setAttribute("item", inventorySlot.getItem());
                
                req.getRequestDispatcher("/inventory-slot-detail.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect("inventory-slots");
        } catch (SQLException e) {
            throw new ServletException("Error retrieving inventory slot details", e);
        }
    }
} 