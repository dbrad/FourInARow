/** class: FIARData
 * 	desc: DB handling class.
 * 
 */
package com.bteam.fiar;

import static android.provider.BaseColumns._ID;
import static com.bteam.fiar.Constants.*;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FIARData extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "fiardb.db";
	private static final int DATABASE_VERSION = 1;

	public FIARData(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + HS_TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + HS_NAME + " TEXT NOT NULL," + HS_SCORE + " INTEGER NOT NULL);");

		db.execSQL("CREATE TABLE " + USR_TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USR_NAME + " TEXT NOT NULL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + HS_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + USR_TABLE_NAME);
		onCreate(db);
	}
}