package cs5200project.model;

public class RankValue {
    private int rankID;
    private String rankName;
    private int requiredExperience;
    
    public RankValue(int rankID, String rankName, int requiredExperience) {
        this.rankID = rankID;
        this.rankName = rankName;
        this.requiredExperience = requiredExperience;
    }
    
    public int getRankID() {
        return rankID;
    }
    
    public void setRankID(int rankID) {
        this.rankID = rankID;
    }
    
    public String getRankName() {
        return rankName;
    }
    
    public void setRankName(String rankName) {
        this.rankName = rankName;
    }
    
    public int getRequiredExperience() {
        return requiredExperience;
    }
    
    public void setRequiredExperience(int requiredExperience) {
        this.requiredExperience = requiredExperience;
    }
} 