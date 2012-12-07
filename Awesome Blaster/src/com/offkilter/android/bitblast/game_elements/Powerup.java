package com.offkilter.android.bitblast.game_elements;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.offkilter.android.bitblast.data.GameData;
import com.offkilter.android.bitblast.data.PowerupType;
import com.offkilter.android.bitblast.graphics.Images;
import com.offkilter.android.bitblast.graphics.SpriteBatcher;
import com.offkilter.android.bitblast.R;

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
    return rand.nextInt(SPAWN_CHANCE) == 1;
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

    switch (type)
    {
    case FREEZE_BLOCKS:
      imageID = R.drawable.ice_powerup;
      break;
    case GOOBLER:
      imageID = R.drawable.goobler_powerup;
      break;
    case INVINCIBLE:
      imageID = R.drawable.invincability_powerup;
      break;
    case BREAK_MIRRORS:
      imageID = R.drawable.mirror_break_powerup;
      break;
    default:
      Log.d("setRandomType() Powerup.java", "Powerup imageID undefined");
      break;
    }
  }

  private void powerupSelector(GameData gamedata, Hero hero)
  {
    switch (type)
    {
    case FREEZE_BLOCKS:
      // gamedata.setState(PowerupType.FREEZE_BLOCKS);
      break;
    case GOOBLER:
      // gamedata.setState(PowerupType.GOOBLER);
      break;
    case INVINCIBLE:
      // hero.setState(PowerupType.INVINCIBLE);
      break;
    case BREAK_MIRRORS:
      // hero.setState(PowerupType.BREAK_MIRRORS);
      break;
    default:
      Log.d("powerupSelector(), Powerup.java", "Powerup undefined.");
      break;
    }

    if (gamedata.getState() == PowerupType.FREEZE_BLOCKS
        || hero.getState() == PowerupType.INVINCIBLE
        || hero.getState() == PowerupType.BREAK_MIRRORS)
    {
      // gamedata.timer.setTimer(350, gamedata.timer);
    }
    if (hero.getState() == PowerupType.INVINCIBLE)
    {
      // TODO: Line below doesn't do anything as the opengl renderer uses id's
      // hero.setImage((BitmapFactory.decodeResource(GameData.leContext.getResources(),
      // R.drawable.invince_ship)));
    }
  }

  public void update(GLSurfaceView surface, GameData gamedata, Hero hero)
  {
    if (isMoving())
    {
      if (mY >= surface.getHeight())
        resetPowerup();
      else
      {
        mY += GameData.POWERUP_SPEED;

        for (int i = 0; i < hero.bullets.length; i++)
        {
          if (hit(hero.bullets[i]))
          {
            powerupSelector(gamedata, hero);
            hero.bullets[i].resetBullet();
            resetPowerup();
          }
        }

        if (hero.hit(this))
        {
          powerupSelector(gamedata, hero);
          resetPowerup();
        }
      }
    } else
    {
      if (spawn() && gamedata.getState() != PowerupType.FREEZE_BLOCKS)
      {
        startMoving();
        setXRandomly(surface.getHeight() - getWidth());
        mY = 0;
        setRandomType();
      }
    }
  }

  public void draw(GL10 gl, SpriteBatcher spriteBatcher, GameData gamedata,
      Images images, Hero hero)
  {
    if (isMoving())
    {
      dest = new Rect(mX, mY, mX + width, mY + height);
      spriteBatcher.draw(gl, imageID, src, dest);
    }
  }
}
