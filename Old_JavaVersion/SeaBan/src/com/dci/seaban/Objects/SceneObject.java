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
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

public class SceneObject {

	public int pos = 0;	
	
	public float alpha = 0;	
	public float radius = 0;
	
	public float x;
	public float y;
	public float z;
	
	protected Context context;
	protected SceneRender render;
	protected static final String TAG = null;
	
//	protected int attrib_vertex;
//	protected int unif_color;

	private float a = 0;

	protected int mPointProgramHandle;
	
	protected int[] normalbuf;

//	private float[] normalResultBuf;
//	private float[] textureBuf;
//	private float[] rocketBuffer = new float[4 * 30]; // vec4 for 30 rockets

	private long time;
	
	public boolean levelScale = false;

	//protected int mTextureCoordinateHandle;
	//protected int mTextureUniformHandle;

	//protected int mTextureCoordinateHandleF;
	//protected int mTextureUniformHandleF;

	public float[] mModelMatrix = new float[16];

	public float[] mMVPMatrix = new float[16];

	//protected int mMVPMatrixHandle;
	//protected int mMVMatrixHandle;

	// protected ShaderCompiller mapShader;
	// public float[] resultBuf;
	final int FLOAT_SIZE = 4; // GL float size?

	//protected int lightPos;
	//private int rocketsPos;

	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private FloatBuffer textureBuffer;
	//private FloatBuffer lightBuffer;

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
	
	//private Swing l = new Swing(20.0f, 0.05f);
	//private Swing l1 = new Swing(1.0f, 0.05f);
	//private Swing l2 = new Swing(1.0f, 0.5f);
	
	
	public SceneObject(Context context, String fileName, String shaderName, SceneRender render, int textureResID, int texture2ResID, int texture3ResID) {
		
		if (context == null) return;
		
		if (fileName != "") 
		{
		 this.context = context;
		 this.render = render;
		 this.fileName = fileName;
		 //this.fileName = "flat_obj";
		 this.shaderName = shaderName;
		 this.textureResID = textureResID;
		 this.texture2ResID = texture2ResID;
		 this.texture3ResID = texture3ResID;
		 initShader();		
		}
		else
		{
			 this.context = context;
			 this.render = render;
			 this.fileName = "";			
			 this.textureResID = -1;
			 this.texture2ResID = -1;
			 this.texture3ResID = -1;
		}
		
		
		
		ObjectManager.add(this);
	}
	
	
		

	public void initShader() {

		if (fileName == "") return;
		
		    ShaderManager.addShader(context, shaderName);
			
		    TextureManager.getTextureHandle(context, textureResID);
		    TextureManager.getTextureHandle(context, texture2ResID);
		    TextureManager.getTextureHandle(context, texture3ResID);
			
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
				GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBOManager.getVBO(fileName)[2]);
				GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, normalBuffer.capacity() * FLOAT_SIZE, normalBuffer, GLES20.GL_STATIC_DRAW); // vbb.capacity()
				normalBuffer = null;
				// is

				GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBOManager.getVBO(fileName)[1]);
				GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, textureBuffer.capacity() * FLOAT_SIZE, textureBuffer, GLES20.GL_STATIC_DRAW); // vbb.capacity()
				textureBuffer = null;
				System.gc();
				
			}
		}		
	}
	
	//----------------------------------------------------------------------
	public void selectUsedVBO(int number)
	{
		if (files.size() == 0) return;
		if (number > files.size()-1) return;
		fileName = files.get(number);
	}

	public void drawBlend() {		
		GLES20.glEnable(GLES20.GL_BLEND);
		//GLES20.glBlendFunc(GLES20.GL_ONE_MINUS_DST_COLOR, GLES20.GL_ONE_MINUS_SRC_ALPHA); //smoke
		//GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE); //water
		switch (blendType)
		{
			case 0:
				GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
				break;
			case 1: 
				GLES20.glBlendFunc(GLES20.GL_ONE_MINUS_DST_COLOR, GLES20.GL_ONE_MINUS_SRC_ALPHA);
				break;		  
		}
		
		draw();
		
		GLES20.glDisable(GLES20.GL_BLEND);

	}
	
	@SuppressLint("NewApi")
	public void draw() {

		if (levelScale)
		{
		 Matrix.scaleM(mModelMatrix, 0, GlobalVar.levelScale, GlobalVar.levelScale, GlobalVar.levelScale);
		}
		
		
		
		ShaderCompiller shader = ShaderManager.getShader(shaderName);
		
		GLES20.glUseProgram(shader.program);


		final int size = 4; // data count 2 for vec2 4 for vec4
		final int b = 0;
		final int c = 0;


		GLES20.glEnableVertexAttribArray(ShaderManager.getShader(shaderName).attrib_vertex);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBOManager.getVBO(fileName)[0]);
		GLES20.glVertexAttribPointer(ShaderManager.getShader(shaderName).attrib_vertex, size, GLES20.GL_FLOAT, false, b, c);
		
		GLES20.glEnableVertexAttribArray(ShaderManager.getShader(shaderName).normal);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBOManager.getVBO(fileName)[2]);
		GLES20.glVertexAttribPointer(ShaderManager.getShader(shaderName).normal, size, GLES20.GL_FLOAT, false, b, c);

		//GLES20.glEnableVertexAttribArray(unif_color);
		//GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBOManager.getVBO(fileName)[2]);
		//GLES20.glVertexAttribPointer(unif_color, size, GLES20.GL_FLOAT, false, b, c);

		GLES20.glEnableVertexAttribArray(ShaderManager.getShader(shaderName).mTextureCoordinateHandle);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBOManager.getVBO(fileName)[1]);
		int bsize = 2;
		GLES20.glVertexAttribPointer(ShaderManager.getShader(shaderName).mTextureCoordinateHandle, bsize, GLES20.GL_FLOAT, false, b, c);
		
		
		
		GLES20.glUniform3f(ShaderManager.getShader(shaderName).lightPos, render.eyeX, render.eyeY, render.eyeZ);
		
	//	GLES20.glUniform3f(ShaderManager.getShader(shaderName).lightPos, 0.0f, 0.0f, 0.0f);
		
		
		if (ShaderManager.getShader(shaderName).unif_color != -1)
		  GLES20.glUniform4f(ShaderManager.getShader(shaderName).unif_color, ColorR, ColorG, ColorB, ColorA);



		
		
		Matrix.multiplyMM(mMVPMatrix, 0, render._mViewMatrix, 0, mModelMatrix, 0);
		GLES20.glUniformMatrix4fv(ShaderManager.getShader(shaderName).mMVMatrixHandle, 1, false, mMVPMatrix, 0); //MV Matrix
		
		Matrix.multiplyMM(mMVPMatrix, 0, render.mProjectionMatrix, 0, mMVPMatrix, 0);
		GLES20.glUniformMatrix4fv(ShaderManager.getShader(shaderName).mMVPMatrixHandle, 1, false, mMVPMatrix, 0); //MVP Matrix


		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, TextureManager.getTextureHandle(context, textureResID));
		GLES20.glUniform1i(ShaderManager.getShader(shaderName).mTextureUniformHandle, 0);

		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, TextureManager.getTextureHandle(context, texture2ResID));
		GLES20.glUniform1i(ShaderManager.getShader(shaderName).mTextureUniformHandleF, 1);
		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, TextureManager.getTextureHandle(context, texture3ResID));
		GLES20.glUniform1i(ShaderManager.getShader(shaderName).mTextureUniformHandleS, 2);
		
		
		
		
		
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, VBOManager.getSize(fileName));



	GLES20.glUseProgram(0);

	}

	// ------------------------------------------------------------------------------------------------------------------------------
	public boolean loadingBinModel(String fileName) {

		byte[] byteArray;
		float[] floatArray;
		int size;

		try {
			// Vertex
			System.gc();
			int fileResID = context.getResources().getIdentifier(fileName + "_vert", "raw", context.getPackageName());
			InputStream fileIn = context.getResources().openRawResource(fileResID);
			DataInputStream dataStream = new DataInputStream(fileIn);

			vertexBuffer = null;

			size = dataStream.available();
			byteArray = new byte[size];
			dataStream.readFully(byteArray); // read byte array

			floatArray = new float[size / 4];
			ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer().get(floatArray); 

			vertexBuffer = FloatBuffer.allocate(size / 4); // float array to
															// FloatBuffer
			vertexBuffer.put(floatArray, 0, size / 4);
			vertexBuffer.flip();

			VBOManager.setSize(fileName, vertexBuffer.capacity() / 4);

			byteArray = null;
			floatArray = null;
			
			dataStream.close();
			fileIn.close();

			// -------------------------------------------------------------------------------------------------------------------------
			fileResID = context.getResources().getIdentifier(fileName + "_norm", "raw", context.getPackageName());
			fileIn = context.getResources().openRawResource(fileResID);
			dataStream = new DataInputStream(fileIn);

			normalBuffer = null;

			size = dataStream.available();
			byteArray = new byte[size];
			dataStream.readFully(byteArray); // read byte array

			floatArray = new float[size / 4];
			ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer().get(floatArray); 

			normalBuffer = FloatBuffer.allocate(size / 4); // float array to
															// FloatBuffer
			normalBuffer.put(floatArray, 0, size / 4);
			normalBuffer.flip();

			byteArray = null;
			floatArray = null;
			
			dataStream.close();
			fileIn.close();
			
			System.gc();
			// -------------------------------------------------------------------------------------------------------------------------
			fileResID = context.getResources().getIdentifier(fileName + "_texture", "raw", context.getPackageName());
			fileIn = context.getResources().openRawResource(fileResID);
			dataStream = new DataInputStream(fileIn);

			textureBuffer = null;

			size = dataStream.available();
			byteArray = new byte[size];
			dataStream.readFully(byteArray); // read byte array

			floatArray = new float[size / 4];
			ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer().get(floatArray); 
			textureBuffer = FloatBuffer.allocate(size / 4); // float array to
															// FloatBuffer
			textureBuffer.put(floatArray, 0, size / 4);
			textureBuffer.flip();

			byteArray = null;
			floatArray = null;
			
			dataStream.close();
			fileIn.close();
			System.gc();

			return true;

		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}
	}

	// -----------------------------------------------------------
	public FloatBuffer loadingModel(String fileName) {
		/*
		 * FloatBuffer fb; float[] buf; float[] tbuf; float[] nbuf;
		 * 
		 * // float[] resultBuf; int[] facebuf; int[] texbuf;
		 * 
		 * //temp int fileResID = context.getResources().getIdentifier(fileName,
		 * "raw", context.getPackageName());
		 * 
		 * InputStream fileIn =
		 * context.getResources().openRawResource(fileResID); BufferedReader
		 * buffer = new BufferedReader(new InputStreamReader(fileIn));
		 * 
		 * String line; String currentMaterial = ""; int count = 0; int
		 * faceCount = 0; int texCount = 0; int normalCount = 0; try { while
		 * ((line = buffer.readLine()) != null) { // Log.e(TAG, line);
		 * StringTokenizer parts = new StringTokenizer(line, " "); int numTokens
		 * = parts.countTokens(); if (numTokens == 0) continue;
		 * 
		 * String type = parts.nextToken(); if (type.equals("v")) { count += 4;
		 * }
		 * 
		 * if (type.equals("f")) { faceCount += 3; }
		 * 
		 * if (type.equals("vt")) { texCount += 2; }
		 * 
		 * if (type.equals("vn")) { normalCount += 4; }
		 * 
		 * } buffer = null; fileIn.close();
		 * 
		 * fileIn = context.getResources().openRawResource(fileResID); buffer =
		 * new BufferedReader(new InputStreamReader(fileIn));
		 * 
		 * buf = new float[count + 4]; tbuf = new float[texCount + 2]; nbuf =
		 * new float[normalCount + 4];
		 * 
		 * count = 0; texCount = 0; normalCount = 0;
		 * 
		 * facebuf = new int[faceCount]; normalbuf = new int[faceCount]; texbuf
		 * = new int[faceCount]; faceCount = 0;
		 * 
		 * while ((line = buffer.readLine()) != null) { // Log.e(TAG, line);
		 * StringTokenizer parts = new StringTokenizer(line, " "); int numTokens
		 * = parts.countTokens(); if (numTokens == 0) continue;
		 * 
		 * String type = parts.nextToken(); if (type.equals("v")) { buf[count] =
		 * Float.parseFloat(parts.nextToken()); // x buf[count + 1] =
		 * Float.parseFloat(parts.nextToken()); // y buf[count + 2] =
		 * Float.parseFloat(parts.nextToken()); // z buf[count + 3] = 1f; // w
		 * count += 4;
		 * 
		 * }
		 * 
		 * if (type.equals("vn")) { nbuf[normalCount] =
		 * Float.parseFloat(parts.nextToken()); // x nbuf[normalCount + 1] =
		 * Float.parseFloat(parts.nextToken()); // y nbuf[normalCount + 2] =
		 * Float.parseFloat(parts.nextToken()); // z nbuf[normalCount + 3] = 0f;
		 * // normalCount += 4;
		 * 
		 * }
		 * 
		 * if (type.equals("vt")) { tbuf[texCount] =
		 * Float.parseFloat(parts.nextToken()); // x tbuf[texCount + 1] =
		 * Float.parseFloat(parts.nextToken()); // y texCount += 2;
		 * 
		 * }
		 * 
		 * if (type.equals("f")) { if (line.contains("//")) continue; String
		 * token = parts.nextToken(); int index = token.indexOf("/"); String
		 * face = token.substring(0, index); String normal =
		 * token.substring(index + 1, token.length()); index =
		 * normal.indexOf("/"); String tex = normal.substring(0, index); normal
		 * = normal.substring(index + 1, normal.length());
		 * 
		 * facebuf[faceCount] = (int) Float.parseFloat(face); // face // point
		 * // index texbuf[faceCount] = (int) Float.parseFloat(tex); // normal
		 * // point // index normalbuf[faceCount] = (int)
		 * Float.parseFloat(normal); // normal // point // index
		 * 
		 * token = parts.nextToken(); index = token.indexOf("/"); face =
		 * token.substring(0, index); normal = token.substring(index + 1,
		 * token.length()); index = normal.indexOf("/"); tex =
		 * normal.substring(0, index); normal = normal.substring(index + 1,
		 * normal.length());
		 * 
		 * facebuf[faceCount + 1] = (int) Float.parseFloat(face); //
		 * texbuf[faceCount + 1] = (int) Float.parseFloat(tex); // normal //
		 * point // index normalbuf[faceCount + 1] = (int)
		 * Float.parseFloat(normal); //
		 * 
		 * token = parts.nextToken(); index = token.indexOf("/"); face =
		 * token.substring(0, index); normal = token.substring(index + 1,
		 * token.length()); index = normal.indexOf("/"); tex =
		 * normal.substring(0, index); normal = normal.substring(index + 1,
		 * normal.length());
		 * 
		 * facebuf[faceCount + 2] = (int) Float.parseFloat(face); //
		 * texbuf[faceCount + 2] = (int) Float.parseFloat(tex); // normal //
		 * point // index normalbuf[faceCount + 2] = (int)
		 * Float.parseFloat(normal); //
		 * 
		 * faceCount += 3;
		 * 
		 * }
		 * 
		 * } fileIn.close();
		 * 
		 * textureBuf = new float[texbuf.length * 2]; int textureCount = 0; for
		 * (int i = 0; i < texbuf.length; i++) { int j = (texbuf[i] - 1) * 2;
		 * textureBuf[textureCount] = tbuf[j]; textureBuf[textureCount + 1] = 1
		 * - tbuf[j + 1]; // "1 - texture coord" // the blender // texture Y //
		 * xoord from 1 // - 0, the // OpenGL ES at // this config // is 0 - 1
		 * 
		 * textureCount += 2; }
		 * 
		 * resultBuf = new float[facebuf.length * 4]; normalResultBuf = new
		 * float[facebuf.length * 4];
		 * 
		 * int resCount = 0; normalCount = 0; int allNormalCount = 0; int t = 0;
		 * for (int i = 0; i < facebuf.length; i++) {
		 * 
		 * int j = (facebuf[i] - 1) * 4; resultBuf[resCount] = buf[j];
		 * resultBuf[resCount + 1] = buf[j + 1]; resultBuf[resCount + 2] = buf[j
		 * + 2]; resultBuf[resCount + 3] = buf[j + 3]; resCount += 4;
		 * 
		 * j = (normalbuf[i] - 1) * 4; normalResultBuf[normalCount] = nbuf[j];
		 * normalResultBuf[normalCount + 1] = nbuf[j + 1];
		 * normalResultBuf[normalCount + 2] = nbuf[j + 2];
		 * normalResultBuf[normalCount + 3] = 0f;
		 * 
		 * normalCount += 4;
		 * 
		 * }
		 * 
		 * if (resultBuf != null) { int aSize = resultBuf.length;
		 * 
		 * fb = FloatBuffer.allocate(aSize); // next Clear, Put and Flip - // in
		 * documentation fb.clear(); fb.put(resultBuf, 0, aSize); fb.flip();
		 * 
		 * return fb; } } catch (IOException e) { e.printStackTrace(); }
		 */
		return null;

	}
}
