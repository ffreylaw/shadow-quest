package main;
/* SWEN20003 Object Oriented Software Development
 * RPG Game Engine
 * Author: Geoffrey Law <glaw>
 */


import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import character.*;
import character.Character;


/** Represents the entire game world.
 * (Designed to be instantiated just once for the whole game).
 */
public class World {
	
	/** Objects needed for creating a world */
	private Map map;
	private Player player;
	private Vector<NonPlayer> npc;
	private Vector<Item> item;
	private Camera camera;
	
	
    /** Create a new World object. 
     * @throws SlickException, IOException 
     */
    public World()
    throws SlickException, IOException {
    	map = new Map();
    	
    	Vector<Character> unit = new Vector<Character>();
    	Character.unitGenerator(unit);			// generate all units
    	
    	npc = new Vector<NonPlayer>();
    	for (Character ch: unit) {
    		if (ch instanceof Player) {
    			player = (Player)ch;			// assign the player
    		}
    		if (ch instanceof NonPlayer) {
    			npc.addElement((NonPlayer)ch);	// construct NPC vector
    		}
    	}
    	
    	item = new Vector<Item>();
    	Item.itemGenerator(item);				// generate all items
    	
    	camera = new Camera(player, RPG.SCREENWIDTH, RPG.SCREENHEIGHT - RPG.PANELHEIGHT);
    }

    
    /** Update the game state for a frame.
     * @param input The input from keyboard/mouse
     * @param delta Time passed since last frame (milliseconds).
     * @throws SlickException
     */
    public void update(Input input, int delta)
    throws SlickException {
    	// update the player
    	player.update(input, delta, map);
    	if (input.isKeyPressed(Input.KEY_H)) {
    		player.toggleGuideWindow();
    	}
    	if (input.isKeyPressed(Input.KEY_B)) {
    		player.castTeleport();
    	}
    	
    	// update all alive NPCs
    	for (NonPlayer np: npc) {
    		np.update(delta, map);
    		if (np instanceof AggressiveMonster) {
    			if (((AggressiveMonster)np).withinRange(player)) {
    				((AggressiveMonster)np).moveToward(player, delta, map);
    			}
    		}
    		if (np instanceof PassiveMonster) {
				if (((PassiveMonster)np).onAttacked()) {
					((PassiveMonster)np).moveAway(player, delta, map);
				} else {
					((PassiveMonster)np).wander(delta, map);
				}
			}
    		if (Character.isNearbyPlayer(player, np.getX(), np.getY())) {
	    		if (np instanceof Villager) {
	    			if (input.isKeyPressed(Input.KEY_T))
	    				((Villager)np).talk(player);
	    		}
	    		if (np instanceof AggressiveMonster || 
	    			np instanceof PassiveMonster) {
	    			if (input.isKeyDown(Input.KEY_A)) {
	    				player.attack(np);
	    			}
	    			if (np instanceof AggressiveMonster) {
	    				np.attack(player);
	    			}
	    		}
    		}
    	}
    	
    	// Remove dead NPCs from NPC vector
    	NonPlayer.cleanDeadNPCs(npc);
    	
    	// pick up nearby items
    	Item temp = null;
    	for (Item it: item) {
    		if (Character.isNearbyPlayer(player, it.getX(), it.getY())) {
	    		player.pick(it);
	    		temp = it;
	    		break;
    		}
    	}
    	item.removeElement(temp);
    	
    	// update the camera
    	camera.update(map);
    }
    
    /** Render the radar on the screen.
	 * @param g The Slick graphics object.
	 * @param camera The camera object.
	 * @throws SlickException
	 */
	public void renderRadar(Graphics g, Camera camera) 
	throws SlickException {
		float rectWidth = (float)(((map.getScreenWidthInTiles() + 2)*map.getTileWidth() + 100)*0.15);
    	float rectHeight = (float)(((map.getScreenHeightInTiles() + 2)*map.getTileHeight() + 100)*0.15);
    	Color BLACK = new Color(0.0f, 0.0f, 0.0f);
    	Color BLACK_T = new Color(0.0f, 0.0f, 0.0f, 0.7f);
    	Color WHITE = new Color(1.0f, 1.0f, 1.0f);
    	g.setColor(BLACK);
    	g.fillRect(0, 0, rectWidth, rectHeight);
		
        g.scale(0.15f, 0.15f);
		map.renderRadar(g, camera);
    	player.renderRadar(g, camera, map);
    	for (NonPlayer ch: npc) ch.renderRadar(g, camera, map);
    	for (Item it: item) it.renderRadar(g, camera, map);
    	
    	int barWidth = 15;
    	int barHeight = 15;
    	g.scale(20/3f, 20/3f);
    	g.setColor(BLACK);
    	g.fillRect(0, 0, rectWidth, barHeight);
    	g.fillRect(0, 0, barWidth, rectHeight);
    	g.fillRect(rectWidth - barHeight, 0, barWidth, rectHeight);
    	g.fillRect(0, rectHeight - barWidth, rectWidth, barHeight);
    	g.setColor(BLACK_T);
        g.fillRect(0, rectHeight, rectWidth, 30);
    	
		int textPosX = 20;
		int textPosY = (int) (rectHeight + 5);
		g.setColor(WHITE);
        NumberFormat formatter = new DecimalFormat("0000");   
    	g.drawString("x: " + formatter.format(player.getX()) + 
    			     "  y: " + formatter.format(player.getY()), 
    			     textPosX, textPosY);
	}

    /** Render the entire screen, so it reflects the current game state.
     * @param g The Slick graphics object, used for drawing.
     * @throws SlickException
     */
    public void render(Graphics g)
    throws SlickException {
    	map.render(camera);
    	for (NonPlayer ch: npc) ch.render(g, camera);
    	for (Item it: item) it.render(camera);
    	player.render(g, camera);
    	player.renderPanel(g);
    	renderRadar(g, camera);
    }
    
}
