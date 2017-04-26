package com.dci.seaban.Service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.opengl.GLES20;

public final class TextureManager {
	private static final List<TextureItem> TextureItems = new ArrayList<TextureItem>();
	
	public static final void clearTextures()
	{
		for (TextureItem textItem: TextureItems)
		{
			textItem = null;
		}
		
		TextureItems.clear();
	}
	
	public static final int getTextureHandle(Context context, int resID )
	{		
		for (TextureItem textItem: TextureItems)
		{
			if (textItem.resID == resID)
			{
				return textItem.handle;
			}	
		}
		
		TextureItem newTextItem = new TextureItem(resID);
		if (context != null)
		{		  
		  newTextItem.handle = Texture.loadTexture(context, resID);		
		  TextureItems.add(newTextItem);
		}
		
		return newTextItem.handle;
	}
			
	
}
