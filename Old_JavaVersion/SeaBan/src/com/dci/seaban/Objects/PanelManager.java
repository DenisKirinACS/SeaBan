package com.dci.seaban.Objects;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import com.dci.seaban.GameEngine;
import com.dci.seaban.R;
import com.dci.seaban.Render.RenderManager;
import com.dci.seaban.Service.GlobalVar;
import com.dci.seaban.Service.Texture;
import com.dci.seaban.Service.TextureItem;

public class PanelManager {

	private static final List<Panel> panels = new ArrayList<Panel>();
	
	private static final List<Panel> way = new ArrayList<Panel>();
	
	private static final List<Point> boatWay = new ArrayList<Point>();
	
	public static boolean completeWay = false;
	
	public static Panel add(int x, int y){
		
		
		Panel panel = new Panel(RenderManager.context, "plashka_obj", "panel", RenderManager.getCurrent(), -1, -1, -1);
		panel.gameX = x;
		panel.gameY = y;
		
		panel.x = GlobalVar.GameXToGLX(x);
		panel.y = -1.0f;
		panel.z = GlobalVar.GameXToGLX(y);
		
		panel.blend = GlobalVar.blendYes;
				
		panels.add(panel);
		
		return panel;
	}
	
	public static void clear(){
		panels.clear();
	}
	
	public static void draw(Canvas canvas){

		/*
		float x1, z1; // LT
		float x2, z2; // RT
		float x3, z3; // LB
		float x4, z4; // RB
		
        	
		Paint paint = new Paint();
		
		for (Panel panel: panels)
		{
			
			float sum = 0; 
			
			x1 = GlobalVar.getScrX(panel.x - (GlobalVar.coef / 2.0f), -1.0f, panel.z - (GlobalVar.coef / 2.0f));
			z1 = GlobalVar.getScrY(panel.x - (GlobalVar.coef / 2.0f), -1.0f, panel.z - (GlobalVar.coef / 2.0f));
			
			x2 = GlobalVar.getScrX(panel.x + (GlobalVar.coef / 2.0f), -1.0f, panel.z - (GlobalVar.coef / 2.0f));
			z2 = GlobalVar.getScrY(panel.x + (GlobalVar.coef / 2.0f), -1.0f, panel.z - (GlobalVar.coef / 2.0f));
			
			x3 = GlobalVar.getScrX(panel.x - (GlobalVar.coef / 2.0f), -1.0f, panel.z + (GlobalVar.coef / 2.0f));
			z3 = GlobalVar.getScrY(panel.x - (GlobalVar.coef / 2.0f), -1.0f, panel.z + (GlobalVar.coef / 2.0f));
			
			x4 = GlobalVar.getScrX(panel.x + (GlobalVar.coef / 2.0f), -1.0f, panel.z + (GlobalVar.coef / 2.0f));
			z4 = GlobalVar.getScrY(panel.x + (GlobalVar.coef / 2.0f), -1.0f, panel.z + (GlobalVar.coef / 2.0f));
			
			
			paint.setColor(Color.BLUE);
			paint.setAlpha(100);
			
			
			canvas.drawLine(x1, z1, x3, z3, paint);
			canvas.drawLine(x2, z2, x1, z1, paint);
			canvas.drawLine(x2, z2, x4, z4, paint);
			canvas.drawLine(x4, z4, x3, z3, paint);
			
			
		}

*/
		
	}
	
	public static Panel getTouch(int x, int y){
		
		Panel resultPanel = null; 
		
		float x1, z1; // LT
		float x2, z2; // RT
		float x3, z3; // LB
		float x4, z4; // RB
		
        				
		
		for (Panel panel: panels)
		{
			
			float sum = 0; 
			
			x1 = GlobalVar.getScrX(panel.x - (GlobalVar.coef / 2.0f), 0.0f, panel.z - (GlobalVar.coef / 2.0f));
			z1 = GlobalVar.getScrY(panel.x - (GlobalVar.coef / 2.0f), 0.0f, panel.z - (GlobalVar.coef / 2.0f));
			
			x2 = GlobalVar.getScrX(panel.x + (GlobalVar.coef / 2.0f), 0.0f, panel.z - (GlobalVar.coef / 2.0f));
			z2 = GlobalVar.getScrY(panel.x + (GlobalVar.coef / 2.0f), 0.0f, panel.z - (GlobalVar.coef / 2.0f));
			
			x3 = GlobalVar.getScrX(panel.x - (GlobalVar.coef / 2.0f), 0.0f, panel.z + (GlobalVar.coef / 2.0f));
			z3 = GlobalVar.getScrY(panel.x - (GlobalVar.coef / 2.0f), 0.0f, panel.z + (GlobalVar.coef / 2.0f));
			
			x4 = GlobalVar.getScrX(panel.x + (GlobalVar.coef / 2.0f), 0.0f, panel.z + (GlobalVar.coef / 2.0f));
			z4 = GlobalVar.getScrY(panel.x + (GlobalVar.coef / 2.0f), 0.0f, panel.z + (GlobalVar.coef / 2.0f));
			
		//	if ((x > x3) && ( x < x2) && (y > z1) && ( y < z4))
			//if ((x > x3) && (y > z1))
		//					return panel;
			/*
			Log.d("Alpha", "----------------------------------------");
			
			Log.d("Alpha", "D1: " + String.valueOf(GlobalVar.pointOf(x, y, x3, z3, x1, z1) ));
			Log.d("Alpha", "D2: " + String.valueOf(GlobalVar.pointOf(x, y, x2, z2, x1, z1) ));
			Log.d("Alpha", "D3: " + String.valueOf(GlobalVar.pointOf(x, y, x2, z2, x4, z4) ));
			Log.d("Alpha", "D4: " + String.valueOf(GlobalVar.pointOf(x, y, x3, z3, x4, z4) ));
			*/
			
			float d1 = GlobalVar.pointOf(x, y, x3, z3, x1, z1); 
			float d2 = GlobalVar.pointOf(x, y, x2, z2, x1, z1);
			float d3 = GlobalVar.pointOf(x, y, x2, z2, x4, z4);
			float d4 = GlobalVar.pointOf(x, y, x3, z3, x4, z4);
			
			if (( d1 > 0) && (d2 < 0) && (d3 > 0) && (d4 < 0)) return panel;

			
			

		}

		return resultPanel; 
		
	}
	
	public static int getCount(){
		return panels.size();
	}
	

	public static Panel getPanel(int index){
		return panels.get(index);
	}
	
	//------------------------------------------------------------------------------------
	public static void clearWay(){
		way.clear();
		boatWay.clear();
		completeWay = false;
	}
	
	public static boolean addToWay(Panel panel){
		boolean result = false;
		
		if (panelInWay(panel)) return true;
		
		if (GameEngine.getObjectAtXY(panel.gameX, panel.gameY) == GameEngine.contObj) return false;
		
		if (way.size() == 0) 
		{
			way.add(panel);
			Point point = new Point();
			point.x = panel.gameX;
			point.y = panel.gameY;
			boatWay.add(point);			
			result = true;
		}
		else
		{
		  Panel lastPanel = way.get(way.size()-1); //Top panel 
		  
		  if (((panel.gameX == lastPanel.gameX+1)
		      ||
		      (panel.gameX == lastPanel.gameX-1)
		      ||
		      (panel.gameY == lastPanel.gameY+1)
		      ||
		      (panel.gameY == lastPanel.gameY-1))
			  &&
			  ((panel.gameY == lastPanel.gameY) || (panel.gameX == lastPanel.gameX)))
		   {
			  way.add(panel);
			  
			  if (getWaySize() == 2)
			  {
					if (boatWay.get(0).x != panel.gameX) boatWay.get(0).x = panel.gameX;
					else
						boatWay.get(0).y = panel.gameY;
			  }
			  else
			  {
				  Panel secondPanel = way.get(way.size()-3);
				  
				  if ((panel.gameX == lastPanel.gameX) && (lastPanel.gameX== secondPanel.gameX)) boatWay.get(boatWay.size()-1).y = panel.gameY;
				  else
				  if ((panel.gameY == lastPanel.gameY) && (lastPanel.gameY== secondPanel.gameY)) boatWay.get(boatWay.size()-1).x = panel.gameX;
				  else
				  {
						Point point = new Point();

						
						point.x = panel.gameX;
						point.y = panel.gameY;
						boatWay.add(point);								  				  
						
						
						
				  }
			  }
			  
			  result = true;
		   }
		  
		}
		return result;
	}
	
	public static boolean panelInWay(Panel panel){
		
		for (Panel cPanel: way) 
		{
			if (cPanel == panel) return true;
		}
		return false;
	}
	
	public static int getWaySize(){
		return way.size();
	}
	
	public static Panel getWay(int index){
		if (index > way.size()) return null;
		return way.get(index);
	}
	
	/*
	public static Panel getNextWay(){
	 if (!completeWay) return null;	
		
	  if (getWaySize() <= 1) 
	  {
		  clearWay();
		  return null; 
	  }
		
	  way.remove(0);
	  return way.get(0);
	}
	*/
	
	public static void printWay(){
		for (Point point: boatWay) 
		{
			Log.e("BOAT WAY: ", String.valueOf(point.x) + ":" +  String.valueOf(point.y));
		}
		
	}
	
	public static Point getNextTrace(){
		
		 
		 if (!completeWay) return null;	
			
		  if (boatWay.size() == 0) return null; 
			
		  Point point = boatWay.get(0);
		  
		  if (boatWay.size() >= 1) boatWay.remove(0);
		  
		  if (boatWay.size() == 0) clearWay();
		  
		  
		  
		  return point;
		}
	
	public static Panel getPanelByCoord(int x, int y){
		
		for (Panel panel: panels)
		{
		  if ((panel.gameX == x) && (panel.gameY == y)) return panel; 
		}
		return null;
	}
	
	public static void makeAutoWay(int x, int y){
		clearWay();

		try
		{
		List<Point> tempWay = new ArrayList<Point>();
		Point point = new Point();
		point.x = GameEngine.getBoat().x;
		point.y = GameEngine.getBoat().y;
		tempWay.add(point);
		
		int stepCount = 0;
		boolean noWay  = false;
		int stepBreak = 100;
		//------- right hand 
		while (true)
		{			
			stepCount++;
			if (stepCount > stepBreak) 
			{
				tempWay.clear();
				noWay = true; 
				break; 
			}
			
			Point newStep = new Point();
			newStep.x = tempWay.get(tempWay.size()-1).x;
			newStep.y = tempWay.get(tempWay.size()-1).y;
			
			if (newStep.x == x) 
			{
				newStep.y++;
				if (GameEngine.getObjectAtXY(newStep.x, newStep.y) != 1)
				{
					newStep.y = tempWay.get(tempWay.size()-1).y; //back to x
					newStep.y--;
				}
				if (GameEngine.getObjectAtXY(newStep.x, newStep.y) != 1)
				{
					newStep.y = tempWay.get(tempWay.size()-1).y; //back to x
				}					
				tempWay.add(newStep);
			}
			else
			if (newStep.x < x) //touch up
			{
				newStep.x++;
				if (GameEngine.getObjectAtXY(newStep.x, newStep.y) != 1)
				{
					newStep.x = tempWay.get(tempWay.size()-1).x; //back to x
					newStep.y++;
					if (GameEngine.getObjectAtXY(newStep.x, newStep.y) != 1)
					{
						newStep.y = tempWay.get(tempWay.size()-1).y; //back to x
						newStep.y--;
					}					
				}
				tempWay.add(newStep);
			}
			else
			if (newStep.x > x) //touch down
			{
				newStep.x--;
				if (GameEngine.getObjectAtXY(newStep.x, newStep.y) != 1)
				{
					newStep.x = tempWay.get(tempWay.size()-1).x; //back to x
					newStep.y--;
					if (GameEngine.getObjectAtXY(newStep.x, newStep.y) != 1)
					{
						newStep.y = tempWay.get(tempWay.size()-1).y; //back to x
						newStep.y++;
					}					
				}
				tempWay.add(newStep);
			}
				
		
			if ((newStep.x == x) && (newStep.y == y)) 
			{
				noWay = false; 
				break;
			}
			
		}
		//Left hand --------------
		if (noWay)
		{
			noWay = false;
			stepCount = 0;
			tempWay.add(point);
			
			while (true)
			{			
				stepCount++;
				if (stepCount > stepBreak) 
				{
					tempWay.clear();
					noWay = true; 
					break; 
				}
				
				Point newStep = new Point();
				newStep.x = tempWay.get(tempWay.size()-1).x;
				newStep.y = tempWay.get(tempWay.size()-1).y;
				
				if (newStep.x == x) 
				{
					newStep.y--;
					if (GameEngine.getObjectAtXY(newStep.x, newStep.y) != 1)
					{
						newStep.y = tempWay.get(tempWay.size()-1).y; //back to x
						newStep.y++;
					}
					if (GameEngine.getObjectAtXY(newStep.x, newStep.y) != 1)
					{
						newStep.y = tempWay.get(tempWay.size()-1).y; //back to x
					}					
					tempWay.add(newStep);
				}
				else
				if (newStep.x < x) //touch up
				{
					newStep.x++;
					if (GameEngine.getObjectAtXY(newStep.x, newStep.y) != 1)
					{
						newStep.x = tempWay.get(tempWay.size()-1).x; //back to x
						newStep.y--;
						if (GameEngine.getObjectAtXY(newStep.x, newStep.y) != 1)
						{
							newStep.y = tempWay.get(tempWay.size()-1).y; //back to x
							newStep.y++;
						}					
					}
					tempWay.add(newStep);
				}
				else
				if (newStep.x > x) //touch down
				{
					newStep.x--;
					if (GameEngine.getObjectAtXY(newStep.x, newStep.y) != 1)
					{
						newStep.x = tempWay.get(tempWay.size()-1).x; //back to x
						newStep.y++;
						if (GameEngine.getObjectAtXY(newStep.x, newStep.y) != 1)
						{
							newStep.y = tempWay.get(tempWay.size()-1).y; //back to x
							newStep.y--;
						}					
					}
					tempWay.add(newStep);
				}
					
			
				if ((newStep.x == x) && (newStep.y == y)) 
				{
					noWay = false;
					break;
				}
				
			}
			
		}
		//Top way --------------
		if (noWay)
		{
			noWay = false;
			stepCount = 0;
			tempWay.add(point);
			
			while (true)
			{			
				stepCount++;
				if (stepCount > stepBreak) 
				{
					tempWay.clear();
					noWay = true; 
					break; 
				}
				
				Point newStep = new Point();
				newStep.x = tempWay.get(tempWay.size()-1).x;
				newStep.y = tempWay.get(tempWay.size()-1).y;
				
				if (newStep.y == y) 
				{
					newStep.x--;
					if (GameEngine.getObjectAtXY(newStep.x, newStep.y) != 1)
					{
						newStep.x = tempWay.get(tempWay.size()-1).x; //back to x
						newStep.x++;
					}
					if (GameEngine.getObjectAtXY(newStep.x, newStep.y) != 1)
					{
						newStep.x = tempWay.get(tempWay.size()-1).x; //back to x
					}					
					tempWay.add(newStep);
				}
				else
				if (newStep.y < y) //touch up
				{
					newStep.y++;
					if (GameEngine.getObjectAtXY(newStep.x, newStep.y) != 1)
					{
						newStep.y = tempWay.get(tempWay.size()-1).y; //back to x
						newStep.x--;
						if (GameEngine.getObjectAtXY(newStep.x, newStep.y) != 1)
						{
							newStep.x = tempWay.get(tempWay.size()-1).x; //back to x
							newStep.x++;
						}					
					}
					tempWay.add(newStep);
				}
				else
				if (newStep.y > y) //touch down
				{
					newStep.y--;
					if (GameEngine.getObjectAtXY(newStep.x, newStep.y) != 1)
					{
						newStep.y = tempWay.get(tempWay.size()-1).y; //back to x
						newStep.x++;
						if (GameEngine.getObjectAtXY(newStep.x, newStep.y) != 1)
						{
							newStep.x = tempWay.get(tempWay.size()-1).x; //back to x
							newStep.x--;
						}					
					}
					tempWay.add(newStep);
				}
					
			
				if ((newStep.x == x) && (newStep.y == y)) 
				{
					noWay = false;	 
					break;
				}
				
			}
			
		}
	
		if (!noWay)
		{
			for (Point nPoint: tempWay)
			{
				addToWay(getPanelByCoord(nPoint.x, nPoint.y));
			}
			completeWay = true;
		}
		}
		catch (Exception e)
		{
			clearWay();
		}
		
	}
	
	
}
	

