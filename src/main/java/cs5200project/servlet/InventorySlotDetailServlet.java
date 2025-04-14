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
    private final CharacterDao characterDao = CharacterDao.getInstance();
    private final InventorySlotDao inventorySlotDao = InventorySlotDao.getInstance();
    private final ItemDao itemDao = ItemDao.getInstance();
    
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
                GameCharacter character = characterDao.getCharacterById(connection, characterId);
                if (character == null) {
                    resp.sendRedirect("inventory-slots");
                    return;
                }
                
                InventorySlot inventorySlot = inventorySlotDao.getInventorySlotByCharacterIdAndSlotNumber(
                    connection, characterId, slotNumber);
                    
                if (inventorySlot == null) {
                    resp.sendRedirect("inventory-slots");
                    return;
                }
                
                Item item = itemDao.getItemById(connection, inventorySlot.getItemId());
                
                req.setAttribute("inventorySlot", inventorySlot);
                req.setAttribute("character", character);
                req.setAttribute("item", item);
                
                req.getRequestDispatcher("/inventory-slot-detail.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect("inventory-slots");
        } catch (SQLException e) {
            throw new ServletException("Error retrieving inventory slot details", e);
        }
    }
} 