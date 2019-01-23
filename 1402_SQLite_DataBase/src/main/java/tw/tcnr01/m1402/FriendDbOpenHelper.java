package tw.tcnr01.m1402;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class FriendDbOpenHelper extends SQLiteOpenHelper {//處理SQLi時一定要有
    public String sCreateTableCommand;
    String TAG="tcnr01=>";

    public FriendDbOpenHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        //傳入的參數說明
//		context: 用來開啟或建立資料庫的應用程式物件，如 Activity 物件
//		name: 資料庫檔案名稱，若傳入 null 表示將資料庫暫存在記憶體
//		factory: 用來建立指標物件的類別，若傳入 null 表示使用預設值
//		version: 即將要建立的資料庫版本 (版本編號從 1 開始)
//		        若資料庫檔案不存在，就會呼叫 onCreate() 方法
//		        若即將建立的資料庫版本比現存的資料庫版本新，就會呼叫 onUpgrade() 方法
//		        若即將建立的資料庫版本比現存的資料庫版本舊，就會呼叫 onDowngrade() 方法
//		errHandler: 當資料庫毀損時的處理程式，若傳入 null 表示使用預設的處理程式

        // TODO Auto-generated constructor stub
        Log.d(TAG, "FriendDbHelper()");

        sCreateTableCommand="";

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        Log.d(TAG, "onCreate()");

        if (sCreateTableCommand.isEmpty())
            return;

        db.execSQL(sCreateTableCommand);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        Log.d(TAG, "onUpgrade()");
    }

}
