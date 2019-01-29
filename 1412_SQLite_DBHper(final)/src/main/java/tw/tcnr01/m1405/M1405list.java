package tw.tcnr01.m1405;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.tcnr01.m1405.FriendDbHelper;
import tw.tcnr01.m1405.R;


public class M1405list extends ListActivity {

    private FriendDbHelper dbHper;
    private static final String DB_FILE = "friends.db";
    private static final String DB_TABLE = "member";
    private static final int DBversion = 1;
    //-----------------
    private TextView tvTitle;
    private ArrayList<String> recSet;
    //--------------------------
    String TAG = "oldpa=";
    //----------------------------------------
    List<Map<String, Object>> mList;
    //--------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m1405list);
        setupViewComponent();

    }

    private void setupViewComponent() {
        initDB();
        tvTitle = (TextView) findViewById(R.id.tvIdTitle);
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.Navy));
        tvTitle.setBackgroundResource(R.color.Aqua);
        tvTitle.setText("顯示資料： 共 " + recSet.size() + " 筆");
        //===========取SQLite 資料=============
        mList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < recSet.size(); i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            String[] fld = recSet.get(i).split("#");
            item.put("imgView", R.drawable.userconfig);
            item.put("txtView", "id:" + fld[0] + "\nname:" + fld[1] + "\ngroup:" + fld[2] + "\naddr:" + fld[3]);
            mList.add(item);
        }
        //=========設定listview========
        SimpleAdapter adapter = new SimpleAdapter(this,
                mList, R.layout.list_item,
                new String[]{"imgView",
                        "txtView"},
                new int[]{R.id.imgView, R.id.txtView} );
        setListAdapter(adapter);
        //----------------------------------
        ListView listview = getListView();
        listview.setTextFilterEnabled(true);
        listview.setOnItemClickListener(listviewOnItemClkLis);
    }
    private ListView.OnItemClickListener listviewOnItemClkLis = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String s = "你按下第 "  + Integer.toString(position +1)   + "筆\n" + ((TextView) view.findViewById(R.id.txtView)).getText()   .toString();
            tvTitle.setText(s);
        } };

    private void initDB() {
        if (dbHper == null)
            dbHper = new FriendDbHelper(this, DB_FILE, null, DBversion);
        recSet = dbHper.getRecSet();
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