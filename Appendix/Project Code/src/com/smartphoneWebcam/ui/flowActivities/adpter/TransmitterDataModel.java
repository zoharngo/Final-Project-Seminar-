package com.smartphoneWebcam.ui.flowActivities.adpter;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class TransmitterDataModel {
	private static TransmitterDataModel dataModel = null;
	private ArrayList<Transmitter> transList = null;

	private ModelHandler modelHandler = null;

	private TransmitterDataModel(Context context) throws SQLiteException {
		transList = new ArrayList<Transmitter>();
		modelHandler = new ModelHandler(context);
		transList = modelHandler.getAllTransmitter();
	}

	public static TransmitterDataModel getInstance(Context context)
			throws SQLiteException {

		if (dataModel == null) {
			dataModel = new TransmitterDataModel(context);
		}
		return dataModel;
	}

	public int getSize() {
		return transList.size();
	}

	public Transmitter getItem(int pos) throws SQLiteException {
		return transList.get(pos);
	}

	public void addTransmitter(Transmitter transmitter) throws SQLiteException {
		modelHandler.addTransmitter(transmitter);
		transList.add(transmitter);
	}

	public void removeTransmitter(ArrayList<Transmitter> transmitters) {
		transList.removeAll(transmitters);
		modelHandler.removeTransmitters(transmitters);

		System.out.println();
	}

	public boolean isEmptyList() {
		return transList.isEmpty();
	}

	private static class ModelHandler extends SQLiteOpenHelper {
		public static final String KEY_ID = "id_pk";
		public static final String KEY_NICK_NAME = "nick_name";
		public static final String KEY_IP = "ip";

		private static final String DATABASE_NAME = "transmitter_db";
		private static final String DATABASE_TABLE = "transmitters";
		private static final int DATABASE_VERSION = 3;
		private static final String DATABASE_CREATE = "create table if not exists transmitters"
				+ "(id_pk integer primary key autoincrement"
				+ ",nick_name VARCHAR" + ",ip VARCHAR);";

		public ModelHandler(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) throws SQLiteException {
			try {
				db.execSQL(DATABASE_CREATE);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
				throws SQLiteException {

			db.execSQL("drop table if exists " + DATABASE_TABLE);
			onCreate(db);
		}

		public void addTransmitter(Transmitter transmitter)
				throws SQLiteException {
			SQLiteDatabase db = getWritableDatabase();
			try {

				ContentValues contentValues = createContentValues(transmitter);
				transmitter.setId((int) db.insert(DATABASE_TABLE, null,
						contentValues));
			} catch (SQLiteException e) {
				e.printStackTrace();
				throw e;
			} finally {
				db.close();
			}
		}

		public ArrayList<Transmitter> getAllTransmitter()
				throws SQLiteException {
			SQLiteDatabase db = getReadableDatabase();
			ArrayList<Transmitter> transList = new ArrayList<Transmitter>();
			Cursor cursor = db
					.rawQuery("select * from " + DATABASE_TABLE, null);
			try {
				while (cursor.moveToNext()) {
					Transmitter transmitter = new Transmitter();
					transmitter.setId(cursor.getInt(0));
					transmitter.setNickName(cursor.getString(1));
					transmitter.setIp(cursor.getString(2));
					transList.add(transmitter);
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
				throw e;
			} finally {
				db.close();
			}

			return transList;
		}

		public void removeTransmitters(ArrayList<Transmitter> transmitters)
				throws SQLiteException {
			SQLiteDatabase db = getWritableDatabase();
			try {
				String sql = "Delete from transmitters where id_pk = ? ";
				SQLiteStatement delete = db.compileStatement(sql);
				for (Transmitter transmitter : transmitters) {
					delete.bindLong(1, transmitter.getId());
					delete.execute();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
				throw e;
			} finally {
				db.close();
			}
		}

		public ContentValues createContentValues(Transmitter transmitter) {
			ContentValues contentValues = new ContentValues();
			contentValues.put(KEY_IP, transmitter.getIp());
			contentValues.put(KEY_NICK_NAME, transmitter.getNickName());
			return contentValues;
		}

	}

}