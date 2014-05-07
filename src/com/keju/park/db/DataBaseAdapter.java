/**
 * 
 */
package com.keju.park.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库操作类
 * 
 * @author Zhoujun 说明： 1、数据库操作类 2、定义好数据表名，数据列，数据表创建语句 3、操作表的方法紧随其后
 */
public class DataBaseAdapter {
	/**
	 * 数据库版本
	 */
	private static final int DATABASE_VERSION = 1;
	/**
	 * 数据库名称
	 */
	private static final String DATABASE_NAME = "park.db";
	/**
	 * 数据库表id
	 */
	public static final String RECORD_ID = "_id";

	private SQLiteDatabase db;
	private ReaderDbOpenHelper dbOpenHelper;

	public DataBaseAdapter(Context context) {
		this.dbOpenHelper = new ReaderDbOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void open() {
		this.db = dbOpenHelper.getWritableDatabase();
	}

	public void close() {
		if (db != null) {
			db.close();
		}
		if (dbOpenHelper != null) {
			dbOpenHelper.close();
		}
	}

	private class ReaderDbOpenHelper extends SQLiteOpenHelper {

		public ReaderDbOpenHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			// 创建表
			_db.execSQL(CREATE_SQL_SEARCH_HISTORY);
		}

		/**
		 * 升级应用时，有数据库改动在此方法中修改。
		 */
		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {

		}
	}

	/************************************************** 用户 ********************************************************/
	/**
	 * 搜索历史表名
	 */
	public static final String TABLE_NAME_SEARCH_HISTORY = "search_history";

	/**
	 * 搜索历史列定义
	 * 
	 */
	public interface HISTORY_Columns {
		public static final String NAME = "name";// 搜索历史名字
		public static final String LONGITUDE = "longitude";// 经度
		public static final String LATITUDE = " latitude";// 纬度

	}

	/**
	 * 搜索历史表查询列
	 */
	public static final String[] PROJECTION_SEARCH_HISTORY = new String[] { RECORD_ID, HISTORY_Columns.NAME,
			HISTORY_Columns.LONGITUDE, HISTORY_Columns.LATITUDE };

	/**
	 * 搜索历史表的创建语句
	 */
	public static final String CREATE_SQL_SEARCH_HISTORY = "create table " + TABLE_NAME_SEARCH_HISTORY + " ("
			+ RECORD_ID + " integer primary key autoincrement," + HISTORY_Columns.NAME + " text ,"
			+ HISTORY_Columns.LONGITUDE + " text ," + HISTORY_Columns.LATITUDE + " text " + ");";

	/**
	 * 插入数据
	 * 
	 * @param list
	 */
	public synchronized Boolean inserData(String bean) {
		SQLiteDatabase localDb = db;
		Boolean isInser = false;
		try {
			localDb.beginTransaction();
			// localDb.delete(TABLE_NAME_FOLLOW_UP_RECORD, null, null);
			// for (FollowUpRecordBean bean : list) {
			String sql = "insert into " + TABLE_NAME_SEARCH_HISTORY + " (" + HISTORY_Columns.NAME + ","
					+ HISTORY_Columns.LONGITUDE + "," + HISTORY_Columns.LATITUDE + "," + ") values(?,?,?)";
			localDb.execSQL(sql, new Object[] {});
			localDb.setTransactionSuccessful();
			isInser = true;
		} finally {
			localDb.endTransaction();
		}
		return isInser;
	}

	/**
	 * 判断数据库中的表是否为空数据
	 * 
	 * @param list
	 */
	public boolean tabbleIsExist(String tableName) {
		boolean is_true = false;
		if (tableName == null) {
			return false;
		}
		Cursor cursor = null;
		String sql = "select * from " + tableName + "";
		cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			is_true = true;
		}
		return is_true;
	}

	/**
	 * 清空表数据
	 * 
	 * @param list
	 */
	public boolean clearTableData(String tableName) {
		boolean is_crear = false;
		SQLiteDatabase localDb = db;
		if (tableName == null) {
			is_crear = false;
		}
		try {
			localDb.delete(tableName, null, null);
			is_crear = true;
		} finally {

		}
		return is_crear;
	}

}
