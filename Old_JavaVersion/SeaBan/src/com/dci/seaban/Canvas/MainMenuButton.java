package com.dci.seaban.Canvas;

import com.dci.seaban.Render.RenderManager;
import com.dci.seaban.Service.GlobalVar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public class MainMenuButton extends Button {

	private String caption;
	private Paint paint;
	private float  poprovkaNaVeter; 
	
	
	
	public MainMenuButton(Context context, String caption, int imageId, int selImageId, float x, float y, float width, float height, float poprovkaNaVeter) {
		super(context, imageId, selImageId, x, y, width, height);
		
		this.paint = new Paint();
		
		Typeface tf = Typeface.createFromAsset(RenderManager.context.getAssets(), "fonts/comic.ttf");			
		paint.setTypeface(tf);		
		
		this.caption = caption;
		this.poprovkaNaVeter =  GlobalVar.GetScrY(poprovkaNaVeter); 
	}

	
	public void Draw(Canvas canvas){
		
		super.Draw(canvas);
		if (visible)
		{
			
			paint.setTextSize( ((GlobalVar.GetScrX(60) + GlobalVar.GetScrY(60)) / 2) * (width / 400) );
			//paint.setTextSize( GlobalVar.GetScrX(60));
			
			int color = Color.rgb(0x31, 0x31, 0x31);
			paint.setColor(color);
		    Rect bounds = new Rect();		   
		    paint.getTextBounds(caption, 0, caption.length(), bounds);
		    
		    int tX =  (int)((GlobalVar.GetScrX(width) - bounds.right) / 2.0f);
		    int tY =  (int)(GlobalVar.GetScrY(height) - (bounds.bottom * 0.2f)); 
			
			canvas.drawText(caption, GlobalVar.GetScrX(x) + tX , GlobalVar.GetScrY(y) + tY -  poprovkaNaVeter, paint);

		}
	}

	

}
