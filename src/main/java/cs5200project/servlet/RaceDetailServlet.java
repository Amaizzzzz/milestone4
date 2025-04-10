package cs5200project.servlet;

import cs5200project.dal.RaceDao;
import cs5200project.dal.GameCharacterDao;
import cs5200project.model.Race;
import cs5200project.model.GameCharacter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/race")
public class RaceDetailServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        String raceIdStr = req.getParameter("id");
        if (raceIdStr == null || raceIdStr.trim().isEmpty()) {
            resp.sendRedirect("races");
            return;
        }

        try (Connection connection = ConnectionManager.getConnection()) {
            int raceId = Integer.parseInt(raceIdStr);
            Race race = RaceDao.getInstance().getRaceById(connection, raceId);
            if (race == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Race not found");
                return;
            }

            List<GameCharacter> characters = GameCharacterDao.getInstance()
                    .getCharactersByRace(connection, raceId);

            req.setAttribute("race", race);
            req.setAttribute("characters", characters);
            req.getRequestDispatcher("/race-detail.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error retrieving race details", e);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid race ID format");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        String action = req.getParameter("action");
        String raceIdStr = req.getParameter("id");

        if (raceIdStr == null || raceIdStr.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Race ID is required");
            return;
        }

        try (Connection connection = ConnectionManager.getConnection()) {
            int raceId = Integer.parseInt(raceIdStr);

            if ("delete".equals(action)) {
                RaceDao.getInstance().delete(connection, raceId);
                resp.sendRedirect("races");
            } else {
                // Handle update
                String raceName = req.getParameter("raceName");
                String description = req.getParameter("description");
                int baseStrength = Integer.parseInt(req.getParameter("baseStrength"));
                int baseDexterity = Integer.parseInt(req.getParameter("baseDexterity"));
                int baseIntelligence = Integer.parseInt(req.getParameter("baseIntelligence"));

                Race race = RaceDao.getInstance().update(connection, raceId, raceName, 
                        description, baseStrength, baseDexterity, baseIntelligence);
                resp.sendRedirect("race?id=" + race.getRaceID());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error updating race", e);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number format");
        }
    }
} 