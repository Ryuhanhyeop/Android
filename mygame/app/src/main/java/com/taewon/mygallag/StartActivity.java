package com.taewon.mygallag;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    int characterId, effectId;

    ImageButton startBtn;

    TextView guideTv;

    MediaPlayer mediaPlayer;

    ImageView imgView[] = new ImageView[8];

    Integer img_id[] = {R.id.ship_001,R.id.ship_002,R.id.ship_003,R.id.ship_004,R.id.ship_005,R.id.ship_006,R.id.ship_007,R.id.ship_008};

    Integer img[] = {R.drawable.ship_0000,R.drawable.ship_0001,R.drawable.ship_0002,
            R.drawable.ship_0003,R.drawable.ship_0004,R.drawable.ship_0005,R.drawable.ship_0006,R.drawable.ship_0007 };

    SoundPool soundPool;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mediaPlayer = MediaPlayer.create(this, R.raw.robby_bgm);
        mediaPlayer.setLooping(true); //반복
        mediaPlayer.start();
        soundPool=new SoundPool(5, AudioManager.USE_DEFAULT_STREAM_TYPE ,0); //1. 5개까지 2.
        effectId = soundPool.load(this,R.raw.reload_sound,1); //1. 현재 화면에 2. raw파일에 있는 음악파일
        startBtn = findViewById(R.id.startBtn); //시작버튼
        guideTv = findViewById(R.id.guideTv);

        for(int i = 0;i< imgView.length; i++){
            imgView[i] = findViewById(img_id[i]);
            int index = i; //선택한 이미지 번호 알기
            imgView[i].setOnClickListener(view -> {
                characterId = img[index];
                startBtn.setVisibility(View.VISIBLE);
                startBtn.setEnabled(true);  //시작버튼 활성화
                startBtn.setImageResource(characterId); //버튼에 선택한 이미지 넣기
                guideTv.setVisibility(View.INVISIBLE);  //마지막 TextView 숨기기
                soundPool.play(effectId, 1,1,0,0,1.0f);
                //볼륨 레벨, 좌우 밸런스, 우선 순위, 루프 및 재생 속도

            });
        }
            init();
        }
        private void init() {
        findViewById(R.id.startBtn).setVisibility(View.GONE);  //버튼 위치는 남겨두고 숨기기
        findViewById(R.id.startBtn).setEnabled(false);  //선택 안되게 하기
        findViewById(R.id.startBtn).setOnClickListener(view->{
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("character",characterId);  //선택한 이미지 넘기기
            startActivity(intent);
            finish();
        });
        }

    @Override
    protected void onDestroy() { //액티비티 소멸 직전 호출 mediaPlayer가 살아있으면 리소스 소멸시킨다.
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

