package com.taewon.mygallag.sprites;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.taewon.mygallag.MainActivity;
import com.taewon.mygallag.SpaceInvadersView;
import com.taewon.mygallag.items.HealitemSprite;
import com.taewon.mygallag.items.PowerItemSprite;
import com.taewon.mygallag.items.SpeedItemSprite;

import java.util.ArrayList;
import java.util.Random;

public class AlienSprite extends Sprite{

    private Context context;
    private SpaceInvadersView game;
    ArrayList<AlienShotSprite> alienShotSprites; //외계인 총알을 관리하는 배열

    Handler fireHandler = null;
    //1. Handler를 통해 Main Thread의 MessageQueue에 Message를 전달 (FIFO 방식)
    //2. Looper를 통해 MessageQueue에서 Message를 꺼내 처리하는 역할 수행
    // Handler는 스레드마다 여러개 생성 가능
    boolean isDestroyed = false;

    //AlienSprite 객체가 생성될 때 호출
    // 매개변수 (context, SpaceInvadersView, 적 이미지 리소스, 적 생성 위치 좌표(x,y)
    public AlienSprite(Context context, SpaceInvadersView game, int resourceId, int x,int y) {
        super(context, resourceId, x, y); //Sprite 생성자 호출
        //외계인 만들기
        this.context=context;
        this.game=game;
        alienShotSprites = new ArrayList<>();
        Random r = new Random();
        int randomDx = r.nextInt(5);
        int randomDy = r.nextInt(5);
        if(randomDy <= 0 ) dy=1;
        dx = randomDx; dy=randomDy;
        //Handler()사용 X, 핸들러 생성 중에 암시적으로 루퍼가 선택되면 작업이 자동으로 손실되는 버그(핸들러가 새 작업을 예상하지 않고 종료되는 경우),
        // 충돌(루퍼가 활성화되지 않은 스레드에서 핸들러가 생성되는 경우) 또는 경합 상태가 발생할 수 있습니다.
        // Looper를 명시적으로 선언해줘야해서 아래 Handler방식으로 사용함
        fireHandler = new android.os.Handler(Looper.getMainLooper());
        fireHandler.postDelayed( //Runnable이 메시지 대기열에 추가되고 지정된 시간 경과 후 실행
                new Runnable() {
                    @Override
                    public void run() {
                        Log.d("run", "동작: ");
                        Random r= new Random();
                        boolean isFire = r.nextInt(100)+1 <=30;
                        if(isFire && !isDestroyed){
                            fire();
                            fireHandler.postDelayed(this,1000);
                        }
                    }
                },1000);

    }

    @Override
    public void move() {
        super.move();
        if(((dx<0) && (x <10)) || ((dx >0) && (x>800))){
            dx = -dx;
            if(y>game.screenH){game.removeSprite(this);} //SpaceInvadersView
        }
    }

    @Override
    public void handleCollision(Sprite other) {
        if(other instanceof ShotSprite){
            game.removeSprite(other);
            game.removeSprite(this);
            destroyAlien();
            return;
        }
        if(other instanceof SpecialshotSprite){
            game.removeSprite(this);
            destroyAlien();
            return;
        }
    }
    
    private void destroyAlien(){
        isDestroyed = true;
        game.setCurrEnemycount(game.getCurrEnemyCount()-1);
        for(int i = 0;i<alienShotSprites.size(); i++)
            game.removeSprite(alienShotSprites.get(i));
        spawnHealItem();
        spawnPowerItem();
        spawnSpeedItem();
        game.setScore(game.getScore() +1);
        MainActivity.scoreTv.setText(Integer.toString(game.getScore()));
    }

    private void fire(){
        AlienShotSprite alienShotSprite = new AlienShotSprite(context, game, getX(), getY()+30, 16);
        alienShotSprites.add(alienShotSprite); //AlienShotSprite을 위한 ArrayList에 추가
        game.getSprites().add(alienShotSprite); //SpaceInvadersView의 ArrayList에 추가
    }

    private void spawnSpeedItem(){
        Random r = new Random();
        int speedItemDrop = r.nextInt(100) + 1;
        if(speedItemDrop <=20){
            int dx = r.nextInt(10) +1;
            int dy = r.nextInt(10) +5;
            game.getSprites().add(new SpeedItemSprite(context,game, (int) this.getX(), (int) this.getY(),dx,dy));
        }
    }

    private void spawnPowerItem(){
        Random r = new Random();
        int powerItemDrop = r.nextInt(100) + 1;
        if(powerItemDrop <=15){
            int dx = r.nextInt(10) +1;
            int dy = r.nextInt(10) +10;
            game.getSprites().add(new PowerItemSprite(context,game, (int) this.getX(), (int) this.getY(),dx,dy));
        }
    }

    private void spawnHealItem(){
        Random r = new Random();
        int healItemDrop = r.nextInt(100) + 1;
        if(healItemDrop <= 10){
            int dx = r.nextInt(10) +1;
            int dy = r.nextInt(10) +10;
            game.getSprites().add(new HealitemSprite(context,game, (int) this.getX(), (int) this.getY(),dx,dy));
        }
    }
}
