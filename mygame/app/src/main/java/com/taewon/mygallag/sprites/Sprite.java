package com.taewon.mygallag.sprites;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

//최상위 클래스, 생성자로 데이터, bitmap받아와서 저장, rect
public class Sprite {
    protected float x,y;
    protected int width,height;
    protected float dx, dy;
    private Bitmap bitmap;
    protected int id;
    private RectF rect;

    public Sprite(Context context, int resourceId, float x, float y){
        this.id = resourceId;
        this.x = x; this.y = y;
        bitmap = BitmapFactory.decodeResource(context.getResources(),resourceId);
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        rect = new RectF();
    }

    //가로세로 길이 받아오기
    public int getWidth() {return width;}
    public int getHeight() {return height;}

    //canvas에 그려주기
    public void draw(Canvas canvas, Paint paint){canvas.drawBitmap(bitmap,x,y,paint);}

    //객체가 사각형 안에 들어가 있음
    public void move(){
        x=x+dx; y=y+dy;
        rect.left = x; rect.right = x + width;
        rect.top = y; rect.bottom = y + height;
    }
    public float getX() {return x;}
    public float getY() {return y;}
    public float getDx() {return dx;}
    public float getDy() {return dy;}

    public void setDx(float dx) {this.dx = dx;}
    public void setDy(float dy) {this.dy = dy;}

    public RectF getRect() {return rect;}

    public boolean checkCollision(Sprite other){
        return RectF.intersects(this.getRect(),other.getRect());
        //지정된 사각형이 이 사각형과 교차하면 true반환하고 이 사각형을 해당 교차점으로 설정하고, 그렇지 않으면 false 반환
    }

    public void handleCollision(Sprite other){ }//충돌처리를 위한
    public Bitmap getBitmap() {return bitmap;}
    public void setBitmap(Bitmap bitmap) {this.bitmap = bitmap;}

}
