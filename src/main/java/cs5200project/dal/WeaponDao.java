package cs5200project.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    protected WeaponDao() {
        // Prevent instantiation
    }

    public static WeaponDao getInstance() {
        if (instance == null) {
            instance = new WeaponDao();
        }
        return instance;
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
        String insertWeapon = "INSERT INTO Weapon (itemID, requiredLevel, damage, attackSpeed, weaponType, gearSlotID, jobID, weaponDurability, rankValue) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
        try (PreparedStatement stmt = cxn.prepareStatement(insertWeapon)) {
            stmt.setInt(1, itemID);
            stmt.setInt(2, requiredLevel);
            stmt.setInt(3, damage);
            stmt.setInt(4, attackSpeed);
            stmt.setString(5, weaponType);
            stmt.setInt(6, gearSlot.getGearSlotID());
            stmt.setInt(7, requiredJob.getJobID());
            stmt.setString(8, weaponDurability.name());
            stmt.setString(9, rankValue.name());
            stmt.executeUpdate();

            return new Weapon(itemID, itemName, itemLevel, maxStackSize, price, quantity, 
                    requiredLevel, damage, attackSpeed, weaponType, requiredJob,
                    weaponDurability, rankValue);
        }
    }

    public static Weapon getWeaponById(Connection cxn, int weaponId) throws SQLException {
        String selectWeapon = "SELECT w.*, i.itemName, i.itemLevel, i.maxStackSize, i.price, i.quantity, " +
                            "j.jobName, g.gearSlotName " +
                            "FROM Weapon w " +
                            "JOIN Item i ON w.itemID = i.itemID " +
                            "JOIN Job j ON w.jobID = j.jobID " +
                            "JOIN GearSlot g ON w.gearSlotID = g.gearSlotID " +
                            "WHERE w.itemID = ?";

        try (PreparedStatement stmt = cxn.prepareStatement(selectWeapon)) {
            stmt.setInt(1, weaponId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Weapon(
                        rs.getInt("itemID"),
                        rs.getString("itemName"),
                        rs.getInt("itemLevel"),
                        rs.getInt("maxStackSize"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getInt("requiredLevel"),
                        rs.getInt("damage"),
                        rs.getInt("attackSpeed"),
                        rs.getString("weaponType"),
                        rs.getString("jobName"),
                        WeaponDurability.valueOf(rs.getString("weaponDurability")),
                        RankValue.valueOf(rs.getString("rankValue"))
                    );
                }
                return null;
            }
        }
    }

    public static List<Weapon> getAllWeapons(Connection cxn) throws SQLException {
        String selectWeapons = "SELECT w.*, i.itemName, i.itemLevel, i.maxStackSize, i.price, i.quantity, " +
                             "j.jobName, g.gearSlotName " +
                             "FROM Weapon w " +
                             "JOIN Item i ON w.itemID = i.itemID " +
                             "JOIN Job j ON w.jobID = j.jobID " +
                             "JOIN GearSlot g ON w.gearSlotID = g.gearSlotID";

        List<Weapon> weapons = new ArrayList<>();
        try (PreparedStatement stmt = cxn.prepareStatement(selectWeapons);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                weapons.add(new Weapon(
                    rs.getInt("itemID"),
                    rs.getString("itemName"),
                    rs.getInt("itemLevel"),
                    rs.getInt("maxStackSize"),
                    rs.getDouble("price"),
                    rs.getInt("quantity"),
                    rs.getInt("requiredLevel"),
                    rs.getInt("damage"),
                    rs.getInt("attackSpeed"),
                    rs.getString("weaponType"),
                    rs.getString("jobName"),
                    WeaponDurability.valueOf(rs.getString("weaponDurability")),
                    RankValue.valueOf(rs.getString("rankValue"))
                ));
            }
        }
        return weapons;
    }
}
