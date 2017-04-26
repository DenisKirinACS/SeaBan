package com.dci.seaban.Render;

import javax.microedition.khronos.opengles.GL10;
import com.dci.seaban.Objects.Map;
import com.dci.seaban.Objects.ObjectManager;
import com.dci.seaban.Objects.ShaderCompiller;
import com.dci.seaban.Service.GlobalVar;

import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class SceneRender  {
	
	public Context context;
	
	
	public float cameraAlpha, cameraTheta;
	public float cameraRadius;
	
	// We are looking toward the distance
	public float lookX = 0.0f;
	public float lookY = 0.0f; // 1
	public float lookZ = 0.0f; // 3.1
	// Position the eye behind the origin.
	public float eyeX = 0.0f;
	public float eyeY = 100.0f; // 2.0f
	public float eyeZ = 0.0f;	
	// Set our up vector. This is where our head would be pointing were we
	// holding the camera.
	protected float upX = 0.0f;
	protected float upY = 1.0f; // 1.0f; //1
	protected float upZ = 0.0f; // 2.0f;

	public Map map;

	public float[] _mViewMatrix = new float[16];
	public float[] mViewMatrixCopy = new float[16];
	public float[] mProjectionMatrix = new float[16];

	public final float[] mLightPosInEyeSpace = new float[4];
	public float[] mLightModelMatrix = new float[16];
	public final float[] mLightPosInWorldSpace = new float[4];


	public final float[] mCameraPosInEyeSpace = new float[4];
	public final float[] mCameraPosInWorldSpace = new float[4];

	
	
	public int tx;
	public int ty;
	float lCoef1;
	

	float boatDestZoom = 0.0f;


//	public int mTextureDataHandle;
//	public int mTextureDataHandleF;
	
	public int width;
	public int height; 

	
	public SceneRender() {
		// TODO Auto-generated constructor stub
	}
	
	public void createObjects(int level){
		
		this.context = RenderManager.context;
		

	}
	
	public void initShaders(){
		for (int i=0; i < ObjectManager.getCount(); i++)
		{
			ObjectManager.get(i).initShader();
		}			
	}
	
	public void render(GL10 gl){
		
		for (int i=0; i < ObjectManager.getCount(); i++)
		{

			if (!ObjectManager.get(i).parentDraw)
			{
				if ((ObjectManager.get(i).blend == GlobalVar.blendNone) || (ObjectManager.get(i).blend == GlobalVar.blendBoth)) 						
						ObjectManager.get(i).draw();
				
				
			}
		}
		
	
		for (int i=0; i < ObjectManager.getCount(); i++)
		{

			if (!ObjectManager.get(i).parentDraw)
			{
				if ((ObjectManager.get(i).blend == GlobalVar.blendYes) || (ObjectManager.get(i).blend == GlobalVar.blendBoth))				
			     ObjectManager.get(i).drawBlend();
			}
		}

		

		
	}
	
	public void onTouch(int action, int x, int y)
	{
	
	}
	
	public void goPause() 
	{
		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		return true;
	}	
	

}
