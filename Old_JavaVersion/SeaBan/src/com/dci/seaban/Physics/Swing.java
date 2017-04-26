package com.dci.seaban.Physics;

import android.os.SystemClock;
import android.util.Log;

public class Swing {
	
	private long startTime;
		
	private double amp;
	private double period;
	
	public Swing(double amp, double period )
	{
		startTime = SystemClock.uptimeMillis();
		this.amp = amp;
		this.period = period; 
	}

	public float GetX()
	{
		double time = (SystemClock.uptimeMillis() - startTime) / 1000.f;
		return (float)(amp * Math.sin(period * time));
		//Log.d("AMP", "AMP: " + String.valueOf(a * Math.sin(b)));		
	}
}
