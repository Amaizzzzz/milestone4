package cs5200project.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import cs5200project.dal.ConnectionManager;
import cs5200project.dal.JobDao;
import cs5200project.dal.WeaponDao;
import cs5200project.model.Job;
import cs5200project.model.Weapon;
import cs5200project.model.Weapon.WeaponDurability;
import cs5200project.model.Weapon.RankValue;

@WebServlet("/weapon")
public class WeaponDetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final WeaponDao weaponDao = WeaponDao.getInstance();
    private final JobDao jobDao = JobDao.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String itemIdStr = request.getParameter("id");
        
        try (Connection connection = ConnectionManager.getConnection()) {
            if (itemIdStr != null && !itemIdStr.isEmpty()) {
                int itemId = Integer.parseInt(itemIdStr);
                Weapon weapon = weaponDao.getWeaponById(connection, itemId);
                request.setAttribute("weapon", weapon);
                
                List<Job> jobs = jobDao.getJobsForWeapon(connection, itemId);
                request.setAttribute("jobs", jobs);
            }
            request.getRequestDispatcher("/weapon.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error accessing the weapon", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String itemIdStr = request.getParameter("id");
        int itemId = Integer.parseInt(itemIdStr);

        try (Connection connection = ConnectionManager.getConnection()) {
            if ("delete".equals(action)) {
                weaponDao.delete(connection, itemId);
                response.sendRedirect(request.getContextPath() + "/weapons");
                return;
            } else if ("update".equals(action)) {
                String itemName = request.getParameter("itemName");
                int itemLevel = Integer.parseInt(request.getParameter("itemLevel"));
                int maxStackSize = Integer.parseInt(request.getParameter("maxStackSize"));
                double price = Double.parseDouble(request.getParameter("price"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                int requiredLevel = Integer.parseInt(request.getParameter("requiredLevel"));
                int damage = Integer.parseInt(request.getParameter("damage"));
                int attackSpeed = Integer.parseInt(request.getParameter("attackSpeed"));
                String weaponType = request.getParameter("weaponType");
                WeaponDurability durability = WeaponDurability.valueOf(request.getParameter("weaponDurability"));
                RankValue rankValue = RankValue.valueOf(request.getParameter("rankValue"));

                weaponDao.update(connection, itemId, itemName, itemLevel, maxStackSize, price, quantity,
                        requiredLevel, damage, attackSpeed, weaponType, durability, rankValue);

                Weapon weapon = weaponDao.getWeaponById(connection, itemId);
                request.setAttribute("weapon", weapon);
                request.setAttribute("success", "Weapon updated successfully");
                request.getRequestDispatcher("/weapon.jsp").forward(request, response);
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error processing weapon action", e);
        }
    }
} 