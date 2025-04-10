package cs5200project.driver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import cs5200project.dal.*;
import cs5200project.model.*;
import cs5200project.model.Consumable.ConsumablesType;
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

            // 2. Create Race
            Race race = RaceDao.create(cxn, "Human");
            System.out.println("Created race: " + race.getRaceName());

            // 3. Create Job
            Job warriorJob = JobDao.create(cxn, "Warrior");
            Job mageJob = JobDao.create(cxn, "Mage");
            Job rangerJob = JobDao.create(cxn, "Ranger");
            System.out.println("Created jobs: Warrior, Mage, Ranger");

            // 4. Create GearSlot
            GearSlot headSlot = GearSlotDao.create(cxn, "Head");
            GearSlot chestSlot = GearSlotDao.create(cxn, "Chest");
            GearSlot mainHandSlot = GearSlotDao.create(cxn, "MainHand");
            System.out.println("Created gear slots: Head, Chest, MainHand");

            // 5. Create Character
            GameCharacter newChar = new GameCharacter(
                0, // characterID will be set by create
                player.getPlayerID(),
                "Alice",
                "Smith",
                race.getRaceID(),
                new Date(),
                true,
                warriorJob.getJobID()
            );
            GameCharacter character = CharacterDao.getInstance().create(newChar);
            System.out.println("Created character: " + character.getFirstName() + " " + character.getLastName());

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
            CharacterStatsDao.create(cxn, character, hpStat, 150);
            CharacterStatsDao.create(cxn, character, mpStat, 80);
            CharacterStatsDao.create(cxn, character, strStat, 15);
            System.out.println("Created character stats for character: " + character.getCharacterID());

            // 9. Create Character Job
            CharacterJobDao.create(cxn, character, warriorJob, true, 50);
            CharacterJobDao.create(cxn, character, mageJob, true, 20);
            System.out.println("Created character job mappings for character: " + character.getCharacterID());

            // 10. Create Weapon
            // Note: We don't need to create Item separately as WeaponDao.create does that for us
            Weapon ironSword = WeaponDao.create(cxn, 
                0,  // itemID=0 means create a new Item
                "Iron Sword", 
                10, // itemLevel
                1,  // maxStackSize
                100.0, // price
                1,  // quantity
                5,  // requiredLevel
                15, // damage
                2,  // attackSpeed
                "Sword", // weaponType
                mainHandSlot, // gearSlot
                warriorJob, // requiredJob
                WeaponDurability.NEW, // durability
                RankValue.GREEN // rankValue
            );
            System.out.println("Created weapon: " + ironSword.getItemName() + " with ID: " + ironSword.getItemId());

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

            // 12. Create Gear
            // Note: We don't need to create Item separately as GearDao.create does that for us
            Gear ironHelmet = GearDao.create(cxn,
                0,  // itemID=0 means create a new Item
                "Iron Helmet",
                10, // itemLevel
                1,  // maxStackSize
                75.0, // price
                1,  // quantity
                5   // requiredLevel
            );
            System.out.println("Created gear: " + ironHelmet.getItemName() + " with ID: " + ironHelmet.getItemId());

            // 13. Create GearInstance
            GearInstanceDao gearInstanceDao = GearInstanceDao.getInstance();
            GearInstance helmetInstance = gearInstanceDao.create(cxn, 
                ironHelmet,
                character,
                headSlot // Using the headSlot we created earlier
            );
            System.out.println("Created gear instance for gear ID: " + helmetInstance.getGearId());

            // 14. Create GearJob (associate gear with job)
            GearJobDao.create(cxn, ironHelmet.getItemId(), warriorJob.getJobID());
            System.out.println("Created gear job mapping for gear ID: " + ironHelmet.getItemId());

            // 15. Create WeaponStatsBonus
            WeaponStatsBonus swordStrBonus = WeaponStatsBonusDao.create(cxn, 
                ironSword, 
                strStat, 
                5 // bonus value
            );
            System.out.println("Created weapon stat bonus for weapon: " + ironSword.getItemName());

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

            // 18. Create CharacterCurrency
            CharacterCurrency charGold = CharacterCurrencyDao.create(cxn, 
                character.getCharacterID(), 
                gold.getCurrencyID(), 
                1000, // current amount
                true  // is current
            );
            System.out.println("Created character currency for character: " + character.getCharacterID());

            System.out.println("All test data created successfully!");
        }
    }

    private static void resetSchema() throws SQLException {
        try (Connection cxn = ConnectionManager.getSchemalessConnection()) {
            String dropDatabase = "DROP DATABASE IF EXISTS cs5200project";
            String createDatabase = "CREATE DATABASE cs5200project";
            String useDatabase = "USE cs5200project";
            
            cxn.createStatement().execute(dropDatabase);
            cxn.createStatement().execute(createDatabase);
            cxn.createStatement().execute(useDatabase);
            
            // Create tables
            createTables(cxn);
        }
    }

    private static void createTables(Connection cxn) throws SQLException {
        String createPlayer = 
            "CREATE TABLE Player (" +
            "    playerID INT AUTO_INCREMENT," +
            "    username VARCHAR(255) NOT NULL," +
            "    email VARCHAR(255) NOT NULL," +
            "    serverRegion VARCHAR(50) NOT NULL," +
            "    CONSTRAINT pk_Player_playerID PRIMARY KEY (playerID)" +
            ");";

        String createRace = 
            "CREATE TABLE Race (" +
            "    raceID INT AUTO_INCREMENT," +
            "    raceName VARCHAR(50) NOT NULL," +
            "    CONSTRAINT pk_Race_raceID PRIMARY KEY (raceID)" +
            ");";

        String createJob = 
            "CREATE TABLE Job (" +
            "    jobID INT AUTO_INCREMENT," +
            "    jobName VARCHAR(50) NOT NULL," +
            "    CONSTRAINT pk_Job_jobID PRIMARY KEY (jobID)" +
            ");";

        String createCharacter = 
            "CREATE TABLE `Character` (" +
            "    characterID INT AUTO_INCREMENT," +
            "    playerID INT NOT NULL," +
            "    firstName VARCHAR(50) NOT NULL," +
            "    lastName VARCHAR(50) NOT NULL," +
            "    raceID INT NOT NULL," +
            "    creationDate DATE NOT NULL," +
            "    isNewPlayer BOOLEAN NOT NULL," +
            "    currentJobID INT NOT NULL," +
            "    CONSTRAINT pk_Character_characterID PRIMARY KEY (characterID)," +
            "    CONSTRAINT fk_Character_playerID FOREIGN KEY (playerID) REFERENCES Player(playerID) ON DELETE CASCADE," +
            "    CONSTRAINT fk_Character_raceID FOREIGN KEY (raceID) REFERENCES Race(raceID) ON DELETE RESTRICT," +
            "    CONSTRAINT fk_Character_currentJobID FOREIGN KEY (currentJobID) REFERENCES Job(jobID) ON DELETE RESTRICT" +
            ");";

        String createStatType = 
            "CREATE TABLE StatType (" +
            "    statTypeID INT AUTO_INCREMENT," +
            "    statName VARCHAR(50) NOT NULL," +
            "    description VARCHAR(255) NOT NULL," +
            "    CONSTRAINT pk_StatType_statTypeID PRIMARY KEY (statTypeID)" +
            ");";

        String createStatistic = 
            "CREATE TABLE Statistic (" +
            "    statID INT AUTO_INCREMENT," +
            "    statTypeID INT NOT NULL," +
            "    baseValue INT NOT NULL," +
            "    CONSTRAINT pk_Statistic_statID PRIMARY KEY (statID)," +
            "    CONSTRAINT fk_Statistic_statTypeID FOREIGN KEY (statTypeID) REFERENCES StatType(statTypeID) ON DELETE RESTRICT" +
            ");";

        String createCharacterStats = 
            "CREATE TABLE CharacterStats (" +
            "    characterID INT NOT NULL," +
            "    statID INT NOT NULL," +
            "    currentValue INT NOT NULL," +
            "    CONSTRAINT pk_CharacterStats_characterID_statID PRIMARY KEY (characterID, statID)," +
            "    CONSTRAINT fk_CharacterStats_characterID FOREIGN KEY (characterID) REFERENCES `Character`(characterID) ON DELETE CASCADE," +
            "    CONSTRAINT fk_CharacterStats_statID FOREIGN KEY (statID) REFERENCES Statistic(statID) ON DELETE RESTRICT" +
            ");";

        String createCharacterJob = 
            "CREATE TABLE CharacterJob (" +
            "    characterID INT NOT NULL," +
            "    jobID INT NOT NULL," +
            "    isUnlocked BOOLEAN NOT NULL," +
            "    jobLevel INT NOT NULL," +
            "    CONSTRAINT pk_CharacterJob_characterID_jobID PRIMARY KEY (characterID, jobID)," +
            "    CONSTRAINT fk_CharacterJob_characterID FOREIGN KEY (characterID) REFERENCES `Character`(characterID) ON DELETE CASCADE," +
            "    CONSTRAINT fk_CharacterJob_jobID FOREIGN KEY (jobID) REFERENCES Job(jobID) ON DELETE RESTRICT" +
            ");";

        // Execute all create table statements
        cxn.createStatement().execute(createPlayer);
        cxn.createStatement().execute(createRace);
        cxn.createStatement().execute(createJob);
        cxn.createStatement().execute(createCharacter);
        cxn.createStatement().execute(createStatType);
        cxn.createStatement().execute(createStatistic);
        cxn.createStatement().execute(createCharacterStats);
        cxn.createStatement().execute(createCharacterJob);
    }
} 