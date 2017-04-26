package com.dci.seaban.Canvas;

import java.util.Timer;

import com.dci.seaban.R;
import com.dci.seaban.Render.RenderManager;
import com.dci.seaban.Render.SceneRender;
import com.dci.seaban.Service.GlobalVar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

	public class LogoCanvas extends View {
	
	public Timer logoTimer;
	private int timerCount = 0; 
	
	
	protected Paint paint = new Paint();    

    
	private Bitmap logo;
	
	private float i = 0;
    

	@SuppressLint("NewApi")
	public LogoCanvas(Context context) {
		super(context); //context is activity 
		
		

		
		setMinimumWidth(RenderManager.metrics.widthPixels);
		setMinimumHeight(RenderManager.metrics.heightPixels);
					
		setFocusable(false);
				
        logo=BitmapFactory.decodeResource(getResources(), R.drawable.logo);
       		
	}
	

	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(getSuggestedMinimumWidth(),
				getSuggestedMinimumHeight());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);
		
		    paint.setAlpha(100);
		    		
	        
	        GlobalVar.drawScaleBitmap(canvas, logo, 0, 0, GlobalVar.defaultWidth, GlobalVar.defaultHeight);
	        
	        GlobalVar.drawMultilineText("Loading " + String.valueOf((int)i), 0, 100, canvas, 40, 200);
	        i++;
		
	}
	
	


	
}
