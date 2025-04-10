package cs5200project.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs5200project.dal.CharacterDao;
import cs5200project.model.GameCharacter;

@WebServlet("/characters")
public class CharacterServlet extends HttpServlet {
    private CharacterDao characterDao;

    @Override
    public void init() throws ServletException {
        characterDao = CharacterDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        String searchName = req.getParameter("searchName");
        
        try {
            List<GameCharacter> characters;
            if (searchName != null && !searchName.trim().isEmpty()) {
                characters = characterDao.getCharactersByName(searchName);
            } else {
                characters = characterDao.getAllCharacters();
            }
            req.setAttribute("characters", characters);
            req.getRequestDispatcher("/character-list.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
} 