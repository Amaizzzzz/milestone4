package cs5200project.model;

import java.util.Objects;

public class CharacterCurrency {
    private int currencyId;
    private int characterId;
    private int amount;
    
    public CharacterCurrency(int currencyId, int characterId, int amount) {
        this.currencyId = currencyId;
        this.characterId = characterId;
        this.amount = amount;
    }
    
    public int getCurrencyId() {
        return currencyId;
    }
    
    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }
    
    public int getCharacterId() {
        return characterId;
    }
    
    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(currencyId, characterId, amount);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
            
        CharacterCurrency other = (CharacterCurrency) obj;
        return currencyId == other.currencyId 
                && characterId == other.characterId
                && amount == other.amount;
    }
    
    @Override
    public String toString() {
        return String.format(
                "CharacterCurrency(currencyId=%d, characterId=%d, amount=%d)",
                currencyId, characterId, amount);
    }
}
