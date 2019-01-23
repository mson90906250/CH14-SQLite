package tw.tcnr01.m1401;

import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.lang.reflect.Method;


public class GameResult extends AppCompatActivity {

    private EditText mEdtCountSet,
            mEdtCountPlayerWin,
            mEdtCountComWin,
            mEdtCountDraw;
    private Button btnBackToGame;
    private static final int NOTI_ID = 100; //給訊息一個id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(tw.tcnr01.m1401.R.layout.game_result);
        setupViewComponent();
        showResult();
    }

    private void setupViewComponent() {
        mEdtCountSet = (EditText)findViewById(tw.tcnr01.m1401.R.id.edtCountSet);
        mEdtCountPlayerWin = (EditText)findViewById(tw.tcnr01.m1401.R.id.edtCountPlayerWin);
        mEdtCountComWin = (EditText)findViewById(tw.tcnr01.m1401.R.id.edtCountComWin);
        mEdtCountDraw = (EditText)findViewById(tw.tcnr01.m1401.R.id.edtCountDraw);
        btnBackToGame = (Button)findViewById(tw.tcnr01.m1401.R.id.btnBackToGame);

        btnBackToGame.setOnClickListener(btnBackToGameLis);
    }
    private Button.OnClickListener btnBackToGameLis = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            GameResult.this.finish();
        }
    };
    private void showResult() {
        // 從 bundle 中取出資料
        Bundle bundle = this.getIntent().getExtras();

        int iCountSet = bundle.getInt("KEY_COUNT_SET");
        int iCountPlayerWin = bundle.getInt("KEY_COUNT_PLAYER_WIN");
        int iCountComWin = bundle.getInt("KEY_COUNT_COM_WIN");
        int iCountDraw = bundle.getInt("KEY_COUNT_DRAW");

        mEdtCountSet.setText(Integer.toString(iCountSet));
        mEdtCountPlayerWin.setText(Integer.toString(iCountPlayerWin));
        mEdtCountComWin.setText(Integer.toString(iCountComWin));
        mEdtCountDraw.setText(Integer.toString(iCountDraw));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(tw.tcnr01.m1401.R.menu.gameresult, menu);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        NotificationManager notiMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notiMgr.cancel(NOTI_ID);
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        GameResult.this.finish();
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }
}

