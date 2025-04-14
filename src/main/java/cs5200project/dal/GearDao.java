package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cs5200project.model.Gear;
import cs5200project.model.Item;

public class GearDao {
	protected static GearDao instance = null;
	
	protected GearDao() {
		// Exists only to defeat instantiation.
	}
	
	public static GearDao getInstance() {
		if(instance == null) {
			instance = new GearDao();
		}
		return instance;
	}

	public Gear create(Connection connection, String itemName, int itemLevel, int maxStackSize, double price, int quantity, int gearSlotId) throws SQLException {
		String insertGear = "INSERT INTO Gear(itemName, itemLevel, maxStackSize, price, quantity, gearSlotId) VALUES(?,?,?,?,?,?)";
		try (PreparedStatement insertStmt = connection.prepareStatement(insertGear)) {
			insertStmt.setString(1, itemName);
			insertStmt.setInt(2, itemLevel);
			insertStmt.setInt(3, maxStackSize);
			insertStmt.setDouble(4, price);
			insertStmt.setInt(5, quantity);
			insertStmt.setInt(6, gearSlotId);
			insertStmt.executeUpdate();
			
			try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					int itemId = generatedKeys.getInt(1);
					return new Gear(itemId, itemName, itemLevel, maxStackSize, price, quantity, gearSlotId);
				}
			}
		}
		return null;
	}

	public Gear getGearById(Connection connection, int itemId) throws SQLException {
		String selectGear = "SELECT itemName, itemLevel, maxStackSize, price, quantity, gearSlotId FROM Gear WHERE itemId = ?";
		try (PreparedStatement selectStmt = connection.prepareStatement(selectGear)) {
			selectStmt.setInt(1, itemId);
			try (ResultSet rs = selectStmt.executeQuery()) {
				if (rs.next()) {
					return new Gear(
						itemId,
						rs.getString("itemName"),
						rs.getInt("itemLevel"),
						rs.getInt("maxStackSize"),
						rs.getDouble("price"),
						rs.getInt("quantity"),
						rs.getInt("gearSlotId")
					);
				}
			}
		}
		return null;
	}

	public Gear getGearByItemID(Connection connection, int itemId) throws SQLException {
		return getGearById(connection, itemId);
	}

	public List<Gear> getAllGear(Connection connection) throws SQLException {
		List<Gear> gearList = new ArrayList<>();
		String selectGear = "SELECT itemId, itemName, itemLevel, maxStackSize, price, quantity, gearSlotId FROM Gear";
		try (PreparedStatement selectStmt = connection.prepareStatement(selectGear)) {
			try (ResultSet rs = selectStmt.executeQuery()) {
				while (rs.next()) {
					gearList.add(new Gear(
						rs.getInt("itemId"),
						rs.getString("itemName"),
						rs.getInt("itemLevel"),
						rs.getInt("maxStackSize"),
						rs.getDouble("price"),
						rs.getInt("quantity"),
						rs.getInt("gearSlotId")
					));
				}
			}
		}
		return gearList;
	}

	public List<Gear> getGearByJob(Connection connection, int jobId) throws SQLException {
		List<Gear> gearList = new ArrayList<>();
		String selectGear = "SELECT g.itemId, g.itemName, g.itemLevel, g.maxStackSize, g.price, g.quantity, g.gearSlotId " +
						  "FROM Gear g JOIN GearJob gj ON g.itemId = gj.gearId WHERE gj.jobId = ?";
		try (PreparedStatement selectStmt = connection.prepareStatement(selectGear)) {
			selectStmt.setInt(1, jobId);
			try (ResultSet rs = selectStmt.executeQuery()) {
				while (rs.next()) {
					gearList.add(new Gear(
						rs.getInt("itemId"),
						rs.getString("itemName"),
						rs.getInt("itemLevel"),
						rs.getInt("maxStackSize"),
						rs.getDouble("price"),
						rs.getInt("quantity"),
						rs.getInt("gearSlotId")
					));
				}
			}
		}
		return gearList;
	}

	public void updateQuantity(Connection connection, Gear gear, int quantity) throws SQLException {
		ItemDao.getInstance().updateQuantity(connection, gear, quantity);
	}

	public void delete(Connection connection, Gear gear) throws SQLException {
		ItemDao.getInstance().delete(connection, gear);
	}
}
