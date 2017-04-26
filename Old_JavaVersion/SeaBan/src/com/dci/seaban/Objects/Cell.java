package com.dci.seaban.Objects;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import com.dci.seaban.R;
import com.dci.seaban.R.drawable;
import com.dci.seaban.Physics.Swing;
import com.dci.seaban.Physics.Swing2;
import com.dci.seaban.Render.RenderManager;
import com.dci.seaban.Render.SceneRender;
import com.dci.seaban.Service.GlobalVar;
import com.dci.seaban.Service.ShaderManager;
import com.dci.seaban.Service.Sound;
import com.dci.seaban.Service.Texture;
import com.dci.seaban.Service.TextureManager;
import com.dci.seaban.Service.VBOManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

public class Cell extends SceneObject{

	public int pos = 0;	
	
	public float alpha;	
	public float radius;
	
	public float x;
	public float y;
	public float z;
	
	protected Context context;
	protected SceneRender render;
	protected static final String TAG = null;
	
	private float a = 0;

	protected int mPointProgramHandle;
	
	protected int[] normalbuf;

	private long time;
	
	public boolean levelScale = false;

	public float[] mModelMatrix = new float[16];

	public float[] mMVPMatrix = new float[16];

	final int FLOAT_SIZE = 4; // GL float size?

	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private FloatBuffer textureBuffer;

	public String fileName;
	public String shaderName;
	private List<String> files = new ArrayList<String>();

	public float rCoef;
	public float angleInDegrees;
	public float m;
	
	public int blend = GlobalVar.blendNone;
	public int blendType = 0;
	
	
	public int useLight = 0;
	
	
	public float ColorR = 0.0f;
	public float ColorG = 0.0f;
	public float ColorB = 0.0f;
	public float ColorA = 0.0f;
	
	public float screenX;
	public float screenY;
	
	public int health = 100;
	public boolean live = true;
	
	
	private float lp = 0.0f;
		
	public boolean parentDraw = false;
	
	public int textureResID;
	public int texture2ResID;
	public int texture3ResID;
	
	private Swing c1 = new Swing(0.2f, 1.0f);
	private Swing c2 = new Swing(0.2f, 2.0f);
	private Swing c3 = new Swing(0.2f, 0.5f);
	

	
	public Cell(Context context, SceneRender render) {
		super(null, "", "", null, 0, 0, 0 );
		
		 this.context = context;
		 this.render = render;
		 this.fileName = "cell";		 
		 this.shaderName = "cell";
		// levelScale = true;
		 initShader();		
		

		  
		 ObjectManager.add(((SceneObject) this) );
	}
	
	
		

	public void initShader() {

		if (fileName == "") return;
		
		    ShaderManager.addShader(context, shaderName);
			
			
		    loadFileToVBO(fileName);
		

	}
	//----------------------------------------------------------------------
	public void loadFileToVBO(String fileName){
		if (fileName == "") return;
		files.add(fileName);
		int indexResult = VBOManager.getVBOIndex(fileName);
		if (indexResult == -1) {
			if (loadingBinModel(fileName)) {				
				GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBOManager.getVBO(fileName)[0]);
				GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexBuffer.capacity() * FLOAT_SIZE, vertexBuffer, GLES20.GL_STATIC_DRAW); // vbb.capacity()
				vertexBuffer = null;
				// is
				//GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBOManager.getVBO(fileName)[2]);
				//GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, normalBuffer.capacity() * FLOAT_SIZE, normalBuffer, GLES20.GL_STATIC_DRAW); // vbb.capacity()
				//normalBuffer = null;
				// is

			}
		}		
	}
	
	
	@SuppressLint("NewApi")
	public void draw() {

		Matrix.setIdentityM(mModelMatrix, 0);
		
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
		if (levelScale)
		{
		 Matrix.scaleM(mModelMatrix, 0, GlobalVar.levelScale, GlobalVar.levelScale, GlobalVar.levelScale);
		}
		
		ColorR = c1.GetX() + 0.4f;
		ColorG = c2.GetX() + 0.4f;
		ColorB = c3.GetX() + 0.4f;
		ColorA = 0.5f;			
		
		
		ShaderCompiller shader = ShaderManager.getShader(shaderName);
		
		GLES20.glUseProgram(shader.program);


		final int size = 4; // data count 2 for vec2 4 for vec4
		final int b = 0;
		final int c = 0;


		GLES20.glEnableVertexAttribArray(ShaderManager.getShader(shaderName).attrib_vertex);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBOManager.getVBO(fileName)[0]);
		GLES20.glVertexAttribPointer(ShaderManager.getShader(shaderName).attrib_vertex, size, GLES20.GL_FLOAT, false, b, c);
		
	//	GLES20.glEnableVertexAttribArray(ShaderManager.getShader(shaderName).normal);
	///	GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBOManager.getVBO(fileName)[2]);
	//	GLES20.glVertexAttribPointer(ShaderManager.getShader(shaderName).normal, size, GLES20.GL_FLOAT, false, b, c);

				
		
		GLES20.glUniform3f(ShaderManager.getShader(shaderName).lightPos, 0.0f, 20.0f, 0.0f);
		
		
		if (ShaderManager.getShader(shaderName).unif_color != -1)
		  GLES20.glUniform4f(ShaderManager.getShader(shaderName).unif_color, ColorR, ColorG, ColorB, ColorA);



				
		Matrix.multiplyMM(mMVPMatrix, 0, render._mViewMatrix, 0, mModelMatrix, 0);
		GLES20.glUniformMatrix4fv(ShaderManager.getShader(shaderName).mMVMatrixHandle, 1, false, mMVPMatrix, 0); //MV Matrix
		
		Matrix.multiplyMM(mMVPMatrix, 0, render.mProjectionMatrix, 0, mMVPMatrix, 0);
		GLES20.glUniformMatrix4fv(ShaderManager.getShader(shaderName).mMVPMatrixHandle, 1, false, mMVPMatrix, 0); //MVP Matrix

		
		
		GLES20.glDrawArrays(GLES20.GL_LINES, 0, VBOManager.getSize(fileName));
		
		
		
		
		
		GLES20.glDrawArrays(GLES20.GL_POINTS, 0, VBOManager.getSize(fileName));



	GLES20.glUseProgram(0);
	GLES20.glDisable(GLES20.GL_BLEND);
	}

	// ------------------------------------------------------------------------------------------------------------------------------
	public boolean loadingBinModel(String fileName) {


		float[] floatArray;
		int size;

		try {

			vertexBuffer = null;

			size = PanelManager.getCount() * 4 * 2 * 4; //4 points on panel, 2 cord on line, 4 byte on one cord
			
			

			floatArray = new float[size];
			
			float x1, y1, z1; 
			float x2, y2, z2; 

			
			for (int i = 0; i < PanelManager.getCount(); i++)
			{
			  Panel panel = PanelManager.getPanel(i);
				
 
				//Line 1
			  
				x1 = panel.x - GlobalVar.panelSize / 2;
				y1 = panel.y;
				z1 = panel.z + GlobalVar.panelSize / 2;
				
				floatArray[i * 32 + 0] = x1;   //32 = 4 * 2 * 4
				floatArray[i * 32 + 1] = y1;
				floatArray[i * 32 + 2] = z1;
				floatArray[i * 32 + 3] = 1.0f;
				
				x2 = panel.x + GlobalVar.panelSize / 2;
				y2 = panel.y;
				z2 = panel.z + GlobalVar.panelSize / 2;
				
				floatArray[i * 32 + 4] = x2;   //32 = 4 * 2 * 4
				floatArray[i * 32 + 5] = y2;
				floatArray[i * 32 + 6] = z2;
				floatArray[i * 32 + 7] = 1.0f;
				
				
				//Line 2				
				floatArray[i * 32 + 8] = x2;   //32 = 4 * 2 * 4
				floatArray[i * 32 + 9] = y2;
				floatArray[i * 32 + 10] = z2;
				floatArray[i * 32 + 11] = 1.0f;
				
				x1 = panel.x + GlobalVar.panelSize / 2;
				y1 = panel.y;
				z1 = panel.z - GlobalVar.panelSize / 2;
				
				floatArray[i * 32 + 12] = x1;  
				floatArray[i * 32 + 13] = y1;
				floatArray[i * 32 + 14] = z1;
				floatArray[i * 32 + 15] = 1.0f;
				
				//Line 3				
				floatArray[i * 32 + 16] = x1;  
				floatArray[i * 32 + 17] = y1;
				floatArray[i * 32 + 18] = z1;
				floatArray[i * 32 + 19] = 1.0f;
				
				x2 = panel.x - GlobalVar.panelSize / 2;
				y2 = panel.y;
				z2 = panel.z - GlobalVar.panelSize / 2;
				
				floatArray[i * 32 + 20] = x2;   //32 = 4 * 2 * 4
				floatArray[i * 32 + 21] = y2;
				floatArray[i * 32 + 22] = z2;
				floatArray[i * 32 + 23] = 1.0f;
				
				//Line 4
				floatArray[i * 32 + 24] = x2;   //32 = 4 * 2 * 4
				floatArray[i * 32 + 25] = y2;
				floatArray[i * 32 + 26] = z2;
				floatArray[i * 32 + 27] = 1.0f;
				
				x1 = panel.x - GlobalVar.panelSize / 2;
				y1 = panel.y;
				z1 = panel.z + GlobalVar.panelSize / 2;
											
				floatArray[i * 32 + 28] = x1;   //32 = 4 * 2 * 4
				floatArray[i * 32 + 29] = y1;
				floatArray[i * 32 + 30] = z1;
				floatArray[i * 32 + 31] = 1.0f;
				
				
			}
			
			
			
			
			vertexBuffer = FloatBuffer.allocate(size);
			vertexBuffer.put(floatArray, 0, size);
			vertexBuffer.flip();

			VBOManager.setSize(fileName, vertexBuffer.capacity() / 4);

			
			floatArray = null;
			
			
			

			// -------------------------------------------------------------------------------------------------------------------------

			//normalBuffer = null;




/*
			floatArray = new float[size];
			
			floatArray[0] = 1.0f;
			floatArray[1] = 1.0f;
			floatArray[2] = 1.0f;
			floatArray[3] = 1.0f;
			
			floatArray[4] = 1.0f;
			floatArray[5] = 1.0f;
			floatArray[6] = 1.0f;
			floatArray[7] = 1.0f;
			
			floatArray[8] =  1.0f;
			floatArray[9] =  1.0f;
			floatArray[10] = 1.0f;
			floatArray[11] = 1.0f;
			
			
			floatArray[12] = 1.0f;
			floatArray[13] = 1.0f;
			floatArray[14] = 1.0f;
			floatArray[15] = 1.0f;
						 

			normalBuffer = FloatBuffer.allocate(size); // float array to
															// FloatBuffer
			normalBuffer.put(floatArray, 0, size);
			normalBuffer.flip();
*/


			return true;

		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}
	}

}
