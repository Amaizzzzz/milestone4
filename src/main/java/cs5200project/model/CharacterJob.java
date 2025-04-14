package cs5200project.model;

import java.util.Objects;

public class CharacterJob {
    private int characterID;
    private int jobID;
    private boolean isUnlocked;
    private int xp;
    private String jobName;        // From Job table
    private int level;            // Calculated from XP
    private boolean isCurrentJob;  // From GameCharacter table
    private String weaponName;     // From equipped Weapon

    public CharacterJob(int characterID, int jobID, boolean isUnlocked, int xp, 
                       String jobName, int level, boolean isCurrentJob, String weaponName) {
        this.characterID = characterID;
        this.jobID = jobID;
        this.isUnlocked = isUnlocked;
        this.xp = xp;
        this.jobName = jobName;
        this.level = level;
        this.isCurrentJob = isCurrentJob;
        this.weaponName = weaponName;
    }

    // Basic constructor for backward compatibility
    public CharacterJob(int characterID, int jobID, boolean isUnlocked, int xp) {
        this(characterID, jobID, isUnlocked, xp, "", 1, false, "");
    }

    // Getters and Setters
    public int getCharacterID() { 
        return characterID; 
    }
    
    public void setCharacterID(int characterID) { 
        this.characterID = characterID; 
    }

    public int getJobID() { 
        return jobID; 
    }
    
    public void setJobID(int jobID) { 
        this.jobID = jobID; 
    }

    public boolean isUnlocked() { 
        return isUnlocked; 
    }
    
    public void setUnlocked(boolean unlocked) { 
        isUnlocked = unlocked; 
    }

    public int getXp() { 
        return xp; 
    }
    
    public void setXp(int xp) { 
        this.xp = xp; 
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isCurrentJob() {
        return isCurrentJob;
    }

    public void setCurrentJob(boolean currentJob) {
        isCurrentJob = currentJob;
    }

    public String getWeaponName() {
        return weaponName;
    }

    public void setWeaponName(String weaponName) {
        this.weaponName = weaponName;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(characterID, jobID, isUnlocked, xp, jobName, level, isCurrentJob, weaponName);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
            
        CharacterJob other = (CharacterJob) obj;
        return characterID == other.characterID 
                && jobID == other.jobID
                && isUnlocked == other.isUnlocked
                && xp == other.xp
                && level == other.level
                && isCurrentJob == other.isCurrentJob
                && Objects.equals(jobName, other.jobName)
                && Objects.equals(weaponName, other.weaponName);
    }
    
    @Override
    public String toString() {
        return String.format("CharacterJob[characterID=%d, jobID=%d, jobName=%s, level=%d, xp=%d, isUnlocked=%b, isCurrentJob=%b, weaponName=%s]",
                           characterID, jobID, jobName, level, xp, isUnlocked, isCurrentJob, weaponName);
    }
}
