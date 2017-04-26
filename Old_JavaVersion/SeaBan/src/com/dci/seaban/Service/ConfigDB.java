package com.dci.seaban.Service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ConfigDB extends SQLiteOpenHelper {

	public ConfigDB(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);

	}

	
	
	private boolean lockDB = false;
	private final String configTableName = "CONFIG";
	private final String qualityKey = "QUALITY";
	private final String soundKey = "SOUND";
	private final String greenModeKey = "GREEN";
	private final String joysticModeKey = "JOYSTIC";
	

	private final String createQuery = "CREATE TABLE " + configTableName + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ soundKey + " INTEGER,"
			+ greenModeKey + " INTEGER,"
			+ joysticModeKey + " INTEGER,"
			+ qualityKey + " INTEGER);";

	
	public void init()
	{	
		try
		{
			GetQuality();
		}
		catch(Exception e)
		{
			try
			{
				SQLiteDatabase db = this.getWritableDatabase();
				db.execSQL(createQuery);
				db.close();
			}
			catch(Exception e2)
			{
				Log.e("SeaBan exception", "Config DB problem: " + e2.getMessage());
			}
		}
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(createQuery);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + configTableName);
		onCreate(db);
	}

	//---------------------------------------------------------------------------------------------
	public int GetQuality() {
						
		int quality = 2;
		
		if (lockDB) return quality;
		lockDB = true; 
		
		String query = "SELECT  " + qualityKey + " FROM " + configTableName;

		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.rawQuery(query, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			quality = cursor.getInt(0);
		}
		db.close();
		lockDB = false; 
		return quality;
	}

	public void SetQuality(int quality) {
		
		if (lockDB) return;
		lockDB = true; 
		
		String query = "SELECT * FROM " + configTableName;

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues value = new ContentValues();
		value.put(qualityKey, quality);

		Cursor cursor = db.rawQuery(query, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			db.update(configTableName, value, "id = ?", new String[] { cursor.getString(0) });
		} else {
			db.insert(configTableName, null, value);
		}
		db.close();

		lockDB = false;
	}
	//---------------------------------------------------------------------------------------------
	public int GetSound() {
		int sound = 1;
		
		if (lockDB) return sound;
		lockDB = true; 
		
		String query = "SELECT  " + soundKey + " FROM " + configTableName;

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(query, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			sound = cursor.getInt(0);
		}
		db.close();
		lockDB = false;
		return sound;
	}

	public void SetSound(int sound) {

		if (lockDB) return;
		lockDB = true; 
		
		if (sound > 1) sound = 1;
		if (sound < 0) sound = 0;

		String query = "SELECT * FROM " + configTableName;

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues value = new ContentValues();
		value.put(soundKey, sound);

		Cursor cursor = db.rawQuery(query, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			db.update(configTableName, value, "id = ?", new String[] { cursor.getString(0) });
		} else {
			db.insert(configTableName, null, value);
		}		
		db.close();
		lockDB = false;

	}
	
	//---------------------------------------------------------------------------------------------
	public int GetGreenMode() {
		int green = 1;
		
		if (lockDB) return green;
		lockDB = true; 
		
		String query = "SELECT  " + greenModeKey + " FROM " + configTableName;

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(query, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			green = cursor.getInt(0);
			GlobalVar.greenMode = green; 
		}
		db.close();
		lockDB = false;
		return green;
	}

	public void SetGreenMode(int greenMode) {

		if (lockDB) return;
		lockDB = true; 
		
		if (greenMode > 1) greenMode = 1;
		if (greenMode < 0) greenMode = 0;

		String query = "SELECT * FROM " + configTableName;

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues value = new ContentValues();
		value.put(greenModeKey, greenMode);

		Cursor cursor = db.rawQuery(query, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			db.update(configTableName, value, "id = ?", new String[] { cursor.getString(0) });
		} else {
			db.insert(configTableName, null, value);
		}
		GlobalVar.greenMode = greenMode;
		db.close();
		lockDB = false;
	}
	
	//---------------------------------------------------------------------------------------------
	public int GetJoysticMode() {
		int joystic = 1;
		String query = "SELECT  " + joysticModeKey + " FROM " + configTableName;

		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(query, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			joystic = cursor.getInt(0);
		}
		db.close();
		return joystic;
	}

	public void SetJoysticMode(int joysticMode) {
		
		if (joysticMode > 1) joysticMode = 1;
		if (joysticMode < 0) joysticMode = 0;

		String query = "SELECT * FROM " + configTableName;

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues value = new ContentValues();
		value.put(joysticModeKey, joysticMode);

		Cursor cursor = db.rawQuery(query, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			db.update(configTableName, value, "id = ?", new String[] { cursor.getString(0) });
		} else {
			db.insert(configTableName, null, value);
		}
		db.close();

	}
	

}
