package tw.tcnr01.m1405;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class M1405 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m1405);
    }


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
                Toast.makeText(getApplicationContext(), "施工中", Toast.LENGTH_SHORT).show();
                break;
            case R.id.m_update://修改
                Toast.makeText(getApplicationContext(), "施工中", Toast.LENGTH_SHORT).show();
                break;
            case R.id.m_delete://刪除
                Toast.makeText(getApplicationContext(), "施工中", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_settings:
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
