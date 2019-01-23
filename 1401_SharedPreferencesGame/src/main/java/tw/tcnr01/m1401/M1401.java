package tw.tcnr01.m1401;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;


public class M1401 extends AppCompatActivity implements
        ViewSwitcher.ViewFactory,
        View.OnClickListener{


    // ----宣告變數----
//    private ImageView txtComPlay;
    private ImageSwitcher txtComPlay;
    private TextView txtSelect, txtResult;
    private ImageButton btnScissors, btnStone, btnNet;
    private String user_select;
    private String answer;
    private MediaPlayer startmusic; //宣告媒體物件 片頭音樂
    private MediaPlayer mediaWin; // 宣告媒體物件 贏
    private MediaPlayer mediaLose; // 宣告媒體物件 輸
    private MediaPlayer mediaDraw; // 宣告媒體物件 平
    private RelativeLayout r_layout;
    private static final String TAG = "M1401";
    private Button mBtnShowResult;
    private int miCountSet = 0,
                            miCountPlayerWin = 0,
                            miCountComWin = 0,
                             miCountDraw = 0;
    private static final int NOTI_ID = 100; //給訊息一個id



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(tw.tcnr01.m1401.R.layout.m1401);
        setupViewComponent();
        //----log----------
        Log.d(TAG,"onCreate");
    }

    private void setupViewComponent() {
        // ---取得R.java 配置碼---
//        txtComPlay = (ImageView) findViewById(R.id.m1401_c001); // 電腦選擇
        txtComPlay = (ImageSwitcher) findViewById(tw.tcnr01.m1401.R.id.m1401_c001); // 電腦選擇
        txtSelect = (TextView) findViewById(tw.tcnr01.m1401.R.id.m1401_s001); // 選擇結果
        txtResult = (TextView) findViewById(tw.tcnr01.m1401.R.id.m1401_f000); // 輸贏判斷
        btnScissors = (ImageButton) findViewById(tw.tcnr01.m1401.R.id.m1401_b001); // 剪刀
        btnStone = (ImageButton) findViewById(tw.tcnr01.m1401.R.id.m1401_b002); // 石頭
        btnNet = (ImageButton) findViewById(tw.tcnr01.m1401.R.id.m1401_b003); // 布
        mBtnShowResult = (Button)findViewById(tw.tcnr01.m1401.R.id.btnShowResult); //顯示局數統計資料



        // ---開機動畫---
        txtComPlay.setFactory(this);
        r_layout = (RelativeLayout) findViewById(tw.tcnr01.m1401.R.id.m1401_rlay);
        r_layout.setBackgroundResource(tw.tcnr01.m1401.R.drawable.bkr);
        r_layout.setAnimation(AnimationUtils.loadAnimation(this, tw.tcnr01.m1401.R.anim.anim_scale_rotate_out));
        r_layout.setAnimation(AnimationUtils.loadAnimation(this, tw.tcnr01.m1401.R.anim.anim_scale_rotate_in));
        r_layout.setBackgroundResource(tw.tcnr01.m1401.R.drawable.bkr);



        // --開啟片頭音樂-----
        startmusic = MediaPlayer.create(getApplication(), R.raw.guess);
        startmusic.start();

        //--設定音樂連結--
        mediaWin = MediaPlayer.create(getApplication(), tw.tcnr01.m1401.R.raw.vctory);
        mediaLose = MediaPlayer.create(getApplication(), tw.tcnr01.m1401.R.raw.lose);
        mediaDraw = MediaPlayer.create(getApplication(), tw.tcnr01.m1401.R.raw.haha);
        // ---啟動監聽事件----
//        btnScissors.setOnClickListener(btn01On);
//        btnStone.setOnClickListener(btn01On);
//        btnNet.setOnClickListener(btn01On);

        btnScissors.setOnClickListener(this);
        btnStone.setOnClickListener(this);
        btnNet.setOnClickListener(this);
        mBtnShowResult.setOnClickListener(btnShowResultLis);



        //----log----------
        Log.d(TAG,"setupViewComponent");

        u_loaddata();
    }

    private Button.OnClickListener btnShowResultLis = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent it = new Intent();
            it.setClass(M1401.this, GameResult.class);

            Bundle bundle = new Bundle();
            bundle.putInt("KEY_COUNT_SET", miCountSet);
            bundle.putInt("KEY_COUNT_PLAYER_WIN", miCountPlayerWin);
            bundle.putInt("KEY_COUNT_COM_WIN", miCountComWin);
            bundle.putInt("KEY_COUNT_DRAW", miCountDraw);
            it.putExtras(bundle);

            startActivity(it);

        }
    };
    private Button.OnClickListener btn01On = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            int iComPlay = (int) (Math.random() * 3 + 1);
            // 1 - scissors, 2 - stone, 3 - net.
            switch (iComPlay) {
                case 1: //電腦:剪刀scissors
                    user_select = getString(tw.tcnr01.m1401.R.string.m1401_s002) + getString(tw.tcnr01.m1401.R.string.m1401_b001) + " ";


                    txtComPlay.setImageResource(tw.tcnr01.m1401.R.drawable.scissors); // 轉換ImageSwitcher剪刀
                    break;
                case 2: //電腦:石頭stone
                    user_select = getString(tw.tcnr01.m1401.R.string.m1401_s002) + getString(tw.tcnr01.m1401.R.string.m1401_b002) + " ";

                    txtComPlay.setImageResource(tw.tcnr01.m1401.R.drawable.stone); // 轉換ImageSwitcher石頭
                    break;
                case 3: // 電腦:布net
                    user_select = getString(tw.tcnr01.m1401.R.string.m1401_s002) + getString(tw.tcnr01.m1401.R.string.m1401_b003) + " ";

                    txtComPlay.setImageResource(tw.tcnr01.m1401.R.drawable.net); // 轉換ImageSwitcher布
                    break;
            }
            switch (v.getId()) {

                case tw.tcnr01.m1401.R.id.m1401_b001:
                    // 選擇 剪刀scissors
                    user_select += getString(tw.tcnr01.m1401.R.string.m1401_s001) + getString(tw.tcnr01.m1401.R.string.m1401_b001);
                    //change background color
                    u_setalpha();
                    btnScissors.getBackground().setAlpha(150);


                    switch (iComPlay) {
                        case 1:
                            answer = getString(tw.tcnr01.m1401.R.string.m1401_f000) + getString(tw.tcnr01.m1401.R.string.m1401_f003); // 平
                            txtResult.setTextColor(getResources().getColor(tw.tcnr01.m1401.R.color.Yellow)); // 平用黃顯示
                            music(2);
//                            Toast.makeText(getApplicationContext(),                //context       The context to use. Usually your Application or Activity object.
//                                    getText(R.string.m1401_f003),                           //text              The text to show. Can be formatted text.
//                                    Toast.LENGTH_LONG)                                      //duration      How long to display the message. Either LENGTH_SHORT or LENGTH_LONG
//                                    .show();
                            break;
                        case 2:
                            answer = getString(tw.tcnr01.m1401.R.string.m1401_f000) + getString(tw.tcnr01.m1401.R.string.m1401_f002); // 輸
                            txtResult.setTextColor(getResources().getColor(tw.tcnr01.m1401.R.color.Red)); // 輸用紅顯示
                            music(3);

                            break;
                        case 3:
                            answer = getString(tw.tcnr01.m1401.R.string.m1401_f000) + getString(tw.tcnr01.m1401.R.string.m1401_f001); // 贏
                            txtResult.setTextColor(getResources().getColor(tw.tcnr01.m1401.R.color.Lime)); // 贏用綠顯示
                            music(1);

                            break;
                    }

                    break;
                //----------------------------------------------
                case tw.tcnr01.m1401.R.id.m1401_b002:
                    // 選擇 石頭stone
                    user_select += getString(tw.tcnr01.m1401.R.string.m1401_s001)  + getString(tw.tcnr01.m1401.R.string.m1401_b002);
                    //change background color
                    u_setalpha();
                    btnStone.getBackground().setAlpha(150);

                    switch (iComPlay) {
                        case 1:
                            answer = getString(tw.tcnr01.m1401.R.string.m1401_f000) + getString(tw.tcnr01.m1401.R.string.m1401_f001); // 贏
                            txtResult.setTextColor(getResources().getColor(tw.tcnr01.m1401.R.color.Lime)); // 贏用綠顯示
                            music(1);

                            break;
                        case 2:
                            answer = getString(tw.tcnr01.m1401.R.string.m1401_f000) + getString(tw.tcnr01.m1401.R.string.m1401_f003); // 平
                            txtResult.setTextColor(getResources().getColor(tw.tcnr01.m1401.R.color.Yellow)); // 平用黃顯示
                            music(2);

                            break;
                        case 3:
                            answer = getString(tw.tcnr01.m1401.R.string.m1401_f000) + getString(tw.tcnr01.m1401.R.string.m1401_f002); // 輸
                            txtResult.setTextColor(getResources().getColor(tw.tcnr01.m1401.R.color.Red)); // 輸用紅顯示
                            music(3);

                            break;
                    }
                    break;
                //---------------------------------------------
                case tw.tcnr01.m1401.R.id.m1401_b003:

                    // 選擇 布net
                    user_select += getString(tw.tcnr01.m1401.R.string.m1401_s001) + getString(tw.tcnr01.m1401.R.string.m1401_b003);

                    //change background color
                    u_setalpha();
                    btnNet.getBackground().setAlpha(150);

                    switch (iComPlay) {
                        case 1:
                            answer = getString(tw.tcnr01.m1401.R.string.m1401_f000) + getString(tw.tcnr01.m1401.R.string.m1401_f002); // 輸
                            txtResult.setTextColor(getResources().getColor(tw.tcnr01.m1401.R.color.Red)); // 輸用紅顯示
                            music(3);
                            showNotification( getString(tw.tcnr01.m1401.R.string.m1401_f002) + Integer.toString(miCountDraw) +getString(tw.tcnr01.m1401.R.string.m1401_table));

                            break;
                        case 2:
                            answer = getString(tw.tcnr01.m1401.R.string.m1401_f000) + getString(tw.tcnr01.m1401.R.string.m1401_f001); // 贏
                            txtResult.setTextColor(getResources().getColor(tw.tcnr01.m1401.R.color.Lime)); // 贏用綠顯示
                            music(1);
                            showNotification( getString(tw.tcnr01.m1401.R.string.m1401_f001) + Integer.toString(miCountDraw) +getString(tw.tcnr01.m1401.R.string.m1401_table));

                            break;
                        case 3:
                            answer = getString(tw.tcnr01.m1401.R.string.m1401_f000) + getString(tw.tcnr01.m1401.R.string.m1401_f003); // 平
                            txtResult.setTextColor(getResources().getColor(tw.tcnr01.m1401.R.color.Yellow)); // 平用黃顯示
                            music(2);
                            showNotification( getString(tw.tcnr01.m1401.R.string.m1401_f003) + Integer.toString(miCountDraw) +getString(tw.tcnr01.m1401.R.string.m1401_table));

                            break;
                    }
                    break;
            }

            //--------電腦出拳增加動畫---------------
            txtComPlay.clearAnimation();
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), tw.tcnr01.m1401.R.anim.anim_trans_in);
            anim.setInterpolator(new BounceInterpolator());
            txtComPlay.setAnimation(anim);
            //------------------------------------

            //----log----------
            Log.d(TAG,"btn01On");
            txtSelect.setText(user_select);
            txtResult.setText(answer);
        }




    };

    private void u_setalpha() {
        //imageButton 背景為銀色且為全透明
        btnScissors.setBackgroundColor(ContextCompat.getColor(this, tw.tcnr01.m1401.R.color.Silver));
        btnScissors.getBackground().setAlpha(0);
        btnStone.setBackgroundColor(ContextCompat.getColor(this, tw.tcnr01.m1401.R.color.Silver));
        btnStone.getBackground().setAlpha(0);
        btnNet.setBackgroundColor(ContextCompat.getColor(this, tw.tcnr01.m1401.R.color.Silver));
        btnNet.getBackground().setAlpha(0);


    }

    private void music(int i) {
        //--中斷播放中音樂--
        if (startmusic.isPlaying()) startmusic.stop();
        if (mediaWin.isPlaying()) mediaWin.pause();
        if (mediaDraw.isPlaying()) mediaDraw.pause();
        if (mediaLose.isPlaying()) mediaLose.pause();
        //--
        switch (i) {
            case 1: //贏
                mediaWin.start();
//                Toast.makeText(getApplicationContext(),                //context       The context to use. Usually your Application or Activity object.
//                        getText(R.string.m1401_f001),                           //text              The text to show. Can be formatted text.
//                        Toast.LENGTH_LONG)                                      //duration      How long to display the message. Either LENGTH_SHORT or LENGTH_LONG
//                        .show();

                miCountPlayerWin++;

                break;
            case 2: //平
                mediaDraw.start();
//                Toast.makeText(getApplicationContext(),                //context       The context to use. Usually your Application or Activity object.
//                        getText(R.string.m1401_f003),                           //text              The text to show. Can be formatted text.
//                        Toast.LENGTH_LONG)                                      //duration      How long to display the message. Either LENGTH_SHORT or LENGTH_LONG
//                        .show();
                miCountDraw++;
                break;
            case 3: //輸
                mediaLose.start();
//                Toast.makeText(getApplicationContext(),                //context       The context to use. Usually your Application or Activity object.
//                        getText(R.string.m1401_f002),                           //text              The text to show. Can be formatted text.
//                        Toast.LENGTH_LONG)                                      //duration      How long to display the message. Either LENGTH_SHORT or LENGTH_LONG
//                        .show();
                miCountComWin++;
                break;

            case 4: //close music
                startmusic.stop();
                startmusic = MediaPlayer.create(getApplication(), tw.tcnr01.m1401.R.raw.guess);
                startmusic.start();
                break;

        }

        //----log----------
        Log.d(TAG,"music");
        miCountSet++;
    }


    @Override
    public View makeView() {
        ImageView imgv = new ImageView(this);
        imgv.setBackgroundColor(0x00000000);
        imgv.setLayoutParams(new ImageSwitcher.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));


        //----log----------
        Log.d(TAG,"makeView");
        return imgv;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startmusic.stop();
        //----log----------
        Log.d(TAG,"onBackPressed");
    }

    @Override
    public void finish() {
        super.finish();
        // ---關機動畫---
        overridePendingTransition( tw.tcnr01.m1401.R.anim.anim_trans_out, tw.tcnr01.m1401.R.anim.anim_alpha_out);

       //---關機music---
       music(4);

        //----log----------
        Log.d(TAG,"finish");

    }

    @Override
    protected void onPause() {
        super.onPause();
        //----log----------
        Log.d(TAG,"onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //----log----------
        Log.d(TAG,"onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //---關機music---
        startmusic.stop();



        //----log----------
        Log.d(TAG,"onDestroy");

    }

    @Override
    public void onClick(View v) {
        int iComPlay = (int) (Math.random() * 3 + 1);
        // 1 - scissors, 2 - stone, 3 - net.
        switch (iComPlay) {
            case 1: //電腦:剪刀scissors
                user_select = getString(tw.tcnr01.m1401.R.string.m1401_s002) + getString(tw.tcnr01.m1401.R.string.m1401_b001) + " ";

                txtComPlay.setImageResource(tw.tcnr01.m1401.R.drawable.scissors); // 轉換ImageSwitcher剪刀
                break;
            case 2: //電腦:石頭stone
                user_select = getString(tw.tcnr01.m1401.R.string.m1401_s002) + getString(tw.tcnr01.m1401.R.string.m1401_b002) + " ";
                txtComPlay.setImageResource(tw.tcnr01.m1401.R.drawable.stone); // 轉換ImageSwitcher石頭
                break;
            case 3: // 電腦:布net
                user_select = getString(tw.tcnr01.m1401.R.string.m1401_s002) + getString(tw.tcnr01.m1401.R.string.m1401_b003) + " ";
                txtComPlay.setImageResource(tw.tcnr01.m1401.R.drawable.net); // 轉換ImageSwitcher布
                break;
        }
        switch (v.getId()) {

            case tw.tcnr01.m1401.R.id.m1401_b001:
                // 選擇 剪刀scissors
                user_select += getString(tw.tcnr01.m1401.R.string.m1401_s001) + getString(tw.tcnr01.m1401.R.string.m1401_b001);
                //change background color
                u_setalpha();
                btnScissors.getBackground().setAlpha(150);


                switch (iComPlay) {
                    case 1:
                        answer = getString(tw.tcnr01.m1401.R.string.m1401_f000) + getString(tw.tcnr01.m1401.R.string.m1401_f003); // 平
                        txtResult.setTextColor(getResources().getColor(tw.tcnr01.m1401.R.color.Yellow)); // 平用黃顯示
                        music(2);
                        showNotification( getString(tw.tcnr01.m1401.R.string.m1401_f003) + Integer.toString(miCountDraw) +getString(tw.tcnr01.m1401.R.string.m1401_table));
                        break;
                    case 2:
                        answer = getString(tw.tcnr01.m1401.R.string.m1401_f000) + getString(tw.tcnr01.m1401.R.string.m1401_f002); // 輸
                        txtResult.setTextColor(getResources().getColor(tw.tcnr01.m1401.R.color.Red)); // 輸用紅顯示
                        music(3);
                        showNotification( getString(tw.tcnr01.m1401.R.string.m1401_f002) + Integer.toString(miCountDraw) +getString(tw.tcnr01.m1401.R.string.m1401_table));
                        break;
                    case 3:
                        answer = getString(tw.tcnr01.m1401.R.string.m1401_f000) + getString(tw.tcnr01.m1401.R.string.m1401_f001); // 贏
                        txtResult.setTextColor(getResources().getColor(tw.tcnr01.m1401.R.color.Lime)); // 贏用綠顯示
                        music(1);
                        showNotification( getString(tw.tcnr01.m1401.R.string.m1401_f001) + Integer.toString(miCountDraw) +getString(tw.tcnr01.m1401.R.string.m1401_table));
                        break;
                }

                break;
            //----------------------------------------------
            case tw.tcnr01.m1401.R.id.m1401_b002:
                // 選擇 石頭stone
                user_select += getString(tw.tcnr01.m1401.R.string.m1401_s001)  + getString(tw.tcnr01.m1401.R.string.m1401_b002);
                //change background color
                u_setalpha();
                btnStone.getBackground().setAlpha(150);

                switch (iComPlay) {
                    case 1:
                        answer = getString(tw.tcnr01.m1401.R.string.m1401_f000) + getString(tw.tcnr01.m1401.R.string.m1401_f001); // 贏
                        txtResult.setTextColor(getResources().getColor(tw.tcnr01.m1401.R.color.Lime)); // 贏用綠顯示
                        music(1);
                        showNotification( getString(tw.tcnr01.m1401.R.string.m1401_f001) + Integer.toString(miCountDraw) +getString(tw.tcnr01.m1401.R.string.m1401_table));
                        break;
                    case 2:
                        answer = getString(tw.tcnr01.m1401.R.string.m1401_f000) + getString(tw.tcnr01.m1401.R.string.m1401_f003); // 平
                        txtResult.setTextColor(getResources().getColor(tw.tcnr01.m1401.R.color.Yellow)); // 平用黃顯示
                        music(2);
                        showNotification( getString(tw.tcnr01.m1401.R.string.m1401_f003) + Integer.toString(miCountDraw) +getString(tw.tcnr01.m1401.R.string.m1401_table));
                        break;
                    case 3:
                        answer = getString(tw.tcnr01.m1401.R.string.m1401_f000) + getString(tw.tcnr01.m1401.R.string.m1401_f002); // 輸
                        txtResult.setTextColor(getResources().getColor(tw.tcnr01.m1401.R.color.Red)); // 輸用紅顯示
                        music(3);
                        showNotification( getString(tw.tcnr01.m1401.R.string.m1401_f002) + Integer.toString(miCountDraw) +getString(tw.tcnr01.m1401.R.string.m1401_table));
                        break;
                }
                break;
            //---------------------------------------------
            case tw.tcnr01.m1401.R.id.m1401_b003:

                // 選擇 布net
                user_select += getString(tw.tcnr01.m1401.R.string.m1401_s001) + getString(tw.tcnr01.m1401.R.string.m1401_b003);

                //change background color
                u_setalpha();
                btnNet.getBackground().setAlpha(150);

                switch (iComPlay) {
                    case 1:
                        answer = getString(tw.tcnr01.m1401.R.string.m1401_f000) + getString(tw.tcnr01.m1401.R.string.m1401_f002); // 輸
                        txtResult.setTextColor(getResources().getColor(tw.tcnr01.m1401.R.color.Red)); // 輸用紅顯示
                        music(3);
                        showNotification( getString(tw.tcnr01.m1401.R.string.m1401_f002) + Integer.toString(miCountDraw) +getString(tw.tcnr01.m1401.R.string.m1401_table));

                        break;
                    case 2:
                        answer = getString(tw.tcnr01.m1401.R.string.m1401_f000) + getString(tw.tcnr01.m1401.R.string.m1401_f001); // 贏
                        txtResult.setTextColor(getResources().getColor(tw.tcnr01.m1401.R.color.Lime)); // 贏用綠顯示
                        music(1);
                        showNotification( getString(tw.tcnr01.m1401.R.string.m1401_f001) + Integer.toString(miCountDraw) +getString(tw.tcnr01.m1401.R.string.m1401_table));

                        break;
                    case 3:
                        answer = getString(tw.tcnr01.m1401.R.string.m1401_f000) + getString(tw.tcnr01.m1401.R.string.m1401_f003); // 平
                        txtResult.setTextColor(getResources().getColor(tw.tcnr01.m1401.R.color.Yellow)); // 平用黃顯示
                        music(2);
                        showNotification( getString(tw.tcnr01.m1401.R.string.m1401_f003) + Integer.toString(miCountDraw) +getString(tw.tcnr01.m1401.R.string.m1401_table));

                        break;
                }
                break;
        }

        //--------電腦出拳增加動畫---------------
        txtComPlay.clearAnimation();
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), tw.tcnr01.m1401.R.anim.anim_trans_in);
        anim.setInterpolator(new BounceInterpolator());
        txtComPlay.setAnimation(anim);
        //------------------------------------

        //----log----------
        Log.d(TAG,"btn01On");
        txtSelect.setText(user_select);
        txtResult.setText(answer);


    }

    private void showNotification(String s) {
        // 宣告鈴聲
        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100); // 100=max
        toneG.startTone(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE, 200);
        toneG.stopTone();

        Intent it = new Intent(getApplicationContext(), GameResult.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putInt("KEY_COUNT_SET", miCountSet);
        bundle.putInt("KEY_COUNT_PLAYER_WIN", miCountPlayerWin);
        bundle.putInt("KEY_COUNT_COM_WIN", miCountComWin);
        bundle.putInt("KEY_COUNT_DRAW", miCountDraw);
        it.putExtras(bundle);
        PendingIntent penIt = PendingIntent.getActivity(getApplicationContext(), 0, it,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Notification noti = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.btn_star_big_on)
                .setTicker(s)
                .setContentTitle(getString(tw.tcnr01.m1401.R.string.app_name))
                .setContentText(s)
                .setContentIntent(penIt).build();

        NotificationManager notiMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notiMgr.notify(NOTI_ID, noti);
    }

    private void u_cleardata() {
        // 清除SharedPreferences資料
        SharedPreferences gameResultData =
                getSharedPreferences("GAME_RESULT", 0);

        gameResultData
                .edit()
                .clear()
                .commit();


        miCountSet = 0;
        miCountPlayerWin = 0;
        miCountComWin = 0;
        miCountDraw = 0;

        Toast.makeText(M1401.this, "清除完成", Toast.LENGTH_LONG).show();

    }

    private void u_loaddata() {
        // 載入SharedPreferences資料
        SharedPreferences gameResultData =
                getSharedPreferences("GAME_RESULT", 0);

        miCountSet = gameResultData.getInt("COUNT_SET", 0);
        miCountPlayerWin = gameResultData.getInt("COUNT_PLAYER_WIN", 0);
        miCountComWin = gameResultData.getInt("COUNT_COM_WIN", 0);
        miCountDraw = gameResultData.getInt("COUNT_DRAW", 0);

        Toast.makeText(M1401.this, "載入完成", Toast.LENGTH_LONG).show();

    }

    private void u_savedata() {
        // 儲存SharedPreferences資料
        SharedPreferences gameResultData =
                getSharedPreferences("GAME_RESULT", 0);

        gameResultData
                .edit()
                .putInt("COUNT_SET", miCountSet)
                .putInt("COUNT_PLAYER_WIN", miCountPlayerWin)
                .putInt("COUNT_COM_WIN", miCountComWin)
                .putInt("COUNT_DRAW", miCountDraw)
                .commit();

        Toast.makeText(M1401.this, "儲存完成", Toast.LENGTH_LONG).show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.m1401, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnShowResult:
                mBtnShowResult.performClick();
                break;

            case tw.tcnr01.m1401.R.id.btnSaveResult: // 儲存
                u_savedata();

                break;

            case tw.tcnr01.m1401.R.id.btnLoadResult: // 載入
                u_loaddata();
                break;

            case tw.tcnr01.m1401.R.id.btnClearResult: // 清除
                u_cleardata();
                break;

            case tw.tcnr01.m1401.R.id.btnaboutme:
                new AlertDialog.Builder(M1401.this)
                        .setTitle("Preferences範例程式")
                        .setMessage("TCNR雲端製作")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.star_big_on)
                        .setPositiveButton("確定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                    }
                                })
                        .show();
                break;

            case tw.tcnr01.m1401.R.id.action_settings:
                u_savedata();
                this.finish();
                break;




        }
        return super.onOptionsItemSelected(item);
    }

  
}



