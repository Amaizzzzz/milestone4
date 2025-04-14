package cs5200project.model;

import java.util.Date;

public class Character {
    private int characterID;
	private Player player;
    private String firstName;
    private String lastName;
	private Race race;
    private Date creationTime;
    private boolean isNewPlayer;
	private Job currentJob;

	public Character(int characterID, Player player, String firstName,
			String lastName, Race race, Date creationTime, boolean isNewPlayer,
			Job currentJob) {
        this.characterID = characterID;
		this.player = player;
        this.firstName = firstName;
        this.lastName = lastName;
		this.race = race;
        this.creationTime = creationTime;
        this.isNewPlayer = isNewPlayer;
		this.currentJob = currentJob;
    }

	public int getCharacterID() {
		return characterID;
	}

	public void setCharacterID(int characterID) {
		this.characterID = characterID;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
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

	public Race getRace() {
		return race;
	}

	public void setRace(Race race) {
		this.race = race;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public boolean isNewPlayer() {
		return isNewPlayer;
	}

	public void setNewPlayer(boolean isNewPlayer) {
		this.isNewPlayer = isNewPlayer;
	}

	public Job getCurrentJob() {
		return currentJob;
	}

	public void setCurrentJob(Job currentJob) {
		this.currentJob = currentJob;
	}

}
