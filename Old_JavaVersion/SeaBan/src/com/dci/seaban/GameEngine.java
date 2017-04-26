package com.dci.seaban;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Point;

import com.dci.seaban.Objects.Map;
import com.dci.seaban.Objects.ShaderCompiller;
import com.dci.seaban.Service.GlobalVar;

public class GameEngine {

	public static Context context;
	
	public static int levelNumber = 0;
	public static Level level = new Level();
	public static Level levelCopy = new Level();
	private static final List<Level> levelStory = new ArrayList<Level>();
	
	public static final int blockObj = 0;
	public static final int boatObj = 2;
	public static final int dockObj = 4;
	public static final int contObj = 5;
	
	public static int levelWidth = 0;
	public static int levelHeight = 0;
	
	
	public static int moveCount = 0;
	public static int gameScore = 0;
	public static long gameTime = 0;
	public static long gameStars = 0;
	public static long gameStartTime = 0;
	
	public static  void loadLevel(int number){
		
		levelNumber = number;
		
		int fileResID = context.getResources().getIdentifier("level" + String.valueOf(number), "raw", context.getPackageName());

		InputStream fileIn = context.getResources().openRawResource(fileResID);
		BufferedReader buffer = new BufferedReader(new InputStreamReader(fileIn));

		String line;
		
		
		levelWidth = 0;
		levelHeight = 0;
		
		moveCount = 0;
		gameTime = 0;
		gameScore = 0;
	//	gameStartTime = System.currentTimeMillis() - 3000 * 60; //3 min debug
		gameStartTime = System.currentTimeMillis(); 
		
		
		try {
		while ((line = buffer.readLine()) != null) 
		 {
			if (line.length() > 0)
			{
			 if (line.length() > levelWidth) levelWidth = line.length();
			 levelHeight++;
			}
		 }
	
		
		GlobalVar.coef = (float)(GlobalVar.DefGLBlockSize * (GlobalVar.DefLevelWidth / (levelWidth)));
		GlobalVar.panelSize = (float)(GlobalVar.DefGLBlockSize * (GlobalVar.DefLevelWidth / (levelWidth)));
		GlobalVar.levelScale = (float)(GlobalVar.DefLevelWidth / (levelWidth));
		GlobalVar.levelCenter = (float)(levelWidth / 2.0f - 0.5f);
		
		 //level = null;
		 //level = new int[levelWidth][levelHeight];
		 int lineCount = 0;
		 
		 buffer = null;
		 fileIn = null;
		 
		 fileIn = context.getResources().openRawResource(fileResID);
		 buffer = new BufferedReader(new InputStreamReader(fileIn));
			
			while ((line = buffer.readLine()) != null) {
				
				
				for (int i=0; i < line.length(); i++)
				{
				  if (line.charAt(i) == '1' ) level.items[lineCount][i] = 1;
				  else
					  if (line.charAt(i) == 'B' ) level.items[lineCount][i] = boatObj;
					  else
						  if (line.charAt(i) == 'X' ) level.items[lineCount][i] = 3; //reserve
						  else
							  if (line.charAt(i) == 'D' ) level.items[lineCount][i] = dockObj;
							  else
								  if (line.charAt(i) == 'C' ) level.items[lineCount][i] = contObj;
								  else 								  
								    level.items[lineCount][i] = blockObj;
				  
				  levelCopy.items[lineCount][i] = level.items[lineCount][i];
				}
				
				lineCount++;
  		  }
			
			levelStory.clear();
			
		
		} catch (IOException e) {
			e.printStackTrace();
		}
						
	}
	//-------------------------------------------------
	public static int getObjectAtXY(int x, int y){
		if (x < 0) return -1;
		if (y < 0) return -1;
		
		if (x > levelWidth-1) return -1;
		if (x > levelHeight-1) return -1;
		
		
		return level.items[x][y];
	}
	//-------------------------------------------------
	
	public static Point getBoat(){
		Point result = null;
		
		for (int x =0; x < levelWidth; x++)
			for (int y =0; y < levelHeight; y++)
			{
				if (getObjectAtXY(x, y) == boatObj)
				{
					result = new Point(x, y);
					return result;
				}				
			}		
	  return result;
	}

	
	//-------------------------------------------------
	public static boolean canMoveBoat(int x, int y){
		Point boatPos = getBoat();
		
		if (getObjectAtXY(x, y) == blockObj) return false;
		
		if ((x < 0) || (y < 0)) return false;
		
		if ((x >  levelWidth-1) || (y > levelHeight-1)) return false;
		
		if ((x > boatPos.x + 1) || (x < boatPos.x - 1) ||
			(y > boatPos.y + 1) || (y < boatPos.y - 1)) return false;
		
		if ((x != boatPos.x) &&  (y != boatPos.y)) return false;

		if (getObjectAtXY(x, y) == contObj)
		  if (boatNextPos(boatPos.x, boatPos.y, x, y) == null) return false;
		
		return true;
		
	}
	
	//-------------------------------------------------
	public static boolean canMoveCont(int x, int y){
		
		
		if (getObjectAtXY(x, y) == blockObj) return false;
		
		if ((x < 0) || (y < 0)) return false;
		
		if ((x >  levelWidth-1) || (y > levelHeight-1)) return false;
		
		if (getObjectAtXY(x, y) == contObj) return false;
		  
		
		return true;
		
	}
	
	//-------------------------------------------------
	public static Point setBoatPos(int x, int y){
		
		Point newContPos = null;
		
		if (canMoveBoat(x,y))
		{
			Point boatPos = getBoat();
			
			int objAt = getObjectAtXY(x, y);
			
			if (objAt == contObj)
			{ //calc direction 
				newContPos = containerNextPos(boatPos.x, boatPos.y, x, y);
				
				if (newContPos == null)
				{
					checkDock();
					return null;
				}
				
				level.items[x][y] = 1;
				level.items[newContPos.x][newContPos.y] = contObj;
				moveCount++;
								
			}
			
			level.items[boatPos.x][boatPos.y] = 1;
			level.items[x][y] = boatObj;
			pushLevel();
		}		
		
		checkDock();
		return newContPos;
	}
	//---------------------------------------------------------
	public static void setBoatPosDirect(int x, int y){
		
		    Point boatPos = getBoat();
			level.items[boatPos.x][boatPos.y] = 1;
			level.items[x][y] = boatObj;
		    pushLevel();
	}
	
	//--------------------------------------------------------------------------------	
	public static void checkDock(){
		for (int x =0; x < levelWidth; x++)
			for (int y =0; y <  levelHeight; y++)
			{
				if (levelCopy.items[x][y] == dockObj)
				 if (level.items[x][y] != dockObj)
				  if (level.items[x][y] != boatObj)
					  if (level.items[x][y] != contObj)
						  level.items[x][y]  = dockObj;
				
			}

	}
	//--------------------------------------------------------------------------------	
	public static boolean checkGameEnd(){
		 
		for (int x =0; x < levelWidth; x++)
			for (int y =0; y <  levelHeight; y++)
			{
				if (levelCopy.items[x][y] == dockObj)
				 if (level.items[x][y] != contObj)
				 {
					 return false;
				 }
			}
		return true; 

	}
	
	//--------------------------------------------------------------------------------
	public static Point containerNextPos(int boatX, int boatY, int contX, int contY){
		Point newContPos = null;

		newContPos = new Point();
		newContPos.x = contX;
		newContPos.y = contY;
		
		if (boatX > contX) newContPos.x = contX-1; 
		else 
		if (boatX < contX) newContPos.x = contX+1;
		else
		if (boatY > contY)  newContPos.y = contY-1;
		else 
			if (boatY < contY)  newContPos.y = contY+1;
			
		
		if (getObjectAtXY(newContPos.x, newContPos.y) == blockObj) return null;
		if (getObjectAtXY(newContPos.x, newContPos.y) == contObj) return null;
		
		if (newContPos.x < 0) return null;
		if (newContPos.x > levelWidth) return null;

		if (newContPos.y < 0) return null;
		if (newContPos.y > levelHeight) return null;
		
		return newContPos; 
	}

	public static Point boatNextPos(int boatX, int boatY, int contX, int contY){
		Point newContPos = null;

		newContPos = new Point();
		newContPos.x = contX;
		newContPos.y = contY;
		
		if (boatX > contX) newContPos.x = contX-1; 
		else 
		if (boatX < contX) newContPos.x = contX+1;
		else
		if (boatY > contY)  newContPos.y = contY-1;
		else 
			if (boatY < contY)  newContPos.y = contY+1;
			
		
		if (getObjectAtXY(newContPos.x, newContPos.y) == contObj) return null;
		if (getObjectAtXY(newContPos.x, newContPos.y) == -1) return null;
		if (getObjectAtXY(newContPos.x, newContPos.y) == blockObj) return null;
		
		
		
		if (newContPos.x < 0) return null;
		if (newContPos.x > levelWidth) return null;

		if (newContPos.y < 0) return null;
		if (newContPos.y > levelHeight) return null;
		
		return newContPos; 
	}
//Level story ----------------------------------------------------------------------------------------------
public static void pushLevel(){

	Level storyLevel = new Level();
	for (int x =0; x < levelWidth; x++)
		for (int y =0; y < levelHeight; y++)
		{
			storyLevel.items[x][y] = level.items[x][y];
		}
	
	levelStory.add(storyLevel);
}

public static void popLevel(){

	if (levelStory.size() == 0) return; 
	
	Level storyLevel;	
	storyLevel = levelStory.get(levelStory.size()-1);
	levelStory.remove(levelStory.size()-1);
	
	for (int x =0; x < levelWidth; x++)
		for (int y =0; y < levelHeight; y++)
		{
			level.items[x][y] = storyLevel.items[x][y];
		}
		
}

}
