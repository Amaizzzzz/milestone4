package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cs5200project.model.Player;
import cs5200project.model.Race;

public class RaceDao {
	private static RaceDao instance = null;
	
	protected RaceDao() {}
	
	public static RaceDao getInstance() {
		if(instance == null) {
			instance = new RaceDao();
		}
		return instance;
	}

	public Race create(Connection cxn, String raceName) throws SQLException {
		final String insertRace = "INSERT INTO Race(raceName) VALUES (?);";

		try (PreparedStatement insertStmt = cxn.prepareStatement(insertRace,
				Statement.RETURN_GENERATED_KEYS)) {
			insertStmt.setString(1, raceName);
			insertStmt.executeUpdate();

			return new Race(Utils.getAutoIncrementKey(insertStmt), raceName);
		}
	}

	public List<Race> getAllRaces(Connection cxn) throws SQLException {
		List<Race> races = new ArrayList<>();
		final String selectRaces = "SELECT * FROM Race;";
		try (PreparedStatement selectStmt = cxn.prepareStatement(selectRaces)) {
			try (ResultSet results = selectStmt.executeQuery()) {
				while (results.next()) {
					int raceId = results.getInt("raceID");
					String raceName = results.getString("raceName");
					String description = results.getString("description");
					int baseStrength = results.getInt("baseStrength");
					int baseDexterity = results.getInt("baseDexterity");
					int baseIntelligence = results.getInt("baseIntelligence");
					races.add(new Race(raceId, raceName, description, baseStrength, 
									 baseDexterity, baseIntelligence));
				}
			}
		}
		return races;
	}

	public Race getRaceById(Connection cxn, int raceId)
			throws SQLException {
		final String selectRace = "SELECT * FROM Race WHERE raceID = ?;";
		try (PreparedStatement selectStmt = cxn.prepareStatement(selectRace)) {
			selectStmt.setInt(1, raceId);
			try (ResultSet results = selectStmt.executeQuery()) {
				if (results.next()) {
					String raceName = results.getString("raceName");
					String description = results.getString("description");
					int baseStrength = results.getInt("baseStrength");
					int baseDexterity = results.getInt("baseDexterity");
					int baseIntelligence = results.getInt("baseIntelligence");
					return new Race(raceId, raceName, description, baseStrength, 
								  baseDexterity, baseIntelligence);
				} else {
					return null;
				}
			}
		}
	}

	public void delete(Connection cxn, Race race) throws SQLException {
		final String delete = "DELETE FROM Race WHERE raceID = ?;";
		try (PreparedStatement stmt = cxn.prepareStatement(delete)) {
			stmt.setInt(1, race.getRaceID());
			stmt.executeUpdate();
		}
	}

	public Race update(Connection cxn, int raceId, String raceName, 
			String description, int baseStrength, int baseDexterity, 
			int baseIntelligence) throws SQLException {
		String query = "UPDATE Race SET raceName = ?, description = ?, " +
					  "baseStrength = ?, baseDexterity = ?, baseIntelligence = ? " +
					  "WHERE raceID = ?";
		try (PreparedStatement stmt = cxn.prepareStatement(query)) {
			stmt.setString(1, raceName);
			stmt.setString(2, description);
			stmt.setInt(3, baseStrength);
			stmt.setInt(4, baseDexterity);
			stmt.setInt(5, baseIntelligence);
			stmt.setInt(6, raceId);
			
			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				return new Race(raceId, raceName, description, baseStrength, 
							  baseDexterity, baseIntelligence);
			}
			return null;
		}
	}
}
