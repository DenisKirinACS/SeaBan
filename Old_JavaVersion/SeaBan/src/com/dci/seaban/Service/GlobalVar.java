package com.dci.seaban.Service;

import com.dci.seaban.Render.RenderManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.opengl.GLU;
import android.os.Build;
import android.util.Log;

public final class GlobalVar {
	public static final float DefGLBlockSize = 7.0f; // (5x5 level)
	public static final float DefLevelWidth = 5.0f;
	
	public static float coef = 7.0f;
	public static float panelSize = 7.0f;
	
	public static float levelScale = 1.0f;
	public static float levelCenter = 2.5f;
	
	public static Activity mainActivity = null;
	public static float alertPower = 0;

	public static boolean debugMode = false;

	public static int dragonCount = 0;
	
	public static final int blendNone = 0;
	public static final int blendYes = 1;
	public static final int blendBoth = 2;
	
	public static float moveSpeed = 0.2f;
	
	public static final float defaultWidth = 1920;
	public static final float defaultHeight = 1080;
	
	public static int greenMode = 1;
	

	public static float getScrX(float x, float y, float z) {
		if (RenderManager.getCurrent() == null)
			return -1;

		float[] vector = new float[4];
		int[] viewport = new int[] { 0, 0, RenderManager.width, RenderManager.height };

		GLU.gluProject(x, y, z, RenderManager.getCurrent()._mViewMatrix, 0, RenderManager.getCurrent().mProjectionMatrix, 0, viewport, 0, vector, 0);

		if (vector[2] > 1.0f) {
			return -1.0f;

		} else {
			return vector[0];
			// screenY = viewport[3] - vector[1];
		}
	}
	
	public static float getScrY(float x, float y, float z) {
		if (RenderManager.getCurrent() == null)
			return -1;

		float[] vector = new float[4];
		int[] viewport = new int[] { 0, 0, RenderManager.width, RenderManager.height };

		GLU.gluProject(x, y, z, RenderManager.getCurrent()._mViewMatrix, 0, RenderManager.getCurrent().mProjectionMatrix, 0, viewport, 0, vector, 0);

		if (vector[2] > 1.0f) {
			return -1.0f;

		} else {			
			return viewport[3] - vector[1];
		}
	}
	
	public static float getAlpha(float  xFrom, float yFrom, float xTo, float yTo){				
		float xp = xFrom -xTo;
		float yp = yFrom - yTo;				
 	    return (float)(Math.PI +  Math.atan2(yp, xp)) ; 	    
	}

	public static float pointOf(float xp, float yp, float x1, float y1, float x2, float y2){
		float A = -(y2 - y1);
		float B = x2 - x1;
		float C = -(A * x1 + B * y1);
		
		return A * xp + B * yp + C;
		
	}
	
	public static float GameXToGLX(int gameX){
		return (gameX - levelCenter) * coef;
	}

	public static float GameYToGLY(int gameY){
		return (gameY - levelCenter)  * coef;
	}
	
	//Screen Base methods 
	public static float GetScrX(float x)
	{
		if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) || (RenderManager.width == 0))
		{		
			  return x * (RenderManager.metrics.widthPixels / defaultWidth);
		}
		else
			  return x * (RenderManager.width / defaultWidth);
	}
	
	public static float GetScrY(float y)
	{
		if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) || (RenderManager.height == 0))
		{		
			  return y * (RenderManager.metrics.heightPixels / defaultHeight);
		}
		else
			  return y * (RenderManager.height / defaultHeight);
	}
	
	@SuppressLint("NewApi")
	public static float GetDefX(float x)
	{	  
		if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) || (RenderManager.width == 0))
		{		
			mainActivity.getWindowManager().getDefaultDisplay().getRealMetrics(RenderManager.metrics);
			return x / (RenderManager.metrics.widthPixels / defaultWidth);
		}
		else 
			return x / (RenderManager.width / defaultWidth);
			
	}
	
	@SuppressLint("NewApi")
	public static float GetDefY(float y)
	{
		if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) || (RenderManager.height == 0))
		{				
			mainActivity.getWindowManager().getDefaultDisplay().getRealMetrics(RenderManager.metrics);	  
			return y / (RenderManager.metrics.heightPixels / defaultHeight);
		}
		else
			return y / (RenderManager.height / defaultHeight);
	}
	
	
	public static int calculateWidthFromFontSize(String testString, int currentSize, Paint paint)
	{
	    Rect bounds = new Rect();	    
	    
	    paint.setTextSize(GetScrX(currentSize));
	    paint.getTextBounds(testString, 0, testString.length(), bounds);

	    return (int) Math.ceil( bounds.width());
	}

	public static int calculateHeightFromFontSize(Paint paint, String testString, int currentSize)
	{
	    Rect bounds = new Rect();
	    
	    paint.setTextSize(currentSize);
	    paint.getTextBounds(testString, 0, testString.length(), bounds);

	    return (int) Math.ceil( bounds.height());
	}
	
	public static void drawMultilineText(String str, float x, float y, Canvas canvas, int fontSize, float drawWidth) {
	    int      lineHeight = 0;
	    
	    String[] lines      = str.split(" ");

	    Paint paint = new Paint();
	    
	    
 
	    lineHeight = (int) (calculateHeightFromFontSize(paint, str, fontSize) * 1.2);
	    int yoffset = lineHeight;
	    
	    String line = "";
	    for (int i = 0; i < lines.length; ++i) {

	        if(calculateWidthFromFontSize(line + " " + lines[i], fontSize, paint) <= drawWidth){
	            line = line + " " + lines[i];

	        }else{
	            canvas.drawText(line, GetScrX(x), GetScrY(y + yoffset), paint);
	            yoffset += lineHeight;
	            line = lines[i];
	        }
	    }
	    canvas.drawText(line, GetScrX(x), GetScrY(y + yoffset), paint);

	}
	
	public static void  drawScaleBitmap(Canvas canvas, Bitmap bitmap, float x, float y, float width, float height)
	{
	    Rect src = new Rect(0,0,bitmap.getWidth(), bitmap.getHeight());	    
	    Rect dest = new Rect((int)GetScrX(x) , (int)GetScrY(y) , (int)GetScrX(x) + (int)GetScrX(width), (int)GetScrY(y) + (int)GetScrY(height));		    
        canvas.drawBitmap(bitmap, src, dest, null);		
        //getUsedMemorySize();
	}
	
	public static long getUsedMemorySize() {

	    long freeSize = 0L;
	    long totalSize = 0L;
	    long usedSize = -1L;
	    try {
	        Runtime info = Runtime.getRuntime();
	        freeSize = info.freeMemory();
	        totalSize = info.totalMemory();
	        usedSize = totalSize - freeSize;
	        Log.e("MEMORY", String.valueOf(freeSize) + " " + String.valueOf(totalSize) + " " + String.valueOf(usedSize));
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return usedSize;

	}	
	
}
