package com.dci.seaban.Objects;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.dci.seaban.Physics.Move;
import com.dci.seaban.Physics.Swing;
import com.dci.seaban.Physics.SwingEnd;
import com.dci.seaban.Render.RenderManager;
import com.dci.seaban.Render.SceneRender;
import com.dci.seaban.Service.GlobalVar;
import com.dci.seaban.Service.ShaderManager;

public class Stolb extends SceneObject{

	public int gameX = 0;
	public int gameY = 0;
	
	
	
	public Swing swing1 = new Swing(0.5f, 0.1f);
	public Swing scaleSwing = new Swing(0.5f, 0.1f);
	public Swing rSwing = new Swing(3.5f, 0.1f);	
	public Swing rotateSwing = new Swing(360, 0.5f);
	
	
	public boolean visible = false;
	
	private Swing showSwing = new Swing(0.2f, 0.5f);
	private Move showMove = null;
	private Swing showSwingB = new Swing(0.03f, 0.8f);
	
	//------------------------------------------------------
	private Move mainMove = null;
	private Move stolbMove = null;
	private Move stolbMoveUp = null;
	private Move boatMove = null;
	private Boat boat;
	
	public Stolb(Context context, String fileName, String shaderName, SceneRender render, int textureResID, int texture2ResID, int texture3ResID, Boat boat) {
		super(context, fileName, shaderName, render, textureResID, texture2ResID, texture3ResID);
		levelScale = true;
		blend = GlobalVar.blendYes;
		blendType = 0;
		
		this.boat = boat;
		this.boat.y = 100.0f;
		
		x = boat.x;
		z = boat.z;
		
	}
	
	public void Start()
	{
		mainMove = new Move(1,0,14,0,14); //14 steps for 14 sec
	}
	
	public void drawBlend() {
		
		if (mainMove == null) return;
		
		Matrix.setIdentityM(mModelMatrix, 0);
		
		if (mainMove.GetX() < 2) //first step 2s 
		{
			Matrix.translateM(mModelMatrix, 0, x, y, z);
			Matrix.rotateM(mModelMatrix, 0, rotateSwing.GetX(), 0.0f, 1.0f, 0.0f);
			ColorB = 0.0f;
			ColorA = showSwing.GetX() + 0.8f;
			super.drawBlend();
		}
		else 
			if (mainMove.GetX() < 2 + 4) //second step 6 sec (2 + 4)
			{
				if (stolbMove == null)
				{
					stolbMove = new Move(4.0f, 0.0f, 1.0f, 0.0f, 3.0f); // 3sec stolb move
					showMove = new Move(0.0f, 0.0f, 0.3f, 0.0f, 2.5f);
				}
				

				for (int i=0; i < 10; i++)
				{

					Matrix.setIdentityM(mModelMatrix, 0);
				
					Matrix.translateM(mModelMatrix, 0, x, y + i * stolbMove.GetX() , z);
					Matrix.scaleM(mModelMatrix, 0, scaleSwing.GetX() + 1.0f, scaleSwing.GetX() + 1.0f, scaleSwing.GetX() + 1.0f);
					Matrix.rotateM(mModelMatrix, 0, rotateSwing.GetX() + i * rSwing.GetX() * 10.0f, 0.0f, 1.0f, 0.0f);
					ColorB = showSwingB.GetX();
					if (i > 0)  ColorA = showMove.GetX();
					else 
						ColorA = showSwing.GetX() + 0.8f;
					
					super.drawBlend();
				}
			}
			else
				if (mainMove.GetX() < 2 + 4 + 2) 
				{
					if (boatMove == null) boatMove = new Move(40.0f, 0.0f, 0.2f, 0.0f, 2.0f); 
					
					for (int i=0; i < 10; i++)
					{

						Matrix.setIdentityM(mModelMatrix, 0);
					
						Matrix.translateM(mModelMatrix, 0, x, y + i * stolbMove.GetX() , z);
						Matrix.scaleM(mModelMatrix, 0, scaleSwing.GetX() + 1.0f, scaleSwing.GetX() + 1.0f, scaleSwing.GetX() + 1.0f);
						Matrix.rotateM(mModelMatrix, 0, rotateSwing.GetX() + i * rSwing.GetX() * 10.0f, 0.0f, 1.0f, 0.0f);
						ColorB = showSwingB.GetX();
						ColorA = showSwing.GetX() + 0.8f;
						super.drawBlend();
					}					
					boat.y = boatMove.GetX();
				}
				else
					if (mainMove.GetX() < 2 + 4 + 2 + 4) //second step 6 sec (2 + 4)
					{
						if (stolbMoveUp == null)
						{
							stolbMoveUp = new Move(1.0f, 0.0f, 14.0f, 0.0f, 4.0f); // 3sec stolb move
							showMove = new Move(0.3f, 0.0f, 0.0f, 0.0f, 3.5f);
						}

						for (int i=0; i < 10; i++)
						{

							Matrix.setIdentityM(mModelMatrix, 0);
						
							Matrix.translateM(mModelMatrix, 0, x, y + i * stolbMoveUp.GetX() , z);
							Matrix.scaleM(mModelMatrix, 0, scaleSwing.GetX() + 1.0f, scaleSwing.GetX() + 1.0f, scaleSwing.GetX() + 1.0f);
							Matrix.rotateM(mModelMatrix, 0, rotateSwing.GetX() + i * rSwing.GetX() * 10.0f, 0.0f, 1.0f, 0.0f);
							ColorB = 0.0f;
							ColorA = showMove.GetX();
							super.drawBlend();
						}
						
					}
						

		
		
		/*
		for (int i=0; i < 10; i++)
		{
			
		
		
		Matrix.setIdentityM(mModelMatrix, 0);
				
		Matrix.translateM(mModelMatrix, 0, x, y + i /(rSwing.GetX() + 0.1f), z);
		Matrix.scaleM(mModelMatrix, 0, scaleSwing.GetX() + 1.0f, scaleSwing.GetX() + 1.0f, scaleSwing.GetX() + 1.0f);
		Matrix.rotateM(mModelMatrix, 0, rotateSwing.GetX() + i * rSwing.GetX() * 10.0f, 0.0f, 1.0f, 0.0f);


		
			ColorB = showSwingB.GetX();
			ColorA = showSwing.GetX() + 0.8f;
		 super.drawBlend();
		
		}
		 */
	}
	
	

}
