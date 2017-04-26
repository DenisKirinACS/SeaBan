package com.dci.seaban.Objects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//import com.dci.please.R;
import com.dci.seaban.GameEngine;
import com.dci.seaban.R;
import com.dci.seaban.Physics.Move;
import com.dci.seaban.Physics.MoveAcc;
import com.dci.seaban.Physics.Swing;
import com.dci.seaban.Physics.Swing2;
import com.dci.seaban.Render.RenderManager;
import com.dci.seaban.Render.SceneRender;
import com.dci.seaban.Service.GlobalVar;
import com.dci.seaban.Service.SmokeItem;
import com.dci.seaban.Service.Sound;
import com.dci.seaban.Service.SoundManager;
import com.dci.seaban.Service.Texture;
import com.dci.seaban.Service.TextureItem;
import com.dci.seaban.Service.TextureManager;
import com.dci.seaban.Service.VBOItem;
import com.dci.seaban.Service.VBOManager;
import com.dci.seaban.Service.WayItem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;


public class Boat extends SceneObject {

	//public SceneRender render;
	
	
	public float lightX;
	public float lightY;
	public float lightZ;
	
	private BufferedReader buffer;
	private float countFrame = 0;
	
	private final List<SmokeItem> smokeItems = new ArrayList<SmokeItem>();
	
	public float alphaPlus;
	
	
	public float speedX;
	public float speedZ;
	
	public float force;
	public float beta;
	
	
	public float accXdef = 0xFFf;
	public float accYdef = 0xFFf;
	public float accZdef = 0xFFf;

	public float accX = 0;
	public float accY = 0;
	public float accZ = 0;
	
	public long lastTime;
	
	public float lastRadius = 0.0f;
	public float lastAlpha = 0.0f;
	
	
	
	private float leftWeelRotate;
	private float rightWeelRotate;
	private float weelMoveCoef = 60.0f;
		
	public SceneObject wL1, wL2, wL3, wR1, wR2, wR3;
	public SceneObject rocket1, rocket2;
	
	public Swing swing = new Swing(0.1f, 0.52f);
	public Swing swing2 = new Swing(0.1f, 0.52f);
	public Swing swing3 = new Swing(0.1f, 0.53f);
	public Swing swing4 = new Swing(0.12f, 0.4f);
	
	public Swing swingA = new Swing(30.1f, 1.1f);
	
	public Move move;
	public Move moveRotate;

//	public Move moveRotate;

	public int gameX = 2;
	public int gameY = 2;
	
	private List<WayItem> way;
	private int wayIndex;
	
	private SceneObject smokeObj;
	private int streamOnID;
	private long lastNext = -1;
	
	public boolean isMove = false;
	
	public boolean noSwing = true; 
	
	
	
	
	public Boat(Context context, String fileName, String shaderName, SceneRender render, int textureResID, int texture2ResID, int texture3ResID) {
		
		super(context, fileName, shaderName, render, textureResID, texture2ResID, texture3ResID);
		levelScale = true;
		
		for (int i=0; i < 20; i++)
		{
			SmokeItem smokeItem = new SmokeItem();			
			smokeItems.add(smokeItem);		
		}
		
		smokeObj = new SceneObject(context, "smoke_obj", "water", render, R.drawable.smoke, R.drawable.smoke, R.drawable.smoke);
		smokeObj.parentDraw = true;
		smokeObj.blendType = 1;
		smokeObj.blend = GlobalVar.blendYes;
	
		//SoundManager.Add("boat", Sound.diselonSoundID);
		//SoundManager.Play("boat", 0.0f,  0.0f, true);
		
		//streamOnID = Sound.soundPool.play(Sound.diselonSoundID, 0.0f, 0.0f, 1, -1, 1.0f);

	}

	
	public void moveTo(int gameFromX, int gameFromY, int gameToX, int gameToY){
		
		gameX = gameToX;
		gameY = gameToY;
		
		x = GlobalVar.GameXToGLX(gameFromX);
		z = GlobalVar.GameYToGLY(gameFromY);
				
		move = new Move(x, z, GlobalVar.GameYToGLY(gameToX), GlobalVar.GameYToGLY(gameToY), GlobalVar.moveSpeed);

	}
	
	
	public void moveTo(int gameToX, int gameToY){

		
		if (gameX > gameToX) moveRotate = new Move(alpha, 0.0f, 270, 0.0f, 0.2f);  
		else 
			if (gameX < gameToX) moveRotate = new Move(alpha, 0.0f, 90, 0.0f, 0.2f);
			else
		if (gameY > gameToY) moveRotate = new Move(alpha, 0.0f, 180.0f, 0.0f, 0.2f);
		else 
			if (gameY < gameToY) moveRotate = new Move(alpha, 0.0f, 0, 0.0f, 0.2f);
		
		
		move = new Move(x, z, GlobalVar.GameYToGLY(gameToX), GlobalVar.GameYToGLY(gameToY), GlobalVar.moveSpeed);
		
	//	x = GlobalVar.GameXToGLX(gameX);
	//	z = GlobalVar.GameYToGLY(gameY);
		
		gameX = gameToX;
		gameY = gameToY;
		
		
		
	}
	
	

	
	
	@Override
	public void draw() {

		
		
				
		
		Matrix.setIdentityM(mModelMatrix, 0);
		
		try
		{
		
		
		if (move != null)
		{
		  if (!move.stop)
		  {
		   x = move.GetX();
		   z = move.GetY();
		   isMove = true;    
		  }
		  else
		  {
			  isMove = false;
			
			//  if (moveRotate == null)
			  {
			  Point point = PanelManager.getNextTrace();
			  if (point != null)
			  {				  				  
				   moveTo(point.x, point.y);
				   x = move.GetX();
				   z = move.GetY();
				   isMove = true;    
				  
				  GameEngine.setBoatPosDirect(point.x, point.y);
				  
			  }
			  }

		  }
		}
		
				
		
		Matrix.translateM(mModelMatrix, 0, x, 0.0f, z);
		
		
		
		if (!noSwing) 
		 		Matrix.scaleM(mModelMatrix, 0, 1.0f + swing.GetX(), 1.0f + swing2.GetX(), 1.0f);
		
		
		if (moveRotate != null)
		{
			if (!moveRotate.stop)
			{
				alpha = moveRotate.GetX();
				
	
			}
			else
			{
				
			}
				 
		}
		


		
		
		Matrix.rotateM(mModelMatrix, 0, (float) alpha, 0.0f, 1.0f, 0.0f);
		
		if (!noSwing)
		Matrix.rotateM(mModelMatrix, 0, (float) Math.toDegrees(swing4.GetX()), 0.0f, 0.0f, 1.0f);
		}
		catch(Exception e)
		{
		 Log.e("DRAW", e.getMessage());	
		}
		
		
		
		Matrix.scaleM(mModelMatrix, 0, 1.43f, 1.43f, 1.43f);
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		super.draw();
		Matrix.scaleM(mModelMatrix, 0, 0.97f, 0.97f, 0.97f);
		super.draw();
		GLES20.glDisable(GLES20.GL_BLEND);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		Matrix.scaleM(mModelMatrix, 0, 1.02f, 1.02f, 1.02f);
		super.draw();
		
		

		 				
	    
			
	}

	@Override
	public void drawBlend() {

	//	if (RenderManager.quality == 0) return;
		
		Random r = new Random();
				
		
		for (SmokeItem smokeItem: smokeItems)
		{
			if (smokeItem.move.stop)
			{
				if ((lastNext != -1) && (((float)(SystemClock.uptimeMillis() - lastNext) / 1000.0f) < 1.0f)) continue;
				lastNext = SystemClock.uptimeMillis();
				smokeItem.reset(x, y + 1.0f, z, 0.0f);
				smokeItem.scale = 1.01f;
				smokeItem.lastSmokeY = 0;
				

				for (int i=0; i < mModelMatrix.length; i++)
					smokeItem.mModelMatrix[i] = mModelMatrix[i];
				
				
				Matrix.translateM(smokeItem.mModelMatrix, 0, 0.5f,  0.0f, -1.3f);
				Matrix.scaleM(smokeItem.mModelMatrix, 0, 0.2f,  0.2f,  0.2f);
			}
			else
			{
				
				smokeItem.alpha = r.nextFloat() * 5.0f;
				smokeObj.ColorA = (70.0f * GlobalVar.levelScale - smokeItem.move.GetY()) / (100.0f * GlobalVar.levelScale);

				
				//Matrix.scaleM(smokeItem.mModelMatrix, 0, smokeItem.scale,  smokeItem.scale,  smokeItem.scale);
				Matrix.translateM(smokeItem.mModelMatrix, 0, 0.0f,  smokeItem.move.GetY() - smokeItem.lastSmokeY, 0.0f);
				
				
				smokeItem.lastSmokeY = smokeItem.move.GetY();
				
				Matrix.rotateM(smokeItem.mModelMatrix, 0, (float) smokeItem.alpha, 0.0f, 1.0f, 0.0f);
				
				for (int i=0; i < smokeItem.mModelMatrix.length; i++)
					smokeObj.mModelMatrix[i] = smokeItem.mModelMatrix[i];
				
				smokeObj.drawBlend();
			
				
			}
			
		}
		

	}
	
	
	
	/*
	@SuppressLint("NewApi")
	private void drawSmoke(SmokeItem smokeItem)
	{
		
		Matrix.setIdentityM(smokeMatrix, 0);
		
				
		Matrix.translateM(smokeMatrix, 0, smokeItem.x, smokeItem.move.GetY(), smokeItem.z);
		Matrix.rotateM(smokeMatrix, 0, (float) smokeItem.alpha, 1.0f, 1.0f, 1.0f);
		Matrix.scaleM(smokeMatrix, 0, smokeItem.scale, smokeItem.scale, smokeItem.scale);
		

		//Draw GLES 
		GLES20.glUseProgram(program); //temp
		final int size = 4;
		final int b = 0;
		final int c = 0;


		GLES20.glEnableVertexAttribArray(attrib_vertex);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBOManager.getVBO(fileName)[0]);
		GLES20.glVertexAttribPointer(attrib_vertex, size, GLES20.GL_FLOAT, false, b, c);
		
		GLES20.glEnableVertexAttribArray(normal);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBOManager.getVBO(fileName)[2]);
		GLES20.glVertexAttribPointer(normal, size, GLES20.GL_FLOAT, false, b, c);

		GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBOManager.getVBO(fileName)[1]);
		int bsize = 2;
		GLES20.glVertexAttribPointer(mTextureCoordinateHandle, bsize, GLES20.GL_FLOAT, false, b, c);
		
		
		GLES20.glUniform3f(lightPos, 0.0f, 0.0f, 0.0f);
				
		
		Matrix.multiplyMM(mMVPMatrix, 0, render._mViewMatrix, 0, smokeMatrix, 0);
		GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0); //MV Matrix
		
		Matrix.multiplyMM(mMVPMatrix, 0, render.mProjectionMatrix, 0, mMVPMatrix, 0);
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0); //MVP Matrix


		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, TextureManager.getTextureHandle(context, textureResID));
		GLES20.glUniform1i(mTextureUniformHandle, 0);

		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, TextureManager.getTextureHandle(context, texture2ResID));
		GLES20.glUniform1i(mTextureUniformHandleF, 1);
		

		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, VBOManager.getSize(fileName));



		GLES20.glUseProgram(0);
		
	
	}
	*/

}
