package cs5200project.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import cs5200project.dal.ConnectionManager;
import cs5200project.dal.WeaponDao;
import cs5200project.model.Weapon;
import cs5200project.model.Weapon.WeaponDurability;
import cs5200project.model.Weapon.RankValue;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/weapons")
public class WeaponServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private WeaponDao weaponDao;

    @Override
    public void init() throws ServletException {
        weaponDao = WeaponDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try (Connection connection = ConnectionManager.getConnection()) {
            List<Weapon> weapons = weaponDao.getAllWeapons(connection);
            req.setAttribute("weapons", weapons);
            req.getRequestDispatcher("/weapon-list.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error retrieving weapons", e);
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
        int requiredLevel = Integer.parseInt(req.getParameter("requiredLevel"));
        int damage = Integer.parseInt(req.getParameter("damage"));
        int attackSpeed = Integer.parseInt(req.getParameter("attackSpeed"));
        String weaponType = req.getParameter("weaponType");
        WeaponDurability durability = WeaponDurability.valueOf(req.getParameter("durability"));
        RankValue rankValue = RankValue.valueOf(req.getParameter("rankValue"));

        try (Connection connection = ConnectionManager.getConnection()) {
            Weapon weapon = weaponDao.create(connection, 0, itemName, itemLevel,
                    maxStackSize, price, quantity, requiredLevel, damage,
                    attackSpeed, weaponType, null, null, durability, rankValue);
            resp.sendRedirect(req.getContextPath() + "/weapon?id=" + weapon.getItemId());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error creating weapon", e);
        }
    }
} 