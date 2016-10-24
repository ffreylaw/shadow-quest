package main;
/* SWEN20003 Object Oriented Software Development
 * RPG Game Engine
 * Author: Geoffrey Law <glaw>
 */

import java.io.*;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import character.Player;


/** Represents the item.
 */
public class Item {
	
	/** All attributes of an item */
	private Image item;			// image of the item
	private float x;			// x position
	private	float y;			// y position
	private String name;		// name of the item
	private String[] effect;	// effect of the item
	
	
	/** Create a new item object.
	 * @param x Initial x position of the item.
	 * @param y Initial y position of the item.
	 * @param name The name of the item.
	 * @param effect The effect of the item.
	 * @param path The image path of the item.
	 * @throws SlickException
	 */
	public Item(int x, int y, String name, String[] effect, String path) 
	throws SlickException {
		this.item = new Image(path);
		this.x = x;
		this.y = y;
		this.name = name;
		this.effect = effect;
	}
	
	/** Generate the item vector.
	 * @param item The item vector.
	 * @throws SlickException
	 * @throws IOException
	 */
	public static void itemGenerator(Vector<Item> item) 
    throws SlickException, IOException {
		FileInputStream fstream = new FileInputStream(RPG.ITEM_DATA_PATH);
		try (BufferedReader br = new BufferedReader(new InputStreamReader(fstream))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] splited = line.split(",");
				String name = splited[0];
				String path = splited[1];
				String[] effect = splited[2].split(" ");
				int x = Integer.parseInt(splited[3]);
				int y = Integer.parseInt(splited[4]);
				item.addElement(new Item(x, y, name, effect, RPG.ITEMS_PATH + path));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public String getName() {
		return name;
	}

	public String[] getEffect() {
		return effect;
	}
	
	public void effectOn(Player player) {
		if (effect[0].equalsIgnoreCase("NONE")) {
			return;
		} else {
			int effect_amount = Integer.parseInt(effect[0]);
			for (int i = 1; i < effect.length; i++) {
				if (effect[i].equalsIgnoreCase("MAXHP")) {
					player.setMaxHitPoints(player.getMaxHitPoints() + effect_amount);
				}
				if (effect[i].equalsIgnoreCase("HP")) {
					player.setHitPoints(player.getHitPoints() + effect_amount);
				}
				if (effect[i].equalsIgnoreCase("DAMAGE")) {
					player.setDamage(player.getDamage() + effect_amount);
				}
				if (effect[i].equalsIgnoreCase("COOLDOWN")) {
					player.setCooldown(player.getCooldown() + effect_amount);
				}
			}
		}
	}
	
	/** Render the item on the panel.
	 * @param inv_x The x position in the inventory.
	 * @param inv_y The y position in the inventory.
	 */
	public void renderOnPanel(int inv_x, int inv_y) {
		item.draw(inv_x, inv_y);
	}
	
	/** Render the item on the radar.
	 * @param g Graphics.
	 * @param camera The camera object.
	 * @param map The map object.
	 */
	public void renderRadar(Graphics g, Camera camera, Map map) {
		if ((x > camera.getMinX() && x < camera.getMaxX()) &&
			(y > camera.getMinY() && y < camera.getMaxY())) {
			int renderX = (int) (x - camera.getMinX() + map.getTileWidth()*2);
			int renderY = (int) (y - camera.getMinY() + map.getTileHeight()*2);
			int rectWidth = 25;
			int rectHeight = 25;
			
			Color WHITE = new Color(1.0f, 1.0f, 1.0f);
			g.setColor(WHITE);
	        g.fillRect(renderX, renderY, rectWidth, rectHeight);
		}
	}
	
	/** Render the item on the map.
	 * @param camera The camera object.
	 */
	public void render(Camera camera) {
		if ((x > camera.getMinX() && x < camera.getMaxX()) &&
			(y > camera.getMinY() && y < camera.getMaxY())) {
			int renderX = (int) (x - camera.getMinX());
			int renderY = (int) (y - camera.getMinY());
			item.drawCentered(renderX, renderY);
		}
	}
	
}
