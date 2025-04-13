package cs5200project.model;

public class Character_Stats {
    private int statID;
    private int characterID;
    private int value;
    
    public Character_Stats(int statID, int characterID, int value) {
        this.statID = statID;
        this.characterID = characterID;
        this.value = value;
    }
    
    public int getStatID() {
        return statID;
    }
    
    public void setStatID(int statID) {
        this.statID = statID;
    }
    
    public int getCharacterID() {
        return characterID;
    }
    
    public void setCharacterID(int characterID) {
        this.characterID = characterID;
    }
    
    public int getValue() {
        return value;
    }
    
    public void setValue(int value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return "Character_Stats [statID=" + statID + ", characterID=" + characterID + 
               ", value=" + value + "]";
    }
} 