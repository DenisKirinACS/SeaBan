package com.dci.seaban.Service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.dci.seaban.Objects.ShaderCompiller;
import com.dci.seaban.Render.RenderManager;

public class ShaderManager {
	
	private static final List<ShaderCompiller> shaderItems = new ArrayList<ShaderCompiller>();
	
	public static void addShader(Context context, String name){ // Quality 1, 0
		
		if (getShader(name) != null) return;
		
		
		
		String fileName = "";
		
		/*
		switch (RenderManager.db.GetQuality()) {
		 case 0:
		 case 1:
			    fileName = name + "_low"; 
			    break;
		 case 2: fileName = name; 
		        break;
		}
		*/
		//Temporary not use LOW shaders
		fileName = name;
		 
		ShaderCompiller shader = new ShaderCompiller(context, name, fileName);
		
		shaderItems.add(shader);
		
	}
	
	public static void clear(){
		shaderItems.clear();
	}
	
	public static ShaderCompiller getShader(String shaderName){
		for (ShaderCompiller shaderItem: shaderItems)
		{
			if (shaderItem.shaderName.toString().compareTo(shaderName.toString()) == 0 )
			{
				return shaderItem;
			}			
		}
       return null;
	}
}
