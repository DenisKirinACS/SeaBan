package com.dci.seaban.Objects;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.dci.seaban.Physics.Swing;
import com.dci.seaban.Render.RenderManager;
import com.dci.seaban.Render.SceneRender;

public class Sky extends SceneObject {

	public int direction = 0;
	
	
	public Sky(Context context, String fileName, String shaderName, SceneRender render, int textureResID, int texture2ResID, int texture3ResID) {
		super(context, fileName, shaderName, render, textureResID, texture2ResID, texture3ResID);
		
	}

	
	@Override
	public void initShader() {

		super.initShader();
	}


	@Override
	public void draw()
	{
	Matrix.setIdentityM(mModelMatrix, 0);
	
	if (direction != 0)
	{
		alpha+=0.1f;;				
		Matrix.rotateM(mModelMatrix, 0, alpha, 0, 1, 0);
		
	}

	
				
    super.draw();
	}
	
	@Override
	public void drawBlend() {
		
	
		
		Matrix.setIdentityM(mModelMatrix, 0);
		Matrix.scaleM(mModelMatrix, 0, 0.5f, 0.5f, 0.5f);
		
		if (direction != 0)
		{
			alpha+=0.1f;;				
			Matrix.rotateM(mModelMatrix, 0, alpha, 0, 1, 0);
			
		}

					
	    super.drawBlend();
	

	    
		
	
	}
	
	

}
