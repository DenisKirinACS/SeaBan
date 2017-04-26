package com.dci.seaban.Canvas;

import com.dci.seaban.GameEngine;
import com.dci.seaban.R;
import com.dci.seaban.Objects.Panel;
import com.dci.seaban.Objects.PanelManager;
import com.dci.seaban.Physics.Move;
import com.dci.seaban.Render.RenderManager;
import com.dci.seaban.Render.SceneRender;
import com.dci.seaban.Service.GlobalVar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;


	public class LevelMenuCanvas extends View  {
	
	
	public SceneRender render;
	
	
	public boolean Visible = false;
	protected Paint paint = new Paint();    
    public String dataText;
	
	
	public int a = 0;
	public float mWidth =900.0f;// 640.0f;
	
	
	private Bitmap background;
	
	public MainMenuButton closeButton;
	public MainMenuButton cameraButton;
	public MainMenuButton camera2D3DButton;
	public MainMenuButton cameraToObjectButton;
	public MainMenuButton resetButton;
	public MainMenuButton stepBackButton;
	public MainMenuButton soundButton;
	public MainMenuButton homeButton;
	
	public Move move = new Move(-mWidth, 0, 0, 0, 1.0f);
	

	@SuppressLint("NewApi")
	public  LevelMenuCanvas(Context context) {
		super(context); //context is activity 
		
		

		setMinimumWidth(RenderManager.metrics.widthPixels);
		setMinimumHeight(RenderManager.metrics.heightPixels);
					
		setFocusable(false);

		closeButton = new MainMenuButton(context, "", R.drawable.empty , R.drawable.empty, 800, 1080/2-100, 200, 200, 20);
		
		
		resetButton = new MainMenuButton(context, "", R.drawable.replay, R.drawable.replay, 150, 50, 200, 200, 10);
		stepBackButton = new MainMenuButton(context, "Back", R.drawable.levelmenubutton, R.drawable.levelmenubuttonsel, 450, 50, 200, 200, 10);
		
		camera2D3DButton = new MainMenuButton(context, "2D", R.drawable.levelmenubutton, R.drawable.levelmenubuttonsel, 300, 300, 200, 200, 10);
		
		cameraButton = new MainMenuButton(context, "Camera", R.drawable.levelmenubutton, R.drawable.levelmenubuttonsel, 150, 550, 200, 200, 10);		
		cameraToObjectButton = new MainMenuButton(context, "Walk", R.drawable.levelmenubutton, R.drawable.levelmenubuttonsel, 450, 550, 200, 200, 10);
		
		soundButton = new MainMenuButton(context, "", R.drawable.speakoff, R.drawable.speackon, 150, 800, 200, 200, 10);
		
		homeButton = new MainMenuButton(context, "", R.drawable.home, R.drawable.home, 450, 800, 200, 200, 10);
		
		background=BitmapFactory.decodeResource(getResources(), R.drawable.levelmenu);
		
		this.layout((int)-mWidth, 0, (int)mWidth, 0);
	}

	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(getSuggestedMinimumWidth(),
				getSuggestedMinimumHeight());
	}

	
	private void blur(Bitmap bkg, View view) {
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);
		
		//if (Visible) this.layout(0, 0,  (int)mWidth, 1080);
		//else 
		
			//this.layout(-(int)mWidth, 0,  0, 1080);
	
		if (Visible)
		  this.layout((int)(move.GetX()-mWidth), 0,  (int)move.GetX(), 1080);
		else 
			this.layout((int)(move.GetX() - mWidth), 0,  (int)(move.GetX()), 1080);
		
		GlobalVar.drawScaleBitmap(canvas, background, 0, 0, 995, 1080);
		
		closeButton.Draw(canvas);
		cameraButton.Draw(canvas);
		camera2D3DButton.Draw(canvas);
		cameraToObjectButton.Draw(canvas);
		resetButton.Draw(canvas);
		stepBackButton.Draw(canvas);
		soundButton.Draw(canvas);
		homeButton.Draw(canvas);
		if (move.GetX() > -1) RenderManager.lockRender = false;
		
	
		
	
		

		
		//postInvalidate();
		
	}
	
	public void Hide(){
		Visible = false;
		move.Reset(mWidth, 0, 0, 0, 1.0f);
		/*
		for (int i = 0; i < 100; i++)
			try {
				 postInvalidate();
				 Thread.sleep(10);
			    } 
			catch (InterruptedException e) {
			}
			*/
			
	}
	
	public void Show(){
		Visible = true;
		move.Reset(0, 0, mWidth, 0, 1.0f);
		/*
		for (int i = 0; i < 100; i++)
			try {
				 postInvalidate();
				 Thread.sleep(10);
				} 
			catch (InterruptedException e) {
			}
			*/
	}
	

}
