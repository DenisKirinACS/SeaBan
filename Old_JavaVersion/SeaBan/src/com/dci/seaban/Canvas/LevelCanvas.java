package com.dci.seaban.Canvas;

import java.util.concurrent.TimeUnit;

import com.dci.seaban.GameEngine;
import com.dci.seaban.R;
import com.dci.seaban.Objects.Panel;
import com.dci.seaban.Objects.PanelManager;
import com.dci.seaban.Physics.Move;
import com.dci.seaban.Physics.Swing;
import com.dci.seaban.Render.RenderManager;
import com.dci.seaban.Render.SceneRender;
import com.dci.seaban.Service.BitmapManager;
import com.dci.seaban.Service.GlobalVar;
import com.purplebrain.adbuddiz.sdk.AdBuddiz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.View;


	public class LevelCanvas extends View  {
	
	
	public SceneRender render;
	private Bitmap back;
	
	public int hintNumber = 0;
	private Bitmap hintImage1;
	private Bitmap hintImage2;
	private Bitmap hintImage3;
	
	
	protected Paint paint = new Paint();    
    public String dataText;
	//	private Bitmap b;
	
		public int a = 0;
	
	
	public JoyButton tlButton;
	public JoyButton trButton;
	public JoyButton blButton;
	public JoyButton brButton;
	
	public Button menuButton;
	
	public  MainMenuButton nextButton;
	public  MainMenuButton selectLevelButton;
	public  MainMenuButton resetButton;
	public  MainMenuButton closeHintButton;
	
	public int viewMode = 0;
	
	private Swing swing1 = new Swing(20.1, 0.1);
	
	public Move scoreMove = new Move(0,0,0,0,0);
	

	@SuppressLint("NewApi")
	public LevelCanvas(Context context) {
		super(context); //context is activity
		

		Typeface tf = Typeface.createFromAsset(RenderManager.context.getAssets(), "fonts/comic.ttf");			
		paint.setTypeface(tf);
		
		back = BitmapFactory.decodeResource(getResources(), R.drawable.loading);
		
		
		
		setMinimumWidth(RenderManager.metrics.widthPixels);
		setMinimumHeight(RenderManager.metrics.heightPixels);
					
		setFocusable(false);
		
		tlButton = new JoyButton(context, R.drawable.joylt, R.drawable.joyltsel, 0, 0, 200, 200);
		trButton = new JoyButton(context, R.drawable.joyrt, R.drawable.joyrtsel, 1920 - 200, 0, 200, 200);
		blButton = new JoyButton(context, R.drawable.joylb, R.drawable.joylbsel, 0, 1080-200, 200, 200);
		brButton = new JoyButton(context, R.drawable.joyrb, R.drawable.joyrbsel, 1920 - 200, 1080-200, 200, 200);
		
		menuButton = new Button(context, R.drawable.levelmenuopenbutton, R.drawable.levelmenuopenbutton, 0, 1080 / 2 - 100, 120, 160);
		
		selectLevelButton = new MainMenuButton(context, "", R.drawable.home, R.drawable.home, 250, 1080-200, 200, 200, 10);
		nextButton = new MainMenuButton(context, "", R.drawable.next, R.drawable.next, 450, 1080-200, 200, 200, 10);		
		resetButton = new MainMenuButton(context, "", R.drawable.replay, R.drawable.replay, 700, 1080-200, 200, 200, 10);
		closeHintButton = new MainMenuButton(context, "Close", R.drawable.levelmenubutton, R.drawable.levelmenubuttonsel, 700, 1080-200, 200, 200, 10);
		
	}
	
	
	public void joyToFlat(){
		tlButton.toFlat();
		trButton.toFlat();
		blButton.toFlat();
		brButton.toFlat();
		
	}
	
	public void joyFromFlat(){
		tlButton.fromFlat();
		trButton.fromFlat();
		blButton.fromFlat();
		brButton.fromFlat();
		
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(getSuggestedMinimumWidth(),
				getSuggestedMinimumHeight());
	}

   private void drawText(Canvas canvas, String text, int x, int y){

       paint.setTextSize(GlobalVar.GetScrX(60));
		int color = Color.rgb(0x31, 0x31, 0x31);		
		paint.setColor(color);
		paint.setAlpha(100);			
		canvas.drawText(text, x + 5 , y + 5,paint);
		color = Color.rgb(0xD1, 0xD1, 0x31);		
		paint.setColor(color);
		paint.setAlpha(255);
		canvas.drawText(text, x, y, paint);
	   
   }
	
	@SuppressLint("NewApi")
	@Override
	protected void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);
				
		
		
		
		
		
		
		switch (viewMode)
		{
		case 0: // play mode
			/*
		for (int x =0; x < 5; x++)
			for (int y =0; y < 5; y++)
			{
				paint.setColor(Color.WHITE);
				canvas.drawText(String.valueOf(GameEngine.level[x][y]), 500.0f + (x+1) * 20.0f, (y+1) * 20.0f, paint);
			}
			*/
		
			
			GameEngine.gameTime += System.currentTimeMillis() - GameEngine.gameStartTime;
			
			
		if (dataText != null)
			 GlobalVar.drawMultilineText(dataText, 40, 40, canvas,  20, 300);

		String hms = " move: " + String.valueOf(GameEngine.moveCount) + " time: " + String.format("%02d:%02d", 
			    TimeUnit.MILLISECONDS.toMinutes(GameEngine.gameTime),
			    TimeUnit.MILLISECONDS.toSeconds(GameEngine.gameTime) % TimeUnit.MINUTES.toSeconds(1));		
		
		int textWidth = GlobalVar.calculateWidthFromFontSize(hms, 60, paint);
		
		drawText(canvas, hms , RenderManager.metrics.widthPixels / 2 - textWidth / 2, 40);
		
		tlButton.Draw(canvas);
		trButton.Draw(canvas);
		blButton.Draw(canvas);
		brButton.Draw(canvas);
		menuButton.Draw(canvas);
		PanelManager.draw(canvas);
		break;
		case 1: // loading mode
			 if (AdBuddiz.isReadyToShowAd(RenderManager.mainActivity))
			 {
			    AdBuddiz.showAd(RenderManager.mainActivity);
			 }
			 else
			 GlobalVar.drawScaleBitmap(canvas, back, 0, 0, GlobalVar.defaultWidth, GlobalVar.defaultHeight);
			break;
				
		case 2: // pause 
		  GlobalVar.drawScaleBitmap(canvas, back, 0, 0, GlobalVar.defaultWidth, GlobalVar.defaultHeight);
		break;

		case 4: // menu
			  
			Path outerPath = new Path();
			// add rect covering the whole view area
			outerPath.addRect(0, 0, getWidth(), getHeight(), Path.Direction.CW);
			// add "selection" rect;
			RectF inner = new RectF(0, 0, RenderManager.metrics.widthPixels, RenderManager.metrics.heightPixels);
			outerPath.addRect(inner, Path.Direction.CW);
			// set the fill rule so inner area will not be painted
			outerPath.setFillType(Path.FillType.EVEN_ODD);

			// set up paints
			Paint outerPaint = new Paint();
			outerPaint.setColor(0x60000000);

			Paint borderPaint = new Paint();
			borderPaint.setARGB(255, 255, 128, 0);
			borderPaint.setStyle(Paint.Style.STROKE);
			borderPaint.setStrokeWidth(4);

			// draw
			canvas.drawPath(outerPath, outerPaint);
			canvas.drawRect(inner, borderPaint);			
			break;
		//show score	
		case 5:
			outerPath = new Path();
			// add rect covering the whole view area
			outerPath.addRect(0, 0, getWidth(), getHeight(), Path.Direction.CW);
			// add "selection" rect;
			inner = new RectF(0, 0, RenderManager.metrics.widthPixels, RenderManager.metrics.heightPixels);
			outerPath.addRect(inner, Path.Direction.CW);
			// set the fill rule so inner area will not be painted
			outerPath.setFillType(Path.FillType.EVEN_ODD);

			// set up paints
			outerPaint = new Paint();
			outerPaint.setColor(0x60000000);
			
			borderPaint = new Paint();
			borderPaint.setARGB(255, 255, 128, 0);
			borderPaint.setStyle(Paint.Style.STROKE);
			borderPaint.setStrokeWidth(4);

			// draw
			canvas.drawPath(outerPath, outerPaint);
			canvas.drawRect(inner, borderPaint);			

			drawText(canvas, "Level " + String.valueOf(GameEngine.levelNumber), (int)GlobalVar.GetScrX(700) , (int)GlobalVar.GetScrY(100));
			drawText(canvas, "Score: " + String.valueOf((int)scoreMove.GetX()), (int)GlobalVar.GetScrX(200) , (int)GlobalVar.GetScrY(200));
			drawText(canvas, "Steps: " + String.valueOf(GameEngine.moveCount), (int)GlobalVar.GetScrX(200) , (int)GlobalVar.GetScrY(300));
			
			
			hms =  String.format("%02d:%02d", 
				    TimeUnit.MILLISECONDS.toMinutes((long)scoreMove.GetY()),
				    TimeUnit.MILLISECONDS.toSeconds((long)scoreMove.GetY()) % TimeUnit.MINUTES.toSeconds(1));		
			
			drawText(canvas, "Time: " + hms, (int)GlobalVar.GetScrX(200) , (int)GlobalVar.GetScrY(400));
			drawText(canvas, "Stars: " +  String.valueOf(GameEngine.gameStars), (int)GlobalVar.GetScrX(200) , (int)GlobalVar.GetScrY(500));
			
			nextButton.Draw(canvas);
			selectLevelButton.Draw(canvas);
			resetButton.Draw(canvas);
			break;
		
			case 6:
				switch (hintNumber)
				{
				case 1:
				  GlobalVar.drawScaleBitmap(canvas, BitmapManager.getBitmap(RenderManager.context, R.drawable.levelhint1), GlobalVar.defaultWidth / 2 - 1200 / 2 ,  GlobalVar.defaultHeight / 2 - 900 / 2, 1200, 900);
				  break;
				case 2:
					  GlobalVar.drawScaleBitmap(canvas, BitmapManager.getBitmap(RenderManager.context, R.drawable.levelhint2),  GlobalVar.defaultWidth / 2 - 1200 / 2 ,  GlobalVar.defaultHeight / 2 - 900 / 2, 1200, 900);
					  break;
				case 3:
					  GlobalVar.drawScaleBitmap(canvas, BitmapManager.getBitmap(RenderManager.context, R.drawable.levelhint3),  GlobalVar.defaultWidth / 2 - 1200 / 2 ,  GlobalVar.defaultHeight / 2 - 900 / 2, 1200, 900);
					  break;					  				  
				}
				//closeHintButton.Draw(canvas);
				break; 
			
		}
	
		//reset last game time
		GameEngine.gameStartTime = System.currentTimeMillis();
	}
	
	
	

}
