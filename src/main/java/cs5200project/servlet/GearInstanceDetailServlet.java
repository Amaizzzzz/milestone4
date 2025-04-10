package cs5200project.servlet;

import cs5200project.dal.*;
import cs5200project.model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/gear-instance-detail")
public class GearInstanceDetailServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect("gear-instances");
            return;
        }
        
        try {
            int instanceId = Integer.parseInt(idStr);
            try (Connection connection = ConnectionManager.getConnection()) {
                GearInstance gearInstance = GearInstanceDao.getGearInstanceById(connection, instanceId);
                if (gearInstance == null) {
                    resp.sendRedirect("gear-instances");
                    return;
                }
                
                req.setAttribute("gearInstance", gearInstance);
                req.getRequestDispatcher("/gear-instance-detail.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect("gear-instances");
        } catch (SQLException e) {
            throw new ServletException("Error retrieving gear instance details", e);
        }
    }
} 