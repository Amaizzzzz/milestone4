package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs5200project.model.CharacterJob;
import cs5200project.model.GameCharacter;
import cs5200project.model.Job;

public class CharacterJobDao {
    private static CharacterJobDao instance = null;
    
    protected CharacterJobDao() {}
    
    public static CharacterJobDao getInstance() {
        if(instance == null) {
            instance = new CharacterJobDao();
        }
        return instance;
    }

    public CharacterJob create(Connection connection, GameCharacter character, Job job, 
            boolean isUnlocked, int xp) throws SQLException {
        String insertCharacterJob = "INSERT INTO CharacterJob(characterID, jobID, isUnlocked, XP) VALUES(?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertCharacterJob)) {
            stmt.setInt(1, character.getCharacterID());
            stmt.setInt(2, job.getJobID());
            stmt.setBoolean(3, isUnlocked);
            stmt.setInt(4, xp);
            stmt.executeUpdate();
            
            return new CharacterJob(character.getCharacterID(), job.getJobID(), isUnlocked, xp);
        }
    }

    public List<CharacterJob> getCharacterJobs(Connection connection, int characterId) throws SQLException {
        String selectCharacterJobs = "SELECT cj.*, j.jobName FROM CharacterJob cj " +
                                   "JOIN Job j ON cj.jobID = j.jobID " +
                                   "WHERE cj.characterID = ?";
        List<CharacterJob> characterJobs = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(selectCharacterJobs)) {
            stmt.setInt(1, characterId);
            try (ResultSet results = stmt.executeQuery()) {
                while (results.next()) {
                    Job job = new Job(results.getInt("jobID"), results.getString("jobName"));
                    characterJobs.add(new CharacterJob(
                        characterId,
                        job.getJobID(),
                        results.getBoolean("isUnlocked"),
                        results.getInt("XP")
                    ));
                }
            }
        }
        return characterJobs;
    }

    public void updateXP(Connection connection, int characterId, int jobId, int newXP) throws SQLException {
        String updateXP = "UPDATE CharacterJob SET XP = ? WHERE characterID = ? AND jobID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateXP)) {
            stmt.setInt(1, newXP);
            stmt.setInt(2, characterId);
            stmt.setInt(3, jobId);
            stmt.executeUpdate();
        }
    }

    public void unlockJob(Connection connection, int characterId, int jobId) throws SQLException {
        String unlockJob = "UPDATE CharacterJob SET isUnlocked = true WHERE characterID = ? AND jobID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(unlockJob)) {
            stmt.setInt(1, characterId);
            stmt.setInt(2, jobId);
            stmt.executeUpdate();
        }
    }

    public void setCurrentJob(Connection connection, int characterId, int jobId) throws SQLException {
        String setCurrentJob = "UPDATE GameCharacter SET currentJobID = ? WHERE characterID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(setCurrentJob)) {
            stmt.setInt(1, jobId);
            stmt.setInt(2, characterId);
            stmt.executeUpdate();
        }
    }

    public int calculateLevelFromXP(int xp) throws SQLException {
        // This would typically query a LevelThreshold table
        // For now, using a simple formula: level = sqrt(xp/100)
        return (int) Math.sqrt(xp / 100);
    }
}
