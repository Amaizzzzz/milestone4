package cs5200project.driver;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import cs5200project.dal.*;
import cs5200project.model.*;
import cs5200project.util.ConnectionManager;

public class Driver {
    public static void main(String[] args) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection()) {
            // Create DAOs
            RaceDao raceDao = RaceDao.getInstance();
            ClanDao clanDao = ClanDao.getInstance();
            JobDao jobDao = JobDao.getInstance();
            GameCharacterDao characterDao = GameCharacterDao.getInstance();
            CharacterJobDao characterJobDao = CharacterJobDao.getInstance();
            LevelThresholdDao levelThresholdDao = LevelThresholdDao.getInstance();
            
            // Create races
            Race human = raceDao.create(connection, "Human");
            Race elf = raceDao.create(connection, "Elf");
            Race dwarf = raceDao.create(connection, "Dwarf");
            
            // Create clans
            Clan humanClan1 = clanDao.create(connection, "Midlander", human.getRaceID());
            Clan humanClan2 = clanDao.create(connection, "Highlander", human.getRaceID());
            Clan elfClan1 = clanDao.create(connection, "Wildwood", elf.getRaceID());
            Clan elfClan2 = clanDao.create(connection, "Duskwight", elf.getRaceID());
            Clan dwarfClan1 = clanDao.create(connection, "Dunesfolk", dwarf.getRaceID());
            Clan dwarfClan2 = clanDao.create(connection, "Plainsfolk", dwarf.getRaceID());
            
            // Create jobs
            Job warrior = jobDao.create(connection, "Warrior", "A mighty melee fighter", 1, 90);
            Job mage = jobDao.create(connection, "Mage", "A powerful spellcaster", 1, 90);
            Job healer = jobDao.create(connection, "Healer", "A skilled healer", 1, 90);
            
            // Create level thresholds
            levelThresholdDao.create(connection, 1, 0);
            levelThresholdDao.create(connection, 2, 100);
            levelThresholdDao.create(connection, 3, 200);
            levelThresholdDao.create(connection, 4, 400);
            levelThresholdDao.create(connection, 5, 800);
            
            // Create characters
            Timestamp now = new Timestamp(System.currentTimeMillis());
            GameCharacter char1 = characterDao.create(connection, 1, "John", "Doe", human.getRaceID(), 
                    now, true, warrior.getJobID());
            GameCharacter char2 = characterDao.create(connection, 1, "Jane", "Smith", elf.getRaceID(), 
                    now, true, mage.getJobID());
            
            // Add jobs to characters
            characterJobDao.create(connection, char1, warrior, true, 0);
            characterJobDao.create(connection, char1, mage, false, 0);
            characterJobDao.create(connection, char2, mage, true, 0);
            characterJobDao.create(connection, char2, healer, false, 0);
            
            // Update XP for some jobs
            characterJobDao.updateXP(connection, char1.getCharacterID(), warrior.getJobID(), 150);
            characterJobDao.updateXP(connection, char2.getCharacterID(), mage.getJobID(), 350);
            
            // Print character information
            System.out.println("Character Information:");
            printCharacterInfo(connection, char1);
            printCharacterInfo(connection, char2);
            
            // Clean up
            cleanup(connection);
        }
    }
    
    private static void printCharacterInfo(Connection connection, GameCharacter character) throws SQLException {
        CharacterJobDao characterJobDao = CharacterJobDao.getInstance();
        List<CharacterJob> jobs = characterJobDao.getCharacterJobs(connection, character.getCharacterID());
        
        System.out.println("\nName: " + character.getFirstName() + " " + character.getLastName());
        System.out.println("Jobs:");
        for (CharacterJob job : jobs) {
            System.out.println("- " + job.getJobName() + 
                    " (XP: " + job.getXp() + ", Unlocked: " + job.isUnlocked() + ")");
        }
    }
    
    private static void cleanup(Connection connection) throws SQLException {
        String[] tables = {
            "CharacterJob",
            "GameCharacter",
            "Clan",
            "Race",
            "Job",
            "LevelThreshold"
        };
        
        for (String table : tables) {
            String delete = "DELETE FROM " + table;
            try (var stmt = connection.prepareStatement(delete)) {
                stmt.executeUpdate();
            }
        }
    }
} 