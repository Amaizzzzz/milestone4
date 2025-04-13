package cs5200project.servlet;

import cs5200project.dal.RaceDao;
import cs5200project.model.Race;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/races")
public class RaceServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        try (Connection connection = ConnectionManager.getConnection()) {
            List<Race> races = RaceDao.getInstance().getAllRaces(connection);
            req.setAttribute("races", races);
            req.getRequestDispatcher("/race-list.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error retrieving races", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        String raceName = req.getParameter("raceName");
        String description = req.getParameter("description");
        int baseStrength = Integer.parseInt(req.getParameter("baseStrength"));
        int baseDexterity = Integer.parseInt(req.getParameter("baseDexterity"));
        int baseIntelligence = Integer.parseInt(req.getParameter("baseIntelligence"));

        try (Connection connection = ConnectionManager.getConnection()) {
            Race race = RaceDao.getInstance().create(connection, raceName, description,
                    baseStrength, baseDexterity, baseIntelligence);
            resp.sendRedirect("race?id=" + race.getRaceID());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error creating race", e);
        }
    }
} 