package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import cs5200project.model.GameCharacter;

public class GameCharacterDao {
    protected static GameCharacterDao instance = null;
    
    protected GameCharacterDao() {
        // Exists only to defeat instantiation.
    }
    
    public static GameCharacterDao getInstance() {
        if(instance == null) {
            instance = new GameCharacterDao();
        }
        return instance;
    }

    public GameCharacter create(Connection connection, int playerId, String firstName, String lastName, 
            int raceId, Timestamp creationDate, boolean isNewPlayer, int currentJobId) throws SQLException {
        String insertCharacter = "INSERT INTO GameCharacter(playerId, firstName, lastName, raceId, creationDate, isNewPlayer, currentJobId) " +
                               "VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement insertStmt = connection.prepareStatement(insertCharacter)) {
            insertStmt.setInt(1, playerId);
            insertStmt.setString(2, firstName);
            insertStmt.setString(3, lastName);
            insertStmt.setInt(4, raceId);
            insertStmt.setTimestamp(5, creationDate);
            insertStmt.setBoolean(6, isNewPlayer);
            insertStmt.setInt(7, currentJobId);
            insertStmt.executeUpdate();
            
            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int characterId = generatedKeys.getInt(1);
                    return new GameCharacter(characterId, playerId, firstName, lastName, raceId, 
                                           creationDate, isNewPlayer, currentJobId);
                }
            }
        }
        return null;
    }

    public GameCharacter getCharacterById(Connection connection, int characterId) throws SQLException {
        String selectCharacter = "SELECT playerId, firstName, lastName, raceId, creationDate, isNewPlayer, currentJobId " +
                               "FROM GameCharacter WHERE characterId = ?";
        try (PreparedStatement selectStmt = connection.prepareStatement(selectCharacter)) {
            selectStmt.setInt(1, characterId);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    return new GameCharacter(
                        characterId,
                        rs.getInt("playerId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getInt("raceId"),
                        rs.getTimestamp("creationDate"),
                        rs.getBoolean("isNewPlayer"),
                        rs.getInt("currentJobId")
                    );
                }
            }
        }
        return null;
    }

    public List<GameCharacter> getAllCharacters(Connection connection) throws SQLException {
        List<GameCharacter> characters = new ArrayList<>();
        String selectCharacters = "SELECT characterId, playerId, firstName, lastName, raceId, creationDate, isNewPlayer, currentJobId " +
                                "FROM GameCharacter";
        try (PreparedStatement selectStmt = connection.prepareStatement(selectCharacters)) {
            try (ResultSet rs = selectStmt.executeQuery()) {
                while (rs.next()) {
                    characters.add(new GameCharacter(
                        rs.getInt("characterId"),
                        rs.getInt("playerId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getInt("raceId"),
                        rs.getTimestamp("creationDate"),
                        rs.getBoolean("isNewPlayer"),
                        rs.getInt("currentJobId")
                    ));
                }
            }
        }
        return characters;
    }

    public List<GameCharacter> getCharactersByRace(Connection connection, int raceId) throws SQLException {
        List<GameCharacter> characters = new ArrayList<>();
        String selectCharacters = "SELECT characterId, playerId, firstName, lastName, raceId, creationDate, isNewPlayer, currentJobId " +
                                "FROM GameCharacter WHERE raceId = ?";
        try (PreparedStatement selectStmt = connection.prepareStatement(selectCharacters)) {
            selectStmt.setInt(1, raceId);
            try (ResultSet rs = selectStmt.executeQuery()) {
                while (rs.next()) {
                    characters.add(new GameCharacter(
                        rs.getInt("characterId"),
                        rs.getInt("playerId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getInt("raceId"),
                        rs.getTimestamp("creationDate"),
                        rs.getBoolean("isNewPlayer"),
                        rs.getInt("currentJobId")
                    ));
                }
            }
        }
        return characters;
    }

    public List<GameCharacter> getCharactersByJob(Connection connection, int jobId) throws SQLException {
        List<GameCharacter> characters = new ArrayList<>();
        String selectCharacters = "SELECT characterId, playerId, firstName, lastName, raceId, creationDate, isNewPlayer, currentJobId " +
                                "FROM GameCharacter WHERE currentJobId = ?";
        try (PreparedStatement selectStmt = connection.prepareStatement(selectCharacters)) {
            selectStmt.setInt(1, jobId);
            try (ResultSet rs = selectStmt.executeQuery()) {
                while (rs.next()) {
                    characters.add(new GameCharacter(
                        rs.getInt("characterId"),
                        rs.getInt("playerId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getInt("raceId"),
                        rs.getTimestamp("creationDate"),
                        rs.getBoolean("isNewPlayer"),
                        rs.getInt("currentJobId")
                    ));
                }
            }
        }
        return characters;
    }

    public void update(Connection connection, GameCharacter character) throws SQLException {
        String updateCharacter = "UPDATE GameCharacter SET playerId = ?, firstName = ?, lastName = ?, " +
                               "raceId = ?, creationDate = ?, isNewPlayer = ?, currentJobId = ? " +
                               "WHERE characterId = ?";
        try (PreparedStatement updateStmt = connection.prepareStatement(updateCharacter)) {
            updateStmt.setInt(1, character.getPlayerID());
            updateStmt.setString(2, character.getFirstName());
            updateStmt.setString(3, character.getLastName());
            updateStmt.setInt(4, character.getRaceID());
            updateStmt.setTimestamp(5, character.getCreationDate());
            updateStmt.setBoolean(6, character.isNewPlayer());
            updateStmt.setInt(7, character.getCurrentJobID());
            updateStmt.setInt(8, character.getCharacterID());
            updateStmt.executeUpdate();
        }
    }

    public void delete(Connection connection, GameCharacter character) throws SQLException {
        String deleteCharacter = "DELETE FROM GameCharacter WHERE characterId = ?";
        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteCharacter)) {
            deleteStmt.setInt(1, character.getCharacterID());
            deleteStmt.executeUpdate();
        }
    }
} 