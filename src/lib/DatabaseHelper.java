package lib;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

	public class DatabaseHelper extends SQLiteOpenHelper 
	{
		private static final String DATABASE = "database.db";
		
		private static final int DATABASEVERSION = 1;
		
		private SQLiteDatabase db;
		
		public DatabaseHelper(Context context)
		{
			super(context, DATABASE, null, DATABASEVERSION);
			db = this.getWritableDatabase();
		}
		
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			String DATABASE_CREATE_TABLE = "CREATE TABLE headache_record (_ID INTEGER PRIMARY KEY, " +
					" record_date TEXT, " +
					" patient_name TEXT, " +
					" patient_id TEXT, " +
					" headache_level_time_morning TEXT, " +
					" headache_level_time_afternoon TEXT, " +
					" headache_level_time_night TEXT, " +
					" headache_level_time_sleep TEXT, " +
					" headache_symptom_1 TEXT, " +
					" headache_symptom_2 TEXT, " +
					" headache_symptom_3 TEXT, " +
					" headache_symptom_4 TEXT," +
					" headache_symptom_5 TEXT," +
					" headache_symptom_6 TEXT, " +
					" headache_symptom_7 TEXT," +
					" headache_sign_1 TEXT," +
					" headache_sign_2 TEXT," +
					" headache_hours TEXT," +
					" headache_medicine_1 TEXT," +
					" headache_useful_option_1 TEXT," +
					" headache_useful_option_2 TEXT," +
					" headache_useful_option_3 TEXT," +
					" headache_useful_option_4 TEXT," +
					" headache_period TEXT)";
			
			db.execSQL(DATABASE_CREATE_TABLE);		
			
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			db.execSQL("DROP TABLE IF EXISTS headache_record");	
			onCreate(db);
		}
		
		public Cursor getAll(String tableName, String whereStr) 
		{
			//Log.d("DB Query", "SELECT * FROM " + tableName + whereStr);
		    return db.rawQuery("SELECT * FROM " + tableName + whereStr, null);
		}
		
		public long insert(String tableName, String[] column, String[] value) 
		{
			ContentValues args = new ContentValues();
			for(int i = 0; i < column.length; i++)
			{
				args.put(column[i], value[i]);	
			}
	 
			return db.insert(tableName, null, args);
	    }
		
		public int delete(String tableName, long rowId) 
		{
			return db.delete(tableName,		
			"_ID = " + rowId,				
			null							
			);
		}
		
		public int update(String tableName, long rowId, String[] column, String[] value) 
		{
			ContentValues args = new ContentValues();
			for(int i = 0; i < column.length; i++)
			{
				args.put(column[i], value[i]);	
			}
			return db.update(tableName,	
			args,						
			"_ID=" + rowId,				
			null						
			);
		}
	}
