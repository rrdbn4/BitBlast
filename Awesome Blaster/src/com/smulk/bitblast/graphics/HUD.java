package com.smulk.bitblast.graphics;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.opengl.GLSurfaceView;

import com.smulk.bitblast.R;
import com.smulk.bitblast.data.GameData;
import com.smulk.bitblast.game_elements.Hero;

public class HUD
{
  private String scoreLabel;
  
  private Bitmap pauseButton;
  private Bitmap heart;
  
  public HUD(Context context)
  {
  	heart = BitmapFactory.decodeResource(context.getResources(), R.drawable.heart);
  	pauseButton = BitmapFactory.decodeResource(context.getResources(), R.drawable.pause);
  }
      
  public void displayScore(Canvas canvas, GameData gamedata)
  {
    scoreLabel = "Score: " + gamedata.getScore();
    Paint text = new Paint();
    text.setColor(Color.WHITE);
    text.setTextSize(50);
    text.setTextAlign(Align.CENTER);
    canvas.drawText(scoreLabel, canvas.getWidth()/2, 50, text);
  }
  
  public void displayPauseButton(GL10 gl, SpriteBatcher spriteBatcher, GameData gamedata)
  {
    gamedata.BBplayPause.create(0, 0, pauseButton);
    int height = pauseButton.getHeight();
    int width = pauseButton.getWidth();
    Rect dest = new Rect(0, 0, width, height);
    spriteBatcher.draw(gl, R.drawable.pause, new Rect(0,0,width, height), dest);
  }
  
  public void displayHearts(GLSurfaceView surface, GL10 gl, SpriteBatcher spriteBatcher, GameData gamedata, Hero hero)
  {
    int height = heart.getHeight();
    int width = heart.getWidth();
    int mX;
    int mY = surface.getHeight() - height;
    for (int i = 0; i < hero.getHealth(); i++)
    {   
      mX = i * width;
      Rect dest = new Rect(mX, mY, mX + width, mY + height);
      spriteBatcher.draw(gl, R.drawable.heart, new Rect(0,0,width, height), dest);
    }
  }
}
