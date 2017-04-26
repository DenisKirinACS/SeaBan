package com.dci.seaban.Canvas;

import com.dci.seaban.R;
import com.dci.seaban.Physics.Move;
import com.dci.seaban.Render.RenderManager;
import com.dci.seaban.Service.BitmapManager;
import com.dci.seaban.Service.GlobalVar;
import com.dci.seaban.Service.Sound;
import com.dci.seaban.Service.SoundManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.View;

public class Button extends View {
	
	public float x, y, width, height;
	
	public boolean selected = false;
	public boolean click = false;
	public boolean visible = true;
	public boolean enable = true;
	
	public int imageId;
	public int selImageId;
	public boolean soundOn = true; 
	
	
	
	public Button(Context context, int imageId, int selImageId, float x, float y, float width, float height){
		super(context);
		
		this.imageId = imageId;
		this.selImageId = selImageId;

				
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height; 
		
		SoundManager.Add("button", Sound.buttonSoundID);
		
	}
	
	
	public void Draw(Canvas canvas){
		if (visible)
		{
		
		 
		  if (selected) GlobalVar.drawScaleBitmap(canvas, BitmapManager.getBitmap(RenderManager.context, selImageId), x, y, width, height);
		   else 
		    GlobalVar.drawScaleBitmap(canvas, BitmapManager.getBitmap(RenderManager.context, imageId), x, y, width, height);
		}
	}
	
	public boolean CheckTouch(int action, float px, float py){
		
		if (!visible) return false;
		
		selected = false;
		click = false;
		px = GlobalVar.GetDefX(px);
		py = GlobalVar.GetDefY(py);
		
		if ((px > x) && (px < x + width) 
			&&
			(py > y) && (py < y + height))
		{
		
		switch (action & MotionEvent.ACTION_MASK) 
		{
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
		{
			selected = true;
			if (soundOn)
			  SoundManager.Play("button", 1.0f,  1.0f, false);
			break;
		}
		case MotionEvent.ACTION_MOVE:		
		{
			selected = true;
			break;
		}
		
		case MotionEvent.ACTION_UP:		
		case MotionEvent.ACTION_POINTER_UP:
		{
			selected = false;
			click = true;
			break;
		}
				
		}

	
	}
		return click;
	}
	
	
}
