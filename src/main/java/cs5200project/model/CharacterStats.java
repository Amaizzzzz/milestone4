package cs5200project.model;

import java.util.Objects;

public class CharacterStats {
    private GameCharacter character;
    private StatType statType;
    private int value;

    public CharacterStats(GameCharacter character, StatType statType, int value) {
        this.character = character;
        this.statType = statType;
        this.value = value;
    }

    public GameCharacter getCharacter() {
        return character;
    }

    public void setCharacter(GameCharacter character) {
        this.character = character;
    }

    public StatType getStatType() {
        return statType;
    }

    public void setStatType(StatType statType) {
        this.statType = statType;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(character, statType, value);
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
        return character.equals(other.character) 
                && statType.equals(other.statType)
                && value == other.value;
    }
    
    @Override
    public String toString() {
        return String.format(
                "CharacterStats(character=%s, statType=%s, value=%d)",
                character, statType, value);
    }
}
