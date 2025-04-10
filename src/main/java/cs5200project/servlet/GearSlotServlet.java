package cs5200project.servlet;

import cs5200project.dal.*;
import cs5200project.model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/gear-slots")
public class GearSlotServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = ConnectionManager.getConnection()) {
            List<GearSlot> gearSlots = GearSlotDao.getAllGearSlots(connection);
            req.setAttribute("gearSlots", gearSlots);
            req.getRequestDispatcher("/gear-slot-list.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error retrieving gear slots", e);
        }
    }
} 