package tw.tcnr01.m1405;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

public class FriendDbHelper extends SQLiteOpenHelper {

    public String sCreateTableCommand;    // 資料庫名稱
    private static final String DB_FILE = "friends.db";
    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    public static final int VERSION = 1;    // 資料表名稱
    private static final String DB_TABLE = "member";    // 資料庫物件，固定的欄位變數

    //一支app在創建時是沒有SQLite 且不能用其他packagename的資料庫
    private static final String crTBsql = "CREATE TABLE " + DB_TABLE + " ( "
            + "id INTEGER PRIMARY KEY," + "name TEXT NOT NULL," + "grp TEXT,"
            + "address TEXT);";

    private static SQLiteDatabase database;
    private String TAG = "tcnr01=>";

    //----------------------------------------------
    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    public static SQLiteDatabase getDatabase(Context context){
        if (database == null || !database.isOpen())
        {
            database = new FriendDbHelper(context, DB_FILE, null, VERSION)
                    .getWritableDatabase();
        }
        return database;
    }

    public FriendDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
//        super(context, name, factory, version);
        super(context, "friends.db", null, 1);
        sCreateTableCommand = "";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(crTBsql);//建立一個新的member table
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.d(TAG, "onUpgrade()");

        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);

        onCreate(db);
    }

    public long insertRec(String b_name, String b_grp, String b_address) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues rec = new ContentValues();
        rec.put("name", b_name);//"name為欄位名稱"
        rec.put("grp", b_grp);
        rec.put("address", b_address);
        long rowID = db.insert(DB_TABLE, null, rec);//真正寫入資料庫
        db.close();
        return rowID;
    }

    public int RecCount() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;//後面可再加條件
        Cursor recSet = db.rawQuery(sql, null);
        return recSet.getCount();
    }

    public String FindRec(String tname) {
        SQLiteDatabase db = getReadableDatabase();
        String fldSet = null;
        String sql = "SELECT * FROM " + DB_TABLE +
                " WHERE name LIKE ? ORDER BY id ASC";// ?是參數
        String[] args = {"%" + tname + "%"};

        Cursor recSet = db.rawQuery(sql, args);//取出多筆資料,  args 將會放在 ? 裡

        int columnCount = recSet.getColumnCount();//取得一列有幾個欄位
        Log.d(TAG,"ans:"+recSet.getCount());
        if (recSet.getCount() != 0)
        {
            recSet.moveToFirst();//移到第一筆資料

            fldSet = recSet.getString(0) + " "
                    +recSet.getString(1) + " "
                    +recSet.getString(2) + " "
                    +recSet.getString(3) + "\n";

            while (recSet.moveToNext())
            {
                for (int i = 0; i < columnCount; i++)
                {
                    fldSet += recSet.getString(i) + " ";
                }
                fldSet +="\n";
            }
        }
        recSet.close();
        db.close();
        return fldSet;
    }

    public ArrayList<String> getRecSet() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();

        //----------------------------
        Log.d(TAG,"recSet="+recSet);
        int columnCount = recSet.getColumnCount();
        while(recSet.moveToNext()){
            String fldSet = "";
            for(int i=0; i<columnCount; i++)
                fldSet += recSet.getString(i) + "#";
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();

        Log.d(TAG,"recAry="+recAry);
        return recAry;

    }

    public void createTB() {
        // 批次新增
        int maxrec = 20;
        SQLiteDatabase db = getWritableDatabase();
        for (int i = 0; i <= maxrec; i++) {
            ContentValues newRow = new ContentValues();
            newRow.put("name", "路人" +  u_chinayear(i));
            newRow.put("grp", "第" + u_chinano((int) (Math.random() * 4 + 1)) + "組");
            newRow.put("address", "台中市西區工業一路" + (100 + i) + "號");
            db.insert(DB_TABLE, null, newRow);
        }
        db.close();
    }

    private String u_chinayear(int input_i) {
        String c_year = "";
        String china_no[]={"甲","乙","丙","丁","戊","己","庚","辛","壬","癸"};
        c_year = china_no[input_i % 10];

        return c_year;
    }

    private String u_chinano(int i) {
        String c_number = "";
        String china_no[]={"零","一","二","三","四","五","六","七","八","九"};
        c_number = china_no[i % 10];

        return c_number;
    }

// ---------

}
