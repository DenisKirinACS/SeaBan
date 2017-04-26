package com.dci.seaban.Objects;

import com.dci.seaban.R;
import com.dci.seaban.Physics.Swing;
import com.dci.seaban.Physics.Swing2;
import com.dci.seaban.Render.SceneRender;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;


public class Map extends SceneObject {

	public String _fileName = "";
	
	public Map(Context context, String fileName, String shaderName, SceneRender render, int textureResID, int texture2ResID, int texture3ResID) {
				
		super(context, fileName, shaderName, render, textureResID, texture2ResID, texture3ResID);
		_fileName = fileName;

	}
	
	@Override
	public void draw() {

		Matrix.setIdentityM(mModelMatrix, 0);	
		//Matrix.translateM(mModelMatrix, 0, x, y, z);
		
		super.draw();
		
	}

}
