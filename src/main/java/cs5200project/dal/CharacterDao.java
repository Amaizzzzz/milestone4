package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cs5200project.model.Character;
import cs5200project.model.Job;
import cs5200project.model.Player;
import cs5200project.model.Race;

public class CharacterDao {
    // Dao classes should not be instantiated.
    // Pass Connection object as parameter in each method
    // Each method should be static
    private CharacterDao() {
        // Private constructor to prevent instantiation
    }

	public static Character create(Connection cxn, Player player,
			String firstName, String lastName, Race race, Date creationTime,
			boolean isNewPlayer, Job job) throws SQLException {
		String query = "INSERT INTO `Character` (playerID, firstName, lastName, raceID, creationTime, isNewPlayer, currentJobID) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement stmt = cxn.prepareStatement(query,
				PreparedStatement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, player.getPlayerID());
			stmt.setString(2, firstName);
			stmt.setString(3, lastName);
			stmt.setInt(4, race.getRaceID());
			stmt.setTimestamp(5,
					new java.sql.Timestamp(creationTime.getTime()));
			stmt.setBoolean(6, isNewPlayer);
			stmt.setInt(7, job.getJobID());
			stmt.executeUpdate();
			return new Character(Utils.getAutoIncrementKey(stmt), player,
					firstName, lastName, race, creationTime, isNewPlayer, job);

		}
	}

	// Get character by ID, including full Player, Race, Job
    public static Character getCharacterById(Connection cxn, int id) throws SQLException {
		String query = """
				    SELECT c.*, p.username, p.email, p.serverRegion,
				           r.raceName, j.jobName
				    FROM `Character` c
				    JOIN Player p ON c.playerID = p.playerID
				    JOIN Race r ON c.raceID = r.raceID
				    JOIN Job j ON c.currentJobID = j.jobID
				    WHERE c.characterID = ?
				""";

        try (PreparedStatement stmt = cxn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
					Player player = new Player(rs.getInt("playerID"),
							rs.getString("username"), rs.getString("email"),
							rs.getString("serverRegion"));
					Race race = new Race(rs.getInt("raceID"),
							rs.getString("raceName"));
					Job job = new Job(rs.getInt("currentJobID"),
							rs.getString("jobName"));
                    return new Character(
                        rs.getInt("characterID"),
							player,
                        rs.getString("firstName"),
                        rs.getString("lastName"),
							race,
                        rs.getTimestamp("creationTime"),
                        rs.getBoolean("isNewPlayer"),
							job
                    );
                }
            }
        }
        return null;
    }

	// Search characters by last name (JOIN to get Race & Job)
	public static List<Character> getCharactersByLastName(Connection cxn,
			String lastName, String sortField) throws SQLException {
		List<Character> characters = new ArrayList<>();

		String baseQuery = """
				    SELECT c.*, p.username, p.email, p.serverRegion,
				           r.raceName, j.jobName
				    FROM `Character` c
				    JOIN Player p ON c.playerID = p.playerID
				    JOIN Race r ON c.raceID = r.raceID
				    JOIN Job j ON c.currentJobID = j.jobID
				    WHERE c.lastName LIKE ?
				""";
		String orderBy = "ORDER BY c.characterID"; // default
		if ("name".equalsIgnoreCase(sortField)) {
			orderBy = "ORDER BY c.firstName, c.lastName";
		} else if ("creation".equalsIgnoreCase(sortField)) {
			orderBy = "ORDER BY c.creationTime";
		}
		String query = baseQuery + " " + orderBy;

		try (PreparedStatement stmt = cxn.prepareStatement(query)) {
			stmt.setString(1, lastName);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Player player = new Player(rs.getInt("playerID"),
							rs.getString("username"), rs.getString("email"),
							rs.getString("serverRegion"));
					Race race = new Race(rs.getInt("raceID"),
							rs.getString("raceName"));
					Job job = new Job(rs.getInt("currentJobID"),
							rs.getString("jobName"));
					Character character = new Character(
							rs.getInt("characterID"), player,
							rs.getString("firstName"), rs.getString("lastName"),
							race, rs.getTimestamp("creationTime"),
							rs.getBoolean("isNewPlayer"), job);
					characters.add(character);
				}
			}
		}
		return characters;
	}

	public static List<Character> getCharactersByRaceName(Connection cxn,
			String raceName, String sortField) throws SQLException {
		List<Character> characters = new ArrayList<>();

		String baseQuery = """
				    SELECT c.*, p.username, p.email, p.serverRegion,
				           r.raceName, j.jobName
				    FROM `Character` c
				    JOIN Player p ON c.playerID = p.playerID
				    JOIN Race r ON c.raceID = r.raceID
				    JOIN Job j ON c.currentJobID = j.jobID
				    WHERE r.raceName LIKE ?
				""";

		String orderBy = "ORDER BY c.characterID"; // default
		if ("name".equalsIgnoreCase(sortField)) {
			orderBy = "ORDER BY c.firstName, c.lastName";
		} else if ("creation".equalsIgnoreCase(sortField)) {
			orderBy = "ORDER BY c.creationTime";
		}
		String query = baseQuery + " " + orderBy;

		try (PreparedStatement stmt = cxn.prepareStatement(query)) {
			stmt.setString(1, "%" + raceName + "%");
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Player player = new Player(rs.getInt("playerID"),
							rs.getString("username"), rs.getString("email"),
							rs.getString("serverRegion"));

					Race race = new Race(rs.getInt("raceID"),
							rs.getString("raceName"));

					Job job = new Job(rs.getInt("currentJobID"),
							rs.getString("jobName"));

					Character character = new Character(
							rs.getInt("characterID"), player,
							rs.getString("firstName"), rs.getString("lastName"),
							race, rs.getTimestamp("creationTime"),
							rs.getBoolean("isNewPlayer"), job);

					characters.add(character);
				}
			}
		}

		return characters;
	}

	public static void delete(Connection cxn, int characterId)
			throws SQLException {
		String query = "DELETE FROM `Character` WHERE characterID = ?";
		try (PreparedStatement stmt = cxn.prepareStatement(query)) {
			stmt.setInt(1, characterId);
			stmt.executeUpdate();
		}
	}

	public static void updateName(Connection cxn, int characterId,
			String firstName, String lastName) throws SQLException {
		String query = "UPDATE `Character` SET firstName = ?, lastName = ? WHERE characterID = ?";
		try (PreparedStatement stmt = cxn.prepareStatement(query)) {
			stmt.setString(1, firstName);
			stmt.setString(2, lastName);
			stmt.setInt(3, characterId);
			stmt.executeUpdate();
		}
	}

}
