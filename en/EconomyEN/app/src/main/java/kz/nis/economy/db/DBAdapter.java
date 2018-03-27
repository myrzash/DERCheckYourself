package kz.nis.economy.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import kz.nis.economy.Main;
import kz.nis.economy.adapter.FileAdapter;

public class DBAdapter {

    public static final String ATTR_QUESTION = "quest";
    public static final String ATTR_ANSWER = "answer";
    public static final String ATTR_ACTIV = "activ";
    private static final String DATABASE_NAME = "dben.sqlite";
    private static final int DATABASE_VERSION = 1;
    //    TABLE_BASIC
    private static final String TABLE_BASIC = "basic";//-->5 columns
    private static final String ATTR_ID = "_id";
    private static final String ATTR_PART_ID = "partid";
    //    TABLE_PARTS
    private static final String TABLE_PARTS = "parts";//-->4 columns
    public static final String ATTR_PART_NAME = "partname";
    public static final String ATTR_PART_AUTHOR = "author";
    public static final String ATTR_PART_COVER = "cover";
//
    private final Context context;
    private DBOpenHelper dbHelper;
    private SQLiteDatabase DB;
    private ContentValues CV;

    public DBAdapter(Context ctx) {
        this.context = ctx;
    }

    public void open() throws Exception {
        dbHelper = new DBOpenHelper(context, DATABASE_NAME, null,
                DATABASE_VERSION);
        DB = dbHelper.openDataBase();
    }

    public void close() {
        if (dbHelper != null)
            dbHelper.close();
    }

    public Cursor getAllRecords(int partID) {
        String selection = ATTR_PART_ID + " = " + partID;
        Cursor cursor = DB.query(TABLE_BASIC, null, selection, null, null, null, null);
        if (cursor != null)
            cursor.moveToLast();
        return cursor;
    }

    public Cursor getQuestAnswer(int partID) {
        String selection = ATTR_PART_ID + " = " + partID+" AND "+ATTR_ACTIV+"='true' ";
        Cursor cursor = DB.query(TABLE_BASIC, new String[]{ATTR_QUESTION,ATTR_ANSWER}, selection, null, null, null, null);
        if (cursor != null)
            cursor.moveToLast();
        return cursor;
    }

    private void updateAttr(int id, String text, String whereAttr) {
        CV = new ContentValues();
        CV.put(whereAttr, text);
        DB.update(TABLE_BASIC, CV, ATTR_ID + "=" + id, null);
    }

    public void updateQuest(int id, String text) {
        this.updateAttr(id, text, ATTR_QUESTION);
    }

    public void updateAnswer(int id, String text) {
        this.updateAttr(id, text, ATTR_ANSWER);
    }

    public void updateActiv(int id, boolean activ) {

        this.updateAttr(id, String.valueOf(activ), ATTR_ACTIV);
    }

    public void insertRecord(int partID) {
        CV = new ContentValues();
        CV.put(ATTR_QUESTION, "");
        CV.put(ATTR_ANSWER, "");
        CV.put(ATTR_PART_ID, partID);
        DB.insert(TABLE_BASIC, null, CV);
        Log.d(Main.LOG, "insert record: (partID=" + partID + ")");
    }
//
//
//
//
// ARBAITEN FROM PART
    public void insertPart() {
        CV = new ContentValues();
        CV.put(ATTR_PART_NAME, "Part");
        CV.put(ATTR_PART_AUTHOR, "Author");
        DB.insert(TABLE_PARTS, null, CV);
        Log.d(Main.LOG, "insert part ");
    }

    public void updatePartName(int partId,String partName) {
        CV = new ContentValues();
        CV.put(ATTR_PART_NAME, partName);
        DB.update(TABLE_PARTS, CV, ATTR_ID + "=" + partId, null);
        Log.d(Main.LOG, "updatePartName: partId=" + partId);
    }

    public void updatePartCover(int partId) {
        CV = new ContentValues();
        CV.put(ATTR_PART_COVER, FileAdapter.getPhotoName(partId));
        DB.update(TABLE_PARTS, CV, ATTR_ID + "=" + partId, null);
        Log.d(Main.LOG, "updatePartCover: partId=" + partId);
    }

    public void updateAuthorName(int partId,String authorName) {
        CV = new ContentValues();
        CV.put(ATTR_PART_AUTHOR, authorName);
        DB.update(TABLE_PARTS, CV, ATTR_ID + "=" + partId, null);
        Log.d(Main.LOG,"updateAuthorName: "+authorName);
    }

    public Cursor getPart(int id) {
        String selection = ATTR_ID + " = " + id;
        Cursor cursor = DB.query(TABLE_PARTS, null, selection, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }

    public Cursor getParts() {
        Cursor cursor = DB.query(TABLE_PARTS, null, null, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }
//
//
//
//
//	private Cursor getActivRecords(String partName){
//		partName = "'"+partName+"'";
//		String selection = ATTR_PART_INDEX +" = "+partName+" AND "+ATTR_ACTIV + "='true' ";
//		Cursor cursor = DB.query(TABLE_NAME, null, selection, null, null, null, null);
//		if (cursor != null)
//			cursor.moveToFirst();
//		return cursor;
//	}
//
//	private int[] getActivRecNum(String partName){
//		Cursor cursor = getActivRecords(partName);
//		int length = cursor.getCount();
//		int[] array = new int[length];
//		int i=0;
//		do{
//			array[i] = cursor.getInt(0);
//			i++;
//		}while(cursor.moveToNext());
//		return array;
//	}
}
