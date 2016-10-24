package character;
/* SWEN20003 Object Oriented Software Development
 * RPG Game Engine
 * Author: Geoffrey Law <glaw>
 */

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import backend.UnitData;
import main.Camera;
import main.Map;


/** Represents the Non-Player Character.
 */
public abstract class NonPlayer extends Character {
	
	/** Create a new NPC object.
	 * @param data The data of the NPC.
	 * @param x Initial x position of the NPC.
	 * @param y Initial y position of the NPC.
	 * @throws SlickException
	 */
	public NonPlayer(UnitData data, float x, float y) 
	throws SlickException {
		super(data, x, y);
	}
	
	/** Remove all dead NPCs from the NPC vector.
	 * @param npc The NPC vector.
	 */
	public static void cleanDeadNPCs(Vector<NonPlayer> npc) {
    	Vector<NonPlayer> deadUnit = new Vector<NonPlayer>();
    	for (NonPlayer ch: npc) {
    		if (ch.getHitPoints() == 0) {
    			deadUnit.addElement(ch);
    		}
    	}
    	for (NonPlayer ch: deadUnit) npc.removeElement(ch);
    }
	
	/** Update the NPC.
	 * @param delta Time passed since last frame (milliseconds).
	 * @param map The map object.
	 */
	public void update(int delta, Map map) {
		update(delta);
	}
	
	/** Render the NPC on the map.
	 */
	@Override
	public void render(Graphics g, Camera camera) throws SlickException {
		if ((x > camera.getMinX() - 36 && x < camera.getMaxX() + 36) &&
			(y > camera.getMinY() - 36 && y < camera.getMaxY() + 36)) {
			int renderX = (int) (x - camera.getMinX());
			int renderY = (int) (y - camera.getMinY());
			character.drawCentered(renderX, renderY);
			
			Color VALUE = new Color(1.0f, 1.0f, 1.0f);          // White
			Color BAR_BG = new Color(0.0f, 0.0f, 0.0f, 0.8f);   // Black, transp
	        Color BAR = new Color(0.8f, 0.0f, 0.0f, 0.8f);      // Red, transp
			
	        int bar_width = g.getFont().getWidth(name) <= 70 ? 70 : g.getFont().getWidth(name) + 6;
	        int bar_height = g.getFont().getHeight(name) + 4;
	        int bar_x = (int) (x - bar_width/2 - camera.getMinX());
	        int bar_y = (int) (y - bar_height - 36 - camera.getMinY());
	        float health_percent = (float) (1.0*hitPoints/maxHitPoints);
	        float hp_bar_width = (int) (bar_width * health_percent);
	        int text_x = bar_x + (bar_width - g.getFont().getWidth(name)) / 2;
	        int text_y = bar_y + (bar_height - g.getFont().getHeight(name)) / 2;
	        
	        g.setColor(BAR_BG);
	        g.fillRect(bar_x, bar_y, bar_width, bar_height);
	        g.setColor(BAR);
	        g.fillRect(bar_x, bar_y, hp_bar_width, bar_height);
	        g.setColor(VALUE);
	        g.drawString(name, text_x, text_y);
		}
	}

}
