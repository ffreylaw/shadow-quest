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


/** Represents the aggressive monster.
 */
public class AggressiveMonster extends NonPlayer {
	
	/** The velocity of aggressive monster */
	private static final float VELOCITY = 0.25f;
	
	/** Create a new aggressive monster object.
	 * @param data The data of the aggressive monster.
	 * @param x Initial x position of the aggressive monster.
	 * @param y Initial y position of the aggressive monster.
	 * @throws SlickException
	 */
	public AggressiveMonster(UnitData data, float x, float y) 
	throws SlickException {
		super(data, x, y);
	}
	
	/** Update the aggressive monster.
	 */
	@Override
	public void update(int delta, Map map) {
		super.update(delta, map);
	}
	
	/** Move towards the player.
	 * @param player The player object.
	 * @param delta Time passed since last frame (milliseconds).
	 * @param map The map object.
	 */
	public void moveToward(Player player, int delta, Map map) {
		float dist_x = x - player.x;
		float dist_y = y - player.y;
		float amount = (float) (VELOCITY*delta);
		float dist_total = (float) Math.sqrt(Math.pow(dist_x, 2) + Math.pow(dist_y, 2));
		float d_x = -1*dist_x/dist_total*amount;
		float d_y = -1*dist_y/dist_total*amount;
		
		this.updatePosWithBlocking(d_x, d_y, map);
		
		int dir_x = d_x < 0 ? -1 : d_x > 0 ? 1 : 0;
		this.flipImage(dir_x);
	}
	
	/** Determine whether the player within range.
	 * @param player The player object.
	 * @return true if the player within range, false otherwise.
	 */
	public boolean withinRange(Player player) {
		return Math.sqrt(Math.pow(x - player.x, 2) + Math.pow(y - player.y, 2)) <= 150
			   && Math.sqrt(Math.pow(x - player.x, 2) + Math.pow(y - player.y, 2)) >= 50;
	}
	
	/** Render the aggressive monster on the radar.
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
			
			Color RED = new Color(1.0f, 0.0f, 0.0f);
	    	g.setColor(RED);
	        g.fillRect(renderX, renderY, rectWidth, rectHeight);
		}
	}

}
