package character;
/* SWEN20003 Object Oriented Software Development
 * RPG Game Engine
 * Author: Geoffrey Law <glaw>
 */

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import backend.*;
import main.*;


/** Represents the player.
 */
public class Player extends Character {
	
	/** Objects needed for creating a player */
	private Vector<Item> inventory;			// inventory
	private Image panel;					// image of the panel
	private final float VELOCITY = 0.25f;	// movement of the player in pixel per millisecond
	
	/** Variables for teleport feature */
	private boolean castTeleport;			// B key pressed flag
	private int teleportTimer;				// teleport timer
	private final int TLPRT_DRTN = 2000;	// teleport duration
	
	/** Flag for toggle player guide feature */
	private boolean toggleGuideWindow;		// H key pressed flag
	
	
	/** Create a new player object.
	 */
	public Player(UnitData data, float x, float y) 
	throws SlickException {
		super(data, x, y);
		this.inventory = new Vector<Item>();
		this.panel = new Image(RPG.PANEL_PATH);
		this.castTeleport = false;
		this.teleportTimer = TLPRT_DRTN;
		this.toggleGuideWindow = true;
	}
	
	/** Get the player's inventory.
	 * @return a vector of items.
	 */
	public Vector<Item> getInventory() {
		return inventory;
	}
	
	public void toggleGuideWindow() {
		toggleGuideWindow = !toggleGuideWindow;
	}
	
	public void castTeleport() {
		castTeleport = !castTeleport;
		teleportTimer = TLPRT_DRTN;
	}
	
	/** Update the player.
	 * @param input The input object.
     * @param delta Time passed since last frame (milliseconds).
     * @param map The map object.
	 */
	public void update(Input input, int delta, Map map) 
	throws SlickException {
		super.update(delta);
		
		// update the player's movement direction based on keyboard presses.
        int dir_x = 0;
        int dir_y = 0;
        if (input.isKeyDown(Input.KEY_DOWN))
            dir_y += 1;
        if (input.isKeyDown(Input.KEY_UP))
            dir_y -= 1;
        if (input.isKeyDown(Input.KEY_LEFT))
            dir_x -= 1;
        if (input.isKeyDown(Input.KEY_RIGHT))
            dir_x += 1;

		// get the player's velocity and update its position
		float d_x = (float) (dir_x*delta*VELOCITY);
		float d_y = (float) (dir_y*delta*VELOCITY);
		
		// update the player's position
		super.updatePosWithBlocking(d_x, d_y, map);
		
		// Flip the player image if left or right key is pressed
		this.flipImage(dir_x);
		
		// check the player is dead, if dead then respawn
		if (hitPoints == 0) {
			x = INITIAL_X;
			y = INITIAL_Y;
			hitPoints = maxHitPoints;
		}
		
		if (castTeleport && (dir_x != 0 || dir_y != 0)) {
			teleportTimer = TLPRT_DRTN;
			castTeleport = false;
		}
		if (castTeleport) {
			teleportTimer = teleportTimer - delta <= 0 ? TLPRT_DRTN : teleportTimer - delta;
			if (teleportTimer == TLPRT_DRTN) {
				x = INITIAL_X;
				y = INITIAL_Y;
				castTeleport = false;
			}
		}
	}
	
	/** Add item to inventory.
	 * @param it The item object.
	 * @throws SlickException
	 */
	public void pick(Item it) 
	throws SlickException {
		inventory.addElement(it);
		it.effectOn(this);
		
		// set the image of the player with sword
		if (it.getName().equalsIgnoreCase("SWORD OF STRENGTH")) {
			super.character = new Image(RPG.PLAYER_WITH_SWORD_PATH);
			if (this.isImageFlipped) {
				super.character = super.character.getFlippedCopy(true, false);
			}
		}
	}
	
	/** Render the player on the radar.
	 */
	@Override
	public void renderRadar(Graphics g, Camera camera, Map map) {
		int renderX = (int) (x - camera.getMinX() + map.getTileWidth()*2);
		int renderY = (int) (y - camera.getMinY() + map.getTileHeight()*2);
		float scale = 2.0f;
		character.draw(renderX - map.getTileWidth()/2, renderY - map.getTileHeight()/2, scale);
	}
	
	/** Render the panel.
	 * @param g The Slick graphics object.
	 */
	public void renderPanel(Graphics g) {
		// Panel colours
        Color LABEL = new Color(0.9f, 0.9f, 0.4f);          // Gold
        Color VALUE = new Color(1.0f, 1.0f, 1.0f);          // White
        Color BAR_BG = new Color(0.0f, 0.0f, 0.0f, 0.8f);   // Black, transp
        Color BAR = new Color(0.8f, 0.0f, 0.0f, 0.8f);      // Red, transp

        // Variables for layout
        String text;                // Text to display
        int text_x, text_y;         // Coordinates to draw text
        int bar_x, bar_y;           // Coordinates to draw rectangles
        int bar_width, bar_height;  // Size of rectangle to draw
        int hp_bar_width;           // Size of red (HP) rectangle
        int inv_x, inv_y;           // Coordinates to draw inventory item

        float health_percent;       // Player's health, as a percentage

        // Panel background image
        panel.draw(0, RPG.SCREENHEIGHT - RPG.PANELHEIGHT);

        // Display the player's health
        text_x = 15;
        text_y = RPG.SCREENHEIGHT - RPG.PANELHEIGHT + 25;
        g.setColor(LABEL);
        g.drawString("Health:", text_x, text_y);
        text = Integer.toString(hitPoints) + "/" + Integer.toString(maxHitPoints);

        bar_x = 90;
        bar_y = RPG.SCREENHEIGHT - RPG.PANELHEIGHT + 20;
        bar_width = 90;
        bar_height = 30;
        health_percent = (float) (1.0*hitPoints/maxHitPoints);
        hp_bar_width = (int) (bar_width * health_percent);
        text_x = bar_x + (bar_width - g.getFont().getWidth(text)) / 2;
        g.setColor(BAR_BG);
        g.fillRect(bar_x, bar_y, bar_width, bar_height);
        g.setColor(BAR);
        g.fillRect(bar_x, bar_y, hp_bar_width, bar_height);
        g.setColor(VALUE);
        g.drawString(text, text_x, text_y);

        // Display the player's damage and cooldown
        text_x = 200;
        g.setColor(LABEL);
        g.drawString("Damage:", text_x, text_y);
        text_x += 80;
        text = Integer.toString(damage);
        g.setColor(VALUE);
        g.drawString(text, text_x, text_y);
        text_x += 40;
        g.setColor(LABEL);
        g.drawString("Rate:", text_x, text_y);
        text_x += 55;
        text = Integer.toString(cooldown);
        g.setColor(VALUE);
        g.drawString(text, text_x, text_y);

        // Display the player's inventory
        g.setColor(LABEL);
        g.drawString("Items:", 420, text_y);
        bar_x = 490;
        bar_y = RPG.SCREENHEIGHT - RPG.PANELHEIGHT + 10;
        bar_width = 288;
        bar_height = bar_height + 20;
        g.setColor(BAR_BG);
        g.fillRect(bar_x, bar_y, bar_width, bar_height);

        inv_x = 490;
        inv_y = RPG.SCREENHEIGHT - RPG.PANELHEIGHT
            + ((RPG.PANELHEIGHT - 72) / 2);
        for (Item it: inventory) {
            it.renderOnPanel(inv_x, inv_y);
            inv_x += 72;
        }
    }
	
	/** Render the player on the map.
	 */
	@Override
	public void render(Graphics g, Camera camera)
	throws SlickException {
		int renderX = (int) (x - camera.getMinX());
		int renderY = (int) (y - camera.getMinY());
		character.drawCentered(renderX, renderY);
		
		if (castTeleport) {
			Color VALUE = new Color(1.0f, 1.0f, 1.0f);          // White
			Color BAR_BG = new Color(0.0f, 0.0f, 0.0f, 0.8f);   // Black, transp
	        Color BAR = new Color(0.0f, 0.0f, 1.0f, 0.8f);      // Blue, transp
			
	        String text = "Teleporting...";
	        int bar_width = g.getFont().getWidth(text) + 20;
	        int bar_height = g.getFont().getHeight(text) + 4;
	        int bar_x = (int) (x - bar_width/2 - camera.getMinX());
	        int bar_y = (int) (y + bar_height + 18 - camera.getMinY());
	        float percent = (float) (1.0*(TLPRT_DRTN - teleportTimer)/TLPRT_DRTN);
	        float percent_bar_width = (int) (bar_width * percent);
	        int text_x = bar_x + (bar_width - g.getFont().getWidth(text)) / 2;
	        int text_y = bar_y + (bar_height - g.getFont().getHeight(text)) / 2;
	        
	        g.setColor(BAR_BG);
	        g.fillRect(bar_x, bar_y, bar_width, bar_height);
	        g.setColor(BAR);
	        g.fillRect(bar_x, bar_y, percent_bar_width, bar_height);
	        g.setColor(VALUE);
	        g.drawString(text, text_x, text_y);
		}
		
		if (toggleGuideWindow) {
			Color TITLE = new Color(0.9f, 0.9f, 0.4f);          // Gold
			Color VALUE = new Color(1.0f, 1.0f, 1.0f);          // White
			Color BK = new Color(0.0f, 0.0f, 0.0f, 1.0f);   	// Black
			Color BG = new Color(0.0f, 0.0f, 0.0f, 0.6f);   	// Black, transp
			String title = "PLAYER GUIDE";
			String text = "Hold 'Left' - Move twrds West\n"
						+ "Hold 'Right' - Move twrds East\n"
						+ "Hold 'Up' - Move twrds North\n"
						+ "Hold 'Down' - Move twrds South\n"
						+ "Hold 'A' - Attack\n"
						+ "Press 'T' - Talk to Villager\n"
						+ "Press 'B' - Teleport to Village\n"
						+ "\nRadar Usage:\n"
						+ "Green - Villager\n"
						+ "Yellow - Passive Monster\n"
						+ "Red - Aggressive Monster\n"
						+ "White - Item\n"
						+ "\nQuest:\n"
						+ "Seek out item Elixir of Life\n"
						+ "then give it to Prince Aldric\n"
						+ "\nPress 'ESC' - Exit Game\n"
						+ "* Press 'H' - Show/Close Guide *\n";
			int bg_width = 300;
			int bg_height = 450;
			int bg_x = RPG.SCREENWIDTH/2 - bg_width/2;
			int bg_y = RPG.SCREENHEIGHT/2 - RPG.PANELHEIGHT/2 - bg_height/2;
			int text_x = bg_x;
			int text_y = bg_y + 35;
			
			g.setColor(BK);
			g.fillRect(bg_x, bg_y, bg_width, 15);
			g.fillRect(bg_x, bg_y, 15, bg_height);
			g.fillRect(bg_x, bg_y + bg_height - 15, bg_width, 15);
			g.fillRect(bg_x + bg_width - 15, bg_y, 15, bg_height);
			g.fillRect(bg_x, bg_y + 50, bg_width, 15);
			g.setColor(BG);
			g.fillRect(bg_x, bg_y, bg_width, bg_height);
			g.setColor(TITLE);
			g.drawString(title, text_x + g.getFont().getWidth(title)/2 + 20, text_y - 10);
			g.setColor(VALUE);
			text_y += g.getFont().getLineHeight();
			for (String line : text.split("\n"))
		        g.drawString(line, text_x + 20, text_y += g.getFont().getLineHeight());
		}
	}
	
}
