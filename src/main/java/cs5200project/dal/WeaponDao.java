package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs5200project.model.Weapon;
import cs5200project.model.Weapon.RankValue;
import cs5200project.model.Weapon.WeaponDurability;
import cs5200project.model.GearSlot;
import cs5200project.model.Item;
import cs5200project.model.Job;

public class WeaponDao {
	private static WeaponDao instance = null;
	
	private WeaponDao() {}
	
	public static WeaponDao getInstance() {
		if (instance == null) {
			instance = new WeaponDao();
		}
		return instance;
	}
	
	public Weapon create(Connection connection, 
			int itemID, String itemName, int itemLevel, int maxStackSize,
			double price, int quantity, int requiredLevel, int damage,
			int attackSpeed, String weaponType, GearSlot gearSlot, Job requiredJob,
			WeaponDurability weaponDurability, RankValue rankValue) 
			throws SQLException {

		// If itemID is 0, create a new Item in the parent table
		// and get back the auto-generated ID
		if (itemID == 0) {
			itemID = ItemDao.getInstance().create(connection, itemName, itemLevel, maxStackSize, price, quantity);
		}

		// Insert into Weapon table
		String insertWeapon = "INSERT INTO `Weapon` (itemID, requiredLevel, damage, attackSpeed, weaponType, gearSlotID, jobID, weaponDurability, rankValue) " +
							"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
		try (PreparedStatement stmt = connection.prepareStatement(insertWeapon)) {
			stmt.setInt(1, itemID);
			stmt.setInt(2, requiredLevel);
			stmt.setInt(3, damage);
			stmt.setInt(4, attackSpeed);
			stmt.setString(5, weaponType);
			stmt.setInt(6, gearSlot.getSlotID());
			stmt.setInt(7, requiredJob.getJobID());
			stmt.setString(8, weaponDurability.name());
			stmt.setString(9, rankValue.name());
			stmt.executeUpdate();
		}

		// Return a new Weapon object with the generated itemID
		return new Weapon(itemID, itemName, itemLevel, maxStackSize, price,
				quantity, requiredLevel, damage, attackSpeed, weaponType, 
				requiredJob.getJobName(), weaponDurability, rankValue);
	}

	public Weapon getWeaponById(Connection connection, int itemID)
			throws SQLException {
		String query = "SELECT i.itemName, i.itemLevel, i.maxStackSize, i.price, i.quantity, " +
					  "w.requiredLevel, w.damage, w.attackSpeed, w.weaponType, " +
					  "w.gearSlotID, w.jobID, w.weaponDurability, w.rankValue " +
					  "FROM `Weapon` w " +
					  "JOIN `Item` i ON w.itemID = i.itemID " +
					  "WHERE w.itemID = ?";

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setInt(1, itemID);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					String itemName = rs.getString("itemName");
					int itemLevel = rs.getInt("itemLevel");
					int maxStackSize = rs.getInt("maxStackSize");
					double price = rs.getDouble("price");
					int quantity = rs.getInt("quantity");
					int requiredLevel = rs.getInt("requiredLevel");
					int damage = rs.getInt("damage");
					int attackSpeed = rs.getInt("attackSpeed");
					String weaponType = rs.getString("weaponType");
					
					// Get the required job name from the Job table
					int requiredJobID = rs.getInt("jobID");
					Job job = JobDao.getInstance().getJobById(connection, requiredJobID);
					String requiredJobName = job != null ? job.getJobName() : "";
					
					WeaponDurability weaponDurability = WeaponDurability
							.valueOf(rs.getString("weaponDurability"));
					RankValue rankValue = RankValue
							.valueOf(rs.getString("rankValue"));

					return new Weapon(itemID, itemName, itemLevel, maxStackSize,
							price, quantity, requiredLevel, damage, attackSpeed, weaponType,
							requiredJobName, weaponDurability, rankValue);
				}
			}
		}
		return null;
	}

	public List<Weapon> getAllWeapons(Connection connection) throws SQLException {
		List<Weapon> weapons = new ArrayList<>();
		String query = "SELECT w.itemID, i.itemName, i.itemLevel, i.maxStackSize, i.price, i.quantity, " +
					  "w.requiredLevel, w.damage, w.attackSpeed, w.weaponType, " +
					  "w.gearSlotID, w.jobID, w.weaponDurability, w.rankValue " +
					  "FROM `Weapon` w " +
					  "JOIN `Item` i ON w.itemID = i.itemID";

		try (PreparedStatement stmt = connection.prepareStatement(query);
			 ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				int itemID = rs.getInt("itemID");
				String itemName = rs.getString("itemName");
				int itemLevel = rs.getInt("itemLevel");
				int maxStackSize = rs.getInt("maxStackSize");
				double price = rs.getDouble("price");
				int quantity = rs.getInt("quantity");
				int requiredLevel = rs.getInt("requiredLevel");
				int damage = rs.getInt("damage");
				int attackSpeed = rs.getInt("attackSpeed");
				String weaponType = rs.getString("weaponType");
				
				int requiredJobID = rs.getInt("jobID");
				Job job = JobDao.getInstance().getJobById(connection, requiredJobID);
				String requiredJobName = job != null ? job.getJobName() : "";
				
				WeaponDurability weaponDurability = WeaponDurability
						.valueOf(rs.getString("weaponDurability"));
				RankValue rankValue = RankValue
						.valueOf(rs.getString("rankValue"));

				weapons.add(new Weapon(itemID, itemName, itemLevel, maxStackSize,
						price, quantity, requiredLevel, damage, attackSpeed, weaponType,
						requiredJobName, weaponDurability, rankValue));
			}
		}
		return weapons;
	}

	public void update(Connection connection, int itemId, String itemName, int itemLevel, int maxStackSize,
					  double price, int quantity, int requiredLevel, int damage, int attackSpeed,
					  String weaponType, WeaponDurability weaponDurability, RankValue rankValue) throws SQLException {
		// Update Item table
		String updateItem = "UPDATE Item SET itemName = ?, itemLevel = ?, maxStackSize = ?, price = ?, quantity = ? WHERE itemID = ?";
		try (PreparedStatement stmt = connection.prepareStatement(updateItem)) {
			stmt.setString(1, itemName);
			stmt.setInt(2, itemLevel);
			stmt.setInt(3, maxStackSize);
			stmt.setDouble(4, price);
			stmt.setInt(5, quantity);
			stmt.setInt(6, itemId);
			stmt.executeUpdate();
		}

		// Update Weapon table
		String updateWeapon = "UPDATE Weapon SET requiredLevel = ?, damage = ?, attackSpeed = ?, weaponType = ?, weaponDurability = ?, rankValue = ? WHERE itemID = ?";
		try (PreparedStatement stmt = connection.prepareStatement(updateWeapon)) {
			stmt.setInt(1, requiredLevel);
			stmt.setInt(2, damage);
			stmt.setInt(3, attackSpeed);
			stmt.setString(4, weaponType);
			stmt.setString(5, weaponDurability.name());
			stmt.setString(6, rankValue.name());
			stmt.setInt(7, itemId);
			stmt.executeUpdate();
		}
	}

	public void delete(Connection connection, int itemId) throws SQLException {
		// First delete from Weapon table (child)
		String deleteWeapon = "DELETE FROM Weapon WHERE itemID = ?";
		try (PreparedStatement stmt = connection.prepareStatement(deleteWeapon)) {
			stmt.setInt(1, itemId);
			stmt.executeUpdate();
		}

		// Then delete from Item table (parent)
		String deleteItem = "DELETE FROM Item WHERE itemID = ?";
		try (PreparedStatement stmt = connection.prepareStatement(deleteItem)) {
			stmt.setInt(1, itemId);
			stmt.executeUpdate();
		}
	}
}
