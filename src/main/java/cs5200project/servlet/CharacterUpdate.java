package cs5200project.servlet;

import cs5200project.dal.CharacterDao;
import cs5200project.model.Character;
import cs5200project.util.ConnectionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/characterupdate")
public class CharacterUpdate extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, String> messages = new HashMap<>();
		req.setAttribute("messages", messages);

		String characterIdStr = req.getParameter("characterId");
		if (characterIdStr == null || characterIdStr.trim().isEmpty()) {
			messages.put("error", "Please provide a character ID.");
			req.getRequestDispatcher("/FindCharacter.jsp").forward(req, resp);
			return;
		}

		try (Connection connection = ConnectionManager.getConnection()) {
			int characterId = Integer.parseInt(characterIdStr);
			Character character = CharacterDao.getCharacterById(connection, characterId);
			if (character == null) {
				messages.put("error", "Character not found.");
				req.getRequestDispatcher("/FindCharacter.jsp").forward(req, resp);
				return;
			}
			req.setAttribute("character", character);
		} catch (SQLException e) {
			messages.put("error", "Error retrieving character: " + e.getMessage());
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
