package tw.tcnr01.m1405;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class M1405update extends AppCompatActivity implements View.OnClickListener {
    private EditText e001,e002,e003;
    private TextView count_t,tvTitle;

    private FriendDbHelper dbHper;
    private static final String DB_FILE = "friends.db";
    private static final String DB_TABLE = "member";
    private static final int DBversion = 1;
    private Button btNext,btPrev,btTop,btEnd;
    private int index = 0;
    private ArrayList<String> recSet;

    private float x1; // 觸控的 X 軸位置
    private float y1; // 觸控的 Y 軸位置
    private float x2;
    private float y2;
    int range = 50; // 兩點距離
    int ran = 60; // 兩點角度



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m1405update);
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
                ctlFirst();
                break;
            case R.id.btIdend:
                ctlLast();
                break;
            case R.id.btIdPrev:
                ctlPrev();
                break;
            case R.id.btIdNext:
                ctlNext();
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

    private void ctlNext() {
        index++;
        if (index>recSet.size()-1){
            index = 0;
        }
        showRec(index);
    }

    private void ctlPrev() {
        index--;
        if (index<0){
            index = recSet.size()-1;
        }
        showRec(index);
    }

    private void ctlLast() {
        index = recSet.size() - 1;
        showRec(index);
    }

    private void ctlFirst() {
        index = 0;
        showRec(index);
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


//監聽-------------------------------------------------------
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
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
