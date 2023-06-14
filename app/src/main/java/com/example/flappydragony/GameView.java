package com.example.flappydragony;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameView extends View {
    private Dragon dragon;
    private Handler handler;
    private Runnable r;
    private ArrayList<Pipe> arrPipes;
    private int score, bestscore=0;
    private int sumpipe, distance;
    private Context context;
    private boolean start;
    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        SharedPreferences sp = context.getSharedPreferences("gamesetting", Context.MODE_PRIVATE);
        if(sp!=null){
            bestscore = sp.getInt("bestscore", 0);
        }
        this.context = context;
        bestscore = 0;
        start = false;
        score = 0;
        initDragon();
        initPipes();
        handler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                invalidate();

            }
        };
    }

    private void initPipes() {
        sumpipe = 6;
        distance = 250*Constants.SCREEN_HEIGHT/1920;
       arrPipes = new ArrayList<>();
       for (int i = 0; i < sumpipe; i++){
           if(i<sumpipe/2){
               this.arrPipes.add(new Pipe (Constants.SCREEN_WIDTH+i*((Constants.SCREEN_WIDTH+300*Constants.SCREEN_WIDTH/1080)/(sumpipe/2)),
                       0, 200*Constants.SCREEN_WIDTH/1080, Constants.SCREEN_HEIGHT/2));
               this.arrPipes.get(this.arrPipes.size()-1).setBm(BitmapFactory.decodeResource(this.getResources(), R.drawable.pipe2));
               this.arrPipes.get(this.arrPipes.size()-1).randomY();
           }else {
               this.arrPipes.add(new Pipe(this.arrPipes.get(i-sumpipe/2).getX(), this.arrPipes.get(i-sumpipe/2).getY()
               +this.arrPipes.get(i-sumpipe/2).getHeight() +this.distance, 200*Constants.SCREEN_WIDTH/1080, Constants.SCREEN_HEIGHT/2));
               this.arrPipes.get(this.arrPipes.size()-1).setBm(BitmapFactory.decodeResource(this.getResources(), R.drawable.pipe1));
           }
       }
    }

    private void initDragon() {
        dragon = new Dragon();
        dragon.setWidth(100*Constants.SCREEN_WIDTH/1080);
        dragon.setHeight(100*Constants.SCREEN_HEIGHT/1920);
        dragon.setX(100*Constants.SCREEN_WIDTH/1080);
        dragon.setY(Constants.SCREEN_HEIGHT/2-dragon.getHeight()/2);
        ArrayList<Bitmap> arrBms = new ArrayList<>();
        arrBms.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.dragon));
        arrBms.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.dragon2));
        dragon.setArrBms(arrBms);
    }

    public void draw(Canvas canvas){
        super.draw(canvas);
        if (start){
            dragon.draw(canvas);
            for (int i = 0; i<sumpipe; i++){
                if (dragon.getRect().intersect(arrPipes.get(i).getRect())||dragon.getY()-dragon.getHeight()<0||dragon.getY()>Constants.SCREEN_HEIGHT){
                    Pipe.speed = 0;
                    MainActivity.txt_score_over.setText(MainActivity.score.getText());
                    MainActivity.best_score.setText("best:" + bestscore);
                    MainActivity.score.setVisibility(INVISIBLE);
                    MainActivity.rl_game_over.setVisibility(VISIBLE);
                }
                if (this.dragon.getX()+this.dragon.getWidth()>arrPipes.get(i).getX()+arrPipes.get(i).getWidth()/2
                        &&this.dragon.getX()+this.dragon.getWidth()<arrPipes.get(i).getX()+arrPipes.get(i).getWidth()/2+Pipe.speed
                        &&i<sumpipe/2){
                    score++;
                    if (score>bestscore){
                        bestscore = score;
                        SharedPreferences sp = context.getSharedPreferences("gamesetting", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("bestscore", bestscore);
                        editor.apply();
                    }
                    MainActivity.score.setText(""+score);
                }
                if (this.arrPipes.get(i).getX()< -arrPipes.get(i).getWidth()){
                    this.arrPipes.get(i).setX(Constants.SCREEN_WIDTH);
                    if (i <sumpipe/2){
                        arrPipes.get(i).randomY();
                    }else{
                        arrPipes.get(i).setY(this.arrPipes.get(i-sumpipe/2).getY()
                                +this.arrPipes.get(i-sumpipe/2).getHeight() +this.distance);
                    }
                }
                this.arrPipes.get(i).draw(canvas);
            }
        }else{
            if (dragon.getY()>Constants.SCREEN_HEIGHT/2){
                dragon.setDrop(-15*Constants.SCREEN_HEIGHT/1920);

            }
            dragon.draw(canvas);
        }

        handler.postDelayed(r, 5);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            dragon.setDrop(-15);
        }
        return true;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public void reset() {
        MainActivity.score.setText("0");
        score=0;
        initPipes();
        initDragon();
    }
}
