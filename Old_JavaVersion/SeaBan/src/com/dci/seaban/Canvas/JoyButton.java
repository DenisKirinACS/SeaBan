package com.dci.seaban.Canvas;

import com.dci.seaban.R;
import com.dci.seaban.Physics.Move;
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

public class JoyButton extends Button {
	private Bitmap image;
	private Bitmap selImage;
	
	private Bitmap imageFlat;
	private Bitmap selImageFlat;
	
	
	public float x, y, width, height;
	
	public boolean selected = false;
	public boolean click = false;
	public boolean visible = true;
	public boolean enable = true;
	private boolean flatMode = true; 
	
	private Move rotateMove = new Move(0, 0, 0, 0, 0.0);
	
	public JoyButton(Context context, int imageId, int selImageId, float x, float y, float width, float height){
		super(context,  R.drawable.levelbutton, R.drawable.levelbuttonsel, x, y, width, height);
		soundOn = false;
		image=BitmapFactory.decodeResource(getResources(), imageId);
		selImage=BitmapFactory.decodeResource(getResources(), selImageId);

		Matrix matrix = new Matrix();
		matrix.postRotate(-45);
		imageFlat = Bitmap.createBitmap(image , 0, 0, image.getWidth(), image.getHeight(), matrix, true);
		selImageFlat = Bitmap.createBitmap(selImage , 0, 0, selImage.getWidth(), selImage.getHeight(), matrix, true);		
				
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height; 
		
		SoundManager.Add("button", Sound.buttonSoundID);
		
	}
	
	public void toFlat()
	{
		flatMode = true; 
	}
	
	public void fromFlat()
	{
		flatMode = false; 
	}
	
	
	public void Draw(Canvas canvas){
		if (visible)
		{
		 if (!rotateMove.stop)
		 {
           //							 
		 }
	
		
		 
		 if (!flatMode)
		 {
		  if (selected) GlobalVar.drawScaleBitmap(canvas, selImage, x, y, width, height);
		   else 
		    GlobalVar.drawScaleBitmap(canvas, image, x, y, width, height);
		 }
		 else
		 {
			  if (selected) GlobalVar.drawScaleBitmap(canvas, selImageFlat, x, y, width, height);
			   else 
			    GlobalVar.drawScaleBitmap(canvas, imageFlat, x, y, width, height);		 
		 }
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
