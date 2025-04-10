package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cs5200project.model.Player;

public class PlayerDao {
	private static PlayerDao instance = null;

	protected PlayerDao() {
		// Empty constructor - no initialization needed
	}

	public static PlayerDao getInstance() {
		if (instance == null) {
			instance = new PlayerDao();
		}
		return instance;
	}

	public Player create(Player player) throws SQLException {
		String insertPlayer = "INSERT INTO Player(username, email, serverRegion) VALUES(?,?,?);";
		try (Connection connection = ConnectionManager.getConnection();
			PreparedStatement insertStmt = connection.prepareStatement(insertPlayer, Statement.RETURN_GENERATED_KEYS);
			) {
			insertStmt.setString(1, player.getUsername());
			insertStmt.setString(2, player.getEmail());
			insertStmt.setString(3, player.getServerRegion());
			insertStmt.executeUpdate();
			try (ResultSet resultKey = insertStmt.getGeneratedKeys()) {
				int playerID = -1;
				if (resultKey.next()) {
					playerID = resultKey.getInt(1);
				}
				player.setPlayerID(playerID);
				return player;
			}
		}
	}

	public List<Player> getAllPlayers() throws SQLException {
		List<Player> players = new ArrayList<>();
		String selectPlayers = "SELECT playerID, username, email, serverRegion FROM Player;";
		try (Connection connection = ConnectionManager.getConnection();
			PreparedStatement selectStmt = connection.prepareStatement(selectPlayers);
			ResultSet results = selectStmt.executeQuery();
			) {
			while (results.next()) {
				int playerID = results.getInt("playerID");
				String username = results.getString("username");
				String email = results.getString("email");
				String serverRegion = results.getString("serverRegion");
				Player player = new Player(playerID, username, email, serverRegion);
				players.add(player);
			}
		}
		return players;
	}

	public static Player getPlayerByID(Connection cxn, int playerID) throws SQLException {
		String selectPlayer = "SELECT playerID, username, email, serverRegion FROM Player WHERE playerID = ?;";
		try (PreparedStatement selectStmt = cxn.prepareStatement(selectPlayer);
			) {
			selectStmt.setInt(1, playerID);
			try (ResultSet results = selectStmt.executeQuery()) {
				if (results.next()) {
					String username = results.getString("username");
					String email = results.getString("email");
					String serverRegion = results.getString("serverRegion");
					return new Player(playerID, username, email, serverRegion);
				}
				return null;
			}
		}
	}

	public static Player create(Connection cxn, String username, String email,
			String serverRegion) throws SQLException {
		String sql = "INSERT INTO Player(username, email, serverRegion) VALUES (?, ?, ?)";
		try (PreparedStatement ps = cxn.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
			) {
			ps.setString(1, username);
			ps.setString(2, email);
			ps.setString(3, serverRegion);
			ps.executeUpdate();

			return new Player(Utils.getAutoIncrementKey(ps), username, email, serverRegion);
		}
	}

	public static Player updateUsername(Connection cxn, Player player,
			String newUsername) throws SQLException {
		String sql = "UPDATE Player SET username = ? WHERE playerID = ?";
		try (PreparedStatement ps = cxn.prepareStatement(sql);
			) {
			ps.setString(1, newUsername);
			ps.setInt(2, player.getPlayerID());
			ps.executeUpdate();

			player.setUsername(newUsername);
			return player;
		}
	}

	public static void delete(Connection cxn, Player player)
			throws SQLException {
		String sql = "DELETE FROM Player WHERE playerID = ?";	
		try (PreparedStatement ps = cxn.prepareStatement(sql);
			) {
			ps.setInt(1, player.getPlayerID());
			ps.executeUpdate();
		}
	}

	public static List<Player> getPlayersByPartialName(Connection cxn,
			String partialName) throws SQLException {
		String sql = "SELECT playerID, username, email, serverRegion FROM Player WHERE username LIKE ?";
		try (PreparedStatement ps = cxn.prepareStatement(sql);
			) {
			ps.setString(1, "%" + partialName + "%");
			try (ResultSet rs = ps.executeQuery()) {
				List<Player> result = new ArrayList<>();
				while (rs.next()) {
					Player p = new Player();
					p.setPlayerID(rs.getInt("playerID"));
					p.setUsername(rs.getString("username"));
					p.setEmail(rs.getString("email"));
					p.setServerRegion(rs.getString("serverRegion"));
					result.add(p);
				}
				return result;
			}
		}
	}
}
