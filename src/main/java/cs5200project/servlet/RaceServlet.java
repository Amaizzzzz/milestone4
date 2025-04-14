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
import cs5200project.dal.RaceDao;
import cs5200project.model.Race;

@WebServlet("/races")
public class RaceServlet extends HttpServlet {
    private final RaceDao raceDao = RaceDao.getInstance();
    
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = ConnectionManager.getConnection()) {
            List<Race> races = raceDao.getAllRaces(connection);
            req.setAttribute("races", races);
            req.getRequestDispatcher("race-list.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
    
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String raceName = req.getParameter("raceName");
        
        try (Connection connection = ConnectionManager.getConnection()) {
            Race race = raceDao.create(connection, raceName);
            resp.sendRedirect("races");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
} 