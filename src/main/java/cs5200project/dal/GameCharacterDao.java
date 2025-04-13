package cs5200project.dal;

import cs5200project.model.GameCharacter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class GameCharacterDao {
    private static GameCharacterDao instance = null;
    
    protected GameCharacterDao() {}
    
    public static GameCharacterDao getInstance() {
        if(instance == null) {
            instance = new GameCharacterDao();
        }
        return instance;
    }

    public GameCharacter create(Connection connection, GameCharacter character) throws SQLException {
        String sql = "INSERT INTO GameCharacter(PlayerID, FirstName, LastName, RaceID, CreationDate, IsNewPlayer, CurrentJobID) " +
                    "VALUES(?,?,?,?,?,?,?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, character.getPlayerID());
            ps.setString(2, character.getFirstName());
            ps.setString(3, character.getLastName());
            ps.setInt(4, character.getRaceID());
            ps.setTimestamp(5, character.getCreationDate());
            ps.setBoolean(6, character.isNewPlayer());
            ps.setInt(7, character.getCurrentJobID());
            
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if(rs.next()) {
                    int characterId = rs.getInt(1);
                    character.setCharacterID(characterId);
                    return character;
                }
            }
        }
        return null;
    }

    public GameCharacter getCharacterById(Connection connection, int characterId) throws SQLException {
        String sql = "SELECT CharacterID, PlayerID, FirstName, LastName, RaceID, CreationDate, IsNewPlayer, CurrentJobID " +
                    "FROM GameCharacter WHERE CharacterID=?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, characterId);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return new GameCharacter(
                        rs.getInt("CharacterID"),
                        rs.getInt("PlayerID"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getInt("RaceID"),
                        rs.getTimestamp("CreationDate"),
                        rs.getBoolean("IsNewPlayer"),
                        rs.getInt("CurrentJobID")
                    );
                }
            }
        }
        return null;
    }

    public List<GameCharacter> getAllCharacters(Connection connection) throws SQLException {
        List<GameCharacter> characters = new ArrayList<>();
        String sql = "SELECT CharacterID, PlayerID, FirstName, LastName, RaceID, CreationDate, IsNewPlayer, CurrentJobID " +
                    "FROM GameCharacter";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                GameCharacter character = new GameCharacter(
                    rs.getInt("CharacterID"),
                    rs.getInt("PlayerID"),
                    rs.getString("FirstName"),
                    rs.getString("LastName"),
                    rs.getInt("RaceID"),
                    rs.getTimestamp("CreationDate"),
                    rs.getBoolean("IsNewPlayer"),
                    rs.getInt("CurrentJobID")
                );
                characters.add(character);
            }
        }
        return characters;
    }
} 