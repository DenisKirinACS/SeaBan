package com.dci.seaban.Objects;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.dci.seaban.Physics.Swing;
import com.dci.seaban.Physics.SwingEnd;
import com.dci.seaban.Render.RenderManager;
import com.dci.seaban.Render.SceneRender;
import com.dci.seaban.Service.GlobalVar;
import com.dci.seaban.Service.ShaderManager;

public class Panel extends SceneObject{

	public int gameX = 0;
	public int gameY = 0;
	
	
	
	public Swing swing1 = new Swing(0.5f, 0.1f);
	
	public boolean visible = false;
	
	private SwingEnd showSwing = new SwingEnd(0.5f, 0.5f);
	
	
	public Panel(Context context, String fileName, String shaderName, SceneRender render, int textureResID, int texture2ResID, int texture3ResID) {
		super(context, fileName, shaderName, render, textureResID, texture2ResID, texture3ResID);
		levelScale = true;
	}
	
	@Override
	public void initShader() {
		// TODO Auto-generated method stub
		super.initShader();
		
		/*
		mMVPMatrixHandle = ShaderManager.getShader("panel").mMVPMatrixHandle;
		mMVMatrixHandle = RenderManager.panelShader.mMVMatrixHandle; 
		attrib_vertex = RenderManager.panelShader.attrib_vertex; 
		unif_color = RenderManager.panelShader.unif_color; 
		normal = RenderManager.panelShader.normal; 
		lightPos = RenderManager.panelShader.lightPos;
		unif_color = RenderManager.panelShader.unif_color;
		
		 

		mTextureCoordinateHandle =RenderManager.panelShader.mTextureCoordinateHandle; 
		mTextureUniformHandle =RenderManager.panelShader.mTextureUniformHandle; 
		mTextureUniformHandleF =RenderManager.panelShader.mTextureUniformHandleF; 
		
		
		
		program = RenderManager.panelShader.program;
		*/
	}
	
	public void show(){
		visible = true; 
		
		showSwing.Restart();
		
	}

	public void hide(){
		visible = false;
		
		showSwing.Restart();
		
		
	}
	
	
	@Override
	public void drawBlend() {
		Matrix.setIdentityM(mModelMatrix, 0);
		
		x = GlobalVar.GameXToGLX(gameX);		
		z = GlobalVar.GameYToGLY(gameY);
		y = -0.3f;
				
		Matrix.translateM(mModelMatrix, 0, x, y, z);

		if (!PanelManager.panelInWay(this))
		{
		if (visible)
		{
			ColorG = showSwing.GetX();
			ColorA = showSwing.GetX();
			if (showSwing.stop) hide();
		}
		else
		{
			ColorG = 0.5f - showSwing.GetX();
			ColorA = 0.5f - showSwing.GetX();			
		}
		
		if (visible)
		 super.drawBlend();
		}
		else
		{
			ColorR = 0.5f;
			ColorA = 0.7f;
			super.drawBlend();
		}
		

	}
	
	

}
