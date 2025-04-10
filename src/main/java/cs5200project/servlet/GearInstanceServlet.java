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

@WebServlet("/gear-instances")
public class GearInstanceServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = ConnectionManager.getConnection()) {
            List<GearInstance> gearInstances = GearInstanceDao.getAllGearInstances(connection);
            req.setAttribute("gearInstances", gearInstances);
            req.getRequestDispatcher("/gear-instance-list.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error retrieving gear instances", e);
        }
    }
} 