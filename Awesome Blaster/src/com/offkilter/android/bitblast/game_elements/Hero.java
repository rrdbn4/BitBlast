package com.offkilter.android.bitblast.game_elements;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

import com.offkilter.android.bitblast.activities.DeathScreen;
import com.offkilter.android.bitblast.data.GameData;
import com.offkilter.android.bitblast.data.PowerupType;
import com.offkilter.android.bitblast.game_elements.bosses.Burrak;
import com.offkilter.android.bitblast.graphics.Images;
import com.offkilter.android.bitblast.graphics.SpriteBatcher;
import com.offkilter.android.bitblast.R;

public class Hero extends BaseMob
{
  private boolean wounded = false;
  private boolean showRed = false;

  public Bullet bullets[];

  private PowerupType state = PowerupType.NONE;

  // accelerometer data
  private float change, shipPosition;

  public Hero(Context context, Images images)
  {
    imageID = R.drawable.test_ship;
    spriteSheetWidth = 2;
    spriteSheetHeight = 2;
    image = BitmapFactory.decodeResource(context.getResources(), imageID);
    setDimensions(image.getHeight() / spriteSheetHeight, image.getWidth()
        / spriteSheetWidth);
    src = new Rect(srcX, srcY, width + srcX, height + srcY);

    health = 10;

    // TODO: Constant for defualt damage
    bullets = new Bullet[GameData.BULLET_NUM];
    for (int i = 0; i < bullets.length; i++)
      bullets[i] = new Bullet(context, GameData.HERO_BULLET_SPEED, 2);
  }

  public void showDamage()
  {
    showRed = true;
    wounded = true;
  }

  public void endGame(GameData gamedata)
  {
    if (!gamedata.getDeathscreenHit())
    {
      Bundle finalInfo = new Bundle();
      finalInfo.putInt(GameData.getScoreKey(), gamedata.getScore());
      Intent intent = new Intent(GameData.leContext, DeathScreen.class);
      intent.putExtras(finalInfo);
      GameData.leContext.startActivity(intent);
      ((Activity) GameData.leContext).finish();
      gamedata.setDeathscreenHit(true);
    }
  }

  public void shoot()
  {
    for (int i = 0; i < bullets.length; i++)
    {
      if (!bullets[i].isMoving())
      {
        bullets[i].shoot();
        // + speed so next frame it starts just above ship instead of on ship
        bullets[i].setmY(mY + bullets[i].getSpeed());
        bullets[i].setmX(mX + (width / 2));
        i = bullets.length;
      }
    }
  }

  public void setShowRed(Boolean value)
  {
    showRed = value;
  }

  public boolean getShowRed()
  {
    return showRed;
  }

  public void setWounded(Boolean value)
  {
    wounded = value;
  }

  public boolean isWounded()
  {
    return wounded;
  }

  public void setHealth(int value)
  {
    health = value;
  }

  public int getHealth()
  {
    return health;
  }

  public void setChange(float value)
  {
    change = value;
  }

  public void setState(PowerupType condition)
  {
    state = condition;
  }

  public PowerupType getState()
  {
    return state;
  }

  public void damageShip(GameData gamedata, int amount)
  {
    health -= amount;
    showDamage();
    if (health <= 0)
    {
      gamedata.setPrevScore(gamedata.getScore());
      endGame(gamedata);
    }
  }

  public void update(GLSurfaceView surface, GameData gamedata, Burrak burrakBoss)
  {
    // position based on accelerometer
    if (((shipPosition - (change * 5)) > 0)
        && (shipPosition - (change * 5)) < (surface.getWidth() - getWidth()))
      shipPosition -= change * 5;

    // change image based on tilt
    if (change > 1 || change < -1)
    {
      if (change > 0)
      {
        srcX = 0 * width;
        srcY = 1 * height;
      }
      else if (change < 0)
      {
        srcX = 1 * width;
        srcY = 1 * height;
      }
    }
    else
    {
      srcX = 0 * width;
      srcY = 0 * height;
    }

    src = new Rect(srcX, srcY, width + srcX, height + srcY);

    // Log.d("Acceleromter Data", " " + change);

    setmY((surface.getHeight() / 4) * 3);
    setmX(((int) shipPosition));

    if (burrakBoss.isMoving())
    {
      for (int i = 0; i < burrakBoss.bullets.length; i++)
      {
        if (burrakBoss.bullets[i].isMoving())
        {
          if (hit(burrakBoss.bullets[i]))
          {
            damageShip(gamedata, burrakBoss.bullets[i].getDamage());
            burrakBoss.bullets[i].resetBullet();
          }
        }
      }
    }

    for (int i = 0; i < bullets.length; i++)
    {
      if (bullets[i].isMoving())
      {
        if (hit(bullets[i]))
        {
          damageShip(gamedata, bullets[i].getDamage());
          bullets[i].resetBullet();
        }
      }
    }

  }

  public void draw(GL10 gl, SpriteBatcher spriteBatcher)
  {
    dest = new Rect(mX, mY, mX + width, mY + height);
    spriteBatcher.draw(gl, imageID, src, dest);
  }
}
