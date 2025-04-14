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
            cxn.createStatement().executeUpdate("""
                CREATE TABLE Player (
                    playerID INT AUTO_INCREMENT,
                    username VARCHAR(255) NOT NULL,
                    email VARCHAR(255) NOT NULL,
                    serverRegion VARCHAR(50) NOT NULL,
                    CONSTRAINT pk_Player_playerID PRIMARY KEY (playerID),
                    UNIQUE KEY uk_Player_username (username)
                );"""
            );

            cxn.createStatement().executeUpdate("""
                CREATE TABLE Race (
                    raceID INT PRIMARY KEY AUTO_INCREMENT,
                    raceName VARCHAR(50) NOT NULL UNIQUE
                );"""
            );

            cxn.createStatement().executeUpdate("""
                CREATE TABLE Job (
                    jobID INT PRIMARY KEY AUTO_INCREMENT, 
                    jobName VARCHAR(50) NOT NULL UNIQUE
                );"""
            );
            
            // Create GearSlot table before Weapon references it
            cxn.createStatement().executeUpdate("""
                CREATE TABLE GearSlot (
                    slotID INT PRIMARY KEY AUTO_INCREMENT,
                    slotName VARCHAR(50) NOT NULL UNIQUE
                );"""
            );
            
            // Create StatType and Statistic tables before they are referenced
            cxn.createStatement().executeUpdate("""
                CREATE TABLE StatType (
                    statTypeID INT PRIMARY KEY AUTO_INCREMENT,
                    statType VARCHAR(50) NOT NULL UNIQUE,
                    description TEXT
                );"""
            );

            cxn.createStatement().executeUpdate("""
                CREATE TABLE Statistic (
                    statID INT PRIMARY KEY AUTO_INCREMENT, 
                    statTypeID INT NOT NULL,
                    statValue INT UNSIGNED DEFAULT 0,
                    CONSTRAINT fk_Statistic_statTypeID FOREIGN KEY (statTypeID) REFERENCES StatType(statTypeID)
                        ON DELETE CASCADE
                );"""
            );

            cxn.createStatement().executeUpdate("""
                CREATE TABLE `Character` (
                    characterID INT PRIMARY KEY AUTO_INCREMENT,
                    playerID INT NOT NULL,
                    firstName VARCHAR(50) NOT NULL,
                    lastName VARCHAR(50) NOT NULL,
                    raceID INT NOT NULL,
                    creationTime DATETIME DEFAULT CURRENT_TIMESTAMP,
                    isNewPlayer BOOLEAN NOT NULL DEFAULT TRUE,
                    currentJobID INT NOT NULL,
                    CONSTRAINT fk_character_player FOREIGN KEY (playerID) REFERENCES Player(playerID)
                        ON DELETE RESTRICT ON UPDATE CASCADE,
                    CONSTRAINT fk_character_race FOREIGN KEY (raceID) REFERENCES Race(raceID)
                        ON DELETE RESTRICT ON UPDATE CASCADE,
                    CONSTRAINT fk_character_currentJob FOREIGN KEY (currentJobID) REFERENCES Job(jobID)
                        ON DELETE RESTRICT ON UPDATE CASCADE
                );"""
            );

            cxn.createStatement().executeUpdate("""
                CREATE TABLE CharacterStats (
                    characterID INT NOT NULL,
                    statID INT NOT NULL,
                    value INT UNSIGNED NOT NULL DEFAULT 0,
                    CONSTRAINT pk_CharacterStats_characterID_statID PRIMARY KEY (characterID, statID),
                    CONSTRAINT fk_character_stats_character FOREIGN KEY (characterID) REFERENCES `Character`(characterID)
                        ON DELETE CASCADE,
                    CONSTRAINT fk_character_stats_stat FOREIGN KEY (statID) REFERENCES Statistic(statID)
                        ON DELETE CASCADE
                );"""
            );

            cxn.createStatement().executeUpdate("""
                CREATE TABLE CharacterJob (
                    characterID INT NOT NULL,
                    jobID INT NOT NULL,
                    isUnlocked BOOLEAN DEFAULT FALSE,
                    XP INT UNSIGNED NULL DEFAULT 0,
                    CONSTRAINT pk_character_job PRIMARY KEY (characterID, jobID),
                    CONSTRAINT fk_character_job_character FOREIGN KEY (characterID) REFERENCES `Character`(characterID)
                        ON DELETE RESTRICT ON UPDATE CASCADE,
                    CONSTRAINT fk_character_job_job FOREIGN KEY (jobID) REFERENCES Job(jobID)
                        ON DELETE RESTRICT ON UPDATE CASCADE
                );"""
            );

            cxn.createStatement().executeUpdate("""
                CREATE TABLE Item (
                    itemID INT PRIMARY KEY AUTO_INCREMENT,
                    itemName VARCHAR(255) NOT NULL UNIQUE,
                    itemLevel INT UNSIGNED NOT NULL,
                    maxStackSize INT UNSIGNED DEFAULT 1,
                    price DECIMAL(10,2) UNSIGNED NOT NULL,
                    quantity INT UNSIGNED NOT NULL
                );"""
            );

            cxn.createStatement().executeUpdate("""
                CREATE TABLE Weapon (
                    itemID INT PRIMARY KEY,
                    weaponType VARCHAR(50) NOT NULL,
                    gearSlotID INT NOT NULL,
                    jobID INT NOT NULL,
                    damage INT UNSIGNED NOT NULL DEFAULT 0,
                    attackSpeed INT UNSIGNED NOT NULL DEFAULT 1,
                    requiredLevel INT UNSIGNED NOT NULL DEFAULT 1,
                    weaponDurability VARCHAR(50) NOT NULL DEFAULT 'New',
                    rankValue VARCHAR(50) NOT NULL DEFAULT 'White',
                    CONSTRAINT fk_Weapon_itemID FOREIGN KEY (itemID) REFERENCES Item(itemID) 
                        ON DELETE CASCADE,
                    CONSTRAINT fk_Weapon_gearSlotID FOREIGN KEY (gearSlotID) REFERENCES GearSlot(slotID) 
                        ON DELETE CASCADE,
                    CONSTRAINT fk_Weapon_jobID FOREIGN KEY (jobID) REFERENCES Job(jobID) 
                        ON DELETE CASCADE
                );"""
            );

            cxn.createStatement().executeUpdate("""
                CREATE TABLE Consumable (
                    itemID INT PRIMARY KEY,
                    consumablesType VARCHAR(50) NOT NULL,
                    description TEXT NOT NULL,
                    source VARCHAR(255) NOT NULL,
                    CONSTRAINT fk_Consumable_itemID FOREIGN KEY (itemID) REFERENCES Item(itemID) 
                        ON DELETE CASCADE
                );"""
            );

            cxn.createStatement().executeUpdate("""
                CREATE TABLE Gear (
                    itemID INT PRIMARY KEY,
                    requiredLevel INT UNSIGNED NOT NULL,
                    CONSTRAINT fk_Gear_itemID FOREIGN KEY (itemID) REFERENCES Item(itemID) 
                        ON DELETE CASCADE
                );"""
            );

            cxn.createStatement().executeUpdate("""
                CREATE TABLE GearInstance (
                    gearInstanceID INT PRIMARY KEY AUTO_INCREMENT,
                    itemID INT NOT NULL,
                    durability TINYINT UNSIGNED NOT NULL DEFAULT 100,
                    characterID INT,
                    gearSlotID INT NOT NULL,
                    CONSTRAINT fk_GearInstance_itemID FOREIGN KEY (itemID) REFERENCES Gear(itemID)
                        ON DELETE CASCADE,
                    CONSTRAINT fk_GearInstance_characterID FOREIGN KEY (characterID) REFERENCES `Character`(characterID)
                        ON DELETE SET NULL,
                    CONSTRAINT fk_GearInstance_gearSlotID FOREIGN KEY (gearSlotID) REFERENCES GearSlot(slotID)
                        ON DELETE CASCADE,
                    CONSTRAINT chk_GearInstance_durability CHECK (durability BETWEEN 0 AND 100)
                );"""
            );

            cxn.createStatement().executeUpdate("""
                CREATE TABLE GearJob (
                    itemID INT NOT NULL,
                    jobID INT NOT NULL,
                    CONSTRAINT pk_GearJob_itemID_jobID PRIMARY KEY (itemID, jobID),
                    CONSTRAINT fk_GearJob_itemID FOREIGN KEY (itemID) REFERENCES Gear(itemID) 
                        ON DELETE CASCADE,
                    CONSTRAINT fk_GearJob_jobID FOREIGN KEY (jobID) REFERENCES Job(jobID) 
                        ON DELETE CASCADE
                );"""
            );

            cxn.createStatement().executeUpdate("""
                CREATE TABLE GearStatisticBonus (
                    itemID INT NOT NULL,
                    statID INT NOT NULL,
                    bonusValue INT UNSIGNED NOT NULL DEFAULT 0,
                    CONSTRAINT pk_GearStatisticBonus_itemID_statID PRIMARY KEY (itemID, statID),
                    CONSTRAINT fk_GearStatisticBonus_itemID FOREIGN KEY (itemID) REFERENCES Gear(itemID) 
                        ON DELETE CASCADE,
                    CONSTRAINT fk_GearStatisticBonus_statID FOREIGN KEY (statID) REFERENCES Statistic(statID) 
                        ON DELETE CASCADE
                );"""
            );

            cxn.createStatement().executeUpdate("""
                CREATE TABLE WeaponStatsBonus (
                    itemID INT NOT NULL,
                    statID INT NOT NULL,
                    bonusValue INT NOT NULL,
                    CONSTRAINT pk_WeaponStatsBonus PRIMARY KEY (itemID, statID),
                    CONSTRAINT fk_WeaponStatsBonuses_itemID FOREIGN KEY (itemID) REFERENCES Weapon(itemID)
                        ON DELETE CASCADE,
                    CONSTRAINT fk_WeaponStatsBonuses_statID FOREIGN KEY (statID) REFERENCES Statistic(statID)
                        ON DELETE CASCADE
                );"""
            );

            cxn.createStatement().executeUpdate("""
                CREATE TABLE ConsumablesStatsBonus (
                    itemID INT NOT NULL,
                    statID INT NOT NULL,
                    percentageBonus FLOAT NOT NULL,
                    bonusCap INT NOT NULL,
                    CONSTRAINT pk_ConsumablesStatsBonus PRIMARY KEY (itemID, statID),
                    CONSTRAINT fk_ConsumablesStatsBonus_itemID FOREIGN KEY (itemID) REFERENCES Consumable(itemID)
                        ON DELETE CASCADE,
                    CONSTRAINT fk_ConsumablesStatsBonus_statID FOREIGN KEY (statID) REFERENCES Statistic(statID)
                        ON DELETE CASCADE
                );"""
            );

            cxn.createStatement().executeUpdate("""
                CREATE TABLE Currency (
                    currencyID INT PRIMARY KEY AUTO_INCREMENT,
                    currencyName VARCHAR(50) NOT NULL UNIQUE,
                    maxAmount INT UNSIGNED NOT NULL,
                    weeklyCapAmount INT UNSIGNED NOT NULL
                );"""
            );

            cxn.createStatement().executeUpdate("""
                CREATE TABLE CharacterCurrency (
                    characterID INT NOT NULL,
                    currencyID INT NOT NULL,
                    currentAmount INT UNSIGNED NOT NULL DEFAULT 0,
                    isCurrent BOOLEAN NOT NULL DEFAULT TRUE,
                    CONSTRAINT pk_character_currency PRIMARY KEY (characterID, currencyID),
                    CONSTRAINT fk_character_currency_character FOREIGN KEY (characterID) REFERENCES `Character`(characterID)
                        ON DELETE CASCADE,
                    CONSTRAINT fk_character_currency_currency FOREIGN KEY (currencyID) REFERENCES Currency(currencyID)
                        ON DELETE CASCADE
                );"""
            );

            cxn.createStatement().executeUpdate("""
                CREATE TABLE InventorySlot (
                    characterID INT NOT NULL,
                    itemID INT NOT NULL,
                    slotNum TINYINT UNSIGNED NOT NULL,
                    quantity TINYINT UNSIGNED NOT NULL DEFAULT 1,
                    CONSTRAINT pk_InventorySlot_characterID_slotNum PRIMARY KEY (characterID, slotNum),
                    CONSTRAINT fk_InventorySlot_characterID FOREIGN KEY (characterID) REFERENCES `Character`(characterID)
                        ON DELETE CASCADE,
                    CONSTRAINT fk_InventorySlot_itemID FOREIGN KEY (itemID) REFERENCES Item(itemID)
                        ON DELETE CASCADE,
                    CONSTRAINT chk_InventorySlot_slotNum CHECK (slotNum BETWEEN 1 AND 100),
                    CONSTRAINT chk_InventorySlot_quantity CHECK (quantity > 0)
                );"""
            );

            cxn.createStatement().executeUpdate("""
                CREATE TABLE LevelThreshold (
                    level TINYINT UNSIGNED PRIMARY KEY,
                    experienceRequired INT UNSIGNED NOT NULL,
                    CONSTRAINT chk_LevelThreshold_level CHECK (level BETWEEN 1 AND 99),
                    CONSTRAINT chk_LevelThreshold_experienceRequired CHECK (experienceRequired >= 0)
                );"""
            );

            cxn.createStatement().executeUpdate("""
                CREATE TABLE Clan (
                    clanID INT PRIMARY KEY AUTO_INCREMENT,
                    clanName VARCHAR(50) NOT NULL UNIQUE,
                    raceID INT NOT NULL,
                    CONSTRAINT fk_Clan_raceID FOREIGN KEY (raceID) REFERENCES Race(raceID)
                        ON UPDATE CASCADE ON DELETE RESTRICT
                );"""
            );
            
            System.out.println("All tables created successfully!");
        }
    }
} 