package cs5200project.servlet;

import cs5200project.dal.ConnectionManager;
import cs5200project.dal.ConsumableDao;
import cs5200project.dal.ConsumablesStatsBonusDao;
import cs5200project.dal.StatTypeDao;
import cs5200project.dal.StatisticDao;
import cs5200project.model.Consumable;
import cs5200project.model.ConsumablesStatsBonus;
import cs5200project.model.Statistic;
import cs5200project.model.StatType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/consumable")
public class ConsumableDetailServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        String consumableIdStr = req.getParameter("id");
        if (consumableIdStr == null || consumableIdStr.trim().isEmpty()) {
            resp.sendRedirect("consumables");
            return;
        }

        try (Connection connection = ConnectionManager.getConnection()) {
            int consumableId = Integer.parseInt(consumableIdStr);
            Consumable consumable = ConsumableDao.getByConsumablesId(connection, consumableId);
            if (consumable == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Consumable not found");
                return;
            }

            // Get all stat types and their corresponding bonuses
            List<ConsumablesStatsBonus> statBonuses = new ArrayList<>();
            List<StatType> allStatTypes = StatTypeDao.getStatTypeByName(connection, "");
            for (StatType statType : allStatTypes) {
                Statistic stat = StatisticDao.getStatisticByID(connection, statType.getStatTypeID());
                if (stat != null) {
                    ConsumablesStatsBonus bonus = ConsumablesStatsBonusDao.getByItemIdAndStatId(connection, consumable, stat);
                    if (bonus != null) {
                        statBonuses.add(bonus);
                    }
                }
            }

            req.setAttribute("consumable", consumable);
            req.setAttribute("consumableStats", statBonuses);
            req.getRequestDispatcher("/consumable-detail.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error retrieving consumable details", e);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid consumable ID format");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        String action = req.getParameter("action");
        String consumableIdStr = req.getParameter("id");

        if (consumableIdStr == null || consumableIdStr.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Consumable ID is required");
            return;
        }

        try (Connection connection = ConnectionManager.getConnection()) {
            int consumableId = Integer.parseInt(consumableIdStr);

            if ("delete".equals(action)) {
                Consumable consumable = ConsumableDao.getByConsumablesId(connection, consumableId);
                if (consumable != null) {
                    ConsumableDao.delete(connection, consumable);
                }
                resp.sendRedirect("consumables");
            } else {
                // Handle update
                String itemName = req.getParameter("itemName");
                int itemLevel = Integer.parseInt(req.getParameter("itemLevel"));
                int maxStackSize = Integer.parseInt(req.getParameter("maxStackSize"));
                double price = Double.parseDouble(req.getParameter("price"));
                int quantity = Integer.parseInt(req.getParameter("quantity"));
                String description = req.getParameter("description");
                String source = req.getParameter("source");
                
                Consumable existingConsumable = ConsumableDao.getByConsumablesId(connection, consumableId);
                if (existingConsumable != null) {
                    Consumable updatedConsumable = ConsumableDao.create(connection, 
                        consumableId, itemName, itemLevel, maxStackSize, price, quantity,
                        existingConsumable.getConsumablesType(), description, source);
                    resp.sendRedirect("consumable?id=" + updatedConsumable.getItemId());
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Consumable not found");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error updating consumable", e);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number format");
        }
    }
}
 