package cs5200project.servlet;

import cs5200project.dal.ConnectionManager;
import cs5200project.dal.ConsumableDao;
import cs5200project.model.Consumable;
import cs5200project.model.Consumable.ConsumablesType;

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
    private final ConsumableDao consumableDao = ConsumableDao.getInstance();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        try (Connection connection = ConnectionManager.getConnection()) {
            List<Consumable> consumables = consumableDao.getAllConsumables(connection);
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
        ConsumablesType consumablesType = ConsumablesType.valueOf(req.getParameter("consumablesType"));
        String description = req.getParameter("description");
        String source = req.getParameter("source");

        try (Connection connection = ConnectionManager.getConnection()) {
            Consumable consumable = consumableDao.create(connection, itemName,
                    itemLevel, maxStackSize, price, quantity, consumablesType, description, source);
            resp.sendRedirect("consumable?id=" + consumable.getItemId());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error creating consumable", e);
        }
    }
} 