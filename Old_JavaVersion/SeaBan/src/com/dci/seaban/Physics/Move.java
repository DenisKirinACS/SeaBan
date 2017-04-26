package com.dci.seaban.Physics;

import android.os.SystemClock;
import android.util.Log;

public class Move {
	
	private long startTime;
		
	private float r;
	private float xFrom;
	private float yFrom;
	private float xTo;
	private float yTo;
	private double endTime;
	private float speed;
	private float alpha;
	
	public boolean stop = true; 
	
	
	public Move(float  xFrom, float yFrom, float xTo, float yTo, double endTime)
	{		
		Reset(xFrom, yFrom, xTo, yTo, endTime);	
	}
	
	public void Reset(float  xFrom, float yFrom, float xTo, float yTo, double endTime)
	{
		if (endTime == 0.0f) return; 
		startTime = SystemClock.uptimeMillis();
		
		this.xFrom = xFrom;
		this.yFrom = yFrom;
		this.xTo = xTo;
		this.yTo = yTo;
		this.endTime = endTime;
		
		float xp = xFrom -xTo;
		float yp = yFrom - yTo;
		
		
 	    alpha = (float)(Math.PI +  Math.atan2(yp, xp)) ;
 	    
 	    //int intAlpha = (int)(Math.toDegrees(alpha) / 180d);
 	    
 	    //alpha = (float)(intAlpha * 3.14f);
 	    
		r =  (float)(Math.sqrt( (xp * xp) + (yp * yp)));
 	    
 	
		
		this.speed = (float) (r / endTime);
		
		/*
		Log.d("Alpha", "Alpha: " + String.valueOf(Math.toDegrees(alpha)) + " " +  String.valueOf(xFrom)  
				+ " " +  String.valueOf(yFrom)  
				+ " " +  String.valueOf(xTo)  
				+ " " +  String.valueOf(yTo)
				+ " " +  String.valueOf(r));
		*/
		stop = false;
	}

	public float GetR()
	{
		if (stop)
			return r;
		double time = (SystemClock.uptimeMillis() - startTime) / 1000.f;
		float result = 0.0f;
		if ((time < endTime) && (endTime != 0.0f)) result = (float)(time * speed);
		else 
		{
			stop = true; 
			result = r;
		}
		
		if (result > r) result = r;
		
		
		
		 return result;
		
		
	}
	public float GetX()
	{
		float x = (float)(GetR()*Math.cos(alpha) + xFrom);
		//Log.d("Alpha", "X: " + String.valueOf(x));
		return x;
		
		
				
	}
	
	public float GetY()
	{		
		
		float y = (float)(GetR()*Math.sin(alpha) + yFrom);
		//Log.d("Alpha", "Y: " + String.valueOf(y));
		return y;
				
	}
	
	public float GetAlpha()
	{		
		return (float)(alpha);
				
	}
	
	
}
