package tw.tcnr01.m1405;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/*//---
            mSpnName.setSelection(index, true); //spinner 小窗跳到第幾筆
//---------------*/

public class M1405spinner extends AppCompatActivity implements View.OnClickListener {
    private TextView count_t;
    private EditText e001, e002, e003, e004;

    private FriendDbHelper dbHper;
    private static final String DB_FILE = "friends.db";
    private static final String DB_TABLE = "member";
    private static final int DBversion = 1;
    //-----------------
    private TextView tvTitle;
    private Button btNext, btPrev, btTop, btEnd;
    private ArrayList<String> recSet;
    private int index = 0;
    String msg = null;
    //--------------------------
    private float x1; // 觸控的 X 軸位置
    private float y1; // 觸控的 Y 軸位置
    private float x2;
    private float y2;
    int range = 50; // 兩點距離
    int ran = 60; // 兩點角度

    String TAG = "oldpa=";
    //----------------------------------------
    private Button btEdit, btDel;
    private EditText b_edid;
    String tid, tname, tgrp, taddr;
    int rowsAffected;

    private Spinner mSpnName;

    //--------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m1405spinner);
        setupViewComponent();
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

        btEdit = (Button) findViewById(R.id.btnupdate);
        btDel = (Button) findViewById(R.id.btIdDel);
        b_edid = (EditText) findViewById(R.id.edid);
        b_edid.setKeyListener(null);  //設定ID 不能修改
        btNext.setOnClickListener(this);
        btPrev.setOnClickListener(this);
        btTop.setOnClickListener(this);
        btEnd.setOnClickListener(this);

        btEdit.setOnClickListener(this);
        btDel.setOnClickListener(this);
//--------------------------------
        initDB();
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.Navy));
        //-------------------------------------------------------------------
        mSpnName = (Spinner) this.findViewById(R.id.spnName);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        for (int i = 0; i < recSet.size(); i++) {
            String[] fld = recSet.get(i).split("#");
            adapter.add(fld[0] + " " + fld[1] + " " + fld[2] + " " + fld[3]);
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnName.setAdapter(adapter);
        mSpnName.setOnItemSelectedListener(mSpnNameOnItemSelLis);
        count_t.setText("共計:" + Integer.toString(dbHper.RecCount()) + "筆");
        showRec(index);

    }
    private Spinner.OnItemSelectedListener mSpnNameOnItemSelLis = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView parent, View view, int position, long id) {
            int iSelect = mSpnName.getSelectedItemPosition(); //找到按何項
            String[] fld = recSet.get(iSelect).split("#");
            String s = "資料：共" + recSet.size() + " 筆," + "你按下  " + String.valueOf(iSelect + 1) + "項"; //起始為0
            tvTitle.setText(s);
            b_edid.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.Red));
            b_edid.setText(fld[0]);
            e001.setText(fld[1]);
            e002.setText(fld[2]);
            e003.setText(fld[3]);
            index=position;//改變目前所在的資料位置
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            b_edid.setText("");
            e001.setText("");
            e002.setText("");
            e003.setText("");
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btIdNext:
                ctlNext();
                break;
            case R.id.btIdPrev:
                ctlPrev();
                break;
            case R.id.btIdtop:
                ctlFirst();
                break;
            case R.id.btIdend:
                ctlLast();
                break;
            case R.id.btnupdate:
                // 資料更新
                tid = b_edid.getText().toString().trim();
                tname = e001.getText().toString().trim();
                tgrp = e002.getText().toString().trim();
                taddr = e003.getText().toString().trim();

                rowsAffected = dbHper.updateRec(tid, tname, tgrp, taddr);
                if (rowsAffected == -1) {
                    msg = "資料表已空, 無法修改 !";
                } else if (rowsAffected == 0) {
                    msg = "找不到欲修改的記錄, 無法修改 !";
                } else {
                    msg = "第 " + (index + 1) + " 筆記錄  已修改 ! \n" + "共 " + rowsAffected + " 筆記錄   被修改 !";
                    recSet = dbHper.getRecSet();
                    showRec(index);
                }
                Toast.makeText(M1405spinner.this, msg, Toast.LENGTH_SHORT).show();
                setupViewComponent();//更新完後重新讀取資料
                break;

            case R.id.btIdDel:
                // 刪除資料
                tid = b_edid.getText().toString().trim();
                rowsAffected = dbHper.deleteRec(tid);
                if (rowsAffected == -1) {
                    msg = "資料表已空, 無法刪除 !";
                } else if (rowsAffected == 0) {
                    msg = "找不到欲刪除的記錄, 無法刪除 !";
                } else {
                    msg = "第 " + (index + 1) + " 筆記錄  已刪除 ! \n" + "共 " + rowsAffected + " 筆記錄   被刪除 !";
                    recSet = dbHper.getRecSet();
                    if(index==dbHper.RecCount()){
                        index --;
                    }
                    setupViewComponent();
                    showRec(index);
                    mSpnName.setSelection(index, true); //spinner 小窗跳到第幾筆
                }
                Toast.makeText(M1405spinner.this, msg, Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private void initDB() {
        if (dbHper == null)
            dbHper = new FriendDbHelper(this, DB_FILE, null, DBversion);
        recSet = dbHper.getRecSet();
    }

    private void showRec(int index) {

        if (recSet.size() != 0) {
            String stHead = "顯示資料：第 " + (index + 1) + " 筆 / 共 " + recSet.size() + " 筆";
            tvTitle.setBackgroundColor(ContextCompat.getColor(this,R.color.Teal));
            tvTitle.setTextColor(ContextCompat.getColor(this, R.color.Yellow));
            tvTitle.setText(stHead);

            String[] fld = recSet.get(index).split("#");
            b_edid.setTextColor(ContextCompat.getColor(this, R.color.Red));
            b_edid.setBackgroundColor(ContextCompat.getColor(this,R.color.Yellow));
            b_edid.setText(fld[0]);
            e001.setText(fld[1]);
            e002.setText(fld[2]);
            e003.setText(fld[3]);
            //---
            mSpnName.setSelection(index, true); //spinner 小窗跳到第幾筆
            //---------------
        } else {
            String stHead = "顯示資料：0 筆";
            tvTitle.setTextColor(ContextCompat.getColor(this, R.color.Blue));
            tvTitle.setText(stHead);
            b_edid.setText("");
            e001.setText("");
            e002.setText("");
            e003.setText("");
        }
    }

    //------------------------------------------------
    private void ctlFirst() {
        // 第一筆
        index = 0;
        showRec(index);
    }

    private void ctlPrev() {
        // 上一筆
        index--;
        if (index < 0)
            index = recSet.size() - 1;
        showRec(index);
    }

    private void ctlNext() {
        // 下一筆
        index++;
        if (index >= recSet.size())
            index = 0;
        showRec(index);
//        mSpnName.setSelection(index, true); //spinner 小窗跳到第幾筆
    }


    private void ctlLast() {
        // 最後一筆
        index = recSet.size() - 1;
        showRec(index);
    }

    //---------------------------------------------------
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
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 按下
                x1 = event.getX(); // 觸控按下的 X 軸位置
                y1 = event.getY(); // 觸控按下的 Y 軸位置

                break;
            case MotionEvent.ACTION_MOVE: // 拖曳

                break;
            case MotionEvent.ACTION_UP: // 放開
                x2 = event.getX(); // 觸控放開的 X 軸位置
                y2 = event.getY(); // 觸控放開的 Y 軸位置
                // 判斷左右的方法，因為屏幕的左上角是：0，0 點右下角是max,max
                // 並且移動距離需大於 > range
                float xbar = Math.abs(x2 - x1);
                float ybar = Math.abs(y2 - y1);
                double z = Math.sqrt(xbar * xbar + ybar * ybar);
                int angle = Math.round((float) (Math.asin(ybar / z) / Math.PI * 180));// 角度
                if (x1 != 0 && y1 != 0) {
                    if (x1 - x2 > range) { // 向左滑動
                        ctlPrev();
                    }
                    if (x2 - x1 > range) { // 向右滑動
                        ctlNext();
                        // t001.setText("向右滑動\n" + "滑動參值x1=" + x1 + " x2=" + x2 + "
                        // r=" + (x2 - x1)+"\n"+"ang="+angle);
                    }
                    if (y2 - y1 > range && angle > ran) { // 向下滑動
                        // 往下角度需大於50
                        // 最後一筆
                        ctlLast();
                    }
                    if (y1 - y2 > range && angle > ran) { // 向上滑動
                        // 往上角度需大於50
                        ctlFirst();// 第一筆
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.m1405sub, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.m_return) finish();
        return true;
    }


}

