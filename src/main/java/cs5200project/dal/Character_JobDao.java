package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs5200project.model.Character_Job;
import cs5200project.model.LevelThreshold;

public class Character_JobDao {
    private Character_JobDao() {}
    
    public static Character_Job create(Connection cxn, int characterID, int jobID, boolean isUnlocked, int xp) 
            throws SQLException {
        String query = "INSERT INTO Character_Job (characterID, jobID, isUnlocked, xp) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = cxn.prepareStatement(query)) {
            stmt.setInt(1, characterID);
            stmt.setInt(2, jobID);
            stmt.setBoolean(3, isUnlocked);
            stmt.setInt(4, xp);
            stmt.executeUpdate();
            return new Character_Job(characterID, jobID, isUnlocked, xp);
        }
    }
    
    public static List<Character_Job> getCharacterJobs(Connection cxn, int characterID) throws SQLException {
        List<Character_Job> characterJobs = new ArrayList<>();
        String query = "SELECT cj.*, j.jobName, lt.charLevel " +
                      "FROM Character_Job cj " +
                      "JOIN Job j ON cj.jobID = j.jobID " +
                      "LEFT JOIN LevelThreshold lt ON cj.xp >= lt.requiredXP " +
                      "WHERE cj.characterID = ? " +
                      "ORDER BY lt.charLevel DESC";
        
        try (PreparedStatement stmt = cxn.prepareStatement(query)) {
            stmt.setInt(1, characterID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Character_Job cj = new Character_Job(
                        rs.getInt("characterID"),
                        rs.getInt("jobID"),
                        rs.getBoolean("isUnlocked"),
                        rs.getInt("xp")
                    );
                    characterJobs.add(cj);
                }
            }
        }
        return characterJobs;
    }
    
    public static boolean updateXP(Connection cxn, int characterID, int jobID, int newXP) throws SQLException {
        String query = "UPDATE Character_Job SET xp = ? WHERE characterID = ? AND jobID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(query)) {
            stmt.setInt(1, newXP);
            stmt.setInt(2, characterID);
            stmt.setInt(3, jobID);
            return stmt.executeUpdate() > 0;
        }
    }
    
    public static boolean unlockJob(Connection cxn, int characterID, int jobID) throws SQLException {
        String query = "UPDATE Character_Job SET isUnlocked = true WHERE characterID = ? AND jobID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(query)) {
            stmt.setInt(1, characterID);
            stmt.setInt(2, jobID);
            return stmt.executeUpdate() > 0;
        }
    }
} 