package main;
/* SWEN20003 Object Oriented Software Development
 * RPG Game Engine
 * Author: Geoffrey Law <glaw>
 */

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;


/** Represents a map of the game world 
 */
public class Map {
	
	/** The instance of TiledMap class */
	private TiledMap map;
	
	/** The terrain matrix, check terrain blocking */
	private boolean[][] terrain;
	
	/** The width and height of the entire map in pixels */
	private final int widthInPixels;
	private final int heightInPixels;
	
	/** The width and height of the screen to be rendered in tiles */
	private final int screenWidthInTiles;
	private final int screenHeightInTiles;
	
	
	/** Create a new map object.
	 * @throws SlickException
	 */
	public Map() 
    throws SlickException {
		this.map = new TiledMap(RPG.MAP_TMX_PATH, RPG.ASSETS_PATH);
		
		this.widthInPixels = map.getWidth()*map.getTileWidth();
		this.heightInPixels = map.getHeight()*map.getTileHeight();
    	
		this.screenWidthInTiles = (int)Math.ceil((double)RPG.SCREENWIDTH/map.getTileWidth()) + 1;
		this.screenHeightInTiles = (int)Math.ceil((double)RPG.SCREENHEIGHT/map.getTileHeight()) + 1;
		
		this.terrain = new boolean[map.getHeight() + 1][map.getWidth() + 1];
		
		int tileID;
		String tileProperty;
		// Create the entire terrain map as matrix -> {true, false}
		// true if there is blocked, false otherwise
		for (int i = 0; i < map.getHeight() + 1; i++) {
			for (int j = 0; j < map.getWidth() + 1; j++) {
				if (i == map.getHeight() || j == map.getWidth()) {
					terrain[i][j] = true;
				} else {
					tileID = map.getTileId(i, j, 0);
					tileProperty = map.getTileProperty(tileID, "block", "0");
					terrain[i][j] = tileProperty.equals("0") ? false : true;
				}
			}
		}
	}

	public int getWidthInPixels() {
		return widthInPixels;
	}

	public int getHeightInPixels() {
		return heightInPixels;
	}

	public int getScreenWidthInTiles() {
		return screenWidthInTiles;
	}

	public int getScreenHeightInTiles() {
		return screenHeightInTiles;
	}
	
	public int getTileWidth() {
		return map.getTileWidth();
	}
	
	public int getTileHeight() {
		return map.getTileHeight();
	}
	
	/** Check whether the terrain of the given position is blocked 
	 *  @param xPos The x coordinate of the player on the map
	 *  @param ypos The y coordinate of the player on the map
	 *  @return true if is blocked, false otherwise.
	 */
	public boolean isBlocked(float px, float py) {
		int x = (int) (px/map.getTileWidth());
		int y = (int) (py/map.getTileHeight());
		return terrain[x][y];
	}
	
	/** Render the map on the radar.
	 * @param g The graphics object.
	 * @param camera The camera object.
	 * @throws SlickException
	 */
	public void renderRadar(Graphics g, Camera camera) 
	throws SlickException {
		int cameraOffsetX = -(camera.getMinX() % map.getTileWidth());
    	int cameraOffsetY = -(camera.getMinY() % map.getTileHeight());
    	int tilePosX = camera.getMinX() / map.getTileWidth() - 2;
    	int tilePosY = camera.getMinY() / map.getTileHeight() - 2;
    	
    	map.render(cameraOffsetX, cameraOffsetY, 
    			   tilePosX, tilePosY, 
    			   screenWidthInTiles + 3, screenHeightInTiles + 3);
	}
	
	/** Render the entire map on the screen.
	 * @param camera The camera object.
	 * @throws SlickException
	 */
	public void render(Camera camera)
    throws SlickException
    {
    	int cameraOffsetX = -(camera.getMinX() % map.getTileWidth());
    	int cameraOffsetY = -(camera.getMinY() % map.getTileHeight());
    	int tilePosX = camera.getMinX() / map.getTileWidth();
    	int tilePosY = camera.getMinY() / map.getTileHeight();

    	map.render(cameraOffsetX, cameraOffsetY, 
    			   tilePosX, tilePosY, 
    			   screenWidthInTiles, screenHeightInTiles);
    }
	
}
