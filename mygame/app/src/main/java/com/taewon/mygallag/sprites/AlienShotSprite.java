package com.taewon.mygallag.sprites;

import android.content.Context;

import com.taewon.mygallag.R;
import com.taewon.mygallag.SpaceInvadersView;

public class AlienShotSprite extends Sprite{
    private Context context;
    private SpaceInvadersView game;
    //AilenShotSprite객체를 생성할 때 호출됨
    //매개변수로 (context, SpaceInvadersView객체, 총알의 생성위치 좌표(x,y), 총알 속도
    public AlienShotSprite(Context context, SpaceInvadersView game,
                           float x, float y, int dy) {
        super(context, R.drawable.shot_001, x, y); //Sprite객체로 총알 생성(context, 총알 이미지 리소스, 좌표값)
        this.game = game; //SpaceInvadersView
        this.context = context; //context
        setDy(dy); //Sprite 속도값 설정
    }
}
