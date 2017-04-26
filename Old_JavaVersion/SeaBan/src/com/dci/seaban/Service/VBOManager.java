package com.dci.seaban.Service;

import java.util.ArrayList;
import java.util.List;

import android.opengl.GLES20;

public final class VBOManager {
	private static final List<VBOItem> VBOItems = new ArrayList<VBOItem>();
	
	public static final void clearVBO()
	{
		VBOItems.clear();
	}
	
	public static final int[] getVBO(String name)
	{		
		for (VBOItem vboItem: VBOItems)
		{
			if (vboItem.name.toString().compareTo(name.toString()) == 0)
			{
				return vboItem.VBO;
			}	
		}
		
		VBOItem newVBOItem = new VBOItem(name);
		GLES20.glGenBuffers(3, newVBOItem.VBO, 0);
		VBOItems.add(newVBOItem);		
		return newVBOItem.VBO;
	}
	
	public static final int getVBOIndex(String name)
	{
		int count = 0;
		for (VBOItem vboItem: VBOItems)
		{
			if (vboItem.name.toString().compareTo(name.toString()) == 0 )
			{
				return count;
			}
			count++;
		}
		
		return -1;
	}
	
	public static final void setSize(String name, int size)
	{
	getVBO(name);
	for (VBOItem vboItem: VBOItems)
	{		
		if (vboItem.name.toString().compareTo(name.toString()) == 0 )
		{
			vboItem.size = size;
			break; 
		}		
	}
	}

	public static final int getSize(String name)
	{
	for (VBOItem vboItem: VBOItems)
	{
		if (vboItem.name.toString().compareTo(name.toString()) == 0 )
		{			
			return vboItem.size;
		}		
	}
	return 0;
	}
	
	
}
