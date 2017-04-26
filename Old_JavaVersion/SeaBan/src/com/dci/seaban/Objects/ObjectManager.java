package com.dci.seaban.Objects;

import java.util.ArrayList;
import java.util.List;

import com.dci.seaban.Render.SceneRender;

import android.content.Context;
import android.opengl.GLES20;

public final class ObjectManager {
	private static final List<SceneObject> sceneObject = new ArrayList<SceneObject>();
	
	public static final void clear()
	{
		sceneObject.clear();
	}
	
	public static final SceneObject get(int index)
	{		
		if (index > sceneObject.size()) return null;
		else 
			return sceneObject.get(index); 
	}
	
	public static final int getIndexByObject(SceneObject sObject)
	{		
		
			for (int i=0; i < sceneObject.size(); i++)
			{
		 	  if (sceneObject.get(i) == sObject) return i;
			}
			
			return -1;
	}
	
	
	public static final void del(int index)
	{		
		if (index > sceneObject.size()) return;
		else 
			sceneObject.remove(index); 
	}
	
	
	public static final int getCount()
	{
		return sceneObject.size();
	}
	
	public static final void add(SceneObject object){
		sceneObject.add(object);
	}

	
}
