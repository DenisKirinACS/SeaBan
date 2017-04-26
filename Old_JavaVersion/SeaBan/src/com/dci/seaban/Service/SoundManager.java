package com.dci.seaban.Service;

import java.util.ArrayList;
import java.util.List;




public class SoundManager {

	private static final List<SoundItem> soundItems = new ArrayList<SoundItem>();
	public static boolean isON = true;  	
	public static boolean isPause = false;
	
	public static void Add(String Name, int SoundID ){

		//for (int i = 0; i < soundItems.size(); i ++)
		//{
		//	SoundItem soundItem = soundItems.get(i);			
		//}
		
		for (SoundItem soundItem: soundItems){
			if (soundItem.Name.equals(Name)) return;			
		}
		
		SoundItem soundItem=new SoundItem();
		soundItem.Name=Name;
		soundItem.SoundID=SoundID;
		soundItem.StreamID=-1;
		soundItems.add(soundItem);
				
		}
	
	public static void Play(String Name, float ValumeR,float ValumeL,boolean Loop)
	{
		
		int intLoop = 0;
		if (Loop) intLoop = -1;
		
		for (SoundItem soundItem: soundItems)
		{
			if (soundItem.Name.equals(Name)) 
			{
				soundItem.setValume(ValumeR, ValumeL);
				soundItem.Loop = Loop;
				
				if ((isON)&&(!isPause))
				{
					if (soundItem.StreamID==-1)
					{
						soundItem.StreamID = Sound.soundPool.play(soundItem.SoundID, soundItem.getVolumeL(), soundItem.getVolumeR(), 0, intLoop, 1.0f);
					}
					else
					{
						if (soundItem.Loop)
						{
							SetVolume(Name, ValumeL, ValumeR);
							Sound.soundPool.resume(soundItem.StreamID);
						}
						else
						{
					         
							 Sound.soundPool.stop(soundItem.StreamID);
							 soundItem.StreamID = Sound.soundPool.play(soundItem.SoundID, soundItem.getVolumeL(), soundItem.getVolumeR(), 0, intLoop, 1.0f);
							 
						}
					}					
				}
				else 
				{
					if (soundItem.StreamID==-1)
					{
						soundItem.StreamID = Sound.soundPool.play(soundItem.StreamID , 0.0f, 0.0f, 0, intLoop, 1.0f);
						if (isPause) Sound.soundPool.pause(soundItem.StreamID );
					}
					else
					{
						Sound.soundPool.setVolume(soundItem.StreamID , 0.0f, 0.0f);
						if (isPause) Sound.soundPool.pause(soundItem.StreamID );
						else						
						 Sound.soundPool.resume(soundItem.StreamID );
					}										
					
				}
			}			
		}
				
	}
	
	public static void Clear()
	{
		for (SoundItem soundItem: soundItems)
		{
			Sound.soundPool.stop(soundItem.StreamID);			
			
		}
		
		soundItems.clear();
	}
	//------------------------------------------------------------------------------------------------------------------------------
	public static void SetRate(String name, float rate){
		for (SoundItem soundItem: soundItems)
		{
			if (soundItem.Name.equals(name)) 
			{
				Sound.soundPool.setRate(soundItem.StreamID, rate);
			}
		}
	}

	public static void SetVolume(String name, float volumeLeft, float volumeRight){
		for (SoundItem soundItem: soundItems)
		{
			if (soundItem.Name.equals(name)) 
			{
				soundItem.setValume(volumeRight, volumeLeft);
				if ((isON)&&(!isPause))
				{
				 Sound.soundPool.setVolume(soundItem.StreamID, volumeLeft, volumeRight);
				}
				else 
				{
					Sound.soundPool.setVolume(soundItem.StreamID, 0.0f,  0.0f);
				}
			}
		}
	}
	
	//------------------------------------------------------------------------------------------------------------------------------
	public static void SoundOff(){
		isON = false;
		for (SoundItem soundItem: soundItems)
		{ 
		   if (soundItem.StreamID!=-1)
			{
			   	Sound.soundPool.setVolume(soundItem.StreamID, 0.0f, 0.0f);
				Sound.soundPool.resume(soundItem.StreamID);
			}								
	    }
	}
	
	public static void SoundOn(){
		isON = true;
		for (SoundItem soundItem: soundItems)
		{ 
		   if (soundItem.StreamID!=-1)
			{
				Sound.soundPool.setVolume(soundItem.StreamID, soundItem.getVolumeL(), soundItem.getVolumeR());
				Sound.soundPool.resume(soundItem.StreamID);
			}								
	    }
	}
	
	public static void Pause(){
		isPause = true;
		for (SoundItem soundItem: soundItems)
		{ 
		   if (soundItem.StreamID!=-1)
			{
			   
				Sound.soundPool.pause(soundItem.StreamID);
			}								
	    }
	}
	
	public static void Resume(){
		isPause = false;
		for (SoundItem soundItem: soundItems)
		{ 
		   if (soundItem.StreamID!=-1)
			{
			   
				Sound.soundPool.resume(soundItem.StreamID);
			}								
	    }
	}
	
	
}
