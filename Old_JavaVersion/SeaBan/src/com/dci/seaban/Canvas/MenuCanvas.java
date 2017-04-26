package com.dci.seaban.Canvas;

import com.dci.seaban.R;
import com.dci.seaban.Render.RenderManager;
import com.dci.seaban.Render.SceneRender;
import com.dci.seaban.Service.BitmapManager;
import com.dci.seaban.Service.GlobalVar;
import com.dci.seaban.Service.ConfigDB;
import com.dci.seaban.Service.LevelTableRaw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.View;

public class MenuCanvas extends View {

	protected Paint paint = new Paint();

	private Context context;

//	private Bitmap back;

	private MainMenuButton playButton;
	private MainMenuButton optionButton;
	private MainMenuButton res1Button;
	private MainMenuButton res2Button;

	private LevelButton[][] levelButtons;

	private MainMenuCheckButton GLMode1Button;
	private MainMenuCheckButton GLMode2Button;
	private MainMenuCheckButton GLMode3Button;
	private MainMenuCheckButton GreenModeButton;
	private MainMenuCheckButton SoundButton;
//	private MainMenuCheckButton JoyTypeButton;

	private Button backButton;

	public int menuMode = 0;

	@SuppressLint("NewApi")
	public MenuCanvas(Context context) {
		super(context); // context is activity

		this.context = context;
		
		Typeface tf = Typeface.createFromAsset(RenderManager.context.getAssets(), "fonts/comic.ttf");			
		paint.setTypeface(tf);		
		

		setMinimumWidth(RenderManager.metrics.widthPixels);
		setMinimumHeight(RenderManager.metrics.heightPixels);

		setFocusable(false);

	//	back = BitmapFactory.decodeResource(getResources(), R.drawable.menuback);

		playButton = new MainMenuButton(context, "", R.drawable.playoff, R.drawable.playon, 50, 100, 170 * 3, 96 * 3, 35);
		optionButton = new MainMenuButton(context, "", R.drawable.optionsoff, R.drawable.optionson, 50, 700, 170 * 3, 96 * 3, 35);
		res2Button = new MainMenuButton(context, "Res1", R.drawable.levelmenubutton, R.drawable.levelbuttonsel, 0, 0, 0, 0, 52);
		res2Button.visible = false;
		res1Button = new MainMenuButton(context, "Res2", R.drawable.levelmenubutton, R.drawable.levelbuttonsel, 0, 0, 0, 0, 44);
		res1Button.visible = false;
		

		GLMode1Button = new MainMenuCheckButton(context, "", R.drawable.camoff, R.drawable.camon, R.drawable.camon, 240, 110, 200, 200, 25);
		GLMode1Button.visible = false;
		GLMode2Button = new MainMenuCheckButton(context, "", R.drawable.camoff, R.drawable.camon, R.drawable.camon, 540, 110, 200, 200, 25);
		GLMode2Button.visible = false;
		GLMode3Button = new MainMenuCheckButton(context, "", R.drawable.camoff, R.drawable.camon, R.drawable.camon, 840, 110, 200, 200, 25);
		GLMode3Button.visible = false;
		
		setGLModeButtons();
		
		GreenModeButton = new MainMenuCheckButton(context, "", R.drawable.greenoff, R.drawable.greenon, R.drawable.greenon, 240, 410, 200, 200, 25);
		GreenModeButton.visible = false;
		
		if (RenderManager.db.GetGreenMode() == 1) GreenModeButton.check = true;
		
		SoundButton = new MainMenuCheckButton(context, "", R.drawable.speakoff, R.drawable.speackon, R.drawable.speackon, 240, 620, 200, 200, 25);
		SoundButton.visible = false;
		
		if (RenderManager.db.GetSound() == 1) SoundButton.check = true;   
		
		//JoyTypeButton = new MainMenuCheckButton(context, "Joystick", R.drawable.optionbutton, R.drawable.optionbuttonsel, R.drawable.playbutton, 540, 620, 200, 200, 25);
		//JoyTypeButton.visible = false;
		
		//if (RenderManager.db.GetJoysticMode() == 1) JoyTypeButton.check = true;   
				
		backButton = new Button(context, R.drawable.back, R.drawable.back, 1920 - 200, 0, 200, 200);
		backButton.visible = false;

		createLevelButtons();
	}

	protected void setGLModeButtons(){
		GLMode1Button.check = false;
		GLMode2Button.check = false;
		GLMode3Button.check = false;
		
		switch (RenderManager.db.GetQuality())
		{
		   case 0: GLMode1Button.check = true;break;
		   case 1: GLMode2Button.check = true;break;
		   default:
		   GLMode3Button.check = true;
		}
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(getSuggestedMinimumWidth(), getSuggestedMinimumHeight());
	}

	@Override
	protected void onDraw(Canvas canvas) {

		
		super.onDraw(canvas);
		
		

		GlobalVar.drawScaleBitmap(canvas, BitmapManager.getBitmap(context, R.drawable.info), 1920 / 2 - 1200 / 2, 1080 / 2 - 800 / 2, 1200, 800);

		hideMainMenuButtons();
		hideLevelButtons();
		hideSettingsButtons();
		backButton.visible = false;

		switch (menuMode) {
		case 0:
			showMainMenuButtons();
			playButton.Draw(canvas);							
			optionButton.Draw(canvas);
			res1Button.Draw(canvas);
			res2Button.Draw(canvas);
			break;

		case 1:
			backButton.visible = true;
			backButton.Draw(canvas);
			showLevelButtons();
			drawLevelButtons(canvas);

			break;

		case 2:
			backButton.visible = true;
			backButton.Draw(canvas);
			showSettingsButtons();

			GLMode1Button.Draw(canvas);
			GLMode2Button.Draw(canvas);
			GLMode3Button.Draw(canvas);			
			GreenModeButton.Draw(canvas);
			SoundButton.Draw(canvas);
			//JoyTypeButton.Draw(canvas);

			break;

		}

	//	postInvalidate();

	}

	public void onTouch(int action, int x, int y) {
		

		if (playButton.CheckTouch(action, x, y)) 
		{
		   menuMode = 1;
		   RefreshLevelButton();
		} 
		else 
		if (optionButton.CheckTouch(action, x, y)) 
		{
			menuMode = 2;
		} 
		else 
		if (GLMode1Button.CheckTouch(action, x, y)) 
		{
		
			RenderManager.db.SetQuality(0);
			setGLModeButtons();	
			

		} 
		else 
		if (GLMode2Button.CheckTouch(action, x, y)) 
		{
		
			RenderManager.db.SetQuality(1);
			setGLModeButtons();
		}
		else 
		if (GLMode3Button.CheckTouch(action, x, y)) 
		{
			
			RenderManager.db.SetQuality(2);
			setGLModeButtons();
		}
		else 
		if (SoundButton.CheckTouch(action, x, y)) 
		{
			if (SoundButton.check) RenderManager.db.SetSound(0);
			else 
				RenderManager.db.SetSound(1);
			
			if (RenderManager.db.GetSound() == 0) SoundButton.check = false;
			else 
				SoundButton.check = true;
		}
		else 
		if (GreenModeButton.CheckTouch(action, x, y)) 
		{
			if (GreenModeButton.check) RenderManager.db.SetGreenMode(0);
			else 
				RenderManager.db.SetGreenMode(1);
			
			if (RenderManager.db.GetGreenMode() == 0) GreenModeButton.check = false;
			else 
				GreenModeButton.check = true;
		}
		else 
			/*
		if (JoyTypeButton.CheckTouch(action, x, y)) 
		{
			if (GreenModeButton.check) RenderManager.db.SetJoysticMode(0);
			else 
				RenderManager.db.SetJoysticMode(1);
			
			if (RenderManager.db.GetJoysticMode() == 0) JoyTypeButton.check = false;
			else 
				JoyTypeButton.check = true;
		} 								
		else 
		*/		
		if (backButton.CheckTouch(action, x, y)) 
		{
			menuMode = 0;
		} 
		else 
		{			
			res1Button.CheckTouch(action, x, y);
			res2Button.CheckTouch(action, x, y);

			touchLevelBUttons(action, x, y);
		}

		postInvalidate();

	}

	// ------------------------------------------------------------------------------------------------------
	public void RefreshLevelButton() {
		
		for (int x = 0; x < 5; x++)
			for (int y = 0; y < 4; y++) {
				levelButtons[x][y].levelTableRaw = RenderManager.levelDB.GetRaw(y * 5 + x + 1);
				
				
			}
		
	}
	
	private void createLevelButtons() {
		levelButtons = new LevelButton[5][4];
		
		LevelTableRaw levelTableRaw = new LevelTableRaw();
		

	    if (RenderManager.levelDB.GetRaw(1) == null)
	    {
		
	       for (int i=0; i < 20; i++){
	       	levelTableRaw.levelNumber = i + 1;
	       	RenderManager.levelDB.SetRaw(levelTableRaw);        	
	       }
	    
	       levelTableRaw.levelNumber = 1;
	       levelTableRaw.lock = 0;
	       
	       RenderManager.levelDB.SetRaw(levelTableRaw);
	    }
	       
	       /*
	       
	       levelTableRaw.levelNumber = 2;
	       levelTableRaw.stars = 1;
	       levelDB.SetRaw(levelTableRaw);
	       
	       levelTableRaw.levelNumber = 3;
	       levelTableRaw.stars = 3;
	       levelDB.SetRaw(levelTableRaw);
	       
	       levelTableRaw.levelNumber = 4;
	       levelTableRaw.lock = 0;
	       levelDB.SetRaw(levelTableRaw);
	       */
		
		
		for (int x = 0; x < 5; x++)
			for (int y = 0; y < 4; y++) 
			{
				levelTableRaw = RenderManager.levelDB.GetRaw(y * 5 + x + 1);
				if (levelTableRaw != null)
				{
					levelButtons[x][y] = new LevelButton(context, levelTableRaw, x * 240 + 200, y * 240 + 20, 220, 220);
					levelButtons[x][y].visible = false;
				}
				else 
				{
					levelTableRaw = new LevelTableRaw();
					levelTableRaw.levelNumber = -1;
					levelButtons[x][y] = new LevelButton(context, levelTableRaw, x * 240 + 200, y * 240 + 20, 220, 220);
					levelButtons[x][y].visible = false;
					
				}

			}
	
	}

	private void drawLevelButtons(Canvas canvas) {
		for (int x = 0; x < 5; x++)
			for (int y = 0; y < 4; y++) {
				levelButtons[x][y].Draw(canvas);
			}
		
		

	}

	private void touchLevelBUttons(int action, int xt, int yt) {
		for (int x = 0; x < 5; x++)
			for (int y = 0; y < 4; y++) {
				if (levelButtons[x][y].CheckTouch(action, xt, yt))
				if (levelButtons[x][y].levelTableRaw.lock == 0)
				{
					RenderManager.selectScene((x + 3) + y * 5);
					postInvalidate();
					return;
				}
			}

	}

	private void showMainMenuButtons() {
		playButton.visible = true;
		optionButton.visible = true;
		res1Button.visible = true;
		res2Button.visible = true;
	}

	private void hideMainMenuButtons() {
		playButton.visible = false;
		optionButton.visible = false;
		res1Button.visible = false;
		res2Button.visible = false;
	}

	private void showSettingsButtons() {
		GLMode1Button.visible = true;
		GLMode2Button.visible = true;
		GLMode3Button.visible = true;
		GreenModeButton.visible = true;
		SoundButton.visible = true;
		//JoyTypeButton.visible = true;		
	}

	private void hideSettingsButtons() {
		GLMode1Button.visible = false;
		GLMode2Button.visible = false;
		GLMode3Button.visible = false;
		GreenModeButton.visible = false;
		SoundButton.visible = false;
		//JoyTypeButton.visible = false;
	}

	private void showLevelButtons() {
		for (int x = 0; x < 5; x++)
			for (int y = 0; y < 4; y++) {
				levelButtons[x][y].visible = true;
			}

	}

	private void hideLevelButtons() {
		for (int x = 0; x < 5; x++)
			for (int y = 0; y < 4; y++) {
				levelButtons[x][y].visible = false;
			}
	}

	public void back() {
		menuMode = 0;
		postInvalidate();

	}

}
