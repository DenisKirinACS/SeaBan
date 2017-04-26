package com.dci.seaban.Objects;

import java.util.Random;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.dci.seaban.R;
import com.dci.seaban.Physics.Move;
import com.dci.seaban.Physics.Swing;
import com.dci.seaban.Physics.Swing2;
import com.dci.seaban.Render.SceneRender;
import com.dci.seaban.Service.GlobalVar;

public class Container extends SceneObject{

	public int gameX = 0;
	public int gameY = 0;
	public Swing2 swing;
	public Move move;
	public Pointer pointer;

	public Container(Context context, String fileName, String shaderName, SceneRender render, int textureResID, int texture2ResID, int texture3ResID) {
		super(context, fileName, shaderName, render, textureResID, texture2ResID, texture3ResID);
		
		Random random = new Random();		
		swing = new Swing2(random.nextFloat() / 0.5f , random.nextFloat() / 0.3f);		
		levelScale = true;
		
		pointer = new Pointer(context, "plashka_obj", "pointer", render, R.drawable.pointer2, R.drawable.pointer2, R.drawable.pointer2);	
		pointer.y = 2.0f;
	}
	
	public void moveTo(int gameToX, int gameToY){

		
		x = GlobalVar.GameXToGLX(gameX);
		z = GlobalVar.GameYToGLY(gameY);
		
		gameX = gameToX;
		gameY = gameToY;
		
		
		move = new Move(x, z, GlobalVar.GameYToGLY(gameToX), GlobalVar.GameYToGLY(gameToY), GlobalVar.moveSpeed);
		
	}
	

	@Override
	public void draw() {
		Matrix.setIdentityM(mModelMatrix, 0);
		
		if (move != null)
		{
		  if (!move.stop)
		  {
		   x = move.GetX();
		   z = move.GetY();
		  }
		}
		else 
		{
			x = GlobalVar.GameXToGLX(gameX);		
			z = GlobalVar.GameYToGLY(gameY);
			y = -1.0f;

		}
		
		
		Matrix.translateM(mModelMatrix, 0, x, y, z);
		
		
		super.draw();
		
		pointer.gameX = gameX;
		pointer.gameY = gameY;
		
		
	} 
	
	

}
