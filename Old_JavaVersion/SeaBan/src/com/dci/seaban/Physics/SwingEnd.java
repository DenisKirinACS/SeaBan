package com.dci.seaban.Physics;

import android.os.SystemClock;
import android.util.Log;

public class SwingEnd {
	
	private long startTime;
		
	private double amp;
	private double period;
	public boolean stop; 
	private double v;
	
	public SwingEnd(double amp, double period )
	{
		
		this.amp = amp;
		this.period = period;
		this.stop = true;
	}
	
	public void Restart(){
		if (stop)
		{
		  startTime = SystemClock.uptimeMillis();	
		}		
		
		v = amp / period;
		this.stop = false;
	}

	public float GetX()
	{
		if (stop) return (float)amp;
		
		double time = (SystemClock.uptimeMillis() - startTime) / 1000.f;
		
		if (time >= period) stop = true; 
				
		return (float)(v * time);
				
	}
}
