package cs5200project.model;

import java.util.Objects;

public class GearInstance {
	private int gearInstanceID;
	private Item item;
	private GameCharacter character;
	private GearSlot gearSlot;

	public GearInstance(int gearInstanceID, GearSlot gearSlot,
			GameCharacter character, Item item) {
		this.gearInstanceID = gearInstanceID;
		this.item = item;
		this.character = character;
		this.gearSlot = gearSlot;
	}

	public int getGearInstanceID() {
		return gearInstanceID;
	}

	public void setGearInstanceID(int gearInstanceID) {
		this.gearInstanceID = gearInstanceID;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public int getGearId() {
		return item != null ? item.getItemId() : 0;
	}

	public GameCharacter getCharacter() {
		return character;
	}

	public void setCharacter(GameCharacter character) {
		this.character = character;
	}

	public GearSlot getGearSlot() {
		return gearSlot;
	}

	public void setGearSlot(GearSlot gearSlot) {
		this.gearSlot = gearSlot;
	}

	public int getCharacterId() {
		return character != null ? character.getCharacterID() : 0;
	}

	public int getGearSlotId() {
		return gearSlot != null ? gearSlot.getSlotID() : 0;
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GearInstance other = (GearInstance) obj;
		return Objects.equals(character, other.character)
				&& gearInstanceID == other.gearInstanceID
				&& Objects.equals(gearSlot, other.gearSlot)
				&& Objects.equals(item, other.item);
	}

	@Override
	public String toString() {
		return String.format(
				"GearInstance(id=%d, itemID=%d, characterID=%d, slotID=%d)",
				gearInstanceID, item.getItemId(), character.getCharacterID(),
				gearSlot.getSlotID());
	}
}
