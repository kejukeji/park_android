/**
 * 
 */
package com.parkmecn.android.db;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.parkmecn.android.bean.FuzzyQueryBean;

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
		public static final String CITY = "city";// 搜索历史城市名或省名字
		public static final String ADDRESS = "address";// 详细地址
		public static final String FullNAME = "full_name";// 全称
		public static final String LONGITUDE = "longitude";// 经度
		public static final String LATITUDE = "latitude";// 纬度

	}

	/**
	 * 搜索历史表查询列
	 */
	public static final String[] PROJECTION_SEARCH_HISTORY = new String[] { RECORD_ID, HISTORY_Columns.CITY,
			HISTORY_Columns.ADDRESS, HISTORY_Columns.FullNAME, HISTORY_Columns.LONGITUDE, HISTORY_Columns.LATITUDE };

	/**
	 * 搜索历史表的创建语句
	 */
	public static final String CREATE_SQL_SEARCH_HISTORY = "create table " + TABLE_NAME_SEARCH_HISTORY + " ("
			+ RECORD_ID + " integer primary key autoincrement," + HISTORY_Columns.CITY + " text ,"
			+ HISTORY_Columns.ADDRESS + " text ," + HISTORY_Columns.FullNAME + " text ," + HISTORY_Columns.LONGITUDE
			+ " text ," + HISTORY_Columns.LATITUDE + " text " + ");";

	/**
	 * 插入用户搜索的历史数据
	 * 
	 * @param list
	 */
	public void inserData(String city, String address, String fullName, String Longitude, String Latitude) {
		SQLiteDatabase localDb = db;
		try {
			localDb.beginTransaction();
			// localDb.delete(TABLE_NAME_FOLLOW_UP_RECORD, null, null);
			// for (FollowUpRecordBean bean : list) {
			String sql = "insert into " + TABLE_NAME_SEARCH_HISTORY + " (" + HISTORY_Columns.CITY + ","
					+ HISTORY_Columns.ADDRESS + "," + HISTORY_Columns.FullNAME + "," + HISTORY_Columns.LONGITUDE + ","
					+ HISTORY_Columns.LATITUDE + ") values(?,?,?,?,?)";
			localDb.execSQL(sql, new Object[] { city, address, fullName, Longitude, Latitude });
			localDb.setTransactionSuccessful();
		} finally {
			localDb.endTransaction();
		}
	}

	/**
	 * 查询用户输入的历史记录
	 * 
	 * @param list
	 */
	public ArrayList<FuzzyQueryBean> queryHistoryRecode() {
		ArrayList<FuzzyQueryBean> list = new ArrayList<FuzzyQueryBean>();
		Cursor c = db.query(TABLE_NAME_SEARCH_HISTORY, PROJECTION_SEARCH_HISTORY, null, null, null, null, RECORD_ID);
		while (c.moveToNext()) {
			FuzzyQueryBean bean = new FuzzyQueryBean();
			bean.setCity(c.getString(c.getColumnIndex(HISTORY_Columns.CITY)));
			bean.setAddress(c.getString(c.getColumnIndex(HISTORY_Columns.ADDRESS)));
			bean.setCityFullName(c.getString(c.getColumnIndex(HISTORY_Columns.FullNAME)));
			bean.setLongitude(c.getDouble(c.getColumnIndex(HISTORY_Columns.LONGITUDE)));
			bean.setLatitude(c.getDouble(c.getColumnIndex(HISTORY_Columns.LATITUDE)));
			list.add(bean);
		}
		return list;

	}
	
	/**
	 * 查询数据库是为有相同的数据
	 * 
	 **/
	public boolean isAlikeData(String fullName){
		boolean isAlikeData = false ;
		SQLiteDatabase localDb = db;
		try {
			Cursor c = db.query(TABLE_NAME_SEARCH_HISTORY, PROJECTION_SEARCH_HISTORY, null, null, null, null, RECORD_ID);
			while (c.moveToNext()) {
				String full_name =c.getString(c.getColumnIndex(HISTORY_Columns.FullNAME));
				if(fullName.equals(full_name)){
					isAlikeData = true;
				}else{
					isAlikeData= false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isAlikeData;
		
	}
	
	/**
	 * 删除某一条历史记录
	 * 
	 * @param list
	 */

	public void delOneHistoryRecode(String fullName) {
		SQLiteDatabase localDb = db;
	    try {
			db.execSQL("delete from search_history where full_name = " + "'" + fullName +"'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
