package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cs5200project.model.Clan;
import cs5200project.model.Race;

public class ClanDao {
	private static ClanDao instance = null;
	private final RaceDao raceDao;
	
	protected ClanDao() {
		raceDao = RaceDao.getInstance();
	}
	
	public static ClanDao getInstance() {
		if(instance == null) {
			instance = new ClanDao();
		}
		return instance;
	}

	public Clan create(Connection connection, String clanName, int raceId) throws SQLException {
		String insertClan = "INSERT INTO Clan(clanName, raceID) VALUES(?,?)";
		try (PreparedStatement insertStmt = connection.prepareStatement(insertClan,
				Statement.RETURN_GENERATED_KEYS)) {
			insertStmt.setString(1, clanName);
			insertStmt.setInt(2, raceId);
			insertStmt.executeUpdate();
			
			int clanId = Utils.getAutoIncrementKey(insertStmt);
			Race race = raceDao.getRaceById(connection, raceId);
			return new Clan(clanId, clanName, race);
		}
	}

	public Clan getClanById(Connection connection, int clanId) throws SQLException {
		String selectClan = "SELECT * FROM Clan WHERE clanID = ?";
		try (PreparedStatement selectStmt = connection.prepareStatement(selectClan)) {
			selectStmt.setInt(1, clanId);
			try (ResultSet results = selectStmt.executeQuery()) {
				if (results.next()) {
					String clanName = results.getString("clanName");
					Race race = raceDao.getRaceById(connection, results.getInt("raceID"));
					return new Clan(clanId, clanName, race);
				} else {
					return null;
				}
			}
		}
	}

	public List<Clan> getClansByRace(Connection connection, Race race) throws SQLException {
		List<Clan> clans = new ArrayList<>();
		String selectClans = "SELECT * FROM Clan WHERE raceID = ?";
		try (PreparedStatement selectStmt = connection.prepareStatement(selectClans)) {
			selectStmt.setInt(1, race.getRaceID());
			try (ResultSet results = selectStmt.executeQuery()) {
				while (results.next()) {
					int clanId = results.getInt("clanID");
					String clanName = results.getString("clanName");
					Race race1 = raceDao.getRaceById(connection, results.getInt("raceID"));
					clans.add(new Clan(clanId, clanName, race1));
				}
			}
		}
		return clans;
	}

	public List<Clan> getAllClans(Connection connection) throws SQLException {
		List<Clan> clans = new ArrayList<>();
		String selectClans = "SELECT * FROM Clan";
		try (PreparedStatement selectStmt = connection.prepareStatement(selectClans);
			 ResultSet results = selectStmt.executeQuery()) {
			while (results.next()) {
				int clanId = results.getInt("clanID");
				String clanName = results.getString("clanName");
				Race race = raceDao.getRaceById(connection, results.getInt("raceID"));
				clans.add(new Clan(clanId, clanName, race));
			}
		}
		return clans;
	}

	public void update(Connection connection, int clanId, String clanName, int raceId) throws SQLException {
		String update = "UPDATE Clan SET clanName = ?, raceID = ? WHERE clanID = ?";
		try (PreparedStatement stmt = connection.prepareStatement(update)) {
			stmt.setString(1, clanName);
			stmt.setInt(2, raceId);
			stmt.setInt(3, clanId);
			stmt.executeUpdate();
		}
	}

	public void delete(Connection connection, Clan clan) throws SQLException {
		String delete = "DELETE FROM Clan WHERE clanID = ?";
		try (PreparedStatement stmt = connection.prepareStatement(delete)) {
			stmt.setInt(1, clan.getClanID());
			stmt.executeUpdate();
		}
	}
}