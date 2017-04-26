package com.dci.seaban.Objects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.dci.seaban.Service.ShowMessage;

import android.content.Context;
import android.opengl.GLES20;


public class ShaderCompiller {
	private String shaderSource;
	private String fragmentSource;
	private Context context;
	public String shaderName;
	private String fileName;
	public int program;
	public String result = "";
	
	public int mTextureCoordinateHandle;
	public int mTextureUniformHandle;

	public int mTextureCoordinateHandleF;
	public int mTextureUniformHandleF;
	public int mTextureUniformHandleS;
	
	public int mTextPosHandle;

	public float[] mModelMatrix = new float[16];

	public float[] mMVPMatrix = new float[16];

	public int mMVPMatrixHandle;
	public int mMVMatrixHandle;
	
	public int attrib_vertex;
	public int unif_color;
	public int normal;
	public int lightPos;
	public int fragColorHandle;
	
	

	public ShaderCompiller(Context context, String shaderName, String fileName) {
		this.context = context;
		this.shaderName = shaderName;
		this.fileName = fileName;
		
		shaderSource = loadSource(fileName + "_shader");
		fragmentSource = loadSource(fileName + "_fragment");
		compileShader();
		
		mMVPMatrixHandle = GLES20.glGetUniformLocation(program, "u_MVPMatrix");
		mMVMatrixHandle = GLES20.glGetUniformLocation(program, "u_MVMatrix");
		attrib_vertex = GLES20.glGetAttribLocation(program, "coord");
		normal = GLES20.glGetAttribLocation(program, "a_Normal");
		
		
		 

		mTextureCoordinateHandle = GLES20.glGetAttribLocation(program, "a_TexCoordinate");
		mTextPosHandle = GLES20.glGetUniformLocation(program, "u_TextPos");
		mTextureUniformHandle = GLES20.glGetUniformLocation(program, "u_Texture");
		mTextureUniformHandleF = GLES20.glGetUniformLocation(program, "u_TextureF");
		mTextureUniformHandleS = GLES20.glGetUniformLocation(program, "u_TextureS");
		
		unif_color = GLES20.glGetUniformLocation(program, "u_Color");
		lightPos = GLES20.glGetUniformLocation(program, "u_LightPos");



		
	}

	public void compileShader() {

		int vShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		GLES20.glShaderSource(vShader, shaderSource);
		GLES20.glCompileShader(vShader);

		final int[] compileStatus = new int[1];
		GLES20.glGetShaderiv(vShader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

		if (compileStatus[0] != GLES20.GL_TRUE) {
			result += "vShader: \n";
			result += GLES20.glGetShaderInfoLog(vShader) + "\n";
			ShowMessage.ShowCrash(result);
		}

		// ----------------------------------------------------------------------
		int fShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
		GLES20.glShaderSource(fShader, fragmentSource);
		GLES20.glCompileShader(fShader);

		GLES20.glGetShaderiv(fShader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
		if (compileStatus[0] != GLES20.GL_TRUE) {
			result += "fShader: \n";
			result += GLES20.glGetShaderInfoLog(fShader) + "\n";
			ShowMessage.ShowCrash(result);

		}
		// ----------------------------------------------------------------------

		program = GLES20.glCreateProgram();
		
		

		
		GLES20.glAttachShader(program, vShader);
		GLES20.glAttachShader(program, fShader);

		GLES20.glLinkProgram(program);

		int[] linkStatus = new int[1];
		GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
		if (linkStatus[0] != GLES20.GL_TRUE) {
			result += "Could not link program: \n";
			result += GLES20.glGetProgramInfoLog(program) + "\n";
			ShowMessage.ShowCrash(result);
		}
	}

	// ---------------------------------------------------------------------------------------------------
	public String loadSource(String fileName) {
		int resourceId = context.getResources().getIdentifier(fileName, "raw", context.getPackageName());

		InputStream fileIn = context.getResources().openRawResource(resourceId);
		BufferedReader buffer = new BufferedReader(new InputStreamReader(fileIn));

		String line;
		String source = "";
		try {
			while ((line = buffer.readLine()) != null) {
				source += line + "\n";
			}
		} catch (IOException e) 
		{
			ShowMessage.ShowCrash(e.getMessage());			
		}
		return source;
	}

}
