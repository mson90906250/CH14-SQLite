package tw.tcnr01.m1405;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class M1405 extends AppCompatActivity {
    private FriendDbHelper dbHper;
    private static final String DB_FILE = "friends.db";
    private static final String DB_TABLE = "member";
    private static final int DBversion = 1;
    private ArrayList<String> recSet;
    private int index = 0;
    String msg = null;

    protected static final int BUTTON_POSITIVE = -1;
    protected static final int BUTTON_NEGATIVE = -2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m1405);
        setupViewComponent();
    }

    //自定義方法------------------------------------------------
    private void setupViewComponent() {
        initDB();
    }

    private void initDB() {
        if (dbHper == null)
            dbHper = new FriendDbHelper(this, DB_FILE, null, DBversion);
        recSet = dbHper.getRecSet();

    }


    //生命周期--------------------------------
    @Override
    public void onResume() {
        super.onResume();
        if (dbHper == null)
            dbHper = new FriendDbHelper(this, DB_FILE, null, DBversion);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }

    }

    //menu--------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.m1405, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent it = new Intent();
        switch (item.getItemId())
        {
            case R.id.m_add://新增
                it.setClass(M1405.this, M1405insert.class);
                startActivity(it);
                break;

            case R.id.m_query://查詢
                it.setClass(M1405.this, M1405query.class);
                startActivity(it);
                break;

            case R.id.m_update://修改
                it.setClass(M1405.this, M1405update.class);
                startActivity(it);
                break;

            case R.id.m_delete://刪除
                Toast.makeText(getApplicationContext(), "施工中", Toast.LENGTH_SHORT).show();
                break;

            case R.id.m_batch://批次新增
                dbHper.createTB();
                long totrec=dbHper.RecCount();
                Toast.makeText(getApplicationContext(), "總計:"+totrec, Toast.LENGTH_LONG).show();
                break;

            case R.id.m_clear://清空資料
                // 清空
                MyAlertDialog aldDial = new MyAlertDialog(M1405.this);
                aldDial.setTitle("清空所有資料");
                aldDial.setMessage("資料刪除無法復原\n確定將所有資料刪除嗎?");
                aldDial.setIcon(android.R.drawable.ic_dialog_info);
                aldDial.setCancelable(false); //返回鍵關閉
                aldDial.setButton(BUTTON_POSITIVE, "確定清空", aldBtListener);
                aldDial.setButton(BUTTON_NEGATIVE, "取消清空", aldBtListener);
                aldDial.show();
                break;

            case  R.id.m_list:
                it.setClass(M1405.this, M1405list.class);
                startActivity(it);
                break;

            case  R.id.m_spinner:
                break;

            case R.id.action_settings:
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private DialogInterface.OnClickListener aldBtListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which)
            {
                case BUTTON_POSITIVE:
                    int rowsAffected = dbHper.clearRec();
                    msg = "資料表已空 !共刪除" + rowsAffected + " 筆";
                    break;
                case BUTTON_NEGATIVE:
                    msg = "放棄刪除所有資料 !";
                    break;
            }
            Toast.makeText(M1405.this, msg, Toast.LENGTH_SHORT).show();
        }
    };

}
