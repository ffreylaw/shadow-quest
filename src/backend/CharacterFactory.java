package backend;
/* SWEN20003 Object Oriented Software Development
 * RPG Game Engine
 * Author: Geoffrey Law <glaw>
 */

import java.io.*;
import java.util.Hashtable;

import org.newdawn.slick.SlickException;

import character.*;
import character.Character;


/** A factory class for generating characters.
 */
public class CharacterFactory {
	
	/** A hash table stores all unit data, string as key */
	private Hashtable<String, UnitData> unitData;
	
	
	/** Create a new character factory object.
	 * @throws IOException
	 */
	public CharacterFactory(String unit_data_path) 
	throws IOException {
		unitData = new Hashtable<String, UnitData>();
		
		// Get all the unit data from file
		FileInputStream fstream = new FileInputStream(unit_data_path);
		try (BufferedReader br = new BufferedReader(new InputStreamReader(fstream))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] splited = line.split(",");
				String name = splited[0];
				String imageFile = splited[1];
				String type = splited[2];
				String monsterType = splited[3];
				int HP = Integer.parseInt(splited[4]);
				int damage = Integer.parseInt(splited[5]);
				int cooldown = Integer.parseInt(splited[6]);
				unitData.put(name, new UnitData(name, imageFile, type, monsterType, HP, damage, cooldown));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Get the character object by reference of name.
	 * @param ref The character's name.
	 * @param x Initial x position of the character.
	 * @param y Initial y position of the character.
	 * @return a character object.
	 * @throws SlickException
	 */
	public Character getCharacter(String ref, int x, int y) 
	throws SlickException {
		// Get the specific character data from the hash table by name as key
		UnitData data = (UnitData) unitData.get(ref);
		
		// Determine the type of a character
		if (data.getType().equalsIgnoreCase("PLAYER")) {
			return new Player(data, x, y);
		} else if (data.getType().equalsIgnoreCase("VILLAGER")) {
			return new Villager(data, x, y);
		} else if (data.getMonsterType().equalsIgnoreCase("PASSIVE")) {
			return new PassiveMonster(data, x, y);
		} else if (data.getMonsterType().equalsIgnoreCase("AGGRESSIVE")) {
			return new AggressiveMonster(data, x, y);
		} else {
			System.err.println("Error: returning null pointer");
			System.exit(0);
			return null;
		}
	}
		
}
