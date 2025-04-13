package cs5200project.dal;

import cs5200project.model.CharacterCurrency;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CharacterCurrencyDao {
    private static CharacterCurrencyDao instance = null;
    
    private CharacterCurrencyDao() {}
    
    public static CharacterCurrencyDao getInstance() {
        if (instance == null) {
            instance = new CharacterCurrencyDao();
        }
        return instance;
    }
    
    public CharacterCurrency create(Connection connection, int currencyId, int characterId, int amount) throws SQLException {
        String sql = "INSERT INTO CharacterCurrency(currencyId, characterId, amount) VALUES (?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, currencyId);
            statement.setInt(2, characterId);
            statement.setInt(3, amount);
            statement.executeUpdate();
            
            return new CharacterCurrency(currencyId, characterId, amount);
        }
    }
    
    public List<CharacterCurrency> getCharacterCurrencies(Connection connection, int characterId) throws SQLException {
        List<CharacterCurrency> currencies = new ArrayList<>();
        String sql = "SELECT * FROM CharacterCurrency WHERE characterId = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, characterId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    CharacterCurrency currency = new CharacterCurrency(
                        rs.getInt("currencyId"),
                        rs.getInt("characterId"),
                        rs.getInt("amount")
                    );
                    currencies.add(currency);
                }
            }
        }
        return currencies;
    }
    
    public void updateCurrencyAmount(Connection connection, int currencyId, int characterId, int newAmount) throws SQLException {
        String sql = "UPDATE CharacterCurrency SET amount = ? WHERE currencyId = ? AND characterId = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, newAmount);
            statement.setInt(2, currencyId);
            statement.setInt(3, characterId);
            statement.executeUpdate();
        }
    }
}
