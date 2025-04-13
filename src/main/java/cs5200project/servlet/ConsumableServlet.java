package cs5200project.servlet;

import cs5200project.dal.ConnectionManager;
import cs5200project.dal.ConsumableDao;
import cs5200project.model.Consumable;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/consumables")
public class ConsumableServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        try (Connection connection = ConnectionManager.getConnection()) {
            List<Consumable> consumables = ConsumableDao.getInstance().getAllConsumables(connection);
            req.setAttribute("consumables", consumables);
            req.getRequestDispatcher("/consumable-list.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error retrieving consumables", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        String itemName = req.getParameter("itemName");
        int itemLevel = Integer.parseInt(req.getParameter("itemLevel"));
        int maxStackSize = Integer.parseInt(req.getParameter("maxStackSize"));
        double price = Double.parseDouble(req.getParameter("price"));
        int quantity = Integer.parseInt(req.getParameter("quantity"));
        int duration = Integer.parseInt(req.getParameter("duration"));
        int cooldown = Integer.parseInt(req.getParameter("cooldown"));

        try (Connection connection = ConnectionManager.getConnection()) {
            Consumable consumable = ConsumableDao.getInstance().create(connection, 0, itemName,
                    itemLevel, maxStackSize, price, quantity, duration, cooldown);
            resp.sendRedirect("consumable?id=" + consumable.getItemID());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error creating consumable", e);
        }
    }
} 