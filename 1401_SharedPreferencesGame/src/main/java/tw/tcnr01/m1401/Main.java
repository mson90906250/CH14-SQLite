package tw.tcnr01.m1401;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Main extends AppCompatActivity {

    private Button mBtnLaunchAct;
    private TextView mTxtResult;
    private int LAUNCH_GAME=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(tw.tcnr01.m1401.R.layout.main);

        setupViewComponent();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != LAUNCH_GAME) return;
        switch (resultCode) {
            case RESULT_OK:
                Bundle bundle = data.getExtras();

                int iCountSet = bundle.getInt("KEY_COUNT_SET");
                int iCountPlayerWin = bundle.getInt("KEY_COUNT_PLAYER_WIN");
                int iCountComWin = bundle.getInt("KEY_COUNT_COM_WIN");
                int iCountDraw = bundle.getInt("KEY_COUNT_DRAW");

                String s =getString(tw.tcnr01.m1401.R.string.m1401_result) + iCountSet +getString(tw.tcnr01.m1401.R.string.m1401_table)+" "+
                        getString(tw.tcnr01.m1401.R.string.m1401_PlayerWin) + iCountPlayerWin +getString(tw.tcnr01.m1401.R.string.m1401_table)+" "+
                        getString(tw.tcnr01.m1401.R.string.m1401_comWin)+ iCountComWin +getString(tw.tcnr01.m1401.R.string.m1401_table)+" "+
                        getString(tw.tcnr01.m1401.R.string.m1401_draw)+ iCountDraw +getString(tw.tcnr01.m1401.R.string.m1401_table);
                mTxtResult.setText(s);
                break;
            case RESULT_CANCELED:
                mTxtResult.setText(getString(tw.tcnr01.m1401.R.string.m1401_cancel));
        }

    }

    private void setupViewComponent() {
        mBtnLaunchAct = (Button)findViewById(tw.tcnr01.m1401.R.id.btnLaunchAct);
        mTxtResult = (TextView)findViewById(tw.tcnr01.m1401.R.id.txtResult);
        mBtnLaunchAct.setOnClickListener(btnLaunchActOnClickLis);

    }

    private Button.OnClickListener btnLaunchActOnClickLis = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

            Intent myIntent = new Intent();
            myIntent.setClass(Main.this, M1401.class);
            startActivityForResult(myIntent,LAUNCH_GAME);


        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(tw.tcnr01.m1401.R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }
}
