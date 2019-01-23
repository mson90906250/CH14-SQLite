package tw.tcnr01.m1405;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;

public class M1405browse extends AppCompatActivity implements View.OnClickListener {
    private EditText e001,e002,e003;
    private TextView count_t,tvTitle;

    private FriendDbHelper dbHper;
    private static final String DB_FILE = "friends.db";
    private static final String DB_TABLE = "member";
    private static final int DBversion = 1;
    private Button btNext,btPrev,btTop,btEnd;
    private int index = 0;
    private ArrayList<String> recSet;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m1405browse);
        setupViewComponent();

//        initDB();//起始化DB;
//        count_t.setText("共計:"+Integer.toString(dbHper.RecCount())+"筆");
    }


    private void setupViewComponent() {
        tvTitle = (TextView) findViewById(R.id.tvIdTitle);
        e001 = (EditText) findViewById(R.id.edtName);
        e002 = (EditText) findViewById(R.id.edtGrp);
        e003 = (EditText) findViewById(R.id.edtAddr);
        count_t = (TextView) findViewById(R.id.count_t);

        btNext = (Button) findViewById(R.id.btIdNext);
        btPrev = (Button) findViewById(R.id.btIdPrev);
        btTop = (Button) findViewById(R.id.btIdtop);
        btEnd = (Button) findViewById(R.id.btIdend);

        btNext.setOnClickListener(this);
        btPrev.setOnClickListener(this);
        btTop.setOnClickListener(this);
        btEnd.setOnClickListener(this);

        //
        initDB();//起始化DB;
        count_t.setText("共計:"+Integer.toString(dbHper.RecCount())+"筆");
        showRec(index);


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btIdtop:
                break;
            case R.id.btIdend:
                break;
            case R.id.btIdPrev:
                break;
            case R.id.btIdNext:
                break;
        }
    }

    //自定義函數---------------------------------------------------
    private void initDB() {//確認是否有資料庫 ,若無就給新的
        if (dbHper == null){
            dbHper = new FriendDbHelper(this,DB_FILE,null,DBversion);
            recSet = dbHper.getRecSet();
        }
    }

    private void showRec(int index) {
        if (recSet.size() != 0)   {
            String stHead = "顯示資料：第 " + (index + 1) + " 筆 / 共 " + recSet.size() + " 筆";
            tvTitle.setTextColor(ContextCompat.getColor(this, R.color.Blue));
            tvTitle.setText(stHead);
            //--------------------------
            String[] fld = recSet.get(index).split("#");
            e001.setTextColor(ContextCompat.getColor(this, R.color.Red));
            e001.setText(fld[1]);
            e002.setText(fld[2]);
            e003.setText(fld[3]);
            //---------------------
        } else        {
            e001.setText("");
            e002.setText("");
            e003.setText("");
        }
    }


    //生命周期------------------------------------------------------

    @Override
    protected void onPause() {
        super.onPause();
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dbHper == null)
            dbHper = new FriendDbHelper(this, DB_FILE, null, DBversion);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }
    }





    //menu-----------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.m1405sub, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.m_return) finish();
        return true;
    }



}
