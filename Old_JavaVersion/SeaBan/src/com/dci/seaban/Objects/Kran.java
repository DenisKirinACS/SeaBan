package com.dci.seaban.Objects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.opengl.Matrix;

import com.dci.seaban.R;
import com.dci.seaban.Physics.Swing;
import com.dci.seaban.Render.SceneRender;

public class Kran extends SceneObject {

	private SceneObject tower;
	private SceneObject base;
	private SceneObject hower;
	
	public Swing swing = new Swing(4.1f, 0.1f);
	public Swing swing2 = new Swing(1.5f, 0.1f);
	public Swing swing3 = new Swing(2.5f, 0.5f);
	
	public Kran(Context context, String fileName, String shaderName, SceneRender render, int textureResID, int texture2ResID, int texture3ResID) {
		
		super(context, "", shaderName, null, -1, -1, -1);
		
		tower = new SceneObject(context, "kran_objkrantower_circle281_obj", shaderName, render, R.drawable.towermap,  R.drawable.towermapf,  R.drawable.towermaps);
		tower.parentDraw = true;
		
		base = new SceneObject(context, "kran_objkranbase_arc22_001_obj", shaderName, render,  R.drawable.bodymap,  R.drawable.bodymapf,  R.drawable.bodymaps);
		base.parentDraw = true; 
		
		hower = new SceneObject(context, "kran_objkranhower_circle281_001_obj", shaderName, render, R.drawable.howermap,  R.drawable.howermap,  R.drawable.howermap);
		hower.parentDraw = true; 
		
	}

	@Override
	public void draw() {

		
		base.x = x;
		base.y = y;
		base.z = z;
		
		Matrix.setIdentityM(base.mModelMatrix, 0);
		base.x += swing.GetX();
		base.y = - 0.19f;
		Matrix.translateM(base.mModelMatrix, 0, base.x, base.y, base.z);
	//	Matrix.scaleM(base.mModelMatrix, 0, 16.0f, 16.0f, 16.0f);
		
		
						
		for (int i=0; i < base.mModelMatrix.length; i++)
		 tower.mModelMatrix[i] = base.mModelMatrix[i];
		
		tower.alpha += swing2.GetX();	
		Matrix.rotateM(tower.mModelMatrix, 0, tower.alpha, 0.0f, 1.0f, 0.0f);				
		
		for (int i=0; i < tower.mModelMatrix.length; i++)
			 hower.mModelMatrix[i] = tower.mModelMatrix[i];
		
		hower.y = -swing3.GetX() - 0.2f;
		Matrix.translateM(hower.mModelMatrix, 0, hower.x, hower.y, hower.z);
		
		base.draw();
		tower.draw();
		hower.draw();

	}
	
	
	
	

}
