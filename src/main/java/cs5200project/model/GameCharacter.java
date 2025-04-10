package cs5200project.model;

import java.util.Date;

public class GameCharacter {
    private int characterID;
    private int playerID;
    private String firstName;
    private String lastName;
    private int raceID;
    private Date creationDate;
    private boolean isNewPlayer;
    private int currentJobID;

    public GameCharacter() {}

    public GameCharacter(int characterID, int playerID, String firstName, String lastName,
                        int raceID, Date creationDate, boolean isNewPlayer, int currentJobID) {
        this.characterID = characterID;
        this.playerID = playerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.raceID = raceID;
        this.creationDate = creationDate;
        this.isNewPlayer = isNewPlayer;
        this.currentJobID = currentJobID;
    }

    public int getCharacterID() {
        return characterID;
    }

    public void setCharacterID(int characterID) {
        this.characterID = characterID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getRaceID() {
        return raceID;
    }

    public void setRaceID(int raceID) {
        this.raceID = raceID;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isNewPlayer() {
        return isNewPlayer;
    }

    public void setNewPlayer(boolean isNewPlayer) {
        this.isNewPlayer = isNewPlayer;
    }

    public int getCurrentJobID() {
        return currentJobID;
    }

    public void setCurrentJobID(int currentJobID) {
        this.currentJobID = currentJobID;
    }
} 