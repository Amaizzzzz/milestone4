package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import cs5200project.model.GearSlot;
import cs5200project.model.Job;
import cs5200project.model.Weapon;
import cs5200project.model.Weapon.RankValue;
import cs5200project.model.Weapon.WeaponDurability;
import cs5200project.model.WeaponType;

public class WeaponDao {

	private WeaponDao() {
		// Prevent instantiation
	}

	
	public static Weapon create(Connection cxn, 
			int itemID, String itemName, int itemLevel, int maxStackSize,
			double price, int quantity, int requiredLevel, int damage,
			int attackSpeed, String weaponType, GearSlot gearSlot, Job requiredJob,
			WeaponDurability weaponDurability, RankValue rankValue) 
			throws SQLException {

		// If itemID is 0, create a new Item in the parent table
		// and get back the auto-generated ID
		if (itemID == 0) {
			itemID = ItemDao.create(cxn, itemName, itemLevel, maxStackSize, price, quantity);
		}

		// Insert into Weapon table
		final String insertWeapon = "INSERT INTO Weapon(itemID, weaponTypeID, damage, durability) " +
				"VALUES(?,?,?,?)";
		try (PreparedStatement statement = cxn.prepareStatement(insertWeapon,
				Statement.RETURN_GENERATED_KEYS)) {
			statement.setInt(1, itemID);
			statement.setInt(2, weaponType.hashCode());
			statement.setInt(3, damage);
			statement.setString(4, weaponDurability.name());
			statement.executeUpdate();
		}

		// Return a new Weapon object with the generated itemID
		return new Weapon(itemID, itemName, itemLevel, maxStackSize, price,
				quantity, requiredLevel, damage, attackSpeed, weaponType, 
				requiredJob.getJobName(), weaponDurability, rankValue);
	}


	public static Weapon getWeaponById(Connection cxn, int itemID)
			throws SQLException {
		String query = "SELECT i.itemName, i.itemLevel, i.maxStackSize, i.price, i.quantity, " +
				"w.requiredLevel, w.damage, w.attackSpeed, w.weaponType, " +
				"w.gearSlotID, w.jobID, w.weaponDurability, w.rankValue " +
				"FROM `Weapon` w " +
				"JOIN `Item` i ON w.itemID = i.itemID " +
				"WHERE w.itemID = ?";

		try (PreparedStatement stmt = cxn.prepareStatement(query)) {
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
					Job job = JobDao.getJobById(cxn, requiredJobID);
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

    public List<Weapon> getWeaponsByType(Connection connection, WeaponType type) throws SQLException {
        String query = "SELECT w.itemID, w.weaponTypeID, w.damage, w.durability, " +
                "i.name, i.description, i.source " +
                "FROM Weapon w " +
                "JOIN Item i ON w.itemID = i.itemID " +
                "WHERE w.weaponTypeID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, type.hashCode());
            try (ResultSet rs = statement.executeQuery()) {
                // Implementation of getWeaponsByType method
            }
        }
        return null; // Placeholder return, actual implementation needed
    }
}
