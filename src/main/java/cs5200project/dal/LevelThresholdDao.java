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
		final String insertLevelThreshold = "INSERT INTO LevelThreshold(level, xpThreshold) " +
				"VALUES(?,?)";
		try (PreparedStatement statement = cxn.prepareStatement(insertLevelThreshold,
				Statement.RETURN_GENERATED_KEYS)) {
			statement.setInt(1, charLevel);
			statement.setInt(2, requiredXP);
			statement.executeUpdate();

			try (ResultSet results = statement.getGeneratedKeys()) {
				if (results.next()) {
					return new LevelThreshold(charLevel, requiredXP);
				} else {
					return null;
				}
			}
		}
	}

	public static LevelThreshold getLevelThresholdByLevel(Connection cxn, int charLevel) throws SQLException {
		final String selectLevelThreshold = "SELECT levelThresholdID, level, xpThreshold " +
				"FROM LevelThreshold " +
				"WHERE level = ?";
		try (PreparedStatement selectStmt = cxn.prepareStatement(selectLevelThreshold)) {
			selectStmt.setInt(1, charLevel);
			try (ResultSet results = selectStmt.executeQuery()) {
				if (results.next()) {
					return new LevelThreshold(
						results.getInt("level"),
						results.getInt("xpThreshold")
					);
				} else {
					return null;
				}
			}
		}
	}

	public static List<LevelThreshold> getAllLevelThresholds(Connection cxn) throws SQLException {
		final String selectLevelThreshold = "SELECT levelThresholdID, level, xpThreshold " +
				"FROM LevelThreshold " +
				"ORDER BY level";
		List<LevelThreshold> thresholdList = new ArrayList<>();
		try (PreparedStatement selectStmt = cxn.prepareStatement(selectLevelThreshold)) {
			try (ResultSet results = selectStmt.executeQuery()) {
				while (results.next()) {
					thresholdList.add(new LevelThreshold(
						results.getInt("level"),
						results.getInt("xpThreshold")
					));
				}
				return thresholdList;
			}
		}
	}

	public static <T extends LevelThreshold> T updateRequiredXP(Connection cxn, T levelThreshold,
			int newRequiredXP) throws SQLException {
		final String update = "UPDATE LevelThreshold " +
				"SET xpThreshold = ? " +
				"WHERE level = ?";
		try (PreparedStatement updateStmt = cxn.prepareStatement(update)) {
			updateStmt.setInt(1, newRequiredXP);
			updateStmt.setInt(2, levelThreshold.getCharLevel());
			updateStmt.executeUpdate();

			levelThreshold.setRequiredXP(newRequiredXP);
			return levelThreshold;
		}
	}
}
