package com.smulk.bitblast.graphics;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.opengl.GLSurfaceView;

import com.smulk.awesomeblaster.R;
import com.smulk.bitblast.data.GameData;
import com.smulk.bitblast.gameElements.Hero;

public class HUD
{
  private static String scoreLabel;
      
  public static void displayScore(Canvas canvas, GameData gamedata)
  {
    scoreLabel = "Score: " + gamedata.getScore();
    Paint text = new Paint();
    text.setColor(Color.WHITE);
    text.setTextSize(50);
    text.setTextAlign(Align.CENTER);
    canvas.drawText(scoreLabel, canvas.getWidth()/2, 50, text);
  }
  
  public static void displayPauseButton(GL10 gl, SpriteBatcher spriteBatcher, GameData gamedata, Images images)
  {
    gamedata.BBplayPause.create(0, 0, images.getPause());
    int height = images.getPause().getHeight();
    int width = images.getPause().getWidth();
    Rect dest = new Rect(0, 0, width, height);
    spriteBatcher.draw(gl, R.drawable.pause, new Rect(0,0,width, height), dest);
    //canvas.drawBitmap(images.getPause(), 0, 0, null);
  }
  
  public static void displayHearts(GLSurfaceView surface, GL10 gl, SpriteBatcher spriteBatcher, GameData gamedata, Images images, Hero hero)
  {
    int height = images.getHeart().getHeight();
    int width = images.getHeart().getWidth();
    int mX;
    int mY = surface.getHeight() - height;
    for (int i = 0; i < hero.getHealth(); i++)
    {   
      mX = i*width;
      Rect dest = new Rect(mX, mY, mX + width, mY + height);
      spriteBatcher.draw(gl, R.drawable.heart, new Rect(0,0,width, height), dest);
      //canvas.drawBitmap(images.getProgress(), i*images.getProgress().getWidth(), canvas.getHeight() - images.getProgress().getHeight(), null);
    }
  }
}
