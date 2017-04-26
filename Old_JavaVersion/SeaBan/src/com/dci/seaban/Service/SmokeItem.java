package com.dci.seaban.Service;

import com.dci.seaban.Physics.Move;

public class SmokeItem {
	public float x = -1;  
	public float y = -1;
	public float z = -1;
	public float alpha;
	public float scale; 
	public Move move;
	public float lastSmokeY = 0;
	public float[] mModelMatrix = new float[16];
	
	public SmokeItem()
	{
		move = new Move(x, y, x, y, 0.0f);
	}
	
	public void reset(float x, float y, float z, float alpha)		 
	{
	 this.x = x;
	 this.y = y;
	 this.z = z;
	 this.alpha = alpha;
	 move.Reset(x, y, x, y+70.0f * GlobalVar.levelScale , 10.0f);
	}

}
