package com.taewon.mygallag.items;


import android.content.Context;

import com.taewon.mygallag.R;
import com.taewon.mygallag.SpaceInvadersView;
import com.taewon.mygallag.sprites.Sprite;

import java.util.Timer;
import java.util.TimerTask;

public class HealitemSprite extends Sprite {
    //Sprite class를 상속받음
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
    SpaceInvadersView game; //SpaceInvadersView 객체를 담는 변수

    public HealitemSprite(Context context,SpaceInvadersView game,  int x, int y, int dx, int dy) {
        //HealitemSprite 객체를 만들 때 호출됨
        //매개변수 (1. context, 2. SpaceInvadersView, 3,4. 아이템 생성위치 좌표(x,y), 5,6. 가로 세로로 움직일 거리(속도)
        super(context, R.drawable.heal_item,x,y); //Sprite의 생성자를 호출해 매개변수 값으로 item생성 (context, 아이템 이미지 리소스, 좌표값)
        this.game=game;
        this.dx=dx; //가로로 움직이는 거리
        this.dy=dy; //세로로 움직이는 거리
        new Timer().schedule(new TimerTask() { //Timer를 이용해 10초뒤에 autoRemove()를 호출 -> Timer는 질문하실 수 있으니 찾아보시면 됩니다.
            @Override
            public void run() {
                autoRemove();
            }
        },10000); //1초 = 1000ms

    }
    private void autoRemove() {game.removeSprite(this);} //game = SpaceInvadersView. removeSprite를 호출해 현재 객체(item)울 삭제

    @Override
    public void move() { //0.01초마다 조건에 맞게 움직임 (SpaceInvadersView에 있습니다) 디스플레이의 범위에 닿으면 움직이는 방향을 반대로 바꿔줌
        if((dx < 0) && (x<120)){
            dx *= -1; return;
        }
        if((dx>0)&&(x > game.screenW - 120)){
            dx *= -1; return;
        }
        if((dy < 0) && (y<120)){
            dy *= -1; return;
        }
        if((dy>0)&&(y > game.screenH - 120)){
            dy *= -1; return;
        }
        super.move(); //Sprite의 move를 통해 움직인다
    }
}
