package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs5200project.model.CharacterStats;
import cs5200project.model.GameCharacter;
import cs5200project.model.StatType;

public class CharacterStatsDao {
    protected static CharacterStatsDao instance = null;
    
    protected CharacterStatsDao() {
        // Exists only to defeat instantiation.
    }
    
    public static CharacterStatsDao getInstance() {
        if(instance == null) {
            instance = new CharacterStatsDao();
        }
        return instance;
    }

    public CharacterStats create(Connection connection, CharacterStats stats) throws SQLException {
        String insertStats = "INSERT INTO CharacterStats(characterId, statTypeId, value) VALUES(?,?,?)";
        try (PreparedStatement insertStmt = connection.prepareStatement(insertStats)) {
            insertStmt.setInt(1, stats.getCharacter().getCharacterID());
            insertStmt.setInt(2, stats.getStatType().getStatTypeID());
            insertStmt.setInt(3, stats.getValue());
            insertStmt.executeUpdate();
            return stats;
        }
    }

    public List<CharacterStats> getCharacterStats(Connection connection, int characterId) throws SQLException {
        List<CharacterStats> statsList = new ArrayList<>();
        String selectStats = "SELECT cs.statTypeId, cs.value, st.name, st.description " +
                           "FROM CharacterStats cs " +
                           "JOIN StatType st ON cs.statTypeId = st.statTypeId " +
                           "WHERE cs.characterId = ?";
        
        GameCharacter character = GameCharacterDao.getInstance().getCharacterById(connection, characterId);
        if (character == null) {
            return statsList;
        }

        try (PreparedStatement selectStmt = connection.prepareStatement(selectStats)) {
            selectStmt.setInt(1, characterId);
            try (ResultSet rs = selectStmt.executeQuery()) {
                while (rs.next()) {
                    StatType statType = new StatType(
                        rs.getInt("statTypeId"),
                        rs.getString("name"),
                        rs.getString("description")
                    );
                    statsList.add(new CharacterStats(character, statType, rs.getInt("value")));
                }
            }
        }
        return statsList;
    }

    public void updateStatValue(Connection connection, CharacterStats stats) throws SQLException {
        String updateStats = "UPDATE CharacterStats SET value = ? WHERE characterId = ? AND statTypeId = ?";
        try (PreparedStatement updateStmt = connection.prepareStatement(updateStats)) {
            updateStmt.setInt(1, stats.getValue());
            updateStmt.setInt(2, stats.getCharacter().getCharacterID());
            updateStmt.setInt(3, stats.getStatType().getStatTypeID());
            updateStmt.executeUpdate();
        }
    }

    public void delete(Connection connection, CharacterStats stats) throws SQLException {
        String deleteStats = "DELETE FROM CharacterStats WHERE characterId = ? AND statTypeId = ?";
        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteStats)) {
            deleteStmt.setInt(1, stats.getCharacter().getCharacterID());
            deleteStmt.setInt(2, stats.getStatType().getStatTypeID());
            deleteStmt.executeUpdate();
        }
    }
}
