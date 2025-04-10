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

	private RaceDao() {
	}

	public static RaceDao getInstance() {
		if (instance == null) {
			instance = new RaceDao();
		}
		return instance;
	}

	public Race create(Connection conn, String raceName) throws SQLException {
		String sql = "INSERT INTO Race(raceName) VALUES(?);";
		try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, raceName);
			ps.executeUpdate();
			
			try (ResultSet keys = ps.getGeneratedKeys()) {
				if (keys.next()) {
					return new Race(keys.getInt(1), raceName);
				}
				throw new SQLException("Failed to get generated key.");
			}
		}
	}

	public Race getById(Connection conn, int raceId) throws SQLException {
		String sql = "SELECT raceID, raceName FROM Race WHERE raceID = ?;";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, raceId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new Race(
						rs.getInt("raceID"),
						rs.getString("raceName")
					);
				}
				return null;
			}
		}
	}

	public List<Race> getAll(Connection conn) throws SQLException {
		String sql = "SELECT raceID, raceName FROM Race;";
		List<Race> races = new ArrayList<>();
		try (PreparedStatement ps = conn.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				races.add(new Race(
					rs.getInt("raceID"),
					rs.getString("raceName")
				));
			}
		}
		return races;
	}

	public static void delete(Connection cxn, Race race) throws SQLException {
		final String delete = """
			DELETE FROM Race 
			WHERE raceID = ?;
		""";
		try (PreparedStatement stmt = cxn.prepareStatement(delete)) {
			stmt.setInt(1, race.getRaceID());
			stmt.executeUpdate();
		}
	}
}
