package merk.gymcounter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class exerciseDBHelper extends SQLiteOpenHelper{

    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DATE = "date";
    public static final String KEY_SETS = "setscompleted";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_REPS = "reps";
    public static final String DATABASE_NAME= "exercisedb";
    public static final String DATABASE_TABLE = "exercisetable";
    public static final int DATABASE_VERSION = 1;


    public exerciseDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE + " ("
                + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME + " TEXT, " + KEY_DATE + " TEXT, " + KEY_SETS + " INTEGER, "
                +  KEY_WEIGHT + " INTEGER, " + KEY_REPS + " TEXT )";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXITS " + DATABASE_TABLE);
        onCreate(db);
    }


    public Cursor fetchAllExercises() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor mCursor = db.query(DATABASE_TABLE, new String[] {KEY_ROWID,
                        KEY_NAME, KEY_DATE, KEY_SETS, KEY_WEIGHT, KEY_REPS },
                null, null, null, null, KEY_DATE );
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchEventByDate(String datevalue ) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor mCursor = null;
        if (datevalue == null || datevalue.length () == 0) {
            mCursor = db.query(DATABASE_TABLE, new String[] {KEY_ROWID,
                            KEY_NAME, KEY_DATE, KEY_SETS, KEY_WEIGHT, KEY_REPS },
                    null, null, null, null, null);
        }
        else {
            mCursor  =  db.rawQuery("SELECT * FROM exercisetable WHERE date = ?", new String[] {datevalue});
        }

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    public Cursor fetchEventByDateName(String datevalue, String nameValue) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor mCursor = null;
        if (datevalue == null || datevalue.length () == 0) {
            mCursor = db.query(DATABASE_TABLE, new String[] {KEY_ROWID,
                            KEY_NAME, KEY_DATE, KEY_SETS, KEY_WEIGHT, KEY_REPS },
                    null, null, null, null, null);
        }
        else {
            mCursor  =  db.rawQuery("SELECT * FROM exercisetable WHERE " + KEY_DATE +" = ? AND " + KEY_NAME + " = ?", new String[] {datevalue, nameValue});
        }

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void delete(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        db.delete(DATABASE_TABLE, KEY_ROWID + " = ?",
                new String[] { String.valueOf(id )});
        db.close();
    }

    public void insert(String insertName, int insertSets, int weight, String reps){
        SQLiteDatabase db = this.getReadableDatabase();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        ContentValues insertValues = new ContentValues();
        insertValues.put(KEY_NAME, insertName);
        insertValues.put(KEY_DATE, df.format(c.getTime()));
        insertValues.put(KEY_SETS, insertSets);
        insertValues.put(KEY_WEIGHT, weight);
        insertValues.put(KEY_REPS, reps);

        db.insert(DATABASE_TABLE, null, insertValues);
    }
}

