package main;
/* SWEN20003 Object Oriented Software Development
 * RPG Game Engine
 * Author: Geoffrey Law <glaw>
 */

import java.io.*;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import backend.FontLoader;


/** Main class for the Role-Playing Game engine.
 * Handles initialisation, input and rendering.
 */
public class RPG extends BasicGame {
	
    private World world;
    
    private Font font;

    /** Screen width and height, in pixels. */
    public static final int SCREENWIDTH = 800;
    public static final int SCREENHEIGHT = 600;
    
    /** Panel height, in pixels. */
    public static final int PANELHEIGHT = 70;
    
    public static final String FONT_PATH = "assets/DejaVuSans-Bold.ttf";
    
    public static final String UNITS_PATH = "assets/units/";
    public static final String ITEMS_PATH = "assets/items/";
    
    public static final String UNIT_DATA_PATH = "data/unit_data.txt";
    public static final String PSTN_DATA_PATH = "data/pstn_data.txt";
	public static final String ITEM_DATA_PATH = "data/item_data.txt";
	
	public static final String MAP_TMX_PATH = "assets/map.tmx";
	public static final String ASSETS_PATH = "assets";
	
	public static final String PANEL_PATH = "assets/panel.png";
    
	public static final String PLAYER_WITH_SWORD_PATH = "assets/units/player_ws.png";

    
    /** Create a new RPG object. */
    public RPG() {
        super("Shadow Quest");
    }

    /** Initialize the game state.
     * @param gc The Slick game container object.
     */
    @Override
    public void init(GameContainer gc)
    throws SlickException {
        try {
        	font = FontLoader.loadFont(FONT_PATH, 15);
			world = new World();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    /** Update the game state for a frame.
     * @param gc The Slick game container object.
     * @param delta Time passed since last frame (milliseconds).
     */
    @Override
    public void update(GameContainer gc, int delta)
    throws SlickException {
        // Get data about the current input (keyboard state).
        Input input = gc.getInput();

        // Exit button
        if (input.isKeyPressed(Input.KEY_ESCAPE))
        	gc.exit();
        
        // Let World.update decide what to do with this data.
        world.update(input, delta);
    }

    /** Render the entire screen, so it reflects the current game state.
     * @param gc The Slick game container object.
     * @param g The Slick graphics object, used for drawing.
     */
    public void render(GameContainer gc, Graphics g)
    throws SlickException {
        // Let World.render handle the rendering.
    	g.setFont(font);
        world.render(g);
    }

    /** Start-up method. Creates the game and runs it.
     * @param args Command-line arguments (ignored).
     */
    public static void main(String[] args)
    throws SlickException {
        AppGameContainer app = new AppGameContainer(new RPG());
        // setShowFPS(true), to show frames-per-second.
        app.setShowFPS(true);
        app.setDisplayMode(SCREENWIDTH, SCREENHEIGHT, false);
        app.start();
    }
    
}
