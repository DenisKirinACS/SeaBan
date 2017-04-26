package com.dci.seaban.Objects;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.dci.seaban.Physics.Swing;
import com.dci.seaban.Render.RenderManager;
import com.dci.seaban.Render.SceneRender;

public class Water extends SceneObject {

	public Swing swing1 = new Swing(1.1f, 0.5f);
	public Swing swing2 = new Swing(0.1f, 0.5f);
	public Swing swing3 = new Swing(0.1f, 0.5f);
	public Swing swing4 = new Swing(1.1f, 0.5f);
	public int direction = 0;

	
	public Water(Context context, String fileName, String shaderName, SceneRender render, int textureResID, int texture2ResID, int texture3ResID) {
		super(context, fileName, shaderName, render, textureResID, texture2ResID, texture3ResID);
		
	}

	
	@Override
	public void initShader() {
		// TODO Auto-generated method stub
		super.initShader();
		/*
		mMVPMatrixHandle = RenderManager.waterShader.mMVPMatrixHandle;
		mMVMatrixHandle = RenderManager.waterShader.mMVMatrixHandle; 
		attrib_vertex = RenderManager.waterShader.attrib_vertex; 
		unif_color = RenderManager.waterShader.unif_color; 
		normal = RenderManager.waterShader.normal; 
		lightPos = RenderManager.waterShader.lightPos;
		
		 

		mTextureCoordinateHandle =RenderManager.waterShader.mTextureCoordinateHandle; 
		mTextureUniformHandle =RenderManager.waterShader.mTextureUniformHandle; 
		mTextureUniformHandleF =RenderManager.waterShader.mTextureUniformHandleF; 
		unif_color = RenderManager.waterShader.unif_color;
		
		
		program = RenderManager.waterShader.program;
		*/
	}


	@Override
	public void drawBlend() {
		
	
		
		Matrix.setIdentityM(mModelMatrix, 0);
	//	Matrix.scaleM(mModelMatrix, 0, 50.0f, 1.0f, 50.0f);
		if (direction == 0)
		{
		x = swing1.GetX();
		y = swing2.GetX();		
		Matrix.translateM(mModelMatrix, 0, x, y, z);		
		}
		else
		{
			
			x = swing4.GetX();				
			Matrix.translateM(mModelMatrix, 0, x, y-0.1f, z);
			
			alpha = swing3.GetX();				
			Matrix.rotateM(mModelMatrix, 0, alpha, 0, 1, 0);
		}
			
			
 
		
		
		//GLES20.glEnable(GLES20.GL_BLEND);
		//GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
		 
			
	    super.drawBlend();
	
	    //GLES20.glDisable(GLES20.GL_BLEND);
	    
		
	
	}
	
	

}
