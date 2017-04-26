package com.dci.seaban.Canvas;

import com.dci.seaban.R;
import com.dci.seaban.Render.RenderManager;
import com.dci.seaban.Service.BitmapManager;
import com.dci.seaban.Service.GlobalVar;
import com.dci.seaban.Service.LevelTableRaw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public class LevelButton extends Button{

	
	private Paint paint;
	
	private int starImage;
	private int lockImage;
	
	public LevelTableRaw levelTableRaw;
	
	public LevelButton(Context context, LevelTableRaw levelTableRaw, float x, float y, float width, float height) {
		
		super(context,  R.drawable.levelbutton, R.drawable.levelbuttonsel, x, y, width, height);

		starImage = R.drawable.star;
		lockImage = R.drawable.lock;
		
		this.levelTableRaw = levelTableRaw;
		this.paint = new Paint();
		
 
	}
	
	public void Draw(Canvas canvas){
		
		super.Draw(canvas);
		if (visible)
		{
			
			paint.setTextSize( (GlobalVar.GetScrX(60) + GlobalVar.GetScrY(60)) / 2);
			//60 - 400x400
			//paint.setColor(0x0F0F0F);
		    Rect bounds = new Rect();		   
		    paint.getTextBounds(String.valueOf(levelTableRaw.levelNumber), 0, String.valueOf(levelTableRaw.levelNumber).length(), bounds);
			int color = Color.rgb(0x31, 0x31, 0x31);
			paint.setColor(color);
		    
		    
		    int tX =  (int)((GlobalVar.GetScrX(width) - bounds.right) / 2.0f);
		    int tY =  (int)(GlobalVar.GetScrY(height) - (bounds.bottom * 0.2f)); 
			
			canvas.drawText(String.valueOf(levelTableRaw.levelNumber), GlobalVar.GetScrX(x) + tX , GlobalVar.GetScrY(y) + tY - GlobalVar.GetScrY(110)  , paint);
			
			if (levelTableRaw.stars > 0)
				GlobalVar.drawScaleBitmap(canvas, BitmapManager.getBitmap(RenderManager.context, starImage), x + 40,  y + 240 - 64, 32, 32);
			if (levelTableRaw.stars > 1)
				GlobalVar.drawScaleBitmap(canvas, BitmapManager.getBitmap(RenderManager.context, starImage),  x + 80,  y + 240 - 64, 32, 32);
			if (levelTableRaw.stars > 2)
				GlobalVar.drawScaleBitmap(canvas, BitmapManager.getBitmap(RenderManager.context, starImage),  x + 120,  y + 240 - 64, 32, 32);
			
			if (levelTableRaw.lock != 0)
			 GlobalVar.drawScaleBitmap(canvas, BitmapManager.getBitmap(RenderManager.context, lockImage), x + tX + 64 , y + tY - GlobalVar.GetScrY(100), 64, 64);

		}
	}	

}
