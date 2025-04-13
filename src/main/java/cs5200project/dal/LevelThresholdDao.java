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
	private LevelThresholdDao() {
	}

	public static LevelThreshold create(Connection cxn, int charLevel, int requiredXP) throws SQLException {
		final String insertLevelThreshold = """
			INSERT INTO LevelThreshold(charLevel, requiredXP)
			VALUES (?, ?);
		""";

		try (PreparedStatement insertStmt = cxn.prepareStatement(insertLevelThreshold)) {
			insertStmt.setInt(1, charLevel);
			insertStmt.setInt(2, requiredXP);
			insertStmt.executeUpdate();

			return new LevelThreshold(charLevel, requiredXP);
		}
	}

	public static LevelThreshold getLevelThresholdByLevel(Connection cxn, int charLevel) throws SQLException {
		final String selectLevelThreshold = """
			SELECT * FROM LevelThreshold 
			WHERE charLevel = ?;
		""";
		try (PreparedStatement selectStmt = cxn.prepareStatement(selectLevelThreshold)) {
			selectStmt.setInt(1, charLevel);
			try (ResultSet results = selectStmt.executeQuery()) {
				if (results.next()) {
					return new LevelThreshold(
						results.getInt("charLevel"),
						results.getInt("requiredXP")
					);
				} else {
					return null;
				}
			}
		}
	}

	public static List<LevelThreshold> getAllLevelThresholds(Connection cxn) throws SQLException {
		List<LevelThreshold> thresholds = new ArrayList<>();
		String query = "SELECT * FROM LevelThreshold ORDER BY charLevel";
		
		try (PreparedStatement stmt = cxn.prepareStatement(query)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					thresholds.add(new LevelThreshold(
						rs.getInt("charLevel"),
						rs.getInt("requiredXP")
					));
				}
			}
		}
		return thresholds;
	}

	public static <T extends LevelThreshold> T updateRequiredXP(Connection cxn, T levelThreshold,
			int newRequiredXP) throws SQLException {
		final String update = """
			UPDATE LevelThreshold 
			SET requiredXP = ? 
			WHERE charLevel = ?;
		""";
		try (PreparedStatement updateStmt = cxn.prepareStatement(update)) {
			updateStmt.setInt(1, newRequiredXP);
			updateStmt.setInt(2, levelThreshold.getCharLevel());
			updateStmt.executeUpdate();

			levelThreshold.setRequiredXP(newRequiredXP);
			return levelThreshold;
		}
	}

	public static int getLevelForXP(Connection cxn, int xp) throws SQLException {
		String query = "SELECT MAX(charLevel) as level FROM LevelThreshold WHERE requiredXP <= ?";
		try (PreparedStatement stmt = cxn.prepareStatement(query)) {
			stmt.setInt(1, xp);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("level");
				}
			}
		}
		return 1; // Default to level 1 if no threshold found
	}
	
	public static int getXPForNextLevel(Connection cxn, int currentLevel) throws SQLException {
		String query = "SELECT requiredXP FROM LevelThreshold WHERE charLevel = ?";
		try (PreparedStatement stmt = cxn.prepareStatement(query)) {
			stmt.setInt(1, currentLevel + 1);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("requiredXP");
				}
			}
		}
		return Integer.MAX_VALUE; // Return max value if no next level found
	}
}
