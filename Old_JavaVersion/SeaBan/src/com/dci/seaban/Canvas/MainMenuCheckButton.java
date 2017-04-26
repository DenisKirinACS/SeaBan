package com.dci.seaban.Canvas;

import com.dci.seaban.Render.RenderManager;
import com.dci.seaban.Service.BitmapManager;
import com.dci.seaban.Service.GlobalVar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class MainMenuCheckButton extends MainMenuButton {

	public boolean check = false;
	private int checkImage;
	
	public MainMenuCheckButton(Context context, String caption, int imageId, int selImageId, int checkImageId, float x, float y, float width, float height, float poprovkaNaVeter) {
		super(context, caption, imageId, selImageId, x, y, width, height, poprovkaNaVeter);
		checkImage = checkImageId;
		
	}
	
	public void Draw(Canvas canvas){
		
		if (visible) 
		{
			
			super.Draw(canvas);
			if (check)
				GlobalVar.drawScaleBitmap(canvas, BitmapManager.getBitmap(RenderManager.context, checkImage), x, y, width, height);
				
			
		}
		
	}
	
	

}
