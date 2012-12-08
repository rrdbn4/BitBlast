package com.offkilter.android.bitblast.game_elements.bosses;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;

import com.offkilter.android.bitblast.data.GameData;
import com.offkilter.android.bitblast.data.PowerupType;
import com.offkilter.android.bitblast.game_elements.BaseMob;
import com.offkilter.android.bitblast.game_elements.Bullet;
import com.offkilter.android.bitblast.game_elements.Hero;
import com.offkilter.android.bitblast.graphics.Images;
import com.offkilter.android.bitblast.graphics.SpriteBatcher;
import com.offkilter.android.bitblast.utils.OKTimer;
import com.offkilter.android.bitblast.R;

//TODO: the image of this boss should be a ship to match the theme of the game

public class Burrak extends BaseMob
{
  private final int MAX_HEALTH = 20;
  private final int SHOOT_CHANCE = 100;

  private boolean isMovingRandomly;
  private boolean goingDown;
  private boolean goingLeft;
  private boolean isMad = false;

  public Bullet bullets[];
  
  private OKTimer timer;

  public Burrak(Context context)
  {
    imageID = R.drawable.goobler;
    image = BitmapFactory.decodeResource(context.getResources(), imageID);
    setDimensions(image.getHeight(), image.getWidth());
    src = new Rect(srcX, srcY, width + srcX, height + srcY);

    setSpawnChance(4000);

    bullets = new Bullet[GameData.GOOBLER_BULLET_NUM];
    for (int i = 0; i < bullets.length; i++)
      bullets[i] = new Bullet(context, GameData.GOOBLER_BULLET_SPEED, 2);
    
    timer = new OKTimer();
    timer.setMethodToCall(this, "notMad", 0.2f);

    resetGoobler();
  }
  
  public void setIsMad(Boolean value)
  {
    isMad = value;
  }

  public void resetGoobler()
  {
    resetMob();
    health = MAX_HEALTH;
    isMad = isMovingRandomly = goingLeft = goingDown = false;
  }

  public void takeDamage(int amount)
  {
    health -= amount;
  }

  private boolean shootChance()
  {
    return rand.nextInt(SHOOT_CHANCE) == 1;
  }
  
  public void notMad()
  {
    isMad = false;
    timer.stopTimer();
  }

  private void moveTowardShip(GLSurfaceView surface, int speed, Hero hero, Bitmap picture)
  {
    int target_middle = hero.getmX() + (hero.getWidth() / 2);
    int this_middle = mX + (getWidth() / 2);

    if (shootChance())
    {
      for (int i = 0; i < bullets.length; i++)
      {
        if (!bullets[i].isMoving())
        {
          bullets[i].shoot();
          bullets[i].setmY(mY + height + bullets[i].getSpeed());
          bullets[i].setmX(mX + (width / 2));
          i = bullets.length;
        }
      }
      isMovingRandomly = (isMovingRandomly ? false : true);
    }

    // left and right movement
    if (!isMovingRandomly)
    {
      if (target_middle <= this_middle && (mX - speed) > 0)
      {
        mX -= speed;
      }
      else if (target_middle > this_middle && (mX + getWidth() + speed) < surface.getWidth())
      {
        mX += speed;
      }
    }
    else
    {
      // trigger random decision
      if (shootChance())
      {
        for (int i = 0; i < bullets.length; i++)
        {
          if (!bullets[i].isMoving())
          {
            bullets[i].shoot();
            bullets[i].setmY(mY + bullets[i].getSpeed());
            bullets[i].setmX(mX + (width / 2));
            i = bullets.length;
          }
        }
        goingLeft = (goingLeft ? false : true);
      }

      if (goingLeft)
      {
        if (mX - speed <= 0)
          goingLeft = false;
        else
          mX -= speed;
      }
      else
      {
        if (mX + speed + getWidth() >= surface.getWidth())
          goingLeft = true;
        else
          mX += speed;
      }
    }
    bounce(speed, hero.getmY());
  }

  private void bounce(int speed, int targetY)
  {
    int bottom = mY + getHeight();
    if (goingDown)
    {
      if ((bottom + speed) < targetY - 50)
        mY += speed;
      else
        goingDown = false;
    }
    else
    {
      if ((mY - speed) > 50)
        mY -= speed;
      else
        goingDown = true;
    }
  }

  public void update(GLSurfaceView surface, GameData gamedata, Hero hero, Images images)
  {
    if (gamedata.getState() != PowerupType.FREEZE_BLOCKS && !isMoving())
    {
      if (gamedata.getState() == PowerupType.GOOBLER)
      {
        resetGoobler();
        gamedata.setToNormal(0, gamedata, images, hero);
        startMoving();

        // TODO: Create better way to freeze objects
        gamedata.setAllBlockFreeze(true);
      }
      else if (hero.getScore() >= 3000 && spawn())
      {
        resetGoobler();
        startMoving();

        // TODO: Create better way to freeze objects
        gamedata.setAllBlockFreeze(true);
      }
      else if (hero.getScore() >= 0 && hero.getScore() <= 100)
      {
        resetGoobler();
        startMoving();

        // TODO: Create better way to freeze objects
        gamedata.setAllBlockFreeze(true);
      }
    }

    // if spawned then move with this
    if (isMoving())
    {
      if(isMad)
        timer.startTimer();
        
      moveTowardShip(surface, 3, hero, images.getEnemy());
    }
  }

  public void draw(GL10 gl, SpriteBatcher spriteBatcher)
  {
    if (isMoving())
    {
      dest = new Rect(mX, mY, mX + width, mY + height);
      if (isMad)
      {
        imageID = R.drawable.goobler_mad;
        spriteBatcher.draw(gl, imageID, src, dest);
      }
      else
      {
        imageID = R.drawable.goobler;
        spriteBatcher.draw(gl, imageID, src, dest);
      }
    }
  }
}
