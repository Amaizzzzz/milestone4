package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cs5200project.model.GameCharacter;

public class CharacterDao {
    private static CharacterDao instance = null;

    private CharacterDao() {
    }

    public static CharacterDao getInstance() {
        if (instance == null) {
            instance = new CharacterDao();
        }
        return instance;
    }

    public List<GameCharacter> getAllCharacters() throws SQLException {
        String selectCharacters = 
            "SELECT characterID, playerID, firstName, lastName, raceID, creationDate, isNewPlayer, currentJobID " +
            "FROM `Character`;";
        
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(selectCharacters);
             ResultSet results = selectStmt.executeQuery()) {
            
            List<GameCharacter> characters = new ArrayList<>();
            while (results.next()) {
                GameCharacter character = new GameCharacter(
                    results.getInt("characterID"),
                    results.getInt("playerID"),
                    results.getString("firstName"),
                    results.getString("lastName"),
                    results.getInt("raceID"),
                    results.getDate("creationDate"),
                    results.getBoolean("isNewPlayer"),
                    results.getInt("currentJobID")
                );
                characters.add(character);
            }
            return characters;
        }
    }

    public GameCharacter create(GameCharacter character) throws SQLException {
        String insertCharacter = 
            "INSERT INTO `Character` (playerID, firstName, lastName, raceID, creationDate, isNewPlayer, currentJobID) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?);";
        
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement insertStmt = connection.prepareStatement(insertCharacter, Statement.RETURN_GENERATED_KEYS)) {
            
            insertStmt.setInt(1, character.getPlayerID());
            insertStmt.setString(2, character.getFirstName());
            insertStmt.setString(3, character.getLastName());
            insertStmt.setInt(4, character.getRaceID());
            insertStmt.setDate(5, new java.sql.Date(character.getCreationDate().getTime()));
            insertStmt.setBoolean(6, character.isNewPlayer());
            insertStmt.setInt(7, character.getCurrentJobID());
            
            insertStmt.executeUpdate();
            
            try (ResultSet resultKey = insertStmt.getGeneratedKeys()) {
                if (resultKey.next()) {
                    character.setCharacterID(resultKey.getInt(1));
                }
            }
            
            return character;
        }
    }

    public GameCharacter getCharacterById(Connection connection, int characterId) throws SQLException {
        String selectCharacter = 
            "SELECT characterID, playerID, firstName, lastName, raceID, creationDate, isNewPlayer, currentJobID " +
            "FROM `Character` " +
            "WHERE characterID = ?;";
        
        try (PreparedStatement selectStmt = connection.prepareStatement(selectCharacter)) {
            selectStmt.setInt(1, characterId);
            
            try (ResultSet results = selectStmt.executeQuery()) {
                if (results.next()) {
                    return new GameCharacter(
                        results.getInt("characterID"),
                        results.getInt("playerID"),
                        results.getString("firstName"),
                        results.getString("lastName"),
                        results.getInt("raceID"),
                        results.getDate("creationDate"),
                        results.getBoolean("isNewPlayer"),
                        results.getInt("currentJobID")
                    );
                }
            }
        }
        
        return null;
    }

    public List<GameCharacter> getCharactersByName(String partialName) throws SQLException {
        String selectCharacters = 
            "SELECT c.characterID, c.playerID, c.firstName, c.lastName, c.raceID, " +
            "c.creationDate, c.isNewPlayer, c.currentJobID " +
            "FROM `Character` c " +
            "WHERE c.firstName LIKE ? OR c.lastName LIKE ?;";
        
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(selectCharacters)) {
            
            String searchPattern = "%" + partialName + "%";
            selectStmt.setString(1, searchPattern);
            selectStmt.setString(2, searchPattern);
            
            try (ResultSet results = selectStmt.executeQuery()) {
                List<GameCharacter> characters = new ArrayList<>();
                while (results.next()) {
                    GameCharacter character = new GameCharacter(
                        results.getInt("characterID"),
                        results.getInt("playerID"),
                        results.getString("firstName"),
                        results.getString("lastName"),
                        results.getInt("raceID"),
                        results.getDate("creationDate"),
                        results.getBoolean("isNewPlayer"),
                        results.getInt("currentJobID")
                    );
                    characters.add(character);
                }
                return characters;
            }
        }
    }
}
