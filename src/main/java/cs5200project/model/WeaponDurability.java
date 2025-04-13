package cs5200project.model;

public class WeaponDurability {
    private int weaponID;
    private int currentDurability;
    private int maxDurability;
    
    public WeaponDurability(int weaponID, int currentDurability, int maxDurability) {
        this.weaponID = weaponID;
        this.currentDurability = currentDurability;
        this.maxDurability = maxDurability;
    }
    
    public int getWeaponID() {
        return weaponID;
    }
    
    public void setWeaponID(int weaponID) {
        this.weaponID = weaponID;
    }
    
    public int getCurrentDurability() {
        return currentDurability;
    }
    
    public void setCurrentDurability(int currentDurability) {
        this.currentDurability = currentDurability;
    }
    
    public int getMaxDurability() {
        return maxDurability;
    }
    
    public void setMaxDurability(int maxDurability) {
        this.maxDurability = maxDurability;
    }
} 