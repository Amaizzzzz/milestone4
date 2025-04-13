package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs5200project.model.Character_Stats;

public class Character_StatsDao {
    private Character_StatsDao() {}
    
    public static Character_Stats create(Connection cxn, int statID, int characterID, int value) 
            throws SQLException {
        String query = "INSERT INTO Character_Stats (statID, characterID, value) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = cxn.prepareStatement(query)) {
            stmt.setInt(1, statID);
            stmt.setInt(2, characterID);
            stmt.setInt(3, value);
            stmt.executeUpdate();
            return new Character_Stats(statID, characterID, value);
        }
    }
    
    public static List<Character_Stats> getCharacterStats(Connection cxn, int characterID) throws SQLException {
        List<Character_Stats> characterStats = new ArrayList<>();
        String query = "SELECT cs.*, st.statType, " +
                      "COALESCE(SUM(gsb.bonusValue), 0) as equipmentBonus " +
                      "FROM Character_Stats cs " +
                      "JOIN Statistic s ON cs.statID = s.statID " +
                      "JOIN StatType st ON s.statTypeID = st.statTypeID " +
                      "LEFT JOIN GearStatisticBonus gsb ON cs.statID = gsb.statID " +
                      "WHERE cs.characterID = ? " +
                      "GROUP BY cs.statID, cs.characterID, cs.value, st.statType";
        
        try (PreparedStatement stmt = cxn.prepareStatement(query)) {
            stmt.setInt(1, characterID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Character_Stats cs = new Character_Stats(
                        rs.getInt("statID"),
                        rs.getInt("characterID"),
                        rs.getInt("value")
                    );
                    characterStats.add(cs);
                }
            }
        }
        return characterStats;
    }
    
    public static boolean updateStatValue(Connection cxn, int statID, int characterID, int newValue) 
            throws SQLException {
        String query = "UPDATE Character_Stats SET value = ? WHERE statID = ? AND characterID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(query)) {
            stmt.setInt(1, newValue);
            stmt.setInt(2, statID);
            stmt.setInt(3, characterID);
            return stmt.executeUpdate() > 0;
        }
    }
} 