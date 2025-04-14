package cs5200project.driver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cs5200project.dal.CharacterCurrencyDao;
import cs5200project.dal.CharacterDao;
import cs5200project.dal.CharacterJobDao;
import cs5200project.dal.CharacterStatsDao;
import cs5200project.dal.ConnectionManager;
import cs5200project.dal.ConsumableDao;
import cs5200project.dal.ConsumablesStatsBonusDao;
import cs5200project.dal.CurrencyDao;
import cs5200project.dal.GearDao;
import cs5200project.dal.GearInstanceDao;
import cs5200project.dal.GearJobDao;
import cs5200project.dal.GearSlotDao;
import cs5200project.dal.JobDao;
import cs5200project.dal.PlayerDao;
import cs5200project.dal.RaceDao;
import cs5200project.dal.StatTypeDao;
import cs5200project.dal.StatisticDao;
import cs5200project.dal.WeaponDao;
import cs5200project.dal.WeaponStatsBonusDao;
import cs5200project.model.CharacterCurrency;
import cs5200project.model.Consumable;
import cs5200project.model.Consumable.ConsumablesType;
import cs5200project.model.ConsumablesStatsBonus;
import cs5200project.model.Currency;
import cs5200project.model.Gear;
import cs5200project.model.GearInstance;
import cs5200project.model.GearSlot;
import cs5200project.model.Job;
import cs5200project.model.Player;
import cs5200project.model.Race;
import cs5200project.model.StatType;
import cs5200project.model.Statistic;
import cs5200project.model.Weapon;
import cs5200project.model.Weapon.RankValue;
import cs5200project.model.Weapon.WeaponDurability;

public class Driver {
    public static void main(String[] args) {
        try {
            resetSchema();
            insertRecords();
        } catch (SQLException e) {
            System.out.print("SQL Exception: ");
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void insertRecords() throws SQLException {
        try (Connection cxn = ConnectionManager.getConnection()) {
            // Create test data for each table
            System.out.println("Creating test data...");

            // 1. Create Player
            Player player = PlayerDao.create(cxn, "John Doe", "john@example.com", "NA");
            System.out.println("Created player: " + player.getUsername());

			// 2. Create 6 Races
			String[] raceNames = { "Human", "Elf", "Orc", "Dwarf", "Gnome",
					"Dragonkin" };
			Race[] races = new Race[raceNames.length];

			for (int i = 0; i < raceNames.length; i++) {
				races[i] = RaceDao.create(cxn, raceNames[i]);
				System.out.println("Created race: " + races[i].getRaceName());
			}

			// 3. Create 5 Jobs
			String[] jobNames = { "Warrior", "Mage", "Ranger", "Paladin",
					"Thief" };
			Job[] jobs = new Job[jobNames.length];

			for (int i = 0; i < jobNames.length; i++) {
				jobs[i] = JobDao.create(cxn, jobNames[i]);
				System.out.println("Created job: " + jobs[i].getJobName());
			}

            // 4. Create GearSlot
            GearSlot headSlot = GearSlotDao.create(cxn, "Head");
            GearSlot chestSlot = GearSlotDao.create(cxn, "Chest");
            GearSlot mainHandSlot = GearSlotDao.create(cxn, "MainHand");
			GearSlot[] gearSlots = { headSlot, chestSlot, mainHandSlot };
            System.out.println("Created gear slots: Head, Chest, MainHand");

			// 4. Create 10 Characters
			String[] firstNames = { "Hannah", "Alice", "Julia", "Charlie",
					"Diana", "Ethan", "Fiona", "George", "Ivan", "Bob" };
			String[] lastNames = { "Young", "Smith", "Lee", "Brown", "Johnson",
					"Davis", "Wilson", "Clark", "Lopez", "Lee", };

			List<cs5200project.model.Character> characters = new ArrayList<>();
			Date baseTime = new Date();
			for (int i = 0; i < 10; i++) {
				String firstName = firstNames[i];
				String lastName = lastNames[i];
				boolean isNewPlayer = (i % 2 == 0); // alternate

				Race race = races[i % races.length]; // cycle through races
				Job job = jobs[i % jobs.length]; // cycle through jobs
				Date creationTime = new Date(baseTime.getTime() + (i * 1000L));

				cs5200project.model.Character character = CharacterDao.create(
						cxn, player, firstName, lastName, race,
						creationTime, isNewPlayer, job);
				characters.add(character); // store for stats creation

				System.out.println("Created character: " + firstName + " "
						+ lastName + " (Race: "
						+ races[i % races.length].getRaceName() + ", Job: "
						+ jobs[i % jobs.length].getJobName() + ")");
			}

            // 6. Create Statistic Types
            StatType hpStatType = StatTypeDao.create(cxn, "HP", "Hit Points");
            StatType mpStatType = StatTypeDao.create(cxn, "MP", "Magic Points");
            StatType strengthStatType = StatTypeDao.create(cxn, "STR", "Strength");
            System.out.println("Created stat types: HP, MP, STR");

            // 7. Create Statistics
            Statistic hpStat = StatisticDao.create(cxn, hpStatType.getStatTypeID(), 100);
            Statistic mpStat = StatisticDao.create(cxn, mpStatType.getStatTypeID(), 50);
            Statistic strStat = StatisticDao.create(cxn, strengthStatType.getStatTypeID(), 10);
            System.out.println("Created statistics for HP, MP, and STR");

            // 8. Create Character Stats
			for (cs5200project.model.Character c : characters) {
				CharacterStatsDao.create(cxn, c, hpStat,
						150 + (c.getCharacterID() % 3) * 10); // Slightly vary
																// values
				CharacterStatsDao.create(cxn, c, mpStat,
						80 + (c.getCharacterID() % 2) * 5);
				CharacterStatsDao.create(cxn, c, strStat,
						15 + (c.getCharacterID() % 4));
				System.out.println("Created character stats for character: "
						+ c.getCharacterID());
			}

            // 9. Create Character Job
			for (int i = 0; i < characters.size(); i++) {
				cs5200project.model.Character c = characters.get(i);
				Job currentJob = jobs[i % jobs.length];
				CharacterJobDao.create(cxn, c, currentJob, true, 50 + (i * 10));
				System.out.println(
						"Created character job mappings for character: "
								+ c.getCharacterID() + " → "
								+ currentJob.getJobName() + " (primary), ");
			}

			// 10. Create 5 Weapons
            // Note: We don't need to create Item separately as WeaponDao.create does that for us
			String[] weaponNames = { "Iron Sword", "Magic Staff", "Longbow",
					"Holy Blade", "Dagger" };
			String[] weaponTypes = { "Sword", "Staff", "Bow", "Sword",
					"Knife" };
			int[] damages = { 15, 10, 12, 18, 9 };
			int[] speeds = { 2, 3, 2, 1, 4 };
			Weapon[] weapons = new Weapon[weaponNames.length];

			for (int i = 0; i < weaponNames.length; i++) {
				Job job = jobs[i % jobs.length]; // Assign each weapon to a job
				weapons[i] = WeaponDao.create(cxn, 0, // create new item
						weaponNames[i], 10 + i, // itemLevel
						1, // maxStackSize
						100 + i * 20.0, // price
						1, // quantity
						5 + i, // requiredLevel
						damages[i], speeds[i], weaponTypes[i], mainHandSlot,
						job, WeaponDurability.NEW,
						RankValue.values()[i % RankValue.values().length]
				);
				System.out.println("Created weapon: " + weapons[i].getItemName()
						+ " for job: " + job.getJobName());
			}

            // 11. Create Consumable
            // Note: We don't need to create Item separately as ConsumableDao.create does that for us
            Consumable healthPotion = ConsumableDao.create(cxn,
                0,  // itemID=0 means create a new Item
                "Health Potion",
                5,  // itemLevel
                10, // maxStackSize
                50.0, // price
                5,  // quantity
                ConsumablesType.MEDICINE, // consumablesType
                "Restores 50 HP when consumed", // description
                "Apothecary" // source
            );
            System.out.println("Created consumable: " + healthPotion.getItemName() + " with ID: " + healthPotion.getItemId());

			// 12. Create 3 Gears
            // Note: We don't need to create Item separately as GearDao.create does that for us
			String[] gearNames = { "Iron Helmet", "Chainmail Chest",
					"Steel Boots" };
			int[] gearLevels = { 10, 12, 14 };
			double[] gearPrices = { 75.0, 120.0, 95.0 };
			Gear[] gears = new Gear[gearNames.length];
			for (int i = 0; i < gearNames.length; i++) {
				gears[i] = GearDao.create(cxn, 0, // new item
						gearNames[i], gearLevels[i], 1, // maxStackSize
						gearPrices[i], // price
						1, // quantity
						5 + i // requiredLevel
				);
				System.out.println("Created gear: " + gears[i].getItemName()
						+ " with ID: " + gears[i].getItemId());
			}

			// 13. Create GearInstances for characters (1 gear per character)
			for (int i = 0; i < characters.size(); i++) {
				cs5200project.model.Character c = characters.get(i);
				Gear gear = gears[i % gears.length]; // cycle through gears
				GearSlot slot = gearSlots[i % gearSlots.length];

				GearInstance instance = GearInstanceDao.create(cxn, gear, c,
						slot);
				System.out.println(
						"Created gear instance of " + gear.getItemName()
								+ " for character " + c.getCharacterID());
			}

			// 14. Create GearJob (associate each gear with one job)
			for (int i = 0; i < gears.length; i++) {
				int jobID = jobs[i % jobs.length].getJobID();
				Gear gear = gears[i];

				GearJobDao.create(cxn, gear.getItemId(), jobID);
				System.out.println("Created gear job mapping for gear: "
						+ gear.getItemName() + " → "
						+ jobs[i % jobs.length].getJobName());
			}

			// 15. Create WeaponStatsBonus for each weapon (STR + i)
			for (int i = 0; i < weapons.length; i++) {
				Weapon weapon = weapons[i];
				WeaponStatsBonusDao.create(cxn, weapon, strStat, 5 + i);
				System.out.println("Created weapon stat bonus for weapon: "
						+ weapon.getItemName() + " (+STR " + (5 + i) + ")");
			}

            // 16. Create ConsumablesStatsBonus
            ConsumablesStatsBonus potionHpBonus = ConsumablesStatsBonusDao.create(cxn, 
                healthPotion, 
                hpStat, 
                0.5f, // percentageBonus (50%)
                50    // bonusCap
            );
            System.out.println("Created consumable stat bonus for consumable: " + healthPotion.getItemName());

            // 17. Create Currency
            Currency gold = CurrencyDao.create(cxn, "Gold", 999999, 100000);
            System.out.println("Created currency: " + gold.getCurrencyName());

			// 18. Create CharacterCurrency for all characters
			for (int i = 0; i < characters.size(); i++) {
				cs5200project.model.Character c = characters.get(i);

				int amount = 1000 + (i * 50); // Vary amount: 1000, 1050, 1100,
												// ...

            CharacterCurrency charGold = CharacterCurrencyDao.create(cxn, 
						c.getCharacterID(), gold.getCurrencyID(), amount, true // isCurrent
            );
				System.out.println("Created character currency for character "
						+ c.getCharacterID() + " with amount: " + amount);
			}

            System.out.println("All test data created successfully!");
        }
    }

    private static void resetSchema() throws SQLException {
		try (Connection cxn = ConnectionManager.getSchemalessConnection()) {
			cxn.createStatement().executeUpdate("DROP SCHEMA IF EXISTS cs5200project;");
			cxn.createStatement().executeUpdate("CREATE SCHEMA cs5200project;");
		}
		try (Connection cxn = ConnectionManager.getConnection()) {

            System.out.println("Dropping existing tables...");
            
            // Drop all tables in reverse order of dependencies
            String[] tables = {
                "CharacterCurrency", "CharacterStats", "CharacterJob", 
                "ConsumablesStatsBonus", "WeaponStatsBonus", "GearStatisticBonus",
                "GearInstance", "GearJob", "InventorySlot", 
                "Weapon", "Consumable", "Gear", "Item",
                "`Character`", "Statistic", "StatType",
                "GearSlot", "Clan", "Job", "Race", "Player", "Currency", 
                "LevelThreshold"
            };

            for (String table : tables) {
                try {
                    cxn.createStatement().executeUpdate("DROP TABLE IF EXISTS " + table);
                    System.out.println("Dropped table: " + table);
                } catch (SQLException e) {
                    System.out.println("Warning: Could not drop table " + table + ": " + e.getMessage());
                }
            }

            System.out.println("Creating new tables...");

            // Create tables in order of dependencies
            String[] createTableStatements = {
                "CREATE TABLE IF NOT EXISTS Player (" +
                "playerID INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(50) NOT NULL UNIQUE, " +
                "email VARCHAR(100) NOT NULL UNIQUE, " +
                "serverRegion VARCHAR(50) NOT NULL" +
                ")",

                "CREATE TABLE IF NOT EXISTS Race (" +
                "raceID INT AUTO_INCREMENT PRIMARY KEY, " +
                "raceName VARCHAR(50) NOT NULL UNIQUE" +
                ")",

                "CREATE TABLE IF NOT EXISTS Job (" +
                "jobID INT AUTO_INCREMENT PRIMARY KEY, " +
                "jobName VARCHAR(50) NOT NULL UNIQUE" +
                ")",

                "CREATE TABLE IF NOT EXISTS Character (" +
                "characterID INT AUTO_INCREMENT PRIMARY KEY, " +
                "playerID INT NOT NULL, " +
                "firstName VARCHAR(50) NOT NULL, " +
                "lastName VARCHAR(50) NOT NULL, " +
                "raceID INT NOT NULL, " +
                "creationTime TIMESTAMP NOT NULL, " +
                "isNewPlayer BOOLEAN NOT NULL, " +
                "currentJobID INT NOT NULL, " +
                "FOREIGN KEY (playerID) REFERENCES Player(playerID) ON DELETE CASCADE, " +
                "FOREIGN KEY (raceID) REFERENCES Race(raceID) ON DELETE CASCADE, " +
                "FOREIGN KEY (currentJobID) REFERENCES Job(jobID) ON DELETE CASCADE" +
                ")",

                "CREATE TABLE IF NOT EXISTS CharacterJob (" +
                "characterID INT NOT NULL, " +
                "jobID INT NOT NULL, " +
                "isUnlocked BOOLEAN NOT NULL, " +
                "XP INT NOT NULL, " +
                "PRIMARY KEY (characterID, jobID), " +
                "FOREIGN KEY (characterID) REFERENCES Character(characterID) ON DELETE CASCADE, " +
                "FOREIGN KEY (jobID) REFERENCES Job(jobID) ON DELETE CASCADE" +
                ")",

                "CREATE TABLE IF NOT EXISTS Clan (" +
                "clanID INT AUTO_INCREMENT PRIMARY KEY, " +
                "clanName VARCHAR(50) NOT NULL UNIQUE, " +
                "raceID INT NOT NULL, " +
                "FOREIGN KEY (raceID) REFERENCES Race(raceID) ON DELETE CASCADE" +
                ")",

                "CREATE TABLE IF NOT EXISTS Item (" +
                "itemID INT AUTO_INCREMENT PRIMARY KEY, " +
                "itemName VARCHAR(100) NOT NULL, " +
                "itemLevel INT NOT NULL, " +
                "maxStackSize INT NOT NULL, " +
                "price DECIMAL(10,2) NOT NULL, " +
                "quantity INT NOT NULL" +
                ")",

                "CREATE TABLE IF NOT EXISTS GearSlot (" +
                "slotID INT AUTO_INCREMENT PRIMARY KEY, " +
                "slotName VARCHAR(50) NOT NULL UNIQUE" +
                ")",

                "CREATE TABLE IF NOT EXISTS Weapon (" +
                "itemID INT PRIMARY KEY, " +
                "requiredLevel INT NOT NULL, " +
                "damage INT NOT NULL, " +
                "attackSpeed INT NOT NULL, " +
                "weaponType VARCHAR(50) NOT NULL, " +
                "gearSlotID INT NOT NULL, " +
                "jobID INT NOT NULL, " +
                "weaponDurability VARCHAR(50) NOT NULL, " +
                "rankValue VARCHAR(50) NOT NULL, " +
                "FOREIGN KEY (itemID) REFERENCES Item(itemID) ON DELETE CASCADE, " +
                "FOREIGN KEY (gearSlotID) REFERENCES GearSlot(slotID) ON DELETE CASCADE, " +
                "FOREIGN KEY (jobID) REFERENCES Job(jobID) ON DELETE CASCADE" +
                ")",

                "CREATE TABLE IF NOT EXISTS Consumable (" +
                "itemID INT PRIMARY KEY, " +
                "consumablesType VARCHAR(50) NOT NULL, " +
                "description TEXT NOT NULL, " +
                "source VARCHAR(100) NOT NULL, " +
                "FOREIGN KEY (itemID) REFERENCES Item(itemID) ON DELETE CASCADE" +
                ")",

                "CREATE TABLE IF NOT EXISTS Gear (" +
                "itemID INT PRIMARY KEY, " +
                "requiredLevel INT NOT NULL, " +
                "gearSlotID INT NOT NULL, " +
                "gearDurability VARCHAR(50) NOT NULL, " +
                "rankValue VARCHAR(50) NOT NULL, " +
                "FOREIGN KEY (itemID) REFERENCES Item(itemID) ON DELETE CASCADE, " +
                "FOREIGN KEY (gearSlotID) REFERENCES GearSlot(slotID) ON DELETE CASCADE" +
                ")",

                "CREATE TABLE IF NOT EXISTS GearJob (" +
                "itemID INT NOT NULL, " +
                "jobID INT NOT NULL, " +
                "PRIMARY KEY (itemID, jobID), " +
                "FOREIGN KEY (itemID) REFERENCES Gear(itemID) ON DELETE CASCADE, " +
                "FOREIGN KEY (jobID) REFERENCES Job(jobID) ON DELETE CASCADE" +
                ")",

                "CREATE TABLE IF NOT EXISTS Statistic (" +
                "statID INT AUTO_INCREMENT PRIMARY KEY, " +
                "statName VARCHAR(50) NOT NULL UNIQUE" +
                ")",

                "CREATE TABLE IF NOT EXISTS WeaponStatsBonus (" +
                "itemID INT NOT NULL, " +
                "statID INT NOT NULL, " +
                "bonusValue INT NOT NULL, " +
                "PRIMARY KEY (itemID, statID), " +
                "FOREIGN KEY (itemID) REFERENCES Weapon(itemID) ON DELETE CASCADE, " +
                "FOREIGN KEY (statID) REFERENCES Statistic(statID) ON DELETE CASCADE" +
                ")",

                "CREATE TABLE IF NOT EXISTS GearStatisticBonus (" +
                "itemID INT NOT NULL, " +
                "statID INT NOT NULL, " +
                "bonusValue INT NOT NULL, " +
                "PRIMARY KEY (itemID, statID), " +
                "FOREIGN KEY (itemID) REFERENCES Gear(itemID) ON DELETE CASCADE, " +
                "FOREIGN KEY (statID) REFERENCES Statistic(statID) ON DELETE CASCADE" +
                ")",

                "CREATE TABLE IF NOT EXISTS ConsumablesStatsBonus (" +
                "itemID INT NOT NULL, " +
                "statID INT NOT NULL, " +
                "percentageBonus DECIMAL(5,2) NOT NULL, " +
                "bonusCap INT NOT NULL, " +
                "PRIMARY KEY (itemID, statID), " +
                "FOREIGN KEY (itemID) REFERENCES Consumable(itemID) ON DELETE CASCADE, " +
                "FOREIGN KEY (statID) REFERENCES Statistic(statID) ON DELETE CASCADE" +
                ")",

                "CREATE TABLE IF NOT EXISTS LevelThreshold (" +
                "levelThresholdID INT AUTO_INCREMENT PRIMARY KEY, " +
                "charLevel INT NOT NULL UNIQUE, " +
                "requiredXP INT NOT NULL" +
                ")",

                "CREATE TABLE IF NOT EXISTS Currency (" +
                "currencyID INT AUTO_INCREMENT PRIMARY KEY, " +
                "currencyName VARCHAR(50) NOT NULL UNIQUE" +
                ")",

                "CREATE TABLE IF NOT EXISTS CharacterCurrency (" +
                "characterID INT NOT NULL, " +
                "currencyID INT NOT NULL, " +
                "currentAmount INT NOT NULL, " +
                "isCurrent BOOLEAN NOT NULL, " +
                "PRIMARY KEY (characterID, currencyID), " +
                "FOREIGN KEY (characterID) REFERENCES Character(characterID) ON DELETE CASCADE, " +
                "FOREIGN KEY (currencyID) REFERENCES Currency(currencyID) ON DELETE CASCADE" +
                ")",

                "CREATE TABLE IF NOT EXISTS InventorySlot (" +
                "characterID INT NOT NULL, " +
                "slotNumber INT NOT NULL, " +
                "itemID INT NOT NULL, " +
                "quantityStacked INT NOT NULL, " +
                "PRIMARY KEY (characterID, slotNumber), " +
                "FOREIGN KEY (characterID) REFERENCES Character(characterID) ON DELETE CASCADE, " +
                "FOREIGN KEY (itemID) REFERENCES Item(itemID) ON DELETE CASCADE" +
                ")"
            };

            for (String statement : createTableStatements) {
                cxn.createStatement().executeUpdate(statement);
            }
            
            System.out.println("All tables created successfully!");
        }
    }
} 