package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cs5200project.model.GameCharacter;
import cs5200project.model.Gear;
import cs5200project.model.GearInstance;
import cs5200project.model.GearSlot;
import cs5200project.model.Item;

public class GearInstanceDao {
	private static GearInstanceDao instance = null;
	
	protected GearInstanceDao() {}
	
	public static GearInstanceDao getInstance() {
		if(instance == null) {
			instance = new GearInstanceDao();
		}
		return instance;
	}

	public GearInstance create(Connection connection, Gear gear,
			GameCharacter character, GearSlot gearSlot) throws SQLException {
		String insertGearInstance = "INSERT INTO `GearInstance` (itemID, characterID, gearSlotID) " +
			"VALUES (?, ?, ?)";
		try (PreparedStatement stmt = connection.prepareStatement(insertGearInstance,
				Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, gear.getItemId());
			stmt.setInt(2, character.getCharacterID());
			stmt.setInt(3, gearSlot.getSlotID());
			stmt.executeUpdate();
			
			return new GearInstance(Utils.getAutoIncrementKey(stmt), gearSlot, character, gear);
		}
	}

	public GearInstance getGearInstanceById(Connection connection, int gearInstanceId) throws SQLException {
		String selectInstance = "SELECT * FROM `GearInstance` WHERE gearInstanceID = ?";
		try (PreparedStatement selectStmt = connection.prepareStatement(selectInstance)) {
			selectStmt.setInt(1, gearInstanceId);
			try (ResultSet results = selectStmt.executeQuery()) {
				if (results.next()) {
					GearSlot slot = GearSlotDao.getInstance().getGearSlotById(connection,
							results.getInt("gearSlotID"));
					GameCharacter character = CharacterDao.getInstance().getCharacterById(connection,
							results.getInt("characterID"));
					Gear gear = GearDao.getInstance().getGearByItemID(connection,
							results.getInt("itemID"));
					return new GearInstance(gearInstanceId, slot, character, gear);
				}
				return null;
			}
		}
	}

	public List<GearInstance> getGearInstanceByItemID(Connection connection,
			Gear gear) throws SQLException {
		String selectGearInstance = "SELECT * FROM `GearInstance` WHERE itemID = ?";
		List<GearInstance> gearInstances = new ArrayList<>();
		try (PreparedStatement stmt = connection.prepareStatement(selectGearInstance)) {
			stmt.setInt(1, gear.getItemId());
			try (ResultSet results = stmt.executeQuery()) {
				while (results.next()) {
					int characterID = results.getInt("characterID");
					int gearSlotID = results.getInt("gearSlotID");

					GameCharacter character = CharacterDao.getInstance().getCharacterById(connection,
							characterID);
					GearSlot gearSlot = GearSlotDao.getInstance().getGearSlotById(connection,
							gearSlotID);

					gearInstances.add(
							new GearInstance(results.getInt("gearInstanceID"),
									gearSlot, character, gear));
				}
			}
		}
		return gearInstances;
	}

	public List<GearInstance> getAllGearInstances(Connection connection) throws SQLException {
		String selectGearInstance = "SELECT * FROM `GearInstance`";
		List<GearInstance> gearInstances = new ArrayList<>();
		try (PreparedStatement stmt = connection.prepareStatement(selectGearInstance);
			 ResultSet results = stmt.executeQuery()) {
			while (results.next()) {
				int gearInstanceId = results.getInt("gearInstanceID");
				int characterID = results.getInt("characterID");
				int gearSlotID = results.getInt("gearSlotID");
				int itemID = results.getInt("itemID");

				GameCharacter character = CharacterDao.getInstance().getCharacterById(connection, characterID);
				GearSlot gearSlot = GearSlotDao.getInstance().getGearSlotById(connection, gearSlotID);
				Gear gear = GearDao.getInstance().getGearByItemID(connection, itemID);

				gearInstances.add(new GearInstance(gearInstanceId, gearSlot, character, gear));
			}
		}
		return gearInstances;
	}

	public List<GearInstance> getGearByCharacterId(Connection connection, int characterId) throws SQLException {
		String selectGearInstance = "SELECT * FROM `GearInstance` WHERE characterID = ?";
		List<GearInstance> gearInstances = new ArrayList<>();
		try (PreparedStatement stmt = connection.prepareStatement(selectGearInstance)) {
			stmt.setInt(1, characterId);
			try (ResultSet results = stmt.executeQuery()) {
				while (results.next()) {
					int gearSlotID = results.getInt("gearSlotID");
					int itemID = results.getInt("itemID");

					GameCharacter character = CharacterDao.getInstance().getCharacterById(connection, characterId);
					GearSlot gearSlot = GearSlotDao.getInstance().getGearSlotById(connection, gearSlotID);
					Gear gear = GearDao.getInstance().getGearByItemID(connection, itemID);

					gearInstances.add(
						new GearInstance(results.getInt("gearInstanceID"),
							gearSlot, character, gear));
				}
			}
		}
		return gearInstances;
	}
}