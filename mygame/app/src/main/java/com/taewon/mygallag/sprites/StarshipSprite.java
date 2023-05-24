package com.taewon.mygallag.sprites;


import android.content.Context;
import android.graphics.RectF;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.taewon.mygallag.MainActivity;
import com.taewon.mygallag.R;
import com.taewon.mygallag.SpaceInvadersView;
import com.taewon.mygallag.items.HealitemSprite;
import com.taewon.mygallag.items.PowerItemSprite;
import com.taewon.mygallag.items.SpeedItemSprite;

import java.util.ArrayList;

/*
상속
1. extends (일반 클래스, abstract 클래스, 인터페이스끼리 상속 시 사용)
- 클래스 확장 개념
- 부모에서 선언/정의
- 자식에서 오버라이딩 없이도 부모의 메소드/변수 사용가능 (필요에 따라 오버라이딩 가능)
- 다중 상속 X (부모 클래스 1개)

2. implements ( class가 interface 사용 시 implements사용)
- 인터페이스 구현 개념
- 부모에서 선언, 자식에서 정의 - 오버라이딩(재정의)
- 여러개 사용 가능
- 인터페이스에 선언된 메소드를 모두 구현해야한다

3. abstract ( extends + implements )
- 일반클래스의 기능 + 추상 메소드로 구현되어 있다
- 반드시 재정의가 필요한 추상 메소드만 재정의 할 수 있게 만들어준다.
- 인스턴스를 생성할 수 없다 - 자식 클래스에서 추상클래스의 추상메소드를 모두 오버라이딩 후 인스턴스 생성 가능

*/
public class StarshipSprite extends Sprite{ //Sprite클래스를 상속받는다

    Context context; //컨텍스트를 저장하는 변수
    //현재 액티비티와 애플리케이션에 대한 정보를 얻기 위해 사용
    SpaceInvadersView game;
    public float speed;
    private int bullets, life=3, powerLevel;
    private int specialShotCount;
    private boolean isSpecialShooting;
    private static ArrayList<Integer> bulletSprites = new ArrayList<Integer>();
    private final static float MAX_SPEED = 3.5f;
    private final static  int MAX_HEART = 3;
    private RectF rectF;
    private boolean isReloading = false;

    //StarshipSprite 객체가 생성될 때 호출
    //매개변수 ( context, SpaceInvadersView, 이미지 리소스, 캐릭터 생성 위치 좌표(x,y), speed 기본값)
    public StarshipSprite(Context context, SpaceInvadersView game,
                          int resId, int x, int y, float speed){
        super(context,resId,x,y); //Sprite 생성자 호출
        this.context = context;
        this.game = game;
        this.speed = speed; //1.5
        init(); //init() 호출
    }
 //총알의 형태변경
    public void init(){ //캐릭터 객체 초기화
        dx=dy=0; //캐릭터의 이동거리 초기화
        bullets=30; //총알 30발
        life=3; //생명 3개
        specialShotCount=3; //필살기 3개
        powerLevel=0; //총알 모양 레벨 0
        bulletSprites.clear(); //총알모양 배열 초기화, 없으면 2번째 플레이에서 이상발생
        //총알 모양 리소스를 저장하는 배열
        Integer [] shots = {R.drawable.shot_001,R.drawable.shot_002,R.drawable.shot_003,R.drawable.shot_004,
                R.drawable.shot_005,R.drawable.shot_006,R.drawable.shot_007};
        //반복문을 이용해 배열에 저장된 수만큼 ArrayList에 넣어준다
        for(int i = 0;i<shots.length;i++){
            bulletSprites.add(shots[i]);
        }
    }

    public void move() {
        //벽에 부딪히면 캐릭터 이동 제한
        if((dx<0)&&(x<120)) return;
        if((dx>0)&&(x> game.screenW - 120)) return;
        if((dy<0)&&(y<120)) return;
        if((dy>0)&&(y > game.screenH-120)) return;
        super.move(); //Sprite를 이용해 캐릭터 이동 처리
    }
    //총알 개수 리턴
    public int getBulletsCount() {return bullets;} //총알 개수를 리턴한다

    //위 아래 오른쪽, 왼쪽 이동하기
    //force = 0~10까지
    public void moveRight(double force){setDx((float)(1*force*speed));} // 조이스틱을 움직인 거리와 캐릭터의 속도에 비례
    public void moveLeft(double force){setDx((float)(-1*force*speed));}
    public void moveDown(double force){setDy((float)(1*force*speed));}
    public void moveUp(double force){setDy((float)(-1*force*speed));}

    public void resetDx() {setDx(0);} //캐릭터의 이동거리 초기화
    public void resetDy() {setDy(0);}

    //스피드제어
    public void plusSpeed(float speed) {this.speed += speed;} //캐릭터의 이동속도 증가

    //총알 발사
    //총알 발사 버튼 클릭, (재장전, 필살기 사용)이 아닐때 총알을 생성해 게임에 추가하고 총알 개수 1 감소, 총알 0이면 재장전
    public void fire() {
        if(isReloading | isSpecialShooting) {return;} //재장전(2초) 또는 필살기 사용(5초) 중일 경우 동작 X
        MainActivity.effectSound(MainActivity.PLAYER_SHOT); //메소드 호출, PLAYER_SHOT = 0
        //SoundPool.load로 이미 메모리에 저장해놓고 필요할 때 각각 사용할 수 있다

        //ShotSprite 객체 생성
        //context ,현재 총알 버전, 총알을 생성할 좌표 x,y값, 총알 이동 거리(속도)
        ShotSprite shot = new ShotSprite(context,game,bulletSprites.get(powerLevel),getX()+10,getY()-30,-16);

        //SpaceInvadersView의 getSprites() 구현
        game.getSprites().add(shot);
        bullets--;

        MainActivity.bulletCount.setText(bullets + "/30");
        Log.d("bullets", bullets+"/30");
        if(bullets == 0){
            reloadBullets(); //재장전 메소드 호출
            return;
        }
    }

    //PowerItem과 충돌 시
    // 총알 모양이 마지막 버전이면 스코어를 증가시키고 그렇지 않으면 총알 모양을 다음버전으로 변경
    public void powerUp(){
        if(powerLevel >= bulletSprites.size() - 1){
            game.setScore(game.getScore() +1);
            MainActivity.scoreTv.setText(Integer.toString(game.getScore()));
            return;
        }
        powerLevel++;
        MainActivity.fireBtn.setImageResource(bulletSprites.get(powerLevel));
        //MainActivity.fireBtn.setBackgroundResource(R.drawable.round_button_shape); 필요없음
    }

    //재장전 시
    // 1. 재장전 소리 재생(SoundPool)
    // 2. 총알발사, 재장전 비활성화
    // 3. Handler.postDelayed를 사용해 2초뒤 run 동작을 실행 - 총알발사, 재장전 활성화, 총알개수 30개,
    public void reloadBullets(){
        isReloading = true;
        MainActivity.effectSound(MainActivity.PLAYER_RELOAD);
        MainActivity.fireBtn.setEnabled(false);
        MainActivity.reloadBtn.setEnabled(false);

        //Thread sleep 사용하지 않고 지연시키는 클래스
        //MainThread를 사용하기 때문에 간단한 작업에 한해서 사용, android.os.Handler를사용해야 함
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bullets=30;
                MainActivity.fireBtn.setEnabled(true);
                MainActivity.reloadBtn.setEnabled(true);
                MainActivity.bulletCount.setText(bullets+"/30");
                MainActivity.bulletCount.invalidate(); //화면 새로고침 함수
                //해당 뷰 화면이 무효임을 안드로이드에 알리고 현재 뷰 상태를 반영해 새로화면을 그려줌
                isReloading=false;
            }
        },2000);
    }

    //필살기
    // 1. 필살기 개수 1 감소
    // 2. SpecialshotSprite 객체 생성 (context,SpaceInvadersView laser이미지,캐릭터 사각형의 범위 x,y

    public void specialShot(){
        specialShotCount--;
        //SpecailShotSprite 구현
        Log.d("Special", ""+getRect().right);
        Log.d("Special", ""+getRect().left);
        SpecialshotSprite shot = new SpecialshotSprite(context,game,R.drawable.laser,getRect().right - getRect().left,0);

        //game -> SpaceInvadersView 의 getSprites() : sprite에 shot 추가하기
        //게임에 추가
        game.getSprites().add(shot);
    }

    //현재 필살기 개수 반환
    public int getSpecialShotCount() {return specialShotCount;}

    public boolean isSpecialShooting() {return isSpecialShooting;}
    //필살기 사용 여부
    public void setSpecialShooting(boolean specialShooting) { isSpecialShooting = specialShooting;}
    public int getLife() {return  life;}

    //생명을 잃었을 때
    //생명 모두 소모 시 게임종료
    //아니면 생명 감소
    public void hurt(){
        life--;
        if(life<=0) {
            //life번째 생명을 빈 하트로 만든다
            //id가 할당되어 있지 않을 때 사용하는 방법, 0부터 시작
            ((ImageView) MainActivity.lifeFrame.getChildAt(life)).setImageResource((R.drawable.ic_baseline_favorite_border_24));
            //SpaceInvadersView의 endGame() 에서 game종료시키기
            game.endGame();
            return;
        }
        Log.d("hurt", Integer.toString(life)); //생명 확인하기
        ((ImageView)MainActivity.lifeFrame.getChildAt(life)).setImageResource(R.drawable.ic_baseline_favorite_border_24);
    }

    // 생명을 얻었을 때
    // 생명 최대치에서 아이템 획득 시 스코어 증가
    // 최대치가 아니면 채워진하트로 바꿔준다
    public void heal() {
        Log.d("heal", Integer.toString(life));
        if(life + 1 > MAX_HEART){
            game.setScore(game.getScore()+1);
            MainActivity.scoreTv.setText(Integer.toString(game.getScore()));
            return;
        }
        ((ImageView) MainActivity.lifeFrame.getChildAt(life)).setImageResource(R.drawable.ic_baseline_favorite_24);
        life++;
    }

    //속도 올리기
    // speed 기본값 : 1.5, MAX_SPEED : 3.5
    // 스피드가 3.5가 되면 이후 부터는 아이템 획득 시 스코어 증가
    // 최댓값이 아니면 plusSpeed를 호출해 speed 0.2 증가
    public void speedUp() {
        if(MAX_SPEED >= speed + 0.2f) plusSpeed(0.2f);
        else{
            game.setScore(game.getScore() + 1);
            MainActivity.scoreTv.setText(Integer.toString(game.getScore()));
        }
    }

    //Sprite의 handleCollision() -> 충돌처리
    //instanceof를 활용해 객체타입확인 -> 상속관계에서 주로 사용 (형 변환 가능 여부를 확인해 true, false 반환)
    //매개변수로 넘어온 객체의 객체 타입을 검사해서 알맞은 동작 구현
    public void handleCollision(Sprite other){
        if(other instanceof AlienSprite){
            //Alien 아이템이면
            game.removeSprite(other);
            MainActivity.effectSound(MainActivity.PLAYER_HURT);
            hurt();
        }
        if(other instanceof SpeedItemSprite){
            //스피드 아이템이면
            game.removeSprite(other);
            MainActivity.effectSound(MainActivity.PLAYER_GET_ITEM);
            speedUp();
        }
        if(other instanceof AlienShotSprite){
            //총알 맞으면
            MainActivity.effectSound(MainActivity.PLAYER_HURT);
            game.removeSprite(other);
            hurt();
        }
        if(other instanceof PowerItemSprite){
            //아이템을 맞으면

            MainActivity.effectSound(MainActivity.PLAYER_GET_ITEM);
            powerUp();
            game.removeSprite(other);
        }
        if(other instanceof HealitemSprite){
            //생명 아이템을 맞으면
            MainActivity.effectSound(MainActivity.PLAYER_GET_ITEM);
            game.removeSprite(other);
            heal();
        }
    }
    public int getPowerLevel() {return powerLevel;}

}
