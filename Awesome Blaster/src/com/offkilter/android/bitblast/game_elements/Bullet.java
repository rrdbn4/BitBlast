package com.offkilter.android.bitblast.game_elements;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.offkilter.android.bitblast.data.GameData;
import com.offkilter.android.bitblast.game_elements.bosses.Burrak;
import com.offkilter.android.bitblast.graphics.SpriteBatcher;
import com.offkilter.android.bitblast.sound.Sound;
import com.offkilter.android.bitblast.R;

public class Bullet extends BaseObject
{
  private boolean playerBullet = false;
  private boolean playSound = false;
  private boolean hitMirror = false;

  private int damage;
  private int defaultDamage;

  private int speed;
  private int defaultSpeed;

  public Bullet(Context context, int dSpeed, int dDamage)
  {
    imageID = R.drawable.bullet;
    image = BitmapFactory.decodeResource(context.getResources(), imageID);
    setDimensions(image.getHeight(), image.getWidth());
    src = new Rect(srcX, srcY, width + srcX, height + srcY);

    defaultSpeed = dSpeed;
    defaultDamage = dDamage;
    resetBullet();
  }

  public void resetBullet()
  {
    resetObject();
    speed = defaultSpeed;
    damage = defaultDamage;
    hitMirror = false;
  }
  
  public void setIsPlayer(boolean value)
  {
    playerBullet = value;
  }

  public void shoot()
  {
    playSound = true;
    startMoving();
  }

  public void setHitMirror(boolean value)
  {
    hitMirror = value;
  }

  public boolean getHitMirror()
  {
    return hitMirror;
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

  public void update(GLSurfaceView surface, GameData gamedata, Sound sound, Burrak burrakBoss, Hero hero, Block[] block, Mirror[] mirror)
  {
    if (isMoving())
    { 
      if (playSound)
      {
        playSound = false;
        if (sound.getExplosion() != 0 && gamedata.getPlaySound())
        {
          sound.getBang().play(sound.getExplosion(), 1, 1, 0, 0, 1);
        }
      }
      
      if(burrakBoss.isMoving())
      {
        if(hit(burrakBoss))
        {
          burrakBoss.damage(damage);
          burrakBoss.setIsMad(true);
          
          if(burrakBoss.getHealth() <= 0)
          {
            if(playerBullet)
            {  
              hero.addScore(burrakBoss.getPointValue());
            }
            gamedata.setAllBlockFreeze(false);
          }
          
          resetBullet();
        }
      }
      
      if(hit(hero))
      {
        hero.damage(gamedata, damage);
        resetBullet();
      }
      
      for(int i = 0; i < block.length; i++)
      {
        if(block[i].isMoving())
        {
          if(hit(block[i]))
          {
            block[i].damage(gamedata, damage, sound);
            if(block[i].getHealth() <= 0)
            {
              if(playerBullet)
              {
                hero.addScore(block[i].getPointValue());
              }
            }
            resetBullet();
          }
        }
      }
      
      for(int i = 0; i < mirror.length; i++)
      {
        if(mirror[i].isMoving())
        {
          if(hit(mirror[i]))
          {
            speed = -speed;
            
            //TODO: add check if player has mirror powerup
            if(mirror[i].getHealth() <= 0)
            {
              if(playerBullet)
              {
                hero.addScore(block[i].getPointValue());
              }
              resetBullet();
            }  
          }
        }
      }
      
      if (mY - speed < 0)
        resetBullet();
      else if (mY >= surface.getHeight())
        resetBullet();
      else
        mY += speed;
    }
  }

  public void draw(GL10 gl, SpriteBatcher spriteBatcher)
  {
    if (isMoving())
    {
      dest = new Rect(mX, mY, mX + width, mY + height);
      spriteBatcher.draw(gl, imageID, src, dest);
    }
  }
}