package cs5200project.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs5200project.dal.CharacterDao;
import cs5200project.dal.ConnectionManager;
import cs5200project.model.Character;

@WebServlet("/characterupdate")
public class CharacterUpdate extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int characterId = Integer.parseInt(req.getParameter("characterId"));
		try (Connection cxn = ConnectionManager.getConnection()) {
			Character character = CharacterDao.getCharacterById(cxn,
					characterId);
			req.setAttribute("character", character);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		req.getRequestDispatcher("/CharacterUpdate.jsp").forward(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try (Connection cxn = ConnectionManager.getConnection()) {
			int id = Integer.parseInt(req.getParameter("characterId"));
			String firstName = req.getParameter("firstName");
			String lastName = req.getParameter("lastName");

			CharacterDao.updateName(cxn, id, firstName, lastName);
			resp.sendRedirect("findcharacters");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}
}
