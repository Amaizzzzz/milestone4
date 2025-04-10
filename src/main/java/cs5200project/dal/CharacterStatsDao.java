package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs5200project.model.CharacterStats;
import cs5200project.model.GameCharacter;
import cs5200project.model.Statistic;

public class CharacterStatsDao {
    private static CharacterStatsDao instance = null;
    private StatisticDao statisticDao;

    protected CharacterStatsDao() {
        statisticDao = StatisticDao.getInstance();
    }

    public static CharacterStatsDao getInstance() {
        if (instance == null) {
            instance = new CharacterStatsDao();
        }
        return instance;
    }

    public List<CharacterStats> getByCharacterID(Connection conn, GameCharacter character) throws SQLException {
        String sql = 
            "SELECT cs.characterID, cs.statID, cs.currentValue, " +
            "s.statTypeID, s.baseValue, " +
            "st.statName, st.description " +
            "FROM CharacterStats cs " +
            "JOIN Statistic s ON cs.statID = s.statID " +
            "JOIN StatType st ON s.statTypeID = st.statTypeID " +
            "WHERE cs.characterID = ?;";

        List<CharacterStats> stats = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, character.getCharacterID());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Statistic stat = new Statistic(
                        rs.getInt("statID"),
                        rs.getInt("statTypeID"),
                        rs.getInt("baseValue")
                    );
                    CharacterStats characterStat = new CharacterStats(
                        character,
                        stat,
                        rs.getInt("currentValue")
                    );
                    stats.add(characterStat);
                }
            }
        }
        return stats;
    }

    public CharacterStats create(Connection conn, GameCharacter character, Statistic stat, int currentValue) throws SQLException {
        String sql = "INSERT INTO CharacterStats(characterID, statID, currentValue) VALUES(?, ?, ?);";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, character.getCharacterID());
            ps.setInt(2, stat.getStatID());
            ps.setInt(3, currentValue);
            ps.executeUpdate();
            return new CharacterStats(character, stat, currentValue);
        }
    }

    public static List<CharacterStats> getStatsByCharacterId(Connection cxn, int characterID) throws SQLException {
        List<CharacterStats> stats = new ArrayList<>();
        String query = "SELECT * FROM `CharacterStats` WHERE characterID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(query)) {
            stmt.setInt(1, characterID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    stats.add(new CharacterStats(
                        rs.getInt("characterID"),
                        rs.getInt("statID"),
                        rs.getInt("value")
                    ));
                }
            }
        }
        return stats;
    }
}
