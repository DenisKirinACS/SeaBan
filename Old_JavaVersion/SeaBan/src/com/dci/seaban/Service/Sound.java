package com.dci.seaban.Service;

import com.dci.seaban.R;
import com.dci.seaban.R.raw;
import com.dci.seaban.Render.SceneRender;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.SystemClock;

public final class Sound {

	public static SoundPool soundPool;
	private static boolean playSound = true;
	public static SceneRender render; 
	

	
	public static int diselonSoundID;
	public static int buttonSoundID;
	public static int introSoundID;
	
	

	private static long lastPlay;
	
	public static void init(Activity activity)
	{
		soundPool  = new SoundPool(10, AudioManager.STREAM_MUSIC, 0); //see first arg - maxStream
		diselonSoundID = soundPool.load(activity, R.raw.diselon_mp3, 0);
		buttonSoundID = soundPool.load(activity, R.raw.button_mp3, 0);
		introSoundID = soundPool.load(activity, R.raw.intro3_mp3, 0);
		 
			
		
		
		//soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {			
		//	@Override
		//	public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
		//		Intent intent = new Intent(MainActivity.this, MainActivity.class);
		//		startActivity(intent);
				
		//	}
		//}); 			
	}
	
	   public static void playSound(int id, float x, float z, float vol, boolean loop){
		   
		   
		   
			float xp = x;
			float zp = z;
			
			/*
			if (render != null) 
		   if (render.boat != null){
				 xp -=  render.boat.x;
				zp -=  render.boat.z;							  
		   }
		   */
			   
		   if (playSound){
			   
			   
			float toBoat = (float)Math.sqrt( (xp * xp) + (zp * zp));
			
			if (toBoat < 100)
			{
				toBoat = 1.0f - (toBoat / 100.0f);
				if ( vol > 1.0f) vol = 1.0f;
				//if ( vol == 0) vol = toBoat;
				vol = (vol * toBoat) / 2.0f;
				
				
				double tempAlpha =  Math.PI + Math.atan2(zp, xp);
		   
				float soundR = (float)(vol * Math.sin(tempAlpha)); //-1..+1 -> left right 
				
				float volLeft, volRight;
				if (soundR > 0)
				{
					volLeft = 1.0f - soundR;
					volRight = soundR;
				}
				else 
				{
					volLeft = -soundR;
					volRight = 1 + soundR;				
				}
				
				volLeft = (volLeft * vol) / 2;
				volRight = (volRight * vol) / 2;

				  if ((volLeft < 0.05f) && (volRight < 0.05f)) return;
				
				   if ((SystemClock.uptimeMillis() - lastPlay) < 50) return;
				   lastPlay = SystemClock.uptimeMillis();
				
				
			   if (loop) soundPool.play(id, volLeft, volRight, 0, -1, 4.0f);
			   else 
				   soundPool.play(id, volLeft, volRight, 0, 0, 0.4f);
			}
		   }
	   }
	   
	   public static void setPlaySound(boolean flag){
		   playSound = flag;
	   }
	   
	   public static boolean getPlaySound(){
		   return playSound;
	   }
	   
	   @Override
	   protected void finalize() throws Throwable {
		   soundPool.release();
		super.finalize();
	   }
	   
	
}
