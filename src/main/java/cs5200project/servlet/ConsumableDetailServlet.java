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

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/consumable")
public class ConsumableDetailServlet extends HttpServlet {
    private final ConsumableDao consumableDao = ConsumableDao.getInstance();
    private final StatTypeDao statTypeDao = StatTypeDao.getInstance();
    private final StatisticDao statisticDao = StatisticDao.getInstance();
    private final ConsumablesStatsBonusDao consumablesStatsBonusDao = ConsumablesStatsBonusDao.getInstance();
    
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
            Consumable consumable = consumableDao.getByConsumablesId(connection, consumableId);
            if (consumable == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Consumable not found");
                return;
            }

            // Get all stat types and their corresponding bonuses
            List<ConsumablesStatsBonus> statBonuses = new ArrayList<>();
            List<StatType> allStatTypes = statTypeDao.getStatTypeByName(connection, "");
            for (StatType statType : allStatTypes) {
                Statistic stat = statisticDao.getStatisticByID(connection, statType.getStatTypeID());
                if (stat != null) {
                    ConsumablesStatsBonus bonus = consumablesStatsBonusDao.getByItemIdAndStatId(connection, consumable, stat);
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
                Consumable consumable = consumableDao.getByConsumablesId(connection, consumableId);
                if (consumable != null) {
                    consumableDao.delete(connection, consumable);
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
                
                Consumable existingConsumable = consumableDao.getByConsumablesId(connection, consumableId);
                if (existingConsumable != null) {
                    Consumable updatedConsumable = consumableDao.create(connection, 
                        itemName, itemLevel, maxStackSize, price, quantity,
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
 