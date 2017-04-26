package com.dci.seaban.Objects;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.dci.seaban.GameEngine;
import com.dci.seaban.Physics.Swing;
import com.dci.seaban.Physics.SwingEnd;
import com.dci.seaban.Render.RenderManager;
import com.dci.seaban.Render.SceneRender;
import com.dci.seaban.Service.GlobalVar;
import com.dci.seaban.Service.ShaderManager;

public class Pointer extends SceneObject{

	public int gameX = 0;
	public int gameY = 0;
	
	
	
	public Swing swing1 = new Swing(0.5f, 0.1f);
	public Swing scaleSwing = new Swing(0.1f, 0.1f);
	public Swing rSwing = new Swing(3.5f, 0.1f);	
	public Swing rotateSwing = new Swing(360, 0.5f);
	
	public boolean visible = false;
	
	private Swing showSwing = new Swing(0.2f, 0.5f);
	private Swing showSwingB = new Swing(0.03f, 0.8f);
	
	public boolean top = false;
	public boolean left = false;
	public boolean right = false;
	public boolean bottom = false;
	
	
	public Pointer(Context context, String fileName, String shaderName, SceneRender render, int textureResID, int texture2ResID, int texture3ResID) {
		super(context, fileName, shaderName, render, textureResID, texture2ResID, texture3ResID);
		levelScale = true;
		blend = GlobalVar.blendYes;
		blendType = 0;
	}
	
	public void drawBlend() {
		
		
		
		if (((GameEngine.getBoat().x == gameX-1) || (GameEngine.getBoat().x == gameX) || (GameEngine.getBoat().x == gameX+1))
			&&
			((GameEngine.getBoat().y == gameY-1) || (GameEngine.getBoat().y == gameY) || (GameEngine.getBoat().y == gameY+1)))
		{
		
			
		x = GlobalVar.GameXToGLX(gameX);
		z = GlobalVar.GameYToGLY(gameY);
		
		//1
		
		if ((GameEngine.canMoveCont(gameX+1, gameY)) && (GameEngine.canMoveCont(gameX-1, gameY)))
		{
		Matrix.setIdentityM(mModelMatrix, 0);			
		Matrix.translateM(mModelMatrix, 0, x, y, z);
		Matrix.scaleM(mModelMatrix, 0, scaleSwing.GetX() + 0.5f, scaleSwing.GetX() + 0.5f, scaleSwing.GetX() + 0.5f);
		Matrix.rotateM(mModelMatrix, 0, 0, 0.0f, 1.0f, 0.0f);
		
		ColorA = showSwing.GetX() + 0.8f;
  	    super.drawBlend();

		Matrix.setIdentityM(mModelMatrix, 0);			
		Matrix.translateM(mModelMatrix, 0, x, y+0.02f, z);
		Matrix.scaleM(mModelMatrix, 0, scaleSwing.GetX() + 0.5f, scaleSwing.GetX() + 0.5f, scaleSwing.GetX() + 0.5f);
		Matrix.rotateM(mModelMatrix, 0, 180, 0.0f, 1.0f, 0.0f);
		
		ColorA = showSwing.GetX() + 0.8f;
  	    super.drawBlend();
  	    
  	    
		}
		
  	    
		if ((GameEngine.canMoveCont(gameX, gameY+1)) && (GameEngine.canMoveCont(gameX, gameY-1)))		
		{
		Matrix.setIdentityM(mModelMatrix, 0);			
		Matrix.translateM(mModelMatrix, 0, x, y+0.04f, z);
		Matrix.scaleM(mModelMatrix, 0, scaleSwing.GetX() + 0.5f, scaleSwing.GetX() + 0.5f, scaleSwing.GetX()+ 0.5f);
		Matrix.rotateM(mModelMatrix, 0, 90, 0.0f, 1.0f, 0.0f);
		
		ColorA = showSwing.GetX() + 0.8f;
  	    super.drawBlend();
		
		Matrix.setIdentityM(mModelMatrix, 0);			
		Matrix.translateM(mModelMatrix, 0, x, y+0.06f, z);
		Matrix.scaleM(mModelMatrix, 0, scaleSwing.GetX() + 0.5f, scaleSwing.GetX()+ 0.5f, scaleSwing.GetX() + 0.5f);
		Matrix.rotateM(mModelMatrix, 0, 270, 0.0f, 1.0f, 0.0f);
		
		ColorA = showSwing.GetX() + 0.8f;
  	    super.drawBlend();
		}
		}

	}
	
	

}
