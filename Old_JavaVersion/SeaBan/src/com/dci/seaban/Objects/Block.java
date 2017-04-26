package com.dci.seaban.Objects;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.dci.seaban.Render.SceneRender;
import com.dci.seaban.Service.GlobalVar;

public class Block extends SceneObject{

	public int gameX = 0;
	public int gameY = 0;

	public Block(Context context, String fileName, String shaderName, SceneRender render, int textureResID, int texture2ResID, int texture3ResID) {
		super(context, fileName, shaderName, render, textureResID, texture2ResID, texture3ResID);
		levelScale = true;
	}

	@Override
	public void draw() {
		Matrix.setIdentityM(mModelMatrix, 0);
		
		x = GlobalVar.GameXToGLX(gameX);		
		z = GlobalVar.GameYToGLY(gameY);
		//y = -1.0f;
		
		Matrix.translateM(mModelMatrix, 0, x, y, z);
		
		
		
		super.draw();
		
		
		

	} 
	
	

}
