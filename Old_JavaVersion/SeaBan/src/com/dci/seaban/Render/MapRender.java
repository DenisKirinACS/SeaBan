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

import com.dci.seaban.Service.BitmapManager;
import com.dci.seaban.Service.GlobalVar;
import com.dci.seaban.Service.LevelDB;
import com.dci.seaban.Service.LevelTableRaw;
import com.dci.seaban.Service.Sound;
import com.dci.seaban.Service.SoundManager;
import com.dci.seaban.Service.VBOManager;

import android.content.Context;
import android.graphics.Point;
import android.opengl.GLES20;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MapRender extends SceneRender {

	private Boolean lockTuch = false;
	private float v_width = 800;
	private float v_height = 600;

	private float ry, rx;

	private float saveRY, saveRX;

	private float HistoryX1 = -1.0f;
	private float HistoryY1 = -1.0f;
	private float HistoryX2 = -1.0f;
	private float HistoryY2 = -1.0f;

	public int TouchEx = 0;

	public Swing swing = new Swing(1.12f, 0.1f);
	public Swing swing2 = new Swing(0.12f, 0.12f);
	public Swing swing3 = new Swing(50.15f, 0.3f);
	public Swing swing4 = new Swing(0.06f, 0.04f);
	public Move move = new Move(0.0f, 0.0f, 0.0f, 0.0f, 0.0f);

	public Move cameraMove = new Move(0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
	public int cameraPos = 1;

	private float xa = 0;
	private float ya = 0;

	private float MHistoryX1;
	private float MHistoryY1;

	private float MHistoryX1Down;
	private float MHistoryY1Down;

	Panel plashka;

	public Boat boat;

	public Kran kran;

	Water water;
	Sky sky;

	int level;

	float lx = 0.0f; // (float) (lr * Math.cos((float)
						// (Math.toRadians(-cameraAlpha))) + eyeX +
						// swing.GetX());
	float ly = 0.0f; // eyeY + swing3.GetX() - 30.0f;
	float lz = 0.0f; // (float) (lr * Math.sin((float)
						// (Math.toRadians(-cameraAlpha))) + eyeZ +
						// swing2.GetX());

	float boatDest = 0.0f;

	float cameraUp = 60.0f;
	float cameraXZ = 50.0f;

	Move cmove = new Move(0.0f, 0.0f, 0.0f, 0.0f, 0.0);
	float cameraRange = 4.0f;
	float cameraSmallRange = 1.0f;

	// LevelMenuCanvas levelMenuCanvas;

	public MapRender(Context context) {
		this.context = context;

	}

	private void recalcCamera() { // cameraAlpha, CameraTheta, CameraRadius
		
		lx = cmove.GetX();
		lz = cmove.GetY();
		
		cameraSmallRange = 7.0f / GlobalVar.coef;
		
		
		
		if ((((GameEngine.levelWidth / 2 * 2) == GameEngine.levelWidth)
		||
		  ((GameEngine.levelHeight / 2 * 2) == GameEngine.levelHeight))
		&&
			
		 ((((GameEngine.getBoat().x == GameEngine.levelWidth / 2 ))
			 ||
			 ((GameEngine.getBoat().y == GameEngine.levelHeight / 2 ))
			 ||
			 ((GameEngine.getBoat().x == GameEngine.levelWidth / 2 - 1))
			 ||
			 ((GameEngine.getBoat().y == GameEngine.levelHeight / 2 - 1)))))
		 
		{
			//nothing to do
		}
	else

		if ((boat.x < cameraSmallRange) && (boat.x > -cameraSmallRange) && (boat.z < cameraSmallRange) && (boat.z > -cameraSmallRange)) {
			cmove.Reset(lx, lz, 0, 0, 2.0f);
		} else if ((boat.x < cameraSmallRange) && (boat.x > -cameraSmallRange)) {
			cmove.Reset(lx, lz, 0, lz, 2.0f);
		} else if ((boat.z < cameraSmallRange) && (boat.z > -cameraSmallRange)) {
			cmove.Reset(lx, lz, lx, 0, 2.0f);
		} else

		if (((int)lx < (int)boat.x) && ((int)lz < (int)boat.z))
			cmove.Reset(lx, lz, cameraRange, cameraRange, 2.0f);
		else if (((int)lx > boat.x) && ((int)lz < boat.z))
			cmove.Reset(lx, lz, -cameraRange, cameraRange, 2.0f);
		else if (((int)lx < (int)boat.x) && ((int)lz > (int)boat.z))
			cmove.Reset(lx, lz, cameraRange, -cameraRange, 2.0f);
		else if (((int)lx > (int)boat.x) && ((int)lz > (int)boat.z))
			cmove.Reset(lx, lz, -cameraRange, -cameraRange, 2.0f);
		else
			ly = 0.0f;
		
		eyeX = lx + boatDest + boatDestZoom + cameraMove.GetX();
		eyeZ = lz + boatDest + boatDestZoom + cameraMove.GetY();
		eyeY = cameraUp;

		Matrix.setLookAtM(_mViewMatrix, 0, eyeX, eyeY , eyeZ, lx, ly, lz, 0.0f, 1.0f, 0.0f);
		

		/*
		lx = cmove.GetX();
		lz = cmove.GetY();
		
		cameraSmallRange = 7.0f / GlobalVar.coef;

		if ((boat.x < cameraSmallRange) && (boat.x > -cameraSmallRange) && (boat.z < cameraSmallRange) && (boat.z > -cameraSmallRange)) {
			cmove.Reset(lx, lz, 0, 0, 2.0f);
		} else if ((boat.x < cameraSmallRange) && (boat.x > -cameraSmallRange)) {
			cmove.Reset(lx, lz, 0, lz, 2.0f);
		} else if ((boat.z < cameraSmallRange) && (boat.z > -cameraSmallRange)) {
			cmove.Reset(lx, lz, lx, 0, 2.0f);
		} else

		if ((lx < boat.x) && (lz < boat.z))
			cmove.Reset(lx, lz, cameraRange, cameraRange, 2.0f);
		else if ((lx > boat.x) && (lz < boat.z))
			cmove.Reset(lx, lz, -cameraRange, cameraRange, 2.0f);
		else if ((lx < boat.x) && (lz > boat.z))
			cmove.Reset(lx, lz, cameraRange, -cameraRange, 2.0f);
		else if ((lx > boat.x) && (lz > boat.z))
			cmove.Reset(lx, lz, -cameraRange, -cameraRange, 2.0f);
		else

		
		eyeX = lx + boatDest + cameraMove.GetX();
		eyeZ = lz + boatDest + cameraMove.GetY();
		
		ly = 0.0f;
		eyeY = cameraUp + boatDestZoom;

		Matrix.setLookAtM(_mViewMatrix, 0, eyeX, eyeY , eyeZ, lx, ly, lz, 0.0f, 1.0f, 0.0f);
		*/
				
	}

	public void createObjects(int level) {

		super.createObjects(level);
		this.level = level;

		float xFrom = -50.0f;
		float yFrom = -200.0f;

		ObjectManager.clear();
		VBOManager.clearVBO();
		PanelManager.clear();

		GameEngine.loadLevel(level);

		switch (RenderManager.db.GetQuality()) {
		case 0:
			for (int x = 0; x < GameEngine.levelWidth; x++)
				for (int y = 0; y < GameEngine.levelHeight; y++) {
					if (GameEngine.getObjectAtXY(x, y) != 0) {
						Block block = new Block(context, "flat_obj", "map", this, R.drawable.blueblock, R.drawable.blockf, R.drawable.blocks);
						block.gameX = x;
						block.gameY = y;
						block.y = (float) (-6.0f * GlobalVar.levelScale);
					}
				}

			break; // End of quality 0

		case 2:
			 kran = new Kran(context, "", "map", this, -1, -1, -1);
			 kran.z = -25.0f;

		case 1:
			int fileResID = context.getResources().getIdentifier("level1_obj_lst", "raw", context.getPackageName());
			InputStream fileIn = context.getResources().openRawResource(fileResID);
			BufferedReader buffer = new BufferedReader(new InputStreamReader(fileIn));

			String line;

			try {

				while ((line = buffer.readLine()) != null) {
					map = new Map(context, line, "map", this, R.drawable.room, R.drawable.roomf, R.drawable.roomspec);

				}
				fileIn.close();
				buffer = null;
			} catch (IOException e)

			{
				e.printStackTrace();
			}

			// Water water = new Water(context, "water_obj", "water", this,
			// R.drawable.water2, R.drawable.water2f, R.drawable.water2s);
			// water.blend = GlobalVar.blendYes;

			// water = new Water(context, "water_obj", "water", this,
			// R.drawable.water2, R.drawable.water2f, R.drawable.water2s);
			// water.direction = 1;
			// water.blend = GlobalVar.blendYes;

			sky = new Sky(context, "sky_obj", "map", this, R.drawable.sky, R.drawable.sky, R.drawable.sky);
			sky.direction = 1;
			break;
		} // end of SWITCH

		for (int x = 0; x < GameEngine.levelWidth; x++)
			for (int y = 0; y < GameEngine.levelHeight; y++) {
				switch (GameEngine.getObjectAtXY(x, y)) {
				case 0: {
					Block block = new Block(context, "block_obj", "map", this, R.drawable.block, R.drawable.blockf, R.drawable.blocks);
					block.gameX = x;
					block.gameY = y;
					block.y = -1.0f;
					break;
				}

				case 1:
				case 2: {
					PanelManager.add(x, y);
					break;
				}

				case 4: {
					PanelManager.add(x, y);
					Dock dock = new Dock(context, "dock_obj", "map" +
							"", this, R.drawable.dock, R.drawable.contf, R.drawable.conts);
					dock.gameX = x;
					dock.gameY = y;
					dock.blend = GlobalVar.blendYes;
					break;

				}

				case 5: {
					PanelManager.add(x, y);
					Container container = new Container(context, "container_obj", "map", this, R.drawable.cont, R.drawable.contf, R.drawable.conts);
					container.gameX = x;
					container.gameY = y;
					break;

				}

				}
			}

		Point boatPos = GameEngine.getBoat();
		boat = new Boat(context, "boat_obj", "map", this, R.drawable.boat, R.drawable.boatf, R.drawable.boats);
		boat.blend = GlobalVar.blendBoth;
		boat.moveTo(boatPos.x, boatPos.y, boatPos.x, boatPos.y);

		Cell cell = new Cell(context, this);

		eyeX = boat.x;
		eyeY = boat.y;
		eyeZ = boat.z;
		
		Stolb stolb = new Stolb(context, "plashka_obj", "pointer", this, R.drawable.pointer, R.drawable.pointer, R.drawable.pointer, boat);
		stolb.y = 0.2f;
		stolb.Start();
		
		// cameraAlpha = 80.0f;

		// cameraRadius = 1.0f;
		// cameraTheta = 0.0f;

		// cameraMove.Reset(cameraMove.GetX(), cameraMove.GetY(), cameraXZ,
		// cameraXZ, 2.0f);
		cameraMove.Reset(cameraMove.GetX(), cameraMove.GetY(), cameraXZ, 0, 10.0f);
		recalcCamera();
		GameEngine.pushLevel();
		System.gc();
		// /-->create joystick here
		
		

	}

	public void render(GL10 gl) {
		Sound.render = this;

		
		
		recalcCamera();

		checkJoyButtons(GameEngine.getBoat().x, GameEngine.getBoat().y);

		RenderManager.levelMenuCanvas.postInvalidate();
		super.render(gl);
	}

	public void checkJoyButtons(int gameX, int gameY) {
		LevelCanvas canvas = (LevelCanvas) RenderManager.canvasView;
		switch (cameraPos) {
		case 0:
		case 1:
			if (GameEngine.canMoveBoat(gameX, gameY + 1))
				canvas.tlButton.visible = true;
			else
				canvas.tlButton.visible = false;

			if (GameEngine.canMoveBoat(gameX - 1, gameY))
				canvas.trButton.visible = true;
			else
				canvas.trButton.visible = false;

			if (GameEngine.canMoveBoat(gameX + 1, gameY))
				canvas.blButton.visible = true;
			else
				canvas.blButton.visible = false;

			if (GameEngine.canMoveBoat(gameX, gameY - 1))
				canvas.brButton.visible = true;
			else
				canvas.brButton.visible = false;
			break;

		case 2:
			if (GameEngine.canMoveBoat(gameX - 1, gameY))
				canvas.tlButton.visible = true;
			else
				canvas.tlButton.visible = false;

			if (GameEngine.canMoveBoat(gameX, gameY - 1))
				canvas.trButton.visible = true;
			else
				canvas.trButton.visible = false;

			if (GameEngine.canMoveBoat(gameX, gameY + 1))
				canvas.blButton.visible = true;
			else
				canvas.blButton.visible = false;

			if (GameEngine.canMoveBoat(gameX + 1, gameY))
				canvas.brButton.visible = true;
			else
				canvas.brButton.visible = false;

			break;

		case 3:
			if (GameEngine.canMoveBoat(gameX + 1, gameY))
				canvas.tlButton.visible = true;
			else
				canvas.tlButton.visible = false;

			if (GameEngine.canMoveBoat(gameX, gameY + 1))
				canvas.trButton.visible = true;
			else
				canvas.trButton.visible = false;

			if (GameEngine.canMoveBoat(gameX, gameY - 1))
				canvas.blButton.visible = true;
			else
				canvas.blButton.visible = false;

			if (GameEngine.canMoveBoat(gameX - 1, gameY))
				canvas.brButton.visible = true;
			else
				canvas.brButton.visible = false;

			break;

		case 4:
			if (GameEngine.canMoveBoat(gameX, gameY - 1))
				canvas.tlButton.visible = true;
			else
				canvas.tlButton.visible = false;

			if (GameEngine.canMoveBoat(gameX + 1, gameY))
				canvas.trButton.visible = true;
			else
				canvas.trButton.visible = false;

			if (GameEngine.canMoveBoat(gameX - 1, gameY))
				canvas.blButton.visible = true;
			else
				canvas.blButton.visible = false;

			if (GameEngine.canMoveBoat(gameX, gameY + 1))
				canvas.brButton.visible = true;
			else
				canvas.brButton.visible = false;

			break;

		}

		/*
		 * case 2: MoveBoat(GameEngine.getBoat().x-1, GameEngine.getBoat().y);
		 * break; case 3: MoveBoat(GameEngine.getBoat().x+1,
		 * GameEngine.getBoat().y); break; case 4:
		 * MoveBoat(GameEngine.getBoat().x, GameEngine.getBoat().y - 1); break;
		 * 
		 * canvas.trButton.CheckTouch(action, x, y); if
		 * (canvas.trButton.selected) {
		 * 
		 * switch (cameraPos) { case 0: MoveBoat(GameEngine.getBoat().x - 1,
		 * GameEngine.getBoat().y); break; case 1:
		 * MoveBoat(GameEngine.getBoat().x - 1, GameEngine.getBoat().y); break;
		 * case 2: MoveBoat(GameEngine.getBoat().x, GameEngine.getBoat().y - 1);
		 * break; case 3: MoveBoat(GameEngine.getBoat().x,
		 * GameEngine.getBoat().y + 1); break; case 4:
		 * MoveBoat(GameEngine.getBoat().x + 1, GameEngine.getBoat().y); break;
		 * 
		 * } }
		 * 
		 * canvas.blButton.CheckTouch(action, x, y); if
		 * (canvas.blButton.selected)
		 * 
		 * 
		 * 
		 * {
		 * 
		 * switch (cameraPos) { case 0: MoveBoat(GameEngine.getBoat().x + 1,
		 * GameEngine.getBoat().y); break; case 1:
		 * MoveBoat(GameEngine.getBoat().x + 1, GameEngine.getBoat().y); break;
		 * case 2: MoveBoat(GameEngine.getBoat().x, GameEngine.getBoat().y + 1);
		 * break; case 3: MoveBoat(GameEngine.getBoat().x,
		 * GameEngine.getBoat().y - 1); break; case 4:
		 * MoveBoat(GameEngine.getBoat().x -1, GameEngine.getBoat().y); break;
		 * 
		 * } }
		 * 
		 * canvas.brButton.CheckTouch(action, x, y); if
		 * (canvas.brButton.selected)
		 * 
		 * {
		 * 
		 * switch (cameraPos) { case 0: MoveBoat(GameEngine.getBoat().x,
		 * GameEngine.getBoat().y - 1); break; case 1:
		 * MoveBoat(GameEngine.getBoat().x, GameEngine.getBoat().y - 1); break;
		 * case 2: MoveBoat(GameEngine.getBoat().x+1, GameEngine.getBoat().y);
		 * break; case 3: MoveBoat(GameEngine.getBoat().x -1,
		 * GameEngine.getBoat().y); break; case 4:
		 * MoveBoat(GameEngine.getBoat().x, GameEngine.getBoat().y + 1); break;
		 * 
		 * }
		 * 
		 * }
		 */

	}

	public boolean MoveBoat(int gameX, int gameY) {
		boolean result = false;
		if (boat.isMove) return true;
		if ((!PanelManager.completeWay) && (PanelManager.getWaySize() != 0))  return true;
		
		if (GameEngine.canMoveBoat(gameX, gameY)) {

			boat.isMove = true;
			result = true;
			boat.moveTo(gameX, gameY);

			Point newContPos = GameEngine.setBoatPos(gameX, gameY);

			checkJoyButtons(gameX, gameY);

			if (newContPos != null) {
				for (int i = 0; i < ObjectManager.getCount(); i++) {

					if (ObjectManager.get(i).fileName == "container_obj") {
						Container cont = (Container) ObjectManager.get(i);
						if ((cont.gameX == gameX) && (cont.gameY == gameY)) {
							cont.moveTo(newContPos.x, newContPos.y);
							break;
						}
					}
				}

				
			}

			if (GameEngine.checkGameEnd()) {


				
				int conCount = 0;
				int stars =  0;
				for (int i = 0; i < ObjectManager.getCount(); i++) {

					if (ObjectManager.get(i).fileName == "container_obj") {
						conCount++;
					}
				}
				
				float time = GameEngine.gameTime / 1000.0f /60.0f;
				if (time < 1)
				{
					GameEngine.gameScore = conCount * 10000 + (int)(60.0f * 1000.0f - GameEngine.gameTime) / 10;
					stars = 3;
				}
				else 
				{
					float scoreTime = (float)( time / conCount);	
					float scoreMove = (float)(GameEngine.moveCount / conCount);
					
					GameEngine.gameScore = (int)(conCount * 2000 + scoreTime / 10.0f);
					if ((scoreTime <=1) && (scoreMove <= 10.0f)) stars = 3;
					else 
						if (scoreTime <= 1.5f) stars = 2;
						else 
							stars = 1;
				}
				
				LevelTableRaw levelTableRaw;

				levelTableRaw = RenderManager.levelDB.GetRaw(level);
				levelTableRaw.stars = stars;
				levelTableRaw.score = GameEngine.gameScore;
				GameEngine.gameStars = stars;
				
				RenderManager.levelDB.SetRaw(levelTableRaw);

				levelTableRaw = RenderManager.levelDB.GetRaw(level + 1);
				levelTableRaw.lock = 0;
				RenderManager.levelDB.SetRaw(levelTableRaw);

				LevelCanvas canvas = (LevelCanvas) RenderManager.canvasView;
				canvas.scoreMove.Reset(0, 0, GameEngine.gameScore, GameEngine.gameTime, 2.5f);
				canvas.viewMode = 5;
				
				//RenderManager.selectScene(nextScene);
			}

		}
		return result;
	}

	public void goPause() {
		RenderManager.lockRender = true;
		// levelMenuCanvas = new LevelMenuCanvas(context);
		// RenderManager.relativeLayout.addView(levelMenuCanvas);
		RenderManager.levelMenuCanvas.Show();
		RenderManager.canvasView.viewMode = 4;

	}

	@Override
	public void onTouch(int action, int x, int y) {

		LevelCanvas canvas = (LevelCanvas) RenderManager.canvasView;
		
		if (canvas.viewMode == 6)
		{
			if (action == MotionEvent.ACTION_UP)			
				canvas.viewMode = 0;
		}
		else 
		if (canvas.viewMode != 5)
		{
		
		Panel panel;
		if (!RenderManager.levelMenuCanvas.Visible) {

			if ((action == MotionEvent.ACTION_DOWN) || (action == MotionEvent.ACTION_POINTER_DOWN)) {
				panel = PanelManager.getTouch(x, y);
				if (panel != null) {
					PanelManager.clearWay(); // build new way

					if ((GameEngine.getBoat().x == panel.gameX) && (GameEngine.getBoat().y == panel.gameY)) {
						PanelManager.addToWay(panel);
					}
				}

			}

			if (action == MotionEvent.ACTION_UP) {
				if (PanelManager.getWaySize() > 1) {
					PanelManager.completeWay = true;
				} else {
					panel = PanelManager.getTouch(x, y);
					if (panel != null) {
						if (!panel.visible) {
							if (!GameEngine.canMoveBoat(panel.gameX, panel.gameY))
								PanelManager.makeAutoWay(panel.gameX, panel.gameY);
						}

					}

				}

			} else if (action == MotionEvent.ACTION_MOVE) {

				panel = PanelManager.getTouch(x, y);
				if (panel != null) {
					if (PanelManager.getWaySize() != 0)
						PanelManager.addToWay(panel);
				}

			}
		}
		// else // Buttons touch
		// -------------------------------------------------------

		if (RenderManager.levelMenuCanvas.Visible) // Menu
		{

			if (RenderManager.levelMenuCanvas.closeButton.CheckTouch(action, x, y)) {

				RenderManager.levelMenuCanvas.Hide();// .Reset(0, 0, -640, 0,
														// 1.0f);//

				RenderManager.canvasView.viewMode = 0;
				RenderManager.lockRender = false;

				/*
				 * for (int i = 0; i > 100; i++) try {
				 * 
				 * canvas.postInvalidate();
				 * 
				 * RenderManager.levelMenuCanvas.postInvalidate();
				 * Thread.sleep(10); } catch (InterruptedException e) { }
				 * //RenderManager.levelMenuCanvas = null;
				 * RenderManager.canvasView.viewMode = 0;
				 * RenderManager.lockRender = false;
				 */
			} else if (RenderManager.levelMenuCanvas.cameraButton.CheckTouch(action, x, y)) {
				cameraPos++;
				if (cameraPos > 4)
					cameraPos = 0;
				checkJoyButtons(GameEngine.getBoat().x, GameEngine.getBoat().y);

				switch (cameraPos) {
				case 0:
					cameraMove.Reset(cameraMove.GetX(), cameraMove.GetY(), 0.1f, 0, 2.0f);
					cameraUp = 90;
					canvas.joyToFlat();

					break;
				case 1:
					cameraMove.Reset(cameraMove.GetX(), cameraMove.GetY(), cameraXZ, 0, 2.0f);
					cameraUp = 70;
					canvas.joyToFlat();

					break;
				case 2:
					cameraMove.Reset(cameraMove.GetX(), cameraMove.GetY(), cameraXZ, cameraXZ, 2.0f);
					cameraUp = 70;
					canvas.joyFromFlat();
					break;
				case 3:
					cameraMove.Reset(cameraMove.GetX(), cameraMove.GetY(), 0, -cameraXZ, 2.0f);
					cameraUp = 70;
					canvas.joyToFlat();
					break;
				case 4:
					cameraMove.Reset(cameraMove.GetX(), cameraMove.GetY(), -cameraXZ, cameraXZ, 2.0f);
					cameraUp = 70;
					canvas.joyFromFlat();
					break;
				}
			} else if (RenderManager.levelMenuCanvas.camera2D3DButton.CheckTouch(action, x, y)) {
				if (cameraPos > 0) {
					cameraPos = 0;
					cameraMove.Reset(cameraMove.GetX(), cameraMove.GetY(), 0.01f, 0, 1.0f);
					canvas.joyToFlat();
				} else {
					cameraPos = 1;
					cameraMove.Reset(cameraMove.GetX(), cameraMove.GetY(), cameraXZ, 0, 1.0f);
				}
			} else if (RenderManager.levelMenuCanvas.cameraToObjectButton.CheckTouch(action, x, y)) {
				if (kran != null) {
					if (cameraPos != 5) {
						cameraPos = 5;
						cameraMove.Reset(cameraMove.GetX(), cameraMove.GetY(), kran.x, kran.z, 1.0f);
					} else {
						cameraPos = 1;
						cameraMove.Reset(cameraMove.GetX(), cameraMove.GetY(), cameraXZ, 0, 1.0f);
					}
				}
			} else if (RenderManager.levelMenuCanvas.stepBackButton.CheckTouch(action, x, y)) {

				GameEngine.popLevel();

				Point boatPos = GameEngine.getBoat();
				boat.x = GlobalVar.GameXToGLX(boatPos.x);
				boat.z = GlobalVar.GameYToGLY(boatPos.y);

				boolean containerExist = true;

				while (containerExist) {
					containerExist = false;

					for (int i = 0; i < ObjectManager.getCount(); i++) // delete
																		// containers
					{
						if (ObjectManager.get(i).fileName == "container_obj") {
							Container cont = (Container) ObjectManager.get(i);
							
							ObjectManager.del(ObjectManager.getIndexByObject(cont.pointer));
							cont.pointer = null;
							
							ObjectManager.del(i);
							
							cont = null;
							containerExist = true;
							break;
						}
					}
				}

				for (int xl = 0; xl < GameEngine.levelWidth; xl++)
					for (int yl = 0; yl < GameEngine.levelHeight; yl++) {
						if (GameEngine.getObjectAtXY(xl, yl) == 5) {
							Container container = new Container(context, "container_obj", "map", this, R.drawable.cont, R.drawable.blockf, R.drawable.blocks);
							container.gameX = xl;
							container.gameY = yl;
						}

					}

			} else if (RenderManager.levelMenuCanvas.resetButton.CheckTouch(action, x, y)) {

				// GameEngine.loadLevel(level); //TODO
				RenderManager.selectScene(RenderManager.currentScene);
			}

			else if (RenderManager.levelMenuCanvas.homeButton.CheckTouch(action, x, y)) {

				// GameEngine.loadLevel(level); //TODO
				RenderManager.selectScene(RenderManager.sceneMenu);
			} else if (RenderManager.levelMenuCanvas.soundButton.CheckTouch(action, x, y)) {

				if (SoundManager.isON)
					SoundManager.SoundOff();
				else
					SoundManager.SoundOn();

			}

		} else // Game ------------------------
		{
			if (canvas.menuButton.CheckTouch(action, x, y)) // Show menu
			{
				// RenderManager.pause();
				goPause();

			} else 
			 			
			{
				canvas.tlButton.CheckTouch(action, x, y);
				if (canvas.tlButton.selected) {
					switch (cameraPos) {
					case 0:
						MoveBoat(GameEngine.getBoat().x, GameEngine.getBoat().y + 1);
						break;
					case 1:
						MoveBoat(GameEngine.getBoat().x, GameEngine.getBoat().y + 1);
						break;
					case 2:
						MoveBoat(GameEngine.getBoat().x - 1, GameEngine.getBoat().y);
						break;
					case 3:
						MoveBoat(GameEngine.getBoat().x + 1, GameEngine.getBoat().y);
						break;
					case 4:
						MoveBoat(GameEngine.getBoat().x, GameEngine.getBoat().y - 1);
						break;

					}
				}

				canvas.trButton.CheckTouch(action, x, y);
				if (canvas.trButton.selected) {

					switch (cameraPos) {
					case 0:
						MoveBoat(GameEngine.getBoat().x - 1, GameEngine.getBoat().y);
						break;
					case 1:
						MoveBoat(GameEngine.getBoat().x - 1, GameEngine.getBoat().y);
						break;
					case 2:
						MoveBoat(GameEngine.getBoat().x, GameEngine.getBoat().y - 1);
						break;
					case 3:
						MoveBoat(GameEngine.getBoat().x, GameEngine.getBoat().y + 1);
						break;
					case 4:
						MoveBoat(GameEngine.getBoat().x + 1, GameEngine.getBoat().y);
						break;

					}
				}

				canvas.blButton.CheckTouch(action, x, y);
				if (canvas.blButton.selected)

				{

					switch (cameraPos) {
					case 0:
						MoveBoat(GameEngine.getBoat().x + 1, GameEngine.getBoat().y);
						break;
					case 1:
						MoveBoat(GameEngine.getBoat().x + 1, GameEngine.getBoat().y);
						break;
					case 2:
						MoveBoat(GameEngine.getBoat().x, GameEngine.getBoat().y + 1);
						break;
					case 3:
						MoveBoat(GameEngine.getBoat().x, GameEngine.getBoat().y - 1);
						break;
					case 4:
						MoveBoat(GameEngine.getBoat().x - 1, GameEngine.getBoat().y);
						break;

					}
				}

				canvas.brButton.CheckTouch(action, x, y);
				if (canvas.brButton.selected)

				{

					switch (cameraPos) {
					case 0:
						MoveBoat(GameEngine.getBoat().x, GameEngine.getBoat().y - 1);
						break;
					case 1:
						MoveBoat(GameEngine.getBoat().x, GameEngine.getBoat().y - 1);
						break;
					case 2:
						MoveBoat(GameEngine.getBoat().x + 1, GameEngine.getBoat().y);
						break;
					case 3:
						MoveBoat(GameEngine.getBoat().x - 1, GameEngine.getBoat().y);
						break;
					case 4:
						MoveBoat(GameEngine.getBoat().x, GameEngine.getBoat().y + 1);
						break;

					}

				}
				// Panel touch move
				// ------------------------------------------------------------------------

				panel = PanelManager.getTouch(x, y);
				if (panel != null) {
					if (!panel.visible) {
						if (MoveBoat(panel.gameX, panel.gameY))
							panel.show();

					}

				}

			}

		}
		}
		else
		{
			if (canvas.nextButton.CheckTouch(action, x, y)) // Show menu
			{
				int nextScene = RenderManager.currentScene + 1;
				RenderManager.selectScene(nextScene);

			}
			else
				if (canvas.resetButton.CheckTouch(action, x, y)) // Show menu
				{
					RenderManager.selectScene(RenderManager.currentScene);
					
				}
				else
					if (canvas.selectLevelButton.CheckTouch(action, x, y)) // Show menu
					{
						RenderManager.selectScene(RenderManager.sceneMenu);
						RenderManager.menuCanvas.menuMode = 1;
						
					}
									
		}

	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		LevelCanvas canvas = (LevelCanvas) RenderManager.canvasView;
		
		if (canvas.viewMode != 5)
		{

		 switch (keyCode) {
		    case KeyEvent.KEYCODE_BUTTON_SELECT:		    
			   if (canvas.viewMode == 0 )  goPause();
			   else
			   {
					RenderManager.levelMenuCanvas.Hide();
					RenderManager.canvasView.viewMode = 0;
					RenderManager.lockRender = false;
			   }
				   
		    return true;
		    
		    case KeyEvent.KEYCODE_BUTTON_START:		    
		    	
		    	RenderManager.selectScene(RenderManager.sceneMenu);				  
		        return true;
		    
	        case KeyEvent.KEYCODE_DPAD_DOWN:
				switch (cameraPos) {
				case 0:
					MoveBoat(GameEngine.getBoat().x + 1, GameEngine.getBoat().y);
					break;
				case 1:
					MoveBoat(GameEngine.getBoat().x + 1, GameEngine.getBoat().y);
					break;
				case 2:
					MoveBoat(GameEngine.getBoat().x, GameEngine.getBoat().y + 1);
					break;
				case 3:
					MoveBoat(GameEngine.getBoat().x, GameEngine.getBoat().y - 1);
					break;
				case 4:
					MoveBoat(GameEngine.getBoat().x - 1, GameEngine.getBoat().y);
					break;
				}	        	
	            return true;
	        case KeyEvent.KEYCODE_DPAD_UP:
				switch (cameraPos) {
				case 0:
					MoveBoat(GameEngine.getBoat().x - 1, GameEngine.getBoat().y);
					break;
				case 1:
					MoveBoat(GameEngine.getBoat().x - 1, GameEngine.getBoat().y);
					break;
				case 2:
					MoveBoat(GameEngine.getBoat().x, GameEngine.getBoat().y - 1);
					break;
				case 3:
					MoveBoat(GameEngine.getBoat().x, GameEngine.getBoat().y + 1);
					break;
				case 4:
					MoveBoat(GameEngine.getBoat().x + 1, GameEngine.getBoat().y);
					break;

				}

	            return true;
	        case KeyEvent.KEYCODE_DPAD_LEFT:
				switch (cameraPos) {
				case 0:
					MoveBoat(GameEngine.getBoat().x, GameEngine.getBoat().y + 1);
					break;
				case 1:
					MoveBoat(GameEngine.getBoat().x, GameEngine.getBoat().y + 1);
					break;
				case 2:
					MoveBoat(GameEngine.getBoat().x - 1, GameEngine.getBoat().y);
					break;
				case 3:
					MoveBoat(GameEngine.getBoat().x + 1, GameEngine.getBoat().y);
					break;
				case 4:
					MoveBoat(GameEngine.getBoat().x, GameEngine.getBoat().y - 1);
					break;

				}

	            return true;
	        case KeyEvent.KEYCODE_DPAD_RIGHT:
				switch (cameraPos) {
				case 0:
					MoveBoat(GameEngine.getBoat().x, GameEngine.getBoat().y - 1);
					break;
				case 1:
					MoveBoat(GameEngine.getBoat().x, GameEngine.getBoat().y - 1);
					break;
				case 2:
					MoveBoat(GameEngine.getBoat().x + 1, GameEngine.getBoat().y);
					break;
				case 3:
					MoveBoat(GameEngine.getBoat().x - 1, GameEngine.getBoat().y);
					break;
				case 4:
					MoveBoat(GameEngine.getBoat().x, GameEngine.getBoat().y + 1);
					break;

				}
	        	

	            return true;
	            
	        case KeyEvent.KEYCODE_BUTTON_L1:
	        case KeyEvent.KEYCODE_BUTTON_R1:
	        	
	        	if (keyCode == KeyEvent.KEYCODE_BUTTON_L1) cameraPos--;
	        	else 
	        		cameraPos++;
	        	
	        
			
			if (cameraPos > 4)	cameraPos = 0;
			if (cameraPos < 0)	cameraPos = 4;
			
			checkJoyButtons(GameEngine.getBoat().x, GameEngine.getBoat().y);

			switch (cameraPos) {
			case 0:
				cameraMove.Reset(cameraMove.GetX(), cameraMove.GetY(), 0.1f, 0, 2.0f);
				cameraUp = 90;
				canvas.joyToFlat();

				break;
			case 1:
				cameraMove.Reset(cameraMove.GetX(), cameraMove.GetY(), cameraXZ, 0, 2.0f);
				cameraUp = 70;
				canvas.joyToFlat();

				break;
			case 2:
				cameraMove.Reset(cameraMove.GetX(), cameraMove.GetY(), cameraXZ, cameraXZ, 2.0f);
				cameraUp = 70;
				canvas.joyFromFlat();
				break;
			case 3:
				cameraMove.Reset(cameraMove.GetX(), cameraMove.GetY(), 0, -cameraXZ, 2.0f);
				cameraUp = 70;
				canvas.joyToFlat();
				break;
			case 4:
				cameraMove.Reset(cameraMove.GetX(), cameraMove.GetY(), -cameraXZ, cameraXZ, 2.0f);
				cameraUp = 70;
				canvas.joyFromFlat();
				break;
			}
			return true;
	        
	        default:
	            return false;
		 }
	    }
		else 
		{
			int nextScene = RenderManager.currentScene + 1;
			RenderManager.selectScene(nextScene);
			return true;
		}

	}	
	

}
