package cs5200project.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs5200project.dal.CharacterJobDao;
import cs5200project.dal.ConnectionManager;

@WebServlet("/characterjoblist")
public class CharacterJobList extends HttpServlet {
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

		String jobIdStr = req.getParameter("jobId");

		if (jobIdStr == null || jobIdStr.trim().isEmpty()) {
			messages.put(RESPONSE_MESSAGE, "Please provide a job ID.");
		} else {
			try (Connection cxn = ConnectionManager.getConnection()) {
				int jobId = Integer.parseInt(jobIdStr);

				List<Map<String, Object>> charactersWithJob = CharacterJobDao
						.getCharactersByJobId(cxn, jobId);

				req.setAttribute("charactersWithJob", charactersWithJob);
				messages.put(RESPONSE_MESSAGE,
						"Showing all characters with job ID: " + jobId);
			} catch (SQLException | NumberFormatException e) {
				e.printStackTrace();
				messages.put(RESPONSE_MESSAGE, "Error: " + e.getMessage());
			}
		}

		req.getRequestDispatcher("/CharacterJobList.jsp").forward(req, resp);
	}
}
