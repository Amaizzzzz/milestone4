package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs5200project.model.GameCharacter;
import cs5200project.model.InventorySlot;
import cs5200project.model.Item;

public class InventorySlotDao {
	private static InventorySlotDao instance = null;
	
	private InventorySlotDao() {}
	
	public static InventorySlotDao getInstance() {
		if (instance == null) {
			instance = new InventorySlotDao();
		}
		return instance;
	}

	public InventorySlot create(Connection cxn, GameCharacter character,
			int slotNumber, Item item, int quantityStacked)
			throws SQLException {
		String insertInventorySlot = "INSERT INTO InventorySlot (characterID, slotNumber, itemID, quantityStacked) " +
								   "VALUES (?, ?, ?, ?);";
		try (PreparedStatement insertStmt = cxn
				.prepareStatement(insertInventorySlot)) {
			insertStmt.setInt(1, character.getCharacterID());
			insertStmt.setInt(2, slotNumber);
			insertStmt.setInt(3, item.getItemId());
			insertStmt.setInt(4, quantityStacked);
			insertStmt.executeUpdate();
			return new InventorySlot(character, slotNumber, item,
					quantityStacked);
		}
	}

	public InventorySlot getInventorySlotByCharacterIdAndSlotNumber(
			Connection cxn, int characterId, int slotNumber)
			throws SQLException {
		String selectSlot = "SELECT * FROM InventorySlot " +
						  "WHERE characterID = ? AND slotNumber = ?;";
		try (PreparedStatement selectStmt = cxn.prepareStatement(selectSlot)) {
			selectStmt.setInt(1, characterId);
			selectStmt.setInt(2, slotNumber);

			try (ResultSet results = selectStmt.executeQuery()) {
				if (results.next()) {
					int itemId = results.getInt("itemID");
					Item item = ItemDao.getInstance().getItemById(cxn, itemId);
					GameCharacter character = CharacterDao.getInstance().getCharacterById(cxn, characterId);
					return new InventorySlot(character, slotNumber, item,
							results.getInt("quantityStacked"));
				} else {
					return null;
				}
			}
		}
	}

	public List<InventorySlot> getByCharacterID(Connection cxn,
			int characterId) throws SQLException {
		String selectSlot = "SELECT * FROM InventorySlot WHERE characterID = ?;";
		List<InventorySlot> slots = new ArrayList<>();
		try (PreparedStatement stmt = cxn.prepareStatement(selectSlot)) {
			stmt.setInt(1, characterId);
			try (ResultSet results = stmt.executeQuery()) {
				while (results.next()) {
					int itemId = results.getInt("itemID");
					Item item = ItemDao.getInstance().getItemById(cxn, itemId);
					GameCharacter character = CharacterDao.getInstance().getCharacterById(cxn, characterId);
					slots.add(new InventorySlot(
							character,
							results.getInt("slotNumber"), 
							item,
							results.getInt("quantityStacked")));
				}
			}
		}
		return slots;
	}
}