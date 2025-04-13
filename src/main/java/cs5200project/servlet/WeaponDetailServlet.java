package cs5200project.servlet;

import cs5200project.dal.WeaponDao;
import cs5200project.dal.JobDao;
import cs5200project.model.Weapon;
import cs5200project.model.Job;
import cs5200project.model.WeaponDurability;
import cs5200project.model.RankValue;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/weapon")
public class WeaponDetailServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        String weaponIdStr = req.getParameter("id");
        if (weaponIdStr == null || weaponIdStr.trim().isEmpty()) {
            resp.sendRedirect("weapons");
            return;
        }

        try (Connection connection = ConnectionManager.getConnection()) {
            int weaponId = Integer.parseInt(weaponIdStr);
            Weapon weapon = WeaponDao.getInstance().getWeaponById(connection, weaponId);
            if (weapon == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Weapon not found");
                return;
            }

            List<Job> requiredJobs = JobDao.getInstance().getJobsForWeapon(connection, weaponId);

            req.setAttribute("weapon", weapon);
            req.setAttribute("requiredJobs", requiredJobs);
            req.getRequestDispatcher("/weapon-detail.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error retrieving weapon details", e);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid weapon ID format");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        String action = req.getParameter("action");
        String weaponIdStr = req.getParameter("id");

        if (weaponIdStr == null || weaponIdStr.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Weapon ID is required");
            return;
        }

        try (Connection connection = ConnectionManager.getConnection()) {
            int weaponId = Integer.parseInt(weaponIdStr);

            if ("delete".equals(action)) {
                WeaponDao.getInstance().delete(connection, weaponId);
                resp.sendRedirect("weapons");
            } else {
                // Handle update
                String itemName = req.getParameter("itemName");
                int itemLevel = Integer.parseInt(req.getParameter("itemLevel"));
                int maxStackSize = Integer.parseInt(req.getParameter("maxStackSize"));
                double price = Double.parseDouble(req.getParameter("price"));
                int quantity = Integer.parseInt(req.getParameter("quantity"));
                int requiredLevel = Integer.parseInt(req.getParameter("requiredLevel"));
                int damage = Integer.parseInt(req.getParameter("damage"));
                int attackSpeed = Integer.parseInt(req.getParameter("attackSpeed"));
                String weaponType = req.getParameter("weaponType");
                WeaponDurability durability = WeaponDurability.valueOf(req.getParameter("durability"));
                RankValue rankValue = RankValue.valueOf(req.getParameter("rankValue"));

                Weapon weapon = WeaponDao.getInstance().update(connection, weaponId, itemName,
                        itemLevel, maxStackSize, price, quantity, requiredLevel, damage,
                        attackSpeed, weaponType, durability, rankValue);
                resp.sendRedirect("weapon?id=" + weapon.getItemID());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error updating weapon", e);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number format");
        }
    }
} 