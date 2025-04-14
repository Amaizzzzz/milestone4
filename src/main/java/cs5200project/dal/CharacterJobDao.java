package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs5200project.model.Character;
import cs5200project.model.CharacterJob;
import cs5200project.model.Job;

public class CharacterJobDao {
    // Dao classes should not be instantiated.
    // Pass Connection object as parameter in each method
    // Each method should be static
    private CharacterJobDao() {
        // Private constructor to prevent instantiation
    }

    public static CharacterJob create(Connection connection, Character character, Job job, boolean isUnlocked, int XP) throws SQLException {
        String insertQuery = "INSERT INTO `CharacterJob` (characterID, jobID, isUnlocked, XP) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setInt(1, character.getCharacterID());
            statement.setInt(2, job.getJobID());
            statement.setBoolean(3, isUnlocked);
            statement.setInt(4, XP);
            statement.executeUpdate();
        }
        
        return new CharacterJob(character.getCharacterID(), job.getJobID(), isUnlocked, XP);
    }

    public static List<CharacterJob> getJobsByCharacterId(Connection cxn, int characterID) throws SQLException {
        List<CharacterJob> jobs = new ArrayList<>();
		String query = "SELECT * FROM CharacterJob WHERE characterID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(query)) {
            stmt.setInt(1, characterID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    jobs.add(new CharacterJob(
                        rs.getInt("characterID"),
                        rs.getInt("jobID"),
                        rs.getBoolean("isUnlocked"),
                        rs.getInt("XP")
                    ));
                }
            }
        }
        return jobs;
    }

	// New Method: Get all characters with a specific jobID
	public static List<Map<String, Object>> getCharactersByJobId(Connection cxn,
			int jobId) throws SQLException {
		List<Map<String, Object>> result = new ArrayList<>();

		String query = """
				    SELECT c.characterID, c.firstName, c.lastName, c.currentJobID,
				           cj.XP, cj.isUnlocked,
				           j.jobName
				    FROM CharacterJob cj
				    JOIN `Character` c ON cj.characterID = c.characterID
				    JOIN Job j ON cj.jobID = j.jobID
				    WHERE cj.jobID = ?
				""";

		try (PreparedStatement stmt = cxn.prepareStatement(query)) {
			stmt.setInt(1, jobId);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Map<String, Object> row = new HashMap<>();
					String fullName = rs.getString("firstName") + " "
							+ rs.getString("lastName");
					row.put("characterId", rs.getInt("characterID"));
					row.put("characterName", fullName);
					row.put("jobName", rs.getString("jobName"));
					row.put("xp", rs.getInt("XP"));
					row.put("isUnlocked", rs.getBoolean("isUnlocked"));
					row.put("isCurrent", rs.getInt("currentJobID") == jobId);
					row.put("weaponName", null); // placeholder

					result.add(row);
				}
			}
		}

		return result;
	}
}
