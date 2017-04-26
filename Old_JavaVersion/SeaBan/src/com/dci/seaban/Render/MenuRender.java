package com.dci.seaban.Render;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import com.dci.seaban.GameEngine;
import com.dci.seaban.R;
import com.dci.seaban.Canvas.LevelCanvas;
import com.dci.seaban.Canvas.LevelMenuCanvas;
import com.dci.seaban.Objects.Block;
import com.dci.seaban.Objects.Boat;
import com.dci.seaban.Objects.Cell;
import com.dci.seaban.Objects.Container;
import com.dci.seaban.Objects.Dock;
import com.dci.seaban.Objects.Greenblock;
import com.dci.seaban.Objects.Kran;
import com.dci.seaban.Objects.Map;
import com.dci.seaban.Objects.ObjectManager;
import com.dci.seaban.Objects.Panel;
import com.dci.seaban.Objects.PanelManager;
import com.dci.seaban.Objects.Pointer;
import com.dci.seaban.Objects.Sky;
import com.dci.seaban.Objects.Stolb;
import com.dci.seaban.Objects.Water;
import com.dci.seaban.Physics.Move;
import com.dci.seaban.Physics.Swing;
import com.dci.seaban.Physics.Swing2;

import com.dci.seaban.Service.GlobalVar;
import com.dci.seaban.Service.LevelDB;
import com.dci.seaban.Service.LevelTableRaw;
import com.dci.seaban.Service.Sound;
import com.dci.seaban.Service.SoundManager;
import com.dci.seaban.Service.VBOManager;

import android.content.Context;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.opengl.GLES20;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


public class MenuRender extends SceneRender {

	public int TouchEx = 0;

	public Swing swing = new Swing(80.12f, 0.1f);
	public Swing swing2 = new Swing(2.12f, 0.12f);
	public Swing swing3 = new Swing(5.15f, 0.3f);
	public Swing swing4 = new Swing(0.06f, 0.04f);
	public Move move = new Move(0.0f, 0.0f, 0.0f, 0.0f, 0.0f);

	public Move cameraMove = new Move(0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
	public int cameraPos = 1;
	
	private MediaPlayer mediaPlayer;


	Panel plashka;

	public Boat boat;
	


	float lx = 0.0f; 
	float ly = 0.0f; 
	float lz = 0.0f; 

	float boatDest = 0.0f;

	float cameraUp = 25.0f;

	float cameraXZ = 4.0f;
	float cameraXZeye = 25.0f;
	
	
	Move cmove = new Move(35.0f, 0.0f, 7.0f, 0.0f, 18.0);
	float cameraRange = 4.0f;
	
	Swing alphaSwing = new Swing(40, 0.005f);
	//Swing alphaSwing = new Swing(2, 0.05f);

	public MenuRender(Context context) {
		this.context = context;

	}

	private void recalcCamera() { // cameraAlpha, CameraTheta, CameraRadius

		 
		
		//GOOD 
		
		ly = 4;
		eyeY = cmove.GetX() + swing2.GetX(); 
		 
		lx = (float)( cameraXZ * Math.sin((double)alphaSwing.GetX()+ 90) + boat.x) ;
		lz = (float)( cameraXZ * Math.cos((double)alphaSwing.GetX()+ 90) + boat.z) ;
		 

		eyeX = (float)( (cameraXZeye + swing3.GetX()) * Math.sin((double)alphaSwing.GetX() + 90) + boat.x) ;
		eyeZ = (float)( (cameraXZeye + swing3.GetX()) * Math.cos((double)alphaSwing.GetX() + 90) + boat.z) ;
		

		Matrix.setLookAtM(_mViewMatrix, 0, eyeX, eyeY, eyeZ, lx, ly, lz, 0.0f, 1.0f, 0.0f);
		
		//Test
		
		/*
		ly = 4;
		eyeY = 7; 
		 
		lx = (float)( cameraXZ * Math.sin((double)90 +alphaSwing.GetX())) ;
		lz = (float)( cameraXZ * Math.cos((double)90 +alphaSwing.GetX())) ;
		 

		eyeX = (float)( (cameraXZeye) * Math.sin(90)) ;
		eyeZ = (float)( (cameraXZeye) * Math.cos(90)) ;
		

		Matrix.setLookAtM(_mViewMatrix, 0, eyeX, eyeY, eyeZ, lx, ly, lz, 0.0f, 1.0f, 0.0f);
		*/
		
	}

	public void createObjects(int level) {

		super.createObjects(0);

		
		if (mediaPlayer == null) mediaPlayer = MediaPlayer.create(context, R.raw.level);
		mediaPlayer.setLooping(true);		
		
		
		ObjectManager.clear();
		VBOManager.clearVBO();
		
		GlobalVar.levelScale = 1.0f;

		
		Sky sky = new Sky(context, "sky_obj", "map", this, R.drawable.sky2, R.drawable.sky2, R.drawable.sky2);
		sky.direction = 1;
	
		Map map = new  Map(context, "angar_obj", "map", this, R.drawable.angar,  R.drawable.angarf, R.drawable.angars); 
			 
		
		
		boat = new Boat(context, "boat_obj", "map", this, R.drawable.boat, R.drawable.boatf, R.drawable.boats);
		boat.blend = GlobalVar.blendBoth;
		
		 boat.x = -25.0f; 
		
		//Pointer pointer = new Pointer(context, "plashka_obj", "pointer", this, R.drawable.pointer, R.drawable.pointer, R.drawable.pointer);
		
		//pointer.y = boat.y + 0.2f;
		
		Stolb stolb = new Stolb(context, "plashka_obj", "pointer", this, R.drawable.pointer, R.drawable.pointer, R.drawable.pointer, boat);
		stolb.y = 0.2f;
		stolb.Start();
		

		eyeX = boat.x;
		eyeY = boat.y;
		eyeZ = boat.z;
	//	cameraMove.Reset(cameraMove.GetX(), cameraMove.GetY(), cameraXZ, 0, 10.0f);
		recalcCamera();
		
		//System.gc();
		
		SoundManager.Add("intro", Sound.introSoundID);
		SoundManager.Play("intro", 1.0f, 1.0f, false);
		

	//	mediaPlayer.start();
		
		

	}

	public void render(GL10 gl) {
		recalcCamera();
		super.render(gl);
	}


	public void goPause() {
		RenderManager.lockRender = true;

	}


}
