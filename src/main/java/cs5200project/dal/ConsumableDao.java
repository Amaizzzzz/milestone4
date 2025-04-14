package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs5200project.model.Consumable;
import cs5200project.model.Consumable.ConsumablesType;

public class ConsumableDao {
	protected static ConsumableDao instance = null;
	
	protected ConsumableDao() {
		// Exists only to defeat instantiation.
	}
	
	public static ConsumableDao getInstance() {
		if(instance == null) {
			instance = new ConsumableDao();
		}
		return instance;
	}

	public Consumable create(Connection connection, String itemName, int itemLevel, int maxStackSize, 
			double price, int quantity, ConsumablesType consumablesType, String description, String source) throws SQLException {
		String insertConsumable = "INSERT INTO Consumable(itemName, itemLevel, maxStackSize, price, quantity, consumablesType, description, source) VALUES(?,?,?,?,?,?,?,?)";
		try (PreparedStatement insertStmt = connection.prepareStatement(insertConsumable)) {
			insertStmt.setString(1, itemName);
			insertStmt.setInt(2, itemLevel);
			insertStmt.setInt(3, maxStackSize);
			insertStmt.setDouble(4, price);
			insertStmt.setInt(5, quantity);
			insertStmt.setString(6, consumablesType.name());
			insertStmt.setString(7, description);
			insertStmt.setString(8, source);
			insertStmt.executeUpdate();
			
			try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					int itemId = generatedKeys.getInt(1);
					return new Consumable(itemId, itemName, itemLevel, maxStackSize, price, quantity, consumablesType, description, source);
				}
			}
		}
		return null;
	}

	public Consumable getConsumableById(Connection connection, int itemId) throws SQLException {
		String selectConsumable = "SELECT itemName, itemLevel, maxStackSize, price, quantity, consumablesType, description, source FROM Consumable WHERE itemId = ?";
		try (PreparedStatement selectStmt = connection.prepareStatement(selectConsumable)) {
			selectStmt.setInt(1, itemId);
			try (ResultSet rs = selectStmt.executeQuery()) {
				if (rs.next()) {
					return new Consumable(
						itemId,
						rs.getString("itemName"),
						rs.getInt("itemLevel"),
						rs.getInt("maxStackSize"),
						rs.getDouble("price"),
						rs.getInt("quantity"),
						ConsumablesType.valueOf(rs.getString("consumablesType")),
						rs.getString("description"),
						rs.getString("source")
					);
				}
			}
		}
		return null;
	}

	public List<Consumable> getAllConsumables(Connection connection) throws SQLException {
		List<Consumable> consumables = new ArrayList<>();
		String selectConsumables = "SELECT itemId, itemName, itemLevel, maxStackSize, price, quantity, consumablesType, description, source FROM Consumable";
		try (PreparedStatement selectStmt = connection.prepareStatement(selectConsumables)) {
			try (ResultSet rs = selectStmt.executeQuery()) {
				while (rs.next()) {
					consumables.add(new Consumable(
						rs.getInt("itemId"),
						rs.getString("itemName"),
						rs.getInt("itemLevel"),
						rs.getInt("maxStackSize"),
						rs.getDouble("price"),
						rs.getInt("quantity"),
						ConsumablesType.valueOf(rs.getString("consumablesType")),
						rs.getString("description"),
						rs.getString("source")
					));
				}
			}
		}
		return consumables;
	}

	public Consumable getByConsumablesId(Connection connection, int itemId) throws SQLException {
		return getConsumableById(connection, itemId);
	}

	public void updateQuantity(Connection connection, Consumable consumable, int newQuantity) throws SQLException {
		String updateQuantity = "UPDATE Consumable SET quantity = ? WHERE itemId = ?";
		try (PreparedStatement updateStmt = connection.prepareStatement(updateQuantity)) {
			updateStmt.setInt(1, newQuantity);
			updateStmt.setInt(2, consumable.getItemId());
			updateStmt.executeUpdate();
		}
	}

	public void delete(Connection connection, Consumable consumable) throws SQLException {
		String deleteConsumable = "DELETE FROM Consumable WHERE itemId = ?";
		try (PreparedStatement deleteStmt = connection.prepareStatement(deleteConsumable)) {
			deleteStmt.setInt(1, consumable.getItemId());
			deleteStmt.executeUpdate();
		}
	}

	public List<Consumable> getConsumablesByType(Connection connection, ConsumablesType type) throws SQLException {
		List<Consumable> consumables = new ArrayList<>();
		String selectConsumables = "SELECT itemId, itemName, itemLevel, maxStackSize, price, quantity, consumablesType, description, source FROM Consumable WHERE consumablesType = ?";
		try (PreparedStatement selectStmt = connection.prepareStatement(selectConsumables)) {
			selectStmt.setString(1, type.name());
			try (ResultSet rs = selectStmt.executeQuery()) {
				while (rs.next()) {
					consumables.add(new Consumable(
						rs.getInt("itemId"),
						rs.getString("itemName"),
						rs.getInt("itemLevel"),
						rs.getInt("maxStackSize"),
						rs.getDouble("price"),
						rs.getInt("quantity"),
						ConsumablesType.valueOf(rs.getString("consumablesType")),
						rs.getString("description"),
						rs.getString("source")
					));
				}
			}
		}
		return consumables;
	}
}