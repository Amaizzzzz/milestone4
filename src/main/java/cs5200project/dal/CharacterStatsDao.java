package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs5200project.model.Character;
import cs5200project.model.CharacterStats;
import cs5200project.model.Statistic;
import cs5200project.model.StatType;
import cs5200project.util.ConnectionManager;

public class CharacterStatsDao {
    private static CharacterStatsDao instance = null;

    protected CharacterStatsDao() {
    }

    public static CharacterStatsDao getInstance() {
        if (instance == null) {
            instance = new CharacterStatsDao();
        }
        return instance;
    }

    public static CharacterStats create(Connection connection, Character character, Statistic statistic, int value) throws SQLException {
        String insertCharacterStats = "INSERT INTO CharacterStats(characterID, statID, value) VALUES(?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(insertCharacterStats)) {
            statement.setInt(1, character.getCharacterID());
            statement.setInt(2, statistic.getStatisticID());
            statement.setInt(3, value);
            statement.executeUpdate();
            return new CharacterStats(character.getCharacterID(), statistic.getStatisticID(), value);
        }
    }

    public static CharacterStats getCharacterStats(Connection connection, int characterId, int statId) throws SQLException {
        String selectCharacterStats = "SELECT * FROM CharacterStats WHERE characterID = ? AND statID = ?";
        try (PreparedStatement statement = connection.prepareStatement(selectCharacterStats)) {
            statement.setInt(1, characterId);
            statement.setInt(2, statId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    StatType statType = StatTypeDao.getStatTypeByID(connection, statId);
                    return new CharacterStats(characterId, statType, resultSet.getInt("value"));
                }
                return null;
            }
        }
    }

    public static List<CharacterStats> getCharacterStatsByCharacterId(Connection connection, int characterId) throws SQLException {
        List<CharacterStats> characterStats = new ArrayList<>();
        String selectCharacterStats = "SELECT * FROM CharacterStats WHERE characterID = ?";
        try (PreparedStatement statement = connection.prepareStatement(selectCharacterStats)) {
            statement.setInt(1, characterId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int statId = resultSet.getInt("statID");
                    StatType statType = StatTypeDao.getStatTypeByID(connection, statId);
                    characterStats.add(new CharacterStats(characterId, statType, resultSet.getInt("value")));
                }
            }
        }
        return characterStats;
    }
}
