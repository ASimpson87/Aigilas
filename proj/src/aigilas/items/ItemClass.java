package aigilas.items;

public enum ItemClass {
	Melee_Weapon(ItemSlot.RIGHT_HAND),
	Ranged_Weapon(ItemSlot.RIGHT_HAND),
	Ranged_Ammo(ItemSlot.LEFT_HAND),
	Ring(ItemSlot.LEFT_FINGER),
	Leggings(ItemSlot.LEGS),
	Torso_Garb(ItemSlot.TORSO),
	Feet(ItemSlot.FEET),
	Head_Gear(ItemSlot.HEAD),
	Gloves(ItemSlot.HANDS),
	Shield(ItemSlot.LEFT_HAND);

	public final ItemSlot Slot;

	private ItemClass(ItemSlot slot) {
		Slot = slot;
	}

	public String hudName() {
		return toString().replace("_", " ");
	}
}
