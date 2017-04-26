package com.dci.seaban.Service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public  class LevelDB extends SQLiteOpenHelper {
	
	public LevelDB(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		 
	}

	private final String levelTableName = "LEVELTABLE";
	
	
	private final String createQuery = 
			"CREATE TABLE "
		            + levelTableName + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,"
		            + "levelNumber INTEGER,"
		            + "lock INTEGER,"
		            + "score INTEGER,"
		            + "stars INTEGER);";
		            
	
	public void init()
	{	
		try
		{
			GetRaw(1);
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
				Log.e("SeaBan exception", "Level DB problem: " + e2.getMessage());
			}
		}
	}
	
               
	@Override
    public void onCreate(SQLiteDatabase db) {
    	
       
       db.execSQL(createQuery);
       
       
    }

	public void dropTable(){
		SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + levelTableName);		
	}
	
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	dropTable();
        onCreate(db);
    }	
    
    public LevelTableRaw GetRaw(int levelNumber){
    	
    	 LevelTableRaw levelTableRaw = null;    	 
    		 
    	 
    	 String query = "SELECT * FROM " + levelTableName + " WHERE levelNumber = " + String.valueOf(levelNumber) + ";" ;    	 
    	 SQLiteDatabase db = this.getReadableDatabase();    	     	 
    	 Cursor cursor = db.rawQuery(query, null);
    	     	 
    	 if (cursor.getCount() > 0)
    	 {
    		 
    		 levelTableRaw = new LevelTableRaw();
    		 cursor.moveToFirst();
    		 
    		 levelTableRaw.levelNumber = levelNumber;
    		 levelTableRaw.lock = cursor.getInt(cursor.getColumnIndex("lock"));
    		 levelTableRaw.score = cursor.getInt(cursor.getColumnIndex("score"));
    		 levelTableRaw.stars = cursor.getInt(cursor.getColumnIndex("stars"));
    	 }
    	     	 
    	 db.close();
    	 
    	 return levelTableRaw;
    }
    
    public void SetRaw(LevelTableRaw levelTableRaw){
   	 
    	String query = "SELECT * FROM " + levelTableName + " WHERE levelNumber = " + String.valueOf(levelTableRaw.levelNumber) + ";" ;
   	 
   	     SQLiteDatabase db = this.getWritableDatabase();
   	 
   	      ContentValues value = new ContentValues();   	      
   	      value.put("lock", levelTableRaw.lock);
   	      value.put("score", levelTableRaw.score);
   	      value.put("stars", levelTableRaw.stars);

   	      Cursor cursor = db.rawQuery(query, null);
   	      if (cursor.getCount() > 0)
   	      {
   	    	  cursor.moveToFirst();
   	    	  db.update(levelTableName, value, "id = ?" ,  new String[] { cursor.getString(0) });
   	      }
   	      else 
   	      {
   	    	  value.put("levelNumber", levelTableRaw.levelNumber);
   	    	  db.insert(levelTableName, null, value);
   	      }
   	      db.close();
   	 
   }
    
}
