package com.dci.seaban.Service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;



public final class BitmapManager {
	private static final List<BitmapItem> bitmaps = new ArrayList<BitmapItem>();
	
	public static final void clearTextures()
	{
		bitmaps.clear();
	}
	
	public static final Bitmap getBitmap(Context context, int resID )
	{	
				
		for (BitmapItem bitmap: bitmaps)
		{
			if (bitmap.resID == resID)
			{
				return bitmap.image;
			}	
		}

		BitmapItem bitmap = new BitmapItem();
		if (context != null)
		{

			bitmap.image = BitmapFactory.decodeResource(context.getResources(), resID);
			bitmap.resID = resID;
			
			bitmaps.add(bitmap);
		}
		
		return bitmap.image;
	}
			
	
}
