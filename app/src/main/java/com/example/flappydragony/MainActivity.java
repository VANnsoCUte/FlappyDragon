package com.example.flappydragony;





import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static TextView score, best_score, txt_score_over;
    @SuppressLint("StaticFieldLeak")
    public static RelativeLayout rl_game_over;
    @SuppressLint("StaticFieldLeak")
    public static ImageButton start;
    @SuppressLint("StaticFieldLeak")
    public static ImageView img;
    public GameView gv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH =dm.widthPixels;
        Constants.SCREEN_HEIGHT =dm.heightPixels;
        setContentView(R.layout.activity_main);
        score = findViewById(R.id.score);
        best_score = findViewById(R.id.best_score);
        txt_score_over = findViewById(R.id.txt_score_over);
        rl_game_over = findViewById(R.id.rl_game_over);
        gv = findViewById(R.id.gv);
        start=findViewById(R.id.start);
        img = findViewById(R.id.img);
        start.setOnClickListener(v -> {
            gv.setStart(true);
            score.setVisibility(View.VISIBLE);
            start.setVisibility(View.INVISIBLE);
            img.setVisibility(View.INVISIBLE);
        });
        rl_game_over.setOnClickListener(view -> {
            start.setVisibility(View.VISIBLE);
            img.setVisibility(View.VISIBLE);
            rl_game_over.setVisibility(View.INVISIBLE);
            gv.setStart(false);
            gv.reset();
        });
    }
}