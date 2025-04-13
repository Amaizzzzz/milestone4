package cs5200project.model;

public class Character_Job {
    private int characterID;
    private int jobID;
    private boolean isUnlocked;
    private int xp;
    
    public Character_Job(int characterID, int jobID, boolean isUnlocked, int xp) {
        this.characterID = characterID;
        this.jobID = jobID;
        this.isUnlocked = isUnlocked;
        this.xp = xp;
    }
    
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
    
    public void setUnlocked(boolean isUnlocked) {
        this.isUnlocked = isUnlocked;
    }
    
    public int getXp() {
        return xp;
    }
    
    public void setXp(int xp) {
        this.xp = xp;
    }
    
    @Override
    public String toString() {
        return "Character_Job [characterID=" + characterID + ", jobID=" + jobID + 
               ", isUnlocked=" + isUnlocked + ", xp=" + xp + "]";
    }
} 