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

@WebServlet("/characterdelete")
public class CharacterDelete extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int characterId = Integer.parseInt(req.getParameter("characterId"));
		try (Connection cxn = ConnectionManager.getConnection()) {
			CharacterDao.delete(cxn, characterId);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
		resp.sendRedirect("findcharacters"); // reload listing
	}
}
