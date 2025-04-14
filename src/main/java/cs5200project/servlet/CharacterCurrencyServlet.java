package cs5200project.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import cs5200project.dal.CharacterCurrencyDao;
import cs5200project.model.CharacterCurrency;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/character-currency")
public class CharacterCurrencyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final CharacterCurrencyDao characterCurrencyDao = CharacterCurrencyDao.getInstance();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        try {
            List<CharacterCurrency> currencies = characterCurrencyDao.getAllCharacterCurrencies();
            out.print(gson.toJson(currencies));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(new ErrorResponse(e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        try {
            CharacterCurrency currency = gson.fromJson(request.getReader(), CharacterCurrency.class);
            CharacterCurrency created = characterCurrencyDao.create(currency);
            out.print(gson.toJson(created));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(new ErrorResponse(e.getMessage())));
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        try {
            CharacterCurrency currency = gson.fromJson(request.getReader(), CharacterCurrency.class);
            CharacterCurrency updated = characterCurrencyDao.update(currency);
            out.print(gson.toJson(updated));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(new ErrorResponse(e.getMessage())));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        try {
            int characterId = Integer.parseInt(request.getParameter("characterId"));
            int currencyId = Integer.parseInt(request.getParameter("currencyId"));
            characterCurrencyDao.delete(characterId, currencyId);
            out.print(gson.toJson(new SuccessResponse("Character currency deleted successfully")));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(new ErrorResponse(e.getMessage())));
        }
    }

    private static class ErrorResponse {
        private final String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }

    private static class SuccessResponse {
        private final String message;

        public SuccessResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
} 