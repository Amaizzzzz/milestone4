package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cs5200project.model.GearSlot;

public class GearSlotDao {
	private static GearSlotDao instance = null;
	
	protected GearSlotDao() {}
	
	public static GearSlotDao getInstance() {
		if(instance == null) {
			instance = new GearSlotDao();
		}
		return instance;
	}

	public GearSlot create(Connection connection, String slotName) throws SQLException {
		String insertGearSlot = "INSERT INTO GearSlot(slotName) VALUES(?)";
		try (PreparedStatement stmt = connection.prepareStatement(insertGearSlot,
				Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, slotName);
			stmt.executeUpdate();
			
			return new GearSlot(Utils.getAutoIncrementKey(stmt), slotName);
		}
	}

	public GearSlot getGearSlotById(Connection connection, int slotId) throws SQLException {
		String selectGearSlot = "SELECT * FROM GearSlot WHERE slotID = ?";
		try (PreparedStatement stmt = connection.prepareStatement(selectGearSlot)) {
			stmt.setInt(1, slotId);
			try (ResultSet results = stmt.executeQuery()) {
				if (results.next()) {
					return new GearSlot(results.getInt("slotID"),
							results.getString("slotName"));
				}
				return null;
			}
		}
	}

	public List<GearSlot> getAllGearSlots(Connection connection) throws SQLException {
		String selectGearSlot = "SELECT * FROM GearSlot";
		List<GearSlot> gearSlots = new ArrayList<>();
		try (PreparedStatement stmt = connection.prepareStatement(selectGearSlot);
			 ResultSet results = stmt.executeQuery()) {
			while (results.next()) {
				gearSlots.add(new GearSlot(results.getInt("slotID"),
						results.getString("slotName")));
			}
		}
		return gearSlots;
	}

	public static List<GearSlot> getGearSlotsByName(Connection cxn,
			String slotName)
			throws SQLException {
		final String selectGearSlot = "SELECT * FROM GearSlot WHERE slotName = ?;";
		try (PreparedStatement stmt = cxn.prepareStatement(selectGearSlot);
				ResultSet results = stmt.executeQuery()) {
			List<GearSlot> gearSlots = new ArrayList<>();
			while (results.next()) {
				gearSlots.add(new GearSlot(results.getInt("slotID"), slotName));
			}
			return gearSlots;
		}
	}

	public static void delete(Connection cxn, GearSlot gearSlot)
			throws SQLException {
		final String deleteGearSlot = "DELETE FROM GearSlot WHERE slotID = ?;";
		try (PreparedStatement deleteStmt = cxn
				.prepareStatement(deleteGearSlot)) {
			deleteStmt.setInt(1, gearSlot.getSlotID());
			deleteStmt.executeUpdate();
		}
	}
}

