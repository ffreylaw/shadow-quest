package main;
/* SWEN20003 Object Oriented Software Development
 * RPG Game Engine
 * Author: Geoffrey Law <glaw>
 */

import org.newdawn.slick.SlickException;

import character.Character;


/** Represents the camera that controls our viewpoint.
 */
public class Camera {

    /** The unit this camera is following */
    private Character unitFollow;
    
    /** The width and height of the screen */
    /** Screen width, in pixels. */
    public final int screenwidth;
    /** Screen height, in pixels. */
    public final int screenheight;
    
    /** The camera's position in the world, in x and y coordinates. */
    private int xPos;
    private int yPos;

    
    /** Create a new Camera object. 
     */
    public Camera(Character player, int screenwidth, int screenheight) {   
    	this.unitFollow = player;
    	this.screenwidth = screenwidth;
    	this.screenheight = screenheight;
    	this.xPos = (int) unitFollow.getX();
    	this.yPos = (int) unitFollow.getY();
    }
    
    public int getxPos() {
    	return xPos;
    }

    public int getyPos() {
    	return yPos;
    }

    /** Update the game camera to re-center it's viewpoint around the player 
     */
    public void update(Map map)
    throws SlickException {
    	xPos = (int) unitFollow.getX();
    	yPos = (int) unitFollow.getY();
    	
    	// Restrict camera position
    	xPos = xPos <= screenwidth/2 ? screenwidth/2 
    		   : xPos >= map.getWidthInPixels() - screenwidth/2 ?
    		   map.getWidthInPixels() - screenwidth/2 : xPos;
    		   
	    yPos = yPos <= screenheight/2 ? screenheight/2 
    		   : yPos >= map.getHeightInPixels() - screenheight/2 ?
    		   map.getHeightInPixels() - screenheight/2 : yPos;
    }
    
    /** Returns the minimum x value on screen 
     */
    public int getMinX(){
    	return xPos - screenwidth/2;
    }
    
    /** Returns the maximum x value on screen 
     */
    public int getMaxX(){
    	return xPos + screenwidth/2;
    }
    
    /** Returns the minimum y value on screen 
     */
    public int getMinY(){
    	return yPos - screenheight/2;
    }
    
    /** Returns the maximum y value on screen 
     */
    public int getMaxY(){
    	return yPos + screenheight/2;
    }

    /** Tells the camera to follow a given unit. 
     */
    public void followUnit(Object unit)
    throws SlickException {
    	if (unit instanceof Character) unitFollow = (Character) unit;
    }
    
}