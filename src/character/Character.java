package character;
/* SWEN20003 Object Oriented Software Development
 * RPG Game Engine
 * Author: Geoffrey Law <glaw>
 */

import java.io.*;
import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import backend.CharacterFactory;
import backend.UnitData;
import main.Camera;
import main.Map;
import main.RPG;


/** An abstract class represents the character.
 */
public abstract class Character {
	
	/** All attributes of a unit */
	protected Image character;			// image
	protected String name;				// name
	protected float x;					// x position
	protected float y;					// y position
	protected final float INITIAL_X;	// initial x position
	protected final float INITIAL_Y;	// initial y position
	protected int hitPoints;			// HP
	protected int maxHitPoints;			// max HP
	protected int damage;				// damage
	protected int cooldown;				// cooldown
	protected int CDTimer;				// cooldown timer
	
	protected boolean isImageFlipped;	// filp image flag
	
	
	/** Create a new character object.
	 * @param data The data of the character.
	 * @param x Initial x position of the character.
	 * @param y Initial y position of the character.
	 * @throws SlickException
	 */
	public Character(UnitData data, float x, float y) 
	throws SlickException {
		this.character = new Image(RPG.UNITS_PATH + data.getImageFile());
		this.name = data.getName();
		this.x = this.INITIAL_X = x;
		this.y = this.INITIAL_Y = y;
		this.hitPoints = data.getHP();
		this.maxHitPoints = data.getHP();
		this.damage = data.getDamage();
		this.cooldown = data.getCooldown();
		
		this.isImageFlipped = false;
	}
	
	/** Generate character from the character factory object.
	 * @param unit The unit vector.
	 * @throws SlickException
	 * @throws IOException
	 */
	public static void unitGenerator(Vector<Character> unit) 
    throws SlickException, IOException {
		CharacterFactory cf = new CharacterFactory(RPG.UNIT_DATA_PATH);
		
		FileInputStream fstream = new FileInputStream(RPG.PSTN_DATA_PATH);
		try (BufferedReader br = new BufferedReader(new InputStreamReader(fstream))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] splited = line.split(",");
				String name = splited[0];
				int x = Integer.parseInt(splited[1]);
				int y = Integer.parseInt(splited[2]);
				unit.addElement(cf.getCharacter(name, x, y));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	/** Check the entity (NPC or item) whether is nearby the player.
     * @param x The x position of the entity.
     * @param y The y position of the entity.
     * @return true if is nearby.
     */
    public static boolean isNearbyPlayer(Player player, float x, float y) {
    	return Math.sqrt(Math.pow(x - player.getX(), 2) + Math.pow(y - player.getY(), 2)) <= 50;
    }
	
	public String getName() {
		return name;
	}
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public int getHitPoints() {
		return hitPoints;
	}

	public int getMaxHitPoints() {
		return maxHitPoints;
	}
	
	public int getDamage() {
		return damage;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setHitPoints(int hitPoints) {
		this.hitPoints = hitPoints;
	}

	public void setMaxHitPoints(int maxHitPoints) {
		this.maxHitPoints = maxHitPoints;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}
	
	public void flipImage(int direction) {
		if (direction == -1 && !isImageFlipped) {
			isImageFlipped = !isImageFlipped;
			this.character = this.character.getFlippedCopy(true, false);
		}
		if (direction == 1 && isImageFlipped) {
			isImageFlipped = !isImageFlipped;
			this.character = this.character.getFlippedCopy(true, false);
		}
	}
	
	/** Get the movement in x direction of the character.
	 * @param velocity Velocity of the character.
	 * @param maxWidth The maximum width of the map.
	 * @return the resulted position in x direction.
	 */
	public float getMovementX(double velocity, int maxWidth) {
		return (float) (x + velocity <= 0 ? 0 : 
				     	x + velocity >= maxWidth ?
				        maxWidth : x + velocity);
	}
	
	/** Get the movement in y direction of the character.
	 * @param velocity Velocity of the character.
	 * @param maxHeight The maximum height of the map.
	 * @return the resulted position in y direction.
	 */
	public float getMovementY(double velocity, int maxHeight) {
		return (float) (y + velocity <= 0 ? 0 : 
				     	y + velocity >= maxHeight ?
				     	maxHeight : y + velocity);
	}
	
	/** Update position with terrain blocking involved.
	 * @param d_x Change in x direction.
	 * @param d_y Change in y direction.
	 * @param map The map object.
	 */
	public void updatePosWithBlocking(float d_x, float d_y, Map map) {
		float movementX = this.getMovementX(d_x, map.getWidthInPixels());      
		float movementY = this.getMovementY(d_y, map.getHeightInPixels());
		
		int width = map.getTileWidth();
		int height = map.getTileHeight();
		
		// Move in x first
        double x_sign = Math.signum(d_x);
        if(!map.isBlocked((float)(movementX + x_sign * width / 4), (float)(this.y + height / 4)) 
           && !map.isBlocked((float)(movementX + x_sign * width / 4), (float)(this.y - height / 4))) {
            this.x = movementX;
        }
        
        // Then move in y
        double y_sign = Math.signum(d_y);
        if(!map.isBlocked((float)(this.x + width / 4), (float)(movementY + y_sign * height / 4)) 
           && !map.isBlocked((float)(this.x - width / 4), (float)(movementY + y_sign * height / 4))){
            this.y = movementY;
        }
	}
	
	/** Update position without terrain blocking involved.
	 * (only used when debugging and testing)
	 * @param d_x Change in x direction.
	 * @param d_y Change in y direction.
	 * @param map The map object.
	 */
	public void updatePosWithoutBlocking(float d_x, float d_y, Map map) {
		float movementX = this.getMovementX(d_x, map.getWidthInPixels());      
		float movementY = this.getMovementY(d_y, map.getHeightInPixels());
		x = movementX;
		y = movementY;
	}
	
	/** Attack the target.
	 * @param enemy The enemy object.
	 */
	public void attack(Character target) {
		if (CDTimer == 0) {
			int damage = (int) (Math.random() * this.damage);
			target.hitPoints = target.hitPoints - damage <= 0 ? 0 : target.hitPoints - damage;
			if (target instanceof PassiveMonster) ((PassiveMonster)target).setAttacked();
		}
	}
	
	/** Update the character.
	 * updating cooldown timer in this abstract class.
	 * @param delta Time passed since last frame (milliseconds).
	 */
	public void update(int delta) {
		CDTimer = CDTimer >= cooldown ? 0 : CDTimer + delta;
	}
	
	/** Render the character on the radar.
	 * @param g The Slick graphics object.
	 * @param camera The camera object.
	 * @param map The map object.
	 * @throws SlickException
	 */
	public abstract void renderRadar(Graphics g, Camera camera, Map map) 
	throws SlickException;
	
	/** Render the character on the map.
	 * @param g The graphics object.
	 * @param camera The camera object.
	 * @throws SlickException
	 */
	public abstract void render(Graphics g, Camera camera) 
	throws SlickException;
	
}
