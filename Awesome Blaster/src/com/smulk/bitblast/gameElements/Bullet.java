package com.smulk.bitblast.gameElements;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.smulk.awesomeblaster.R;
import com.smulk.bitblast.data.GameData;
import com.smulk.bitblast.graphics.SpriteBatcher;
import com.smulk.bitblast.sound.Sound;

public class Bullet extends BaseObject
{
  private boolean beenHit = false;
  private boolean playSound = false;
  
  private int damage;
  private int defaultDamage;
  
  private int speed;
  private int resetSpeed;
  
  public Bullet(Context context, int rSpeed, int dDamage)
  {
    imageID = R.drawable.bullet;
    image = BitmapFactory.decodeResource(context.getResources(), imageID);
    setDimensions(image.getHeight(), image.getWidth());
    src = new Rect(srcX, srcY, width + srcX, height + srcY);
    
    resetSpeed = rSpeed;
    defaultDamage = dDamage;
    resetBullet();
  }

  public void resetBullet()
  {
    resetObject();
    speed = resetSpeed;
    damage = defaultDamage;
    beenHit = false;
  }
  
  public void shoot()
  {
    playSound = true;
    startMoving();
  }
  
  public void beenHit()
  {
    beenHit = true;
  } 
  
  public boolean hasBeenBit()
  {
    return beenHit;
  }
  
  public void setSpeed(int value)
  {
    speed = value;
  }
  
  public int getSpeed()
  {
    return speed;
  }
  
  public void setDefaultDamage(int value)
  {
    defaultDamage = value;
  }
  
  public int getDamage()
  {
    return damage;
  }
  
  public void update(GLSurfaceView surface, GameData gamedata, Hero hero, Sound sound)
  {
    if (isMoving())
    {
      if(playSound)
      {
        playSound = false;
        if(sound.getExplosion() != 0 && gamedata.getPlaySound())
        {
          sound.getBang().play(sound.getExplosion(), 1, 1, 0, 0, 1); 
        }
      }
      
      if (mY - speed < 0)
        resetBullet();
      else if (mY >= surface.getHeight())
        resetBullet();
      else
      {
        mY += speed;
      }
    }
  }
  
  public void draw(GL10 gl, SpriteBatcher spriteBatcher)
  {
    if(isMoving())
    {
      dest = new Rect(mX, mY, mX + width, mY + height);
      spriteBatcher.draw(gl, imageID, src, dest);
    }
  }
}