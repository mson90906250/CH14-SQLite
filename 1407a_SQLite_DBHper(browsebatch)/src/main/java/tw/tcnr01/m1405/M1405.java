package tw.tcnr01.m1405;

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
                it.setClass(M1405.this, M1405browse.class);
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
            case R.id.action_settings:
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
