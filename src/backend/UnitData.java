package backend;
/* SWEN20003 Object Oriented Software Development
 * RPG Game Engine
 * Author: Geoffrey Law <glaw>
 */


/** Represents information of a character.
 */
public class UnitData {
	
	/** All attributes of a character */
	private String name;
	private String imageFile;
	private String type;
	private String monsterType;
	private int HP;
	private int damage;
	private int cooldown;
	
	
	/** Create a new unit data object.
	 * @param name The name of the character.
	 * @param imageFile The image path of the character.
	 * @param type The type of the character.
	 * @param monsterType The type of the monster.
	 * @param HP The hit points of the character.
	 * @param damage The damage of the character.
	 * @param cooldown The cooldown of the character.
	 */
	public UnitData(String name, String imageFile, String type, String monsterType, int HP, int damage, int cooldown) {
		this.name = name;
		this.imageFile = imageFile;
		this.type = type;
		this.monsterType = monsterType;
		this.HP = HP;
		this.damage = damage;
		this.cooldown = cooldown;
	}

	public String getName() {
		return name;
	}

	public String getImageFile() {
		return imageFile;
	}

	public String getType() {
		return type;
	}

	public String getMonsterType() {
		return monsterType;
	}

	public int getHP() {
		return HP;
	}

	public int getDamage() {
		return damage;
	}

	public int getCooldown() {
		return cooldown;
	}
	
}
