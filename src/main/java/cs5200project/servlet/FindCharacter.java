package cs5200project.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs5200project.dal.CharacterDao;
import cs5200project.dal.ConnectionManager;
import cs5200project.model.Character;

@WebServlet("/findcharacters")
public class FindCharacter extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String RESPONSE_MESSAGE = "response";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handleRequest(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handleRequest(req, resp);
	}

	private void handleRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map<String, String> messages = new HashMap<>();
		req.setAttribute("messages", messages);

		String sortField = req.getParameter("sortField"); // optional
		if (sortField == null || sortField.isBlank()
				|| "none".equals(sortField))
			sortField = null; // fallback to default sort

		String searchType = req.getParameter("searchType");
		String searchValue = req.getParameter("searchValue");

		if (searchValue == null || searchValue.trim().isEmpty()) {
			messages.put(RESPONSE_MESSAGE, "Please enter a search value.");
		} else {
			try (Connection cxn = ConnectionManager.getConnection()) {
				List<Character> characters;

				if ("lastName".equalsIgnoreCase(searchType)) {
					characters = CharacterDao.getCharactersByLastName(cxn,
							searchValue, sortField);
					messages.put("response",
							"Results for last name: " + searchValue);
				} else if ("raceName".equalsIgnoreCase(searchType)) {
					characters = CharacterDao.getCharactersByRaceName(cxn,
							searchValue, sortField);
					messages.put("response",
							"Results for race: " + searchValue);
				} else {
					characters = new ArrayList<>();
					messages.put("response", "Invalid search type.");
				}

				req.setAttribute("characterList", characters);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new IOException(e);
			}

		}

		req.getRequestDispatcher("/FindCharacter.jsp").forward(req, resp);
	}
}
