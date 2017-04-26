package com.dci.seaban.Render;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.opengles.GL10;


import com.dci.seaban.GameEngine;
import com.dci.seaban.R;
import com.dci.seaban.Canvas.LevelCanvas;
import com.dci.seaban.Canvas.LevelMenuCanvas;
import com.dci.seaban.Canvas.LogoCanvas;
import com.dci.seaban.Canvas.MenuCanvas;
import com.dci.seaban.Objects.Container;
import com.dci.seaban.Objects.ObjectManager;
import com.dci.seaban.Objects.Panel;
import com.dci.seaban.Objects.PanelManager;
import com.dci.seaban.Objects.SceneObject;
import com.dci.seaban.Objects.ShaderCompiller;
import com.dci.seaban.Service.ConfigDB;
import com.dci.seaban.Service.GlobalVar;
import com.dci.seaban.Service.LevelDB;
import com.dci.seaban.Service.ShaderManager;
import com.dci.seaban.Service.ShowMessage;
import com.dci.seaban.Service.Sound;
import com.dci.seaban.Service.SoundManager;
import com.dci.seaban.Service.Texture;
import com.dci.seaban.Service.TextureManager;
import com.dci.seaban.Service.VBOItem;
import com.dci.seaban.Service.VBOManager;
import com.dci.seaban.Service.WayItem;
import com.dci.seaban.MainActivity;


import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class RenderManager {

	//Canvas mode
	public final static int sceneLogo = 1;
	public final static int sceneMenu = 2;
	public final static int sceneMap1 = 3;
	public final static int sceneMap2 = 4;
	public final static int sceneMap3 = 5;
	public final static int sceneMap4 = 6;
	

	private static SceneRender render;
	public static int currentScene;
	
	public static Context context;
	public static MainActivity mainActivity;	
	public static GLSurfaceView mGLSurfaceView;	
	public static LevelCanvas canvasView;
	public static LevelMenuCanvas levelMenuCanvas;
	public static LogoCanvas logoCanvas;
	public static MenuCanvas menuCanvas;
	public static RelativeLayout relativeLayout;
	
	public static int width;
	public static int height;
	public static GL10 gl;
	
	
	public static int mTextureDataHandle;
	public static int mTextureDataHandleF;
	
	public static int mTextureDataHandleKran;
	public static int mTextureDataHandleFKran;
	public static boolean externalPaused = false;
	
	
	public static boolean lockRender = false;
	
	public static DisplayMetrics metrics;
	
		
	public static ConfigDB db;
	public static LevelDB levelDB;
	
	private static int saveCanvasMode = 0;
	
	private static int dx1, dx2, dy1, dy2 = -1;
	
	
	@SuppressLint("NewApi")
	public static int selectScene(int sceneIndex){
	//	if (currentScene == sceneIndex) return 0; //ready loaded
		
		Log.e("SEPAR", "----------------------------");
		GlobalVar.getUsedMemorySize();
		//SoundManager.Clear();
		
		lockRender = true;
		
		
		if (currentScene >= sceneMap1)
		{
			levelMenuCanvas.Hide();
			relativeLayout.removeView(canvasView);
			relativeLayout.removeView(levelMenuCanvas);
			relativeLayout.removeView(mGLSurfaceView);			
			destroyScene();
			render = null;
		}
		else
		{
		switch(currentScene){
		case sceneLogo:
		{
			relativeLayout.removeView(logoCanvas);
			logoCanvas = null; 
			break; 
		}
		case sceneMenu:
		{
			
			relativeLayout.removeView(mGLSurfaceView);	
			relativeLayout.removeView(menuCanvas);
			//menuCanvas = null;
			destroyScene();
			render = null;
			break; 
		}
		}
		
		
		}
		Log.e("SEPAR", "-");
		GlobalVar.getUsedMemorySize();
		currentScene = sceneIndex;
		
		if (currentScene >= sceneMap1)
		{
	  	   canvasView.viewMode = 1;
			
			saveCanvasMode = canvasView.viewMode;
			render = new MapRender(context);
			relativeLayout.addView(mGLSurfaceView);
			relativeLayout.addView(canvasView);
			relativeLayout.addView(levelMenuCanvas);
			
			
			render.width = width;
			render.height = height;
						
		}
		else
		{
		switch(sceneIndex){
		case sceneLogo:
		{
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.topMargin = 0;
			params.alignWithParent = true;
			logoCanvas = new LogoCanvas(context);
			logoCanvas.setLayoutParams(params);
			relativeLayout.addView(logoCanvas);
			
			
			break; 
		}
		case sceneMenu:
		{
			
			render = new MenuRender(context);
			relativeLayout.addView(mGLSurfaceView);
			menuCanvas.RefreshLevelButton();
			relativeLayout.addView(menuCanvas);
			break; 						
		}
		
		default: 
			render = new MapRender(context); break;
		}
		}
		
		

		
		lockRender = false;
		
		
		Log.e("SEPAR", "-");
		GlobalVar.getUsedMemorySize();
		
		return 1;
	}
	
	public static SceneRender getCurrent(){
		return render;
	}
	
	public static int getCurrentIndex(){
		return currentScene;
	}
	
	
	public static void destroyScene(){
		
		if (render != null){	
			
			
			
			for (int i=0; i < ObjectManager.getCount(); i++)
			{
				SceneObject sObject = ObjectManager.get(i);
				
				sObject = ObjectManager.get(i);
				sObject = null;

				
			}
			System.gc();
			
			
			
		}
		
	}
	
	public static void resume(){

		SoundManager.Resume();
		
		
		if (currentScene >= sceneMap1)
		{
		
		RenderManager.relativeLayout.removeView(RenderManager.mGLSurfaceView);
		RenderManager.relativeLayout.removeView(RenderManager.canvasView);
		RenderManager.relativeLayout.removeView(RenderManager.levelMenuCanvas);
		
		
		RenderManager.relativeLayout.addView(RenderManager.mGLSurfaceView);
		RenderManager.relativeLayout.addView(RenderManager.canvasView);
		RenderManager.relativeLayout.addView(RenderManager.levelMenuCanvas);
		
		RenderManager.mGLSurfaceView.onResume();
		

			
		}
		else
		{		
		switch(currentScene)
		{
		case sceneMenu:
			RenderManager.relativeLayout.removeView(RenderManager.mGLSurfaceView);
			RenderManager.relativeLayout.removeView(RenderManager.menuCanvas);
			
			RenderManager.relativeLayout.addView(RenderManager.mGLSurfaceView);
			RenderManager.relativeLayout.addView(RenderManager.menuCanvas);
			RenderManager.mGLSurfaceView.onResume();
			break; 
		}
		}
		
	}
	
	public static void pause(){
		SoundManager.Pause();
		
		if (currentScene >= sceneMap1)
		{
			RenderManager.externalPaused = true; 
			RenderManager.mGLSurfaceView.onPause();
			destroyScene();
			saveCanvasMode = canvasView.viewMode;
			canvasView.viewMode = 2;			

			
		}
		else
		{
		switch(currentScene)
		{
		case sceneMenu:
			RenderManager.externalPaused = true; 
			RenderManager.mGLSurfaceView.onPause();
			destroyScene();
			break; 
		}
		}
		
	}
	
	public static void back(){

		if (currentScene >= sceneMap1)
		{
			selectScene(sceneMenu);
		}
		else
		{
		switch(currentScene)
		{
		case sceneMenu:
			menuCanvas.back();
			break; 
		}
		}
		
	}
	
	
	public static void initShaders(){
				
		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.0f);
		GLES20.glDisable(GLES20.GL_BLEND);		
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);		
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		GLES20.glCullFace(GLES20.GL_BACK);
		
		VBOManager.clearVBO();
		TextureManager.clearTextures();
			
		ShaderManager.clear();
//		ShaderManager.addShader(context, "map", 1);
//		ShaderManager.addShader(context, "water", 1);
//		ShaderManager.addShader(context, "panel", 1);
		
		render.initShaders();
		RenderManager.canvasView.viewMode = saveCanvasMode;
	}
	
	public static void render(GL10 gl){
	
		
	//	if (lockRender) return;
		
		
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		render.render(gl);
		
	}
	
	public static void createObjects(){
		
		
		TextureManager.clearTextures();
		
		
		ShaderManager.clear();
//		ShaderManager.addShader(context, "map", 1);
//		ShaderManager.addShader(context, "water", 1);
//		ShaderManager.addShader(context, "panel", 1);
					
		switch(currentScene)
		{
		case sceneMenu: render.createObjects(0);break;
		default:
		case sceneMap1: render.createObjects(currentScene - sceneMap1 + 1);
		
		}
		
		if (currentScene == sceneMap1)
		{
			
			canvasView.hintNumber = 1;
			canvasView.viewMode = 6;
			
		}
		else 					
			if (currentScene == sceneMap2)
			{
				
				canvasView.hintNumber = 2;
				canvasView.viewMode = 6;
				
			}
			else 	
				if (currentScene == sceneMap3)
				{
					
					canvasView.hintNumber = 3;
					canvasView.viewMode = 6;
					
				}
				else 					

	      canvasView.viewMode = 0;		
		
	}

	public static void onTouch(int action, int x, int y){
		if (currentScene >= sceneMap1)
		{
			  render.onTouch(action, x, y);
		}
		else
		{
		
		switch(currentScene)
		{
		case sceneMenu:
			menuCanvas.onTouch(action, x, y);
			break;
		}
		
	}
	}
	
	public static void onDoubleTouch(int action, int x1, int y1, int x2, int y2){
		if (currentScene >= sceneMap1)
		{
			if ((action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN)
			{
				dx1 = x1;
				dx2 = x2;
				dy1 = y1;
				dy2 = y2;
			}

			if ((action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE)
			{
				if(dx1<x1 || dx2>x2 )
				{
					render.boatDestZoom += 0.5;//(x1 - x2) / (GlobalVar.GetDefX(width) / 200);
					dx1 = x1;
					dx2 = x2;
					
				}

				if(dx1>x1 || dx2<x2 )
				{
					render.boatDestZoom -= 0.5;//(x1 - x2) / (GlobalVar.GetDefX(width) / 200);
					dx1 = x1;
					dx2 = x2;

				}
				
//				  x1 = dx1 - x1;
//				  x2 = dx2 - x2; 
//				  render.boatDestZoom += (x1 - x2) / (GlobalVar.GetDefX(width) / 200);
				  
				  if (render.boatDestZoom > 70.0f) render.boatDestZoom = 70.0f;
				  if (render.boatDestZoom < -40.0f) render.boatDestZoom = -40.0f;
				  
					
				  
			}

		}
		
	
	}
	
	
	
	
	public static boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (currentScene >= sceneMap1)
		{
			  return render.onKeyDown(keyCode, event);
		}
		else 
		{
			switch(currentScene)
			{
			case sceneMenu:
				 switch (keyCode) 
				 {
				    case KeyEvent.KEYCODE_BUTTON_START:		    
					    if (menuCanvas.menuMode ==1) selectScene(sceneMap1);
					    else
					    {
				    	 menuCanvas.menuMode =1;
				    	 menuCanvas.postInvalidate();
					    }
				    	return true;
				 }
			}  
		}
			
		return false;

	}	
	
	
	
}
