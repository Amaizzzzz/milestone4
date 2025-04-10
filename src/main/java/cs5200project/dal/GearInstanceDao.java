package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cs5200project.model.GearInstance;
import cs5200project.model.GameCharacter;
import cs5200project.model.Gear;
import cs5200project.model.GearSlot;
import cs5200project.model.Item;

public class GearInstanceDao {
	private static GearInstanceDao instance = null;
	private CharacterDao characterDao;

	protected GearInstanceDao() {
		characterDao = CharacterDao.getInstance();
	}

	public static GearInstanceDao getInstance() {
		if (instance == null) {
			instance = new GearInstanceDao();
		}
		return instance;
	}

	public GearInstance create(Connection connection, Gear gear, GameCharacter character, GearSlot slot) throws SQLException {
		String insertQuery = "INSERT INTO `GearInstance` (gearSlotID, characterID, itemID) VALUES (?, ?, ?)";
		
		try (PreparedStatement statement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
			statement.setInt(1, slot.getSlotID());
			statement.setInt(2, character.getCharacterID());
			statement.setInt(3, gear.getItemId());
			statement.executeUpdate();
			
			try (ResultSet keys = statement.getGeneratedKeys()) {
				if (keys.next()) {
					return new GearInstance(keys.getInt(1), slot, character, gear);
				}
				throw new SQLException("Failed to get generated key.");
			}
		}
	}

	public GearInstance getGearInstanceByGearInstanceId(Connection cxn, GearInstance gearInstance) throws SQLException {
		String selectInstance = "SELECT * FROM `GearInstance` WHERE gearInstanceID = ?";
		try (PreparedStatement selectStmt = cxn.prepareStatement(selectInstance)) {
			selectStmt.setInt(1, gearInstance.getGearInstanceID());
			try (ResultSet results = selectStmt.executeQuery()) {
				if (results.next()) {
					GearSlot slot = GearSlotDao.getGearSlotById(cxn, results.getInt("gearSlotID"));
					GameCharacter character = characterDao.getCharacterById(cxn, results.getInt("characterID"));
					Gear gear = GearDao.getGearByItemID(cxn, results.getInt("itemID"));
					return new GearInstance(gearInstance.getGearInstanceID(), slot, character, gear);
				}
				return null;
			}
		}
	}

	public List<GearInstance> getGearInstanceByItemID(Connection cxn, Gear gear) throws SQLException {
		String selectGearInstance = "SELECT * FROM `GearInstance` WHERE itemID = ?";
		List<GearInstance> gearInstances = new ArrayList<>();
		try (PreparedStatement stmt = cxn.prepareStatement(selectGearInstance)) {
			stmt.setInt(1, gear.getItemId());
			try (ResultSet results = stmt.executeQuery()) {
				while (results.next()) {
					int characterID = results.getInt("characterID");
					int gearSlotID = results.getInt("gearSlotID");

					GameCharacter character = characterDao.getCharacterById(cxn, characterID);
					GearSlot gearSlot = GearSlotDao.getGearSlotById(cxn, gearSlotID);

					gearInstances.add(new GearInstance(results.getInt("gearInstanceID"), gearSlot, character, gear));
				}
			}
		}
		return gearInstances;
	}

	public static List<GearInstance> getAllGearInstances(Connection connection) throws SQLException {
		List<GearInstance> gearInstances = new ArrayList<>();
		String sql = "SELECT gi.gearInstanceID, gi.gearSlotID, gi.characterID, gi.itemID " +
					"FROM GearInstance gi";
		
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					GearSlot slot = GearSlotDao.getGearSlotById(connection, rs.getInt("gearSlotID"));
					GameCharacter character = CharacterDao.getInstance().getCharacterById(connection, rs.getInt("characterID"));
					Gear gear = GearDao.getGearByItemID(connection, rs.getInt("itemID"));
					
					GearInstance gearInstance = new GearInstance(
						rs.getInt("gearInstanceID"),
						slot,
						character,
						gear
					);
					gearInstances.add(gearInstance);
				}
			}
		}
		return gearInstances;
	}
	
	public static GearInstance getGearInstanceById(Connection connection, int instanceId) throws SQLException {
		String sql = "SELECT gi.gearInstanceID, gi.gearSlotID, gi.characterID, gi.itemID " +
					"FROM GearInstance gi " +
					"WHERE gi.gearInstanceID = ?";
		
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, instanceId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					GearSlot slot = GearSlotDao.getGearSlotById(connection, rs.getInt("gearSlotID"));
					GameCharacter character = CharacterDao.getInstance().getCharacterById(connection, rs.getInt("characterID"));
					Gear gear = GearDao.getGearByItemID(connection, rs.getInt("itemID"));
					
					return new GearInstance(
						rs.getInt("gearInstanceID"),
						slot,
						character,
						gear
					);
				}
			}
		}
		return null;
	}
}