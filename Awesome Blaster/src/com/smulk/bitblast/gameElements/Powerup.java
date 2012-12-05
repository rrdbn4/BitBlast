package com.smulk.bitblast.gameElements;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.smulk.awesomeblaster.R;
import com.smulk.bitblast.data.GameData;
import com.smulk.bitblast.data.PowerupType;
import com.smulk.bitblast.graphics.Images;
import com.smulk.bitblast.graphics.SpriteBatcher;

public class Powerup extends BaseObject
{
  private final int SPAWN_CHANCE = 200; 
  private PowerupType type;  
  
  private Bitmap image = null;
  
  public Powerup(Context context)
  {
    imageID = R.drawable.ice_powerup;
    image = BitmapFactory.decodeResource(context.getResources(), imageID);
    setDimensions(image.getHeight(), image.getWidth());
    src = new Rect(srcX, srcY, width + srcX, height + srcY);
  }
  
  public boolean spawn()
  {
    return rand.nextInt(SPAWN_CHANCE) == 0;
  }
  
  public void resetPowerup()
  {
    stopMoving();
    type = PowerupType.NONE;
    image = null;
    resetObject();
  }
  
  public void setImage(Bitmap powerup)
  {
    image = powerup;
  }
  
  public Bitmap getImage()
  {
    return image;
  }
  
  public void setType(PowerupType powerup)
  {
    type = powerup;
  }
  
  public PowerupType getType()
  {
    return type;
  }
  
  public void PowerupType(PowerupType powerup)
  {
    type = powerup;
  }
  
  public void setRandomType()
  {
    type = PowerupType.randomPowerup();
    image = Images.getPuImage(PowerupType.getIndex());
  }
  
  public void update(GLSurfaceView surface, GameData gamedata, Images images, Hero hero)
  {
    if(isMoving())
    {
      if(mY >= surface.getHeight())
        resetPowerup();
      else
      {
        mY += GameData.POWERUP_SPEED;
        
        for(int i = 0; i < hero.bullets.length; i++)
        {
          if(hit(hero.bullets[i]))
          {
            powerupSelector(gamedata, images, hero);
            hero.bullets[i].resetBullet();
            resetPowerup();    
          }
        }
        
        if(hero.hit(this))
        {
          powerupSelector(gamedata, images, hero);
          resetPowerup();
        }
      }
    }
    else
    {
      if(spawn() && gamedata.getState() != PowerupType.FREEZE_BLOCKS)
      {
        startMoving();
        setXRandomly(surface.getHeight() - getWidth());
        mY = 0;
        setRandomType();
      }
    }
  }
  
  public void draw(GL10 gl, SpriteBatcher spriteBatcher, GameData gamedata, Images images, Hero hero)
  {
    if(isMoving())
    {
      dest = new Rect(mX, mY, mX + width, mY + height);
      spriteBatcher.draw(gl, imageID, src, dest);
      //canvas.drawBitmap(image, mX, mY, null);
    }
  }
  
  public void powerupSelector(GameData gamedata, Images images, Hero hero)
  {
    switch(type)
    {
      case FREEZE_BLOCKS:
        imageID = R.drawable.ice_powerup;
        gamedata.setState(PowerupType.FREEZE_BLOCKS);
        break;
      case GOOBLER:
        imageID = R.drawable.goobler_powerup;
        gamedata.setState(PowerupType.GOOBLER);
        break;
      case INVINCIBLE:
        imageID = R.drawable.invincability_powerup;
        hero.setState(PowerupType.INVINCIBLE);
        break;
      case BREAK_MIRRORS:
        imageID = R.drawable.mirror_break_powerup;
        hero.setState(PowerupType.BREAK_MIRRORS);
        break;
      default:
        break;
    }
    
    if(gamedata.getState() == PowerupType.FREEZE_BLOCKS || hero.getState() == PowerupType.INVINCIBLE || hero.getState() == PowerupType.BREAK_MIRRORS)
    {
      //gamedata.timer.setTimer(350, gamedata.timer);
    }
    if(hero.getState() == PowerupType.INVINCIBLE)
    {
      hero.setImage((BitmapFactory.decodeResource(GameData.leContext.getResources(), R.drawable.invince_ship)));
    }
  }
}
