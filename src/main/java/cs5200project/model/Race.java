package cs5200project.model;

import java.util.Objects;
public class Race {
    private int raceID;         // Primary key
    private String raceName;
    private String description;
    private int baseStrength;
    private int baseDexterity;
    private int baseIntelligence;

    // Full-args constructor
    public Race(int raceID, String raceName) {
        this.raceID = raceID;
        this.raceName = raceName;
    }

    // Constructor with all fields
    public Race(int raceID, String raceName, String description, 
               int baseStrength, int baseDexterity, int baseIntelligence) {
        this.raceID = raceID;
        this.raceName = raceName;
        this.description = description;
        this.baseStrength = baseStrength;
        this.baseDexterity = baseDexterity;
        this.baseIntelligence = baseIntelligence;
    }

    // Getter of raceID
    public int getRaceID() {
        return raceID;
    }

    // Setter of raceID
    public void setRaceID(int raceID) {
        this.raceID = raceID;
    }

    // Getter of raceName
    public String getRaceName() {
        return raceName;
    }

    // Setter of raceName
    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    // Getter of description
    public String getDescription() {
        return description;
    }

    // Setter of description
    public void setDescription(String description) {
        this.description = description;
    }

    // Getter of baseStrength
    public int getBaseStrength() {
        return baseStrength;
    }

    // Setter of baseStrength
    public void setBaseStrength(int baseStrength) {
        this.baseStrength = baseStrength;
    }

    // Getter of baseDexterity
    public int getBaseDexterity() {
        return baseDexterity;
    }

    // Setter of baseDexterity
    public void setBaseDexterity(int baseDexterity) {
        this.baseDexterity = baseDexterity;
    }

    // Getter of baseIntelligence
    public int getBaseIntelligence() {
        return baseIntelligence;
    }

    // Setter of baseIntelligence
    public void setBaseIntelligence(int baseIntelligence) {
        this.baseIntelligence = baseIntelligence;
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(raceID, raceName, description, baseStrength, baseDexterity, baseIntelligence);
    }

    // equals method
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Race race = (Race) obj;
        return raceID == race.raceID && 
               Objects.equals(raceName, race.raceName) &&
               Objects.equals(description, race.description) &&
               baseStrength == race.baseStrength &&
               baseDexterity == race.baseDexterity &&
               baseIntelligence == race.baseIntelligence;
    }     

    // toString method
    @Override
    public String toString() {
        return String.format("Race[%d, %s, %s, %d, %d, %d]", 
            raceID, raceName, description, baseStrength, baseDexterity, baseIntelligence);
    }

}
