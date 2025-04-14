package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cs5200project.model.LevelThreshold;

public class LevelThresholdDao {
	private static LevelThresholdDao instance = null;
	
	protected LevelThresholdDao() {}
	
	public static LevelThresholdDao getInstance() {
		if(instance == null) {
			instance = new LevelThresholdDao();
		}
		return instance;
	}

	public LevelThreshold create(Connection connection, int level, int xpRequired) throws SQLException {
		String insertLevelThreshold = "INSERT INTO LevelThreshold(level, xpRequired) VALUES(?, ?)";
		try (PreparedStatement insertStmt = connection.prepareStatement(insertLevelThreshold,
				Statement.RETURN_GENERATED_KEYS)) {
			insertStmt.setInt(1, level);
			insertStmt.setInt(2, xpRequired);
			insertStmt.executeUpdate();
			
			return new LevelThreshold(level, xpRequired);
		}
	}

	public LevelThreshold getLevelThresholdByLevel(Connection connection, int level) throws SQLException {
		String selectLevelThreshold = "SELECT * FROM LevelThreshold WHERE level = ?";
		try (PreparedStatement selectStmt = connection.prepareStatement(selectLevelThreshold)) {
			selectStmt.setInt(1, level);
			try (ResultSet results = selectStmt.executeQuery()) {
				if (results.next()) {
					return new LevelThreshold(
							results.getInt("level"),
							results.getInt("xpRequired"));
				} else {
					return null;
				}
			}
		}
	}

	public void update(Connection connection, int level, int xpRequired) throws SQLException {
		String update = "UPDATE LevelThreshold SET xpRequired = ? WHERE level = ?";
		try (PreparedStatement stmt = connection.prepareStatement(update)) {
			stmt.setInt(1, xpRequired);
			stmt.setInt(2, level);
			stmt.executeUpdate();
		}
	}

	public void delete(Connection connection, LevelThreshold levelThreshold) throws SQLException {
		String delete = "DELETE FROM LevelThreshold WHERE level = ?";
		try (PreparedStatement stmt = connection.prepareStatement(delete)) {
			stmt.setInt(1, levelThreshold.getCharLevel());
			stmt.executeUpdate();
		}
	}

	public List<LevelThreshold> getAllLevelThresholds(Connection connection) throws SQLException {
		List<LevelThreshold> thresholds = new ArrayList<>();
		String query = "SELECT * FROM LevelThreshold ORDER BY level";
		
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					thresholds.add(new LevelThreshold(
						rs.getInt("level"),
						rs.getInt("xpRequired")
					));
				}
			}
		}
		return thresholds;
	}

	public <T extends LevelThreshold> T updateXPRequired(Connection connection, T levelThreshold, int newRequiredXP) 
			throws SQLException {
		String update = "UPDATE LevelThreshold SET xpRequired = ? WHERE level = ?";
		try (PreparedStatement updateStmt = connection.prepareStatement(update)) {
			updateStmt.setInt(1, newRequiredXP);
			updateStmt.setInt(2, levelThreshold.getCharLevel());
			updateStmt.executeUpdate();

			levelThreshold.setRequiredXP(newRequiredXP);
			return levelThreshold;
		}
	}

	public int getLevelForXP(Connection connection, int xp) throws SQLException {
		String query = "SELECT MAX(level) as level FROM LevelThreshold WHERE xpRequired <= ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setInt(1, xp);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("level");
				}
			}
		}
		return 1; // Default to level 1 if no threshold is found
	}
	
	public int getXPForNextLevel(Connection connection, int currentLevel) throws SQLException {
		String query = "SELECT xpRequired FROM LevelThreshold WHERE level = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setInt(1, currentLevel + 1);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("xpRequired");
				}
			}
		}
		return Integer.MAX_VALUE; // Return max value if no next level exists
	}
}
