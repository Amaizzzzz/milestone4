package cs5200project.model;

import java.util.Objects;

public class CharacterStats {
    private GameCharacter character;
    private Statistic statistic;
    private int currentValue;

    public CharacterStats(GameCharacter character, Statistic statistic, int currentValue) {
        this.character = character;
        this.statistic = statistic;
        this.currentValue = currentValue;
    }

    public GameCharacter getCharacter() {
        return character;
    }

    public void setCharacter(GameCharacter character) {
        this.character = character;
    }

    public Statistic getStatistic() {
        return statistic;
    }

    public void setStatistic(Statistic statistic) {
        this.statistic = statistic;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    @Override
    public String toString() {
        return String.format("CharacterStats[character=%s, statistic=%s, currentValue=%d]",
            character, statistic, currentValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(character, statistic, currentValue);
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
                && statistic.equals(other.statistic)
                && currentValue == other.currentValue;
    }
}
