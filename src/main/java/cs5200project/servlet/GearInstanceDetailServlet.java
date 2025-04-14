package cs5200project.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import cs5200project.dal.ConnectionManager;
import cs5200project.dal.GearInstanceDao;
import cs5200project.model.GearInstance;

@WebServlet("/gear-instance")
public class GearInstanceDetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final GearInstanceDao gearInstanceDao = GearInstanceDao.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String gearInstanceIdStr = request.getParameter("id");
        
        if (gearInstanceIdStr == null || gearInstanceIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/gear-instances");
            return;
        }
        
        try (Connection connection = ConnectionManager.getConnection()) {
            int gearInstanceId = Integer.parseInt(gearInstanceIdStr);
            GearInstance gearInstance = gearInstanceDao.getGearInstanceById(connection, gearInstanceId);
            
            if (gearInstance == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Gear instance not found");
                return;
            }
            
            request.setAttribute("gearInstance", gearInstance);
            request.getRequestDispatcher("/gear-instance-detail.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error retrieving gear instance details", e);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid gear instance ID format");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Handle post requests if needed
        response.sendRedirect(request.getContextPath() + "/gear-instances");
    }
} 