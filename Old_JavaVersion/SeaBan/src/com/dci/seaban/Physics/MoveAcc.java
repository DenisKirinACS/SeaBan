package com.dci.seaban.Physics;

import android.os.SystemClock;
import android.util.Log;

public class MoveAcc {
	
	private long startTime;
		
	private float r;
	private float xFrom;
	private float yFrom;
	private float xTo;
	private float yTo;
	private double endTime;
	private float speed;
	private float alpha;
	private float v0;
	private float acc;
	
	public boolean stop = true; 
	
	
	public MoveAcc(float  xFrom, float yFrom, float xTo, float yTo, double endTime, float v0)
	{		
		Reset(xFrom, yFrom, xTo, yTo, endTime, v0);	
	}
	
	public void Reset(float  xFrom, float yFrom, float xTo, float yTo, double endTime, float v0)
	{
		if (endTime == 0.0f) return; 
		startTime = SystemClock.uptimeMillis();
		
		this.xFrom = xFrom;
		this.yFrom = yFrom;
		this.xTo = xTo;
		this.yTo = yTo;
		this.endTime = endTime;
		this.v0 = v0;
		
		
		float xp = xFrom -xTo;
		float yp = yFrom - yTo;
		
		
 	    alpha = (float)(Math.PI +  Math.atan2(yp, xp)) ;
 	    
 	    
		r =  (float)(Math.sqrt( (xp * xp) + (yp * yp)));
 	    
 	
		
		this.speed = (float) (r / endTime);
		
		this.acc = (float)((r*2) / (endTime*endTime));
		
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
		//if ((time < endTime) && (endTime != 0.0f)) result = (float)( (acc * (time * time)) / 2);
		if ((time < endTime) && (endTime != 0.0f)) result = (float)((((time * 2) * speed) * ((time * 2) * speed)) /  (acc * 2));
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
