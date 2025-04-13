package cs5200project.servlet;

import cs5200project.dal.*;
import cs5200project.model.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/gear-slot-detail")
public class GearSlotDetailServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect("gear-slots");
            return;
        }
        
        try {
            int slotId = Integer.parseInt(idStr);
            try (Connection connection = ConnectionManager.getConnection()) {
                GearSlot gearSlot = GearSlotDao.getGearSlotById(connection, slotId);
                if (gearSlot == null) {
                    resp.sendRedirect("gear-slots");
                    return;
                }
                
                req.setAttribute("gearSlot", gearSlot);
                req.getRequestDispatcher("/gear-slot-detail.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect("gear-slots");
        } catch (SQLException e) {
            throw new ServletException("Error retrieving gear slot details", e);
        }
    }
} 