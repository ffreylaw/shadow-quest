package character;
/* SWEN20003 Object Oriented Software Development
 * RPG Game Engine
 * Author: Geoffrey Law <glaw>
 */

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import backend.UnitData;
import main.Camera;
import main.Map;


/** Represents the passive monster.
 */
public class PassiveMonster extends NonPlayer {
	
	/** A flag check whether the passive monster get attacked */
	private boolean attacked;
	
	/** Variables needed for wander feature */
	private int wanderTimer;					// wander timer
	private int wander_dir_x;					// wander direction in x
	private int wander_dir_y; 					// wander direction in y
	private final float WNDR_VELOCITY = 0.05f;	// wander velocity
	private final int WNDR_DRTN = 3000;			// wander duration each direction
	
	/** Variables needed for move away feature */
	private int moveAwayTimer;					// move away timer
	private final int MOVEAWAY_DRTN = 5000;		// move away duration
	
	/** Velocity of the passive monster */
	private final float VELOCITY = 0.2f;
	
	
	/** Create a new passive monster object.
	 * @param data The data of the passive monster.
	 * @param x Initial x position of the passive monster.
	 * @param y Initial y position of the passive monster.
	 * @throws SlickException
	 */
	public PassiveMonster(UnitData data, float x, float y) 
	throws SlickException {
		super(data, x, y);
		attacked = false;
		wander_dir_x = 0;
		wander_dir_y = 0;
		wanderTimer = WNDR_DRTN;
		moveAwayTimer = MOVEAWAY_DRTN;
	}
	
	public void setAttacked() {
		moveAwayTimer = 1;
		attacked = true;
	}
	
	public boolean onAttacked() {
		return attacked;
	}
	
	/** Update the passive monster.
	 */
	@Override
	public void update(int delta, Map map) {
		super.update(delta, map);
		wanderTimer = wanderTimer + delta >= WNDR_DRTN ? 0 : wanderTimer + delta;
		moveAwayTimer = moveAwayTimer + delta >= MOVEAWAY_DRTN ? 0 : moveAwayTimer + delta;
	}
	
	/** Move away from the player.
	 * @param player The player object.
	 * @param delta Time passed since last frame (milliseconds).
	 * @param map The map object.
	 */
	public void moveAway(Player player, int delta, Map map) {
		if (moveAwayTimer == 0) {
			attacked = false;
		} else {
			float dist_x = x - player.x;
			float dist_y = y - player.y;
			float amount = (float) (VELOCITY*delta);
			float dist_total = (float) Math.sqrt(Math.pow(dist_x, 2) + Math.pow(dist_y, 2));
			float d_x = (dist_x/dist_total)*amount;
			float d_y = (dist_y/dist_total)*amount;
			
			super.updatePosWithBlocking(d_x, d_y, map);
			
			int dir_x = d_x < 0 ? -1 : d_x > 0 ? 1 : 0;
			this.flipImage(dir_x);
		}
	}
	
	/** Wander around on the map.
	 * @param delta Time passed since last frame (milliseconds).
	 * @param map The map object.
	 */
	public void wander(int delta, Map map) {
		if (wanderTimer == 0) {
			// random integer range -1 to 1
			wander_dir_x = -1 + (int)(Math.random()*3);
			wander_dir_y = -1 + (int)(Math.random()*3);
		}
		float d_x = wander_dir_x*WNDR_VELOCITY*delta;
		float d_y = wander_dir_y*WNDR_VELOCITY*delta;
		
		super.updatePosWithBlocking(d_x, d_y, map);
		
		int dir_x = d_x < 0 ? -1 : d_x > 0 ? 1 : 0;
		this.flipImage(dir_x);
	}
	
	/** Render the passive monster on the radar.
	 */
	@Override
	public void renderRadar(Graphics g, Camera camera, Map map) 
	throws SlickException {
		if ((x > camera.getMinX() && x < camera.getMaxX()) &&
			(y > camera.getMinY() && y < camera.getMaxY())) {
			int renderX = (int) (x - camera.getMinX() + map.getTileWidth()*2);
			int renderY = (int) (y - camera.getMinY() + map.getTileHeight()*2);
			int rectWidth = 25;
			int rectHeight = 25;
			
			Color YELLOW = new Color(1.0f, 1.0f, 0.0f);
	    	g.setColor(YELLOW);
	        g.fillRect(renderX, renderY, rectWidth, rectHeight);
		}
	}

}
