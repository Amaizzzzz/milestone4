package cs5200project.model;

import java.util.Objects;

public class CharacterStats {
    private int characterID;
    private int statID;
    private int charValue;
    private StatType statType;

    public CharacterStats(int characterID, int statID, int charValue) {
        this.characterID = characterID;
        this.statID = statID;
        this.charValue = charValue;
    }

    public CharacterStats(int characterID, StatType statType, int charValue) {
        this.characterID = characterID;
        this.statID = statType.getStatTypeID();
        this.charValue = charValue;
        this.statType = statType;
    }

    // Getters and Setters
    public int getCharacterID() { 
        return characterID; 
    }
    
    public void setCharacterID(int characterID) { 
        this.characterID = characterID; 
    }

    public int getStatID() { 
        return statID; 
    }
    
    public void setStatID(int statID) { 
        this.statID = statID; 
    }

    public int getCharValue() { 
        return charValue; 
    }
    
    public void setCharValue(int charValue) { 
        this.charValue = charValue; 
    }

    public StatType getStatType() {
        return statType;
    }

    public void setStatType(StatType statType) {
        this.statType = statType;
        this.statID = statType.getStatTypeID();
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(characterID, statID, charValue, statType);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
            
        CharacterStats other = (CharacterStats) obj;
        return characterID == other.characterID 
                && statID == other.statID
                && charValue == other.charValue
                && Objects.equals(statType, other.statType);
    }
    
    @Override
    public String toString() {
        return String.format(
                "CharacterStats(characterID=%d, statID=%d, charValue=%d, statType=%s)",
                characterID, statID, charValue, statType);
    }
}
