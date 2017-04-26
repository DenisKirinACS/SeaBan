package com.dci.seaban.Render;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.dci.seaban.Service.GlobalVar;
import com.dci.seaban.Service.ShaderManager;


import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class Renderer implements GLSurfaceView.Renderer {

	public Context context;
	
	private long time;
	private boolean lockRender = false;
	private float fps = 0.0f;
	private float maxFPS = 25.0f;
	private int maxFPSSec = (int) (1000 / maxFPS);
	
	private float FPSSum = 0;
	private int FPSCount = 0;
	private int FrameCount = 0;
	

	// ------------------------------------------------------------------------------------------------------------------------------------------------------------
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		//RenderManager.context = context;
		
		if (RenderManager.db.GetGreenMode() == 0) Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		 
	
		if (RenderManager.externalPaused) RenderManager.initShaders();
		else		
		 RenderManager.createObjects();
		
		RenderManager.externalPaused = false;
		
		//GL Settings
		//gl.glShadeModel(GL10.GL_SMOOTH);
		
		//20.05.2015
		//gl.glDisable(GL10.GL_DITHER);
		//gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,    GL10.GL_FASTEST);
	    //gl.glShadeModel(GL10.GL_SMOOTH);	   
	    //gl.glHint(GL10.GL_LINE_SMOOTH_HINT, GL10.GL_NICEST);
	    //gl.glHint(GL10.GL_POLYGON_SMOOTH_HINT, GL10.GL_NICEST);
	    //gl.glEnable(GL10.GL_LINE_SMOOTH);		
		
		GLES20.glClearColor(0.7f, 0.7f, 0.9f, 0.0f);

		GLES20.glDisable(GLES20.GL_BLEND);

		
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		
	
		
		if (RenderManager.currentScene == RenderManager.sceneMenu) GLES20.glCullFace(GLES20.GL_FRONT_AND_BACK);
		else
		{
			GLES20.glEnable(GLES20.GL_CULL_FACE);
			GLES20.glCullFace(GLES20.GL_BACK);
		}
			
		
		

		
	}

	// --------------------------
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {		
		RenderManager.width = width; 
		RenderManager.height = height;
		RenderManager.gl = gl;		
		RenderManager.getCurrent().width = width;
		RenderManager.getCurrent().height = height;		
		GLES20.glViewport(0, 0, width , height );
		final float ratio = (float) width / height ;
		final float left = -ratio;
		final float right = ratio;					
		final float top = 1.0f;
		final float bottom = -1.0f;
		final float near = 5.4f;	
		final float far = 200.0f;	
		Matrix.frustumM(RenderManager.getCurrent().mProjectionMatrix, 0, left, right, bottom, top, near, far);
		
		//ORTHO!
		//Matrix.orthoM(RenderManager.getCurrent().mProjectionMatrix, 0, 0f, width, 0.0f, height, 0, 50);
	}

	// --------------------------
	@SuppressLint("NewApi")
	@Override
	public void onDrawFrame(GL10 gl) {

		
		if (lockRender) return;
		lockRender = true; 
		
		try
		{
		//if (fps > maxFPS) {fps = 0.0f;  Thread.sleep(30);}
		
			
		long currentTime = (SystemClock.uptimeMillis() - time);
		time = SystemClock.uptimeMillis();
		
			
		
		if (currentTime == 0 ) currentTime = 1;
		float fps = (float)(1000 / currentTime);
		
		FPSSum += fps;
		FPSCount++;
		
		if (FPSCount > 100)
		{
			maxFPS = (int)((FPSSum / FPSCount) / 5.0f * 4.0f);
			maxFPSSec = (int) (1000 / maxFPS);
		}
		
		FrameCount++;
		if (FrameCount > 200)
		{
			FrameCount = 0;
		//	System.gc();			
		}
		

		if (RenderManager.canvasView != null) {
			RenderManager.canvasView.dataText = "FPS: " + String.valueOf((int)(FPSSum / FPSCount) + "mFPS " + String.valueOf((int)(maxFPS)) + ":"  + String.valueOf((int)(fps)));
			RenderManager.canvasView.postInvalidate();
		}			
		
		if (GlobalVar.greenMode != 0)
        	if (currentTime < maxFPSSec) {
        		Thread.sleep((int)(maxFPSSec - currentTime));
        		//RenderManager.canvasView.postInvalidate();
        		fps = 0.0f;   
        		 
             }
		
		RenderManager.render(gl);		
		
		
		
		
		 
		

		//GLES20.glUseProgram(ShaderManager.getShader("map").program);		
		
		//GLES20.glUseProgram(0);
		
		
		lockRender = false;
		}
		catch (Exception e)
		{
			Log.e("REDNER",e.getMessage());
		}
	
	}
	
	
}
