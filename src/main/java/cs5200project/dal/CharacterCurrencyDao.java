package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs5200project.model.CharacterCurrency;
import cs5200project.util.ConnectionManager;

public class CharacterCurrencyDao {
    protected static CharacterCurrencyDao instance = null;
    
    protected CharacterCurrencyDao() {
        // Exists only to defeat instantiation.
    }
    
    public static CharacterCurrencyDao getInstance() {
        if(instance == null) {
            instance = new CharacterCurrencyDao();
        }
        return instance;
    }

    public List<CharacterCurrency> getAllCharacterCurrencies() throws SQLException {
        List<CharacterCurrency> currencies = new ArrayList<>();
        String selectAll = "SELECT characterId, currencyId, amount, isActive FROM CharacterCurrency";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(selectAll);
             ResultSet rs = selectStmt.executeQuery()) {
            while (rs.next()) {
                currencies.add(new CharacterCurrency(
                    rs.getInt("currencyId"),
                    rs.getInt("characterId"),
                    rs.getInt("amount"),
                    rs.getBoolean("isActive")
                ));
            }
        }
        return currencies;
    }

    public CharacterCurrency create(CharacterCurrency currency) throws SQLException {
        String insertCurrency = "INSERT INTO CharacterCurrency(characterId, currencyId, amount, isActive) VALUES(?,?,?,?)";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement insertStmt = connection.prepareStatement(insertCurrency)) {
            insertStmt.setInt(1, currency.getCharacterId());
            insertStmt.setInt(2, currency.getCurrencyId());
            insertStmt.setInt(3, currency.getAmount());
            insertStmt.setBoolean(4, currency.isActive());
            insertStmt.executeUpdate();
            return currency;
        }
    }

    public CharacterCurrency update(CharacterCurrency currency) throws SQLException {
        String updateAmount = "UPDATE CharacterCurrency SET amount = ?, isActive = ? WHERE characterId = ? AND currencyId = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement updateStmt = connection.prepareStatement(updateAmount)) {
            updateStmt.setInt(1, currency.getAmount());
            updateStmt.setBoolean(2, currency.isActive());
            updateStmt.setInt(3, currency.getCharacterId());
            updateStmt.setInt(4, currency.getCurrencyId());
            updateStmt.executeUpdate();
            return currency;
        }
    }

    public void delete(int characterId, int currencyId) throws SQLException {
        String deleteCurrency = "DELETE FROM CharacterCurrency WHERE characterId = ? AND currencyId = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement deleteStmt = connection.prepareStatement(deleteCurrency)) {
            deleteStmt.setInt(1, characterId);
            deleteStmt.setInt(2, currencyId);
            deleteStmt.executeUpdate();
        }
    }

    public CharacterCurrency getCharacterCurrency(int characterId, int currencyId) throws SQLException {
        String selectCurrency = "SELECT characterId, currencyId, amount, isActive FROM CharacterCurrency WHERE characterId = ? AND currencyId = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(selectCurrency)) {
            selectStmt.setInt(1, characterId);
            selectStmt.setInt(2, currencyId);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    return new CharacterCurrency(
                        rs.getInt("currencyId"),
                        rs.getInt("characterId"),
                        rs.getInt("amount"),
                        rs.getBoolean("isActive")
                    );
                }
            }
        }
        return null;
    }
}

