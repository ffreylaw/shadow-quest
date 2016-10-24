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
import main.Item;
import main.Map;


/** Represents the friendly NPC.
 */
public class Villager extends NonPlayer {
	
	/** The text to display */
	private String text;
	
	/** Display timer and duration */
	private int displayTimer;
	private final int DISPLAY_DRTN = 4000;
	
	
	/** Create a new friendly NPC object.
	 * @param data The data of the friendly NPC.
	 * @param x Initial x position of the friendly NPC.
	 * @param y Initial y position of the friendly NPC.
	 * @throws SlickException
	 */
	public Villager(UnitData data, float x, float y) 
	throws SlickException {
		super(data, x, y);
		this.text = null;
		this.displayTimer = DISPLAY_DRTN;
	}
	
	/** Update the friendly NPC.
	 */
	@Override
	public void update(int delta, Map map) {
		super.update(delta, map);
		displayTimer = displayTimer + delta >= DISPLAY_DRTN ? 0 : displayTimer + delta;
	}
	
	/** Talk to the player.
	 * @param player
	 */
	public void talk(Player player) {
		displayTimer = 1;
		
		Vector<Item> item = player.getInventory();
		switch (name) {
		case "Prince Aldric":
			boolean hasElixir = false;
			for (Item it: item) hasElixir = it.getName().equalsIgnoreCase("ELIXIR OF LIFE");
			if (!hasElixir) {
				text = "Please seek out the Elixir of Life to cure the king.";
			} else {
				text = "The elixir! My father is cured! Thankyou!";
			}
			break;
		case "Elvira":
			if (player.getHitPoints() == player.getMaxHitPoints()) {
				text = "Return to me if you ever need healing.";
			} else {
				text = "You're looking much healthier now.";
				player.setHitPoints(player.getMaxHitPoints());
			}
			break;
		case "Garth":
			boolean hasAmulet = false;
			boolean hasSword = false;
			boolean hasTome = false;
			for (Item it: item) {
				if (it.getName().equalsIgnoreCase("AMULET OF VITALITY")) {
					hasAmulet = true;
				} 
				if (it.getName().equalsIgnoreCase("SWORD OF STRENGTH")) {
					hasSword = true;
				} 
				if (it.getName().equalsIgnoreCase("TOME OF AGILITY")) {
					hasTome = true;
				}
			}
			if (!hasAmulet) {
				text = "Find the Amulet of Vitality, across the river to the west.";
			} else if (!hasSword) {
				text = "Find the Sword of Strength - cross the bridge to the east, then head south.";
			} else if (!hasTome) {
				text = "Find the Tome of Agility, in the Land of Shadows.";
			} else {
				text = "You have found all the treasure I know of.";
			}
			break;
		}
	}
	
	/** Render the friendly NPC on the radar.
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
			
			Color GREEN = new Color(0.0f, 1.0f, 0.0f);
			g.setColor(GREEN);
	        g.fillRect(renderX, renderY, rectWidth, rectHeight);
		}
	}
	
	/** Render the friendly NPC on the map.
	 */
	@Override
	public void render(Graphics g, Camera camera) throws SlickException {
		super.render(g, camera);
		
		if (text != null) {
			Color VALUE = new Color(1.0f, 1.0f, 1.0f);          // White
			Color BAR_BG = new Color(0.0f, 0.0f, 0.0f, 0.8f);   // Black, transp
			
			int bar_width = g.getFont().getWidth(text) + 6;
	        int bar_height = g.getFont().getHeight(text) + 4;
	        int bar_x = (int) (x - bar_width/2 - camera.getMinX());
	        int bar_y = (int) (y - bar_height*2 - 36 - camera.getMinY());
	        int text_x = bar_x + (bar_width - g.getFont().getWidth(text)) / 2;
	        int text_y = bar_y + (bar_height - g.getFont().getHeight(text)) / 2;
	        
	        g.setColor(BAR_BG);
	        g.fillRect(bar_x, bar_y, bar_width, bar_height);
	        g.setColor(VALUE);
	        g.drawString(text, text_x, text_y);
	        
	        if (displayTimer == 0) text = null;
		}
	}
	
}
