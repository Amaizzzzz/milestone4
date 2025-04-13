package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import cs5200project.model.GameCharacter;
import cs5200project.model.Player;
import cs5200project.model.Race;
import cs5200project.model.Job;

public class CharacterDao {
    // Dao classes should not be instantiated.
    // Pass Connection object as parameter in each method
    // Each method should be static
    private CharacterDao() {
        // Private constructor to prevent instantiation
    }

    public static GameCharacter create(Connection cxn, Player player, String firstName, String lastName, Race race, Date creationTime, boolean isNewPlayer, Job job) throws SQLException {
        String query = "INSERT INTO Character (playerID, firstName, lastName, raceID, creationTime, isNewPlayer, currentJobID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (
            PreparedStatement stmt = 
                cxn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)
            ) {
            stmt.setInt(1, player.getPlayerID());
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setInt(4, race.getRaceID());
            stmt.setTimestamp(5, new java.sql.Timestamp(creationTime.getTime()));
            stmt.setBoolean(6, isNewPlayer);
            stmt.setInt(7, job.getJobID());
            return new GameCharacter(Utils.getAutoIncrementKey(stmt), player.getPlayerID(), firstName, lastName, 
                race.getRaceID(), creationTime, isNewPlayer, job.getJobID()); 
            }
    }
    
    public static GameCharacter create(Connection cxn, int playerID, String firstName, String lastName, 
                                     int raceID, Timestamp creationTime, boolean isNewPlayer, 
                                     int currentJobID) throws SQLException {
        String query = "INSERT INTO `Character` (playerID, firstName, lastName, raceID, creationTime, isNewPlayer, currentJobID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = cxn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, playerID);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setInt(4, raceID);
            stmt.setTimestamp(5, creationTime);
            stmt.setBoolean(6, isNewPlayer);
            stmt.setInt(7, currentJobID);
            
            stmt.executeUpdate();
            
            int generatedKey = Utils.getAutoIncrementKey(stmt);
            return new GameCharacter(generatedKey, playerID, firstName, lastName, 
                raceID, creationTime, isNewPlayer, currentJobID);
        }
    }

    public static GameCharacter getCharacterById(Connection cxn, int id) throws SQLException {
        String query = "SELECT * FROM Character WHERE characterID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new GameCharacter(
                        rs.getInt("characterID"),
                        rs.getInt("playerID"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getInt("raceID"),
                        rs.getTimestamp("creationTime"),
                        rs.getBoolean("isNewPlayer"),
                        rs.getInt("currentJobID")
                    );
                }
            }
        }
        return null;
    }

    public static List<GameCharacter> getAllCharacters(Connection cxn) throws SQLException {
        List<GameCharacter> characters = new ArrayList<>();
        String query = "SELECT * FROM `Character`";
        try (PreparedStatement stmt = cxn.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    characters.add(new GameCharacter(
                        rs.getInt("characterID"),
                        rs.getInt("playerID"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getInt("raceID"),
                        rs.getTimestamp("creationTime"),
                        rs.getBoolean("isNewPlayer"),
                        rs.getInt("currentJobID")
                    ));
                }
            }
        }
        return characters;
    }

    public static List<GameCharacter> getFilteredCharacters(Connection cxn, 
            String nameSearch, Integer raceId, String sortBy) throws SQLException {
        List<GameCharacter> characters = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder(
            "SELECT c.*, r.raceName, j.jobName " +
            "FROM `Character` c " +
            "JOIN Race r ON c.raceID = r.raceID " +
            "JOIN Job j ON c.currentJobID = j.jobID " +
            "WHERE 1=1 "
        );
        
        List<Object> params = new ArrayList<>();
        
        if (nameSearch != null && !nameSearch.trim().isEmpty()) {
            queryBuilder.append("AND CONCAT(firstName, ' ', lastName) LIKE ? ");
            params.add("%" + nameSearch + "%");
        }
        
        if (raceId != null) {
            queryBuilder.append("AND c.raceID = ? ");
            params.add(raceId);
        }
        
        if (sortBy != null) {
            switch (sortBy) {
                case "name":
                    queryBuilder.append("ORDER BY firstName, lastName ");
                    break;
                case "creation":
                    queryBuilder.append("ORDER BY creationTime ");
                    break;
                default:
                    queryBuilder.append("ORDER BY characterID ");
            }
        }
        
        try (PreparedStatement stmt = cxn.prepareStatement(queryBuilder.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    GameCharacter character = new GameCharacter(
                        rs.getInt("characterID"),
                        rs.getInt("playerID"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getInt("raceID"),
                        rs.getTimestamp("creationTime"),
                        rs.getBoolean("isNewPlayer"),
                        rs.getInt("currentJobID")
                    );
                    character.setRaceName(rs.getString("raceName"));
                    character.setJobName(rs.getString("jobName"));
                    characters.add(character);
                }
            }
        }
        return characters;
    }

    public static boolean deleteCharacter(Connection cxn, int characterId) throws SQLException {
        String query = "DELETE FROM `Character` WHERE characterID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(query)) {
            stmt.setInt(1, characterId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public static List<GameCharacter> getCharactersByRace(Connection cxn, int raceId) throws SQLException {
        List<GameCharacter> characters = new ArrayList<>();
        String query = "SELECT * FROM `Character` WHERE raceID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(query)) {
            stmt.setInt(1, raceId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    characters.add(new GameCharacter(
                        rs.getInt("characterID"),
                        rs.getInt("playerID"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getInt("raceID"),
                        rs.getTimestamp("creationTime"),
                        rs.getBoolean("isNewPlayer"),
                        rs.getInt("currentJobID")
                    ));
                }
            }
        }
        return characters;
    }
}
