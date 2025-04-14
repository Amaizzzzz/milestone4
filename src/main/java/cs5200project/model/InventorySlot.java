package cs5200project.model;

import java.util.Objects;

public class InventorySlot {
	private GameCharacter character; // PK, FK
	private int slotNumber; // PK
	private Item item; // FK
	private int quantityStacked;


	public InventorySlot(GameCharacter character, int slotNumber, Item item,
			int quantityStacked) {
		this.character = character;
		this.slotNumber = slotNumber;
		this.item = item;
		this.quantityStacked = quantityStacked;
	}

	public GameCharacter getCharacter() {
		return character;
	}

	public void setCharacter(GameCharacter character) {
		this.character = character;
	}

	public int getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(int slotNumber) {
		this.slotNumber = slotNumber;
	}

	public Item getItem() {
		return item;
	}

	public int getItemId() {
		return item != null ? item.getItemId() : 0;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public int getQuantityStacked() {
		return quantityStacked;
	}

	public void setQuantityStacked(int quantityStacked) {
		this.quantityStacked = quantityStacked;
	}

	@Override
	public int hashCode() {
		return Objects.hash(character, slotNumber, item, quantityStacked);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InventorySlot other = (InventorySlot) obj;
		return Objects.equals(character, other.character)
				&& Objects.equals(item, other.item)
				&& quantityStacked == other.quantityStacked
				&& slotNumber == other.slotNumber;
	}

	@Override
	public String toString() {
		return String.format(
				"InventorySlot(characterID=%d, slotNumber=%d, itemID=%s, quantityStacked=%d)",
				character.getCharacterID(), slotNumber, getItemId(),
				quantityStacked);
	}
}