package com.dci.seaban;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import com.dci.seaban.R;
import com.dci.seaban.Canvas.LevelCanvas;
import com.dci.seaban.Canvas.LevelMenuCanvas;
import com.dci.seaban.Canvas.MenuCanvas;
import com.dci.seaban.Objects.Map;
import com.dci.seaban.Render.RenderManager;
import com.dci.seaban.Render.Renderer;
import com.dci.seaban.Service.ConfigDB;
import com.dci.seaban.Service.GlobalVar;
import com.dci.seaban.Service.LevelDB;
import com.dci.seaban.Service.MultisampleConfigChooser;
import com.dci.seaban.Service.ShowMessage;
import com.dci.seaban.Service.Sound;
import com.dci.seaban.Service.SoundManager;
import com.purplebrain.adbuddiz.sdk.AdBuddiz;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ConfigurationInfo;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RelativeLayout;



public class MainActivity extends Activity implements OnTouchListener {

	private WakeLock wakeLock;
	private int level1;
	private Timer logoTimer;

	private Renderer renderer;

	private int timerCount = 0;
	
	private boolean touchLock = false;

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		GlobalVar.mainActivity = this;


		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		/*
		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE | 
				View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | 
				View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | 
				View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | 
				View.SYSTEM_UI_FLAG_FULLSCREEN | 
				View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
				View.SYSTEM_UI_FLAG_IMMERSIVE);
		getWindow().getDecorView().setSystemUiVisibility(View.GONE);
		*/
		
		
		super.onCreate(savedInstanceState);
		
		
		

		

		// global exception handler
		// --------------------------------------------------------
		if (!GlobalVar.debugMode) {
			Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
				@Override
				public void uncaughtException(Thread paramThread, Throwable paramThrowable) {

					//Log.e("SeaBan exception", paramThrowable.getMessage());
					Log.d("SeaBan", "exception", paramThrowable);	

				}
			});
		}
		
		Sound.init(this);

		PowerManager powerManager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "SeabanUnlockScreen");

		
		RenderManager.db = new ConfigDB(this, "SeaBan", null, 1);
		RenderManager.db.init();

		
		RenderManager.levelDB = new LevelDB(this, "SeaBan", null, 1);
		
		RenderManager.levelDB.init();

		
		
		
		//RenderManager.db.SetQuality(1);
		//int q = RenderManager.db.GetQuality();
	//	RenderManager.db.SetQuality(q + 2);
		
		RenderManager.mGLSurfaceView = new GLSurfaceView(this);
		
		try
		{
		// RenderManager.mGLSurfaceView.setEGLConfigChooser(new MultisampleConfigChooser());
		}
		catch(Exception e){}

		// Check if the system supports OpenGL ES 2.0.
		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

		if (supportsEs2) {
			RenderManager.mGLSurfaceView.setEGLContextClientVersion(2);
		//	RenderManager.mGLSurfaceView.setEGLConfigChooser(true);
			

			renderer = new Renderer();
			renderer.context = this;
			GameEngine.context = this;
			

			setContentView(R.layout.activity_main);

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.topMargin = 0;
			params.alignWithParent = true;

			RenderManager.relativeLayout = (RelativeLayout) this.findViewById(R.id.scene1Holder);
			

			RenderManager.mGLSurfaceView.setRenderer(renderer);
			
			
			//RenderManager.mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
			

			RenderManager.context = renderer.context;
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
			RenderManager.metrics = metrics;
			
			//RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			//params.topMargin = 0;
			//params.alignWithParent = true;
			RenderManager.menuCanvas = new MenuCanvas(this);
			RenderManager.menuCanvas.setLayoutParams(params);			
			

			RenderManager.levelMenuCanvas = new LevelMenuCanvas(this);
			RenderManager.levelMenuCanvas.setLayoutParams(params);
			
			RenderManager.canvasView = new LevelCanvas(this);
			RenderManager.canvasView.setLayoutParams(params);		
			RenderManager.canvasView.setFocusable(true);
			RenderManager.canvasView.setFocusableInTouchMode(false);
			
			
			
			
			//Hide NavigationBar event handler
			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
			{
			View decorView = getWindow().getDecorView();
			decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
			    @Override
			    public void onSystemUiVisibilityChange(int visibility) 
			    {
			      int	mTestFlags = View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
			      if (visibility == 0)
			    	{
			    	  hideNavy();
			    	}
			    }
			      
			    }
			   
			);
		   }
			
			RenderManager.relativeLayout.setOnTouchListener(this);
			
			RenderManager.mainActivity = this;
			AdBuddiz.setPublisherKey("5caae1a5-1956-4446-9a92-1ad510f9cfc1");
		    AdBuddiz.cacheAds(this); // this = current Activity
		    
		    
		
			
		
			RenderManager.selectScene(RenderManager.sceneLogo);
			logoTimer = new Timer();
			logoTimer.schedule(new TimerTask() {          
			        @Override
			        public void run() {
			        	TimerMethod();
			        }

			    }, 0, 100);
			
			
					
			

		} else {
			ShowMessage.ShowCrash("OpenGL ES 2.x not supproted on this device.");
			return;
		}

	}

	@SuppressLint("NewApi")
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			hideNavy();
		}
	}

	// -------------------------------------------------------------------------------------------------------------

	@SuppressLint("NewApi")
	public void TimerMethod() {
		if (RenderManager.logoCanvas != null)
			RenderManager.logoCanvas.postInvalidate();
		timerCount++;
		if (timerCount > 10) {
			logoTimer.cancel();
			this.runOnUiThread(Timer_Tick);
		}
			
			
	}

	private Runnable Timer_Tick = new Runnable() {
		public void run() {
			RenderManager.selectScene(RenderManager.sceneMenu);

		}
	};

	@SuppressLint("NewApi")
	private void hideNavy() {
		
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
		{
		RenderManager.relativeLayout.setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE | 
				View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | 
				View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | 
				View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | 
				View.SYSTEM_UI_FLAG_FULLSCREEN | 
				View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
				View.SYSTEM_UI_FLAG_IMMERSIVE);
		}
				
		/*
		RenderManager.relativeLayout.setSystemUiVisibility(
 
 
 
				View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | 
				View.SYSTEM_UI_FLAG_FULLSCREEN  

);
		*/
	}

	@Override
	protected void onResume() {

		//hideNavy();
		wakeLock.acquire();
		super.onResume();
		RenderManager.resume();
	}

	@Override
	protected void onPause() {

		wakeLock.release();
		super.onPause();
		RenderManager.pause();
	}

	@Override
	public void onBackPressed() {
		// TODO MENU EXIT HERE

		RenderManager.back();
		// super.onBackPressed();
	}

	// @SuppressLint("NewApi")
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		super.onTouchEvent(event);
		
		if (touchLock) return true;
		touchLock = true; 
		
		try
		{
		int action = event.getAction();

		// hideNavy();

		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN: {
			// hideNavy();
			
			//RenderManager.selectScene(RenderManager.sceneMap1);
			

			
		}
		}

			
		if (event.getPointerCount() == 1) {
			RenderManager.onTouch(action, (int) event.getX(), (int) event.getY());
		} else if (event.getPointerCount() == 2) {
			RenderManager.onDoubleTouch(action, (int) event.getX(0), (int) event.getY(0), (int) event.getX(1), (int) event.getY(1));
		}
		}
		catch(Exception e){}
		
		touchLock = false; 
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	 {		
		if (RenderManager.onKeyDown(keyCode, event)) return true;
		
		return super.onKeyDown(keyCode, event);
	 }	

}
