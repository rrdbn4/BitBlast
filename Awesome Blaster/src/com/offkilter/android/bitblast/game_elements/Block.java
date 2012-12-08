package com.offkilter.android.bitblast.game_elements;

import javax.microedition.khronos.opengles.GL10;

import com.offkilter.android.bitblast.data.GameData;
import com.offkilter.android.bitblast.data.PowerupType;
import com.offkilter.android.bitblast.graphics.Images;
import com.offkilter.android.bitblast.graphics.Shrapnel;
import com.offkilter.android.bitblast.graphics.SpriteBatcher;
import com.offkilter.android.bitblast.sound.Sound;
import com.offkilter.android.bitblast.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.util.Log;

public class Block extends BaseMob
{
  protected boolean beenHit;

  private int color = -1;

  protected int speed;
  protected int defaultSpeed;

  protected int damage;
  protected int defaultDamage;

  protected int pointMultiplier = 10;
  protected int pointValue;

  protected Shrapnel shrapnel;

  public Block(Context context, Images images)
  {
    spriteSheetWidth = 4;
    spriteSheetHeight = 2;
    imageID = R.drawable.block_sprites;
    image = BitmapFactory.decodeResource(context.getResources(), imageID);
    setDimensions(image.getHeight() / spriteSheetHeight, image.getWidth() / spriteSheetWidth);
    shrapnel = new Shrapnel(GameData.leContext, GameData.SHRAPNEL_BLOWUP_SIZE, GameData.SHRAPNEL_BLOWUP_SIZE, color);

    defaultDamage = 1;
    defaultSpeed = GameData.BLOCK_SPEED;

    resetBlock();

    setSpawnChance(800);
  }

  public void resetBlock()
  {
    resetMob();
    speed = defaultSpeed;
    damage = defaultDamage;
    color = GameData.blockColors[rand.nextInt(GameData.blockColors.length)];

    switch (color)
    {
      case Color.BLUE:
        srcX = 0 * width;
        srcY = 0 * height;
        health = 7;
        pointValue = health * pointMultiplier;
        break;
      case Color.CYAN:
        srcX = 1 * width;
        srcY = 0 * height;
        health = 6;
        pointValue = health * pointMultiplier;
        break;
      case Color.GREEN:
        srcX = 2 * width;
        srcY = 0 * height;
        health = 5;
        pointValue = health * pointMultiplier;
        break;
      case Color.MAGENTA:
        srcX = 3 * width;
        srcY = 0 * height;
        health = 4;
        pointValue = health * pointMultiplier;
        break;
      case Color.RED:
        srcX = 0 * width;
        srcY = 1 * height;
        health = 3;
        pointValue = health * pointMultiplier;
        break;
      case Color.WHITE:
        srcX = 1 * width;
        srcY = 1 * height;
        health = 2;
        pointValue = health * pointMultiplier;
        break;
      case Color.YELLOW:
        srcX = 2 * width;
        srcY = 1 * height;
        health = 1;
        pointValue = health * pointMultiplier;
        break;
      default:
        Log.d("resetBlock(), Block.java", "Color not found.");
        break;
    }

    beenHit = false;
    src = new Rect(srcX, srcY, width + srcX, height + srcY);
  }

  public void damageBlock(GameData gamedata, int damage, Sound sound)
  {
    health -= damage;
    if (health <= 0)
    {
      if (sound.getBlockExplosion() != 0 && gamedata.getPlaySound())
        sound.getBlowUp().play(sound.getExplosion(), 1, 1, 0, 0, 1);

      shrapnel = new Shrapnel(GameData.leContext, GameData.SHRAPNEL_BLOWUP_SIZE, GameData.SHRAPNEL_BLOWUP_SIZE, color);
      shrapnel.setStart(mX + (width / 2), mY + (height / 2));
    }
  }
  
  public int getPointValue()
  {
    return pointValue;
  }

  public void update(GLSurfaceView surface, GameData gamedata, Hero hero, Sound sound)
  {
    if (isMoving())
    {
      if (mY == 0)
      {
        mY = 1;
      }
      else if (mY >= surface.getHeight())
      {
        // Log.d("Block.java", "Reset called");
        resetBlock();
      }
      else
      {
        if (gamedata.getState() != PowerupType.FREEZE_BLOCKS)
          mY += speed;

        if (hit(hero))
        {
          if (!beenHit)
          {
            // When ship gets hit by block
            beenHit = true;
            hero.damageShip(gamedata, damage);
          }
        }

        /*
        for (int i = 0; i < hero.bullets.length; i++)
        {
          if (hero.bullets[i].isMoving())
          {
            // when block encounters a bullet
            if (hit(hero.bullets[i]))
            {
              damageBlock(gamedata, hero, i, sound);
              hero.bullets[i].resetBullet();
              i = hero.bullets.length; // break out of loop
            }
          }
        }
        */
      }
    }
    else
    // if it isn't moving
    {
      if (spawn() && !gamedata.getBlockFreeze() && gamedata.getState() != PowerupType.FREEZE_BLOCKS
          && gamedata.hasMetGapThreshold())
      {
        // initialize block
        startMoving();
        setXRandomly(surface.getWidth() - getWidth());
        gamedata.resetGapCounter();
        mY = 0;
      }
    }
  }

  public void draw(GL10 gl, SpriteBatcher spriteBatcher)
  {
    shrapnel.draw(gl, spriteBatcher);
    if (isMoving())
    {
      dest = new Rect(mX, mY, mX + width, mY + height);
      spriteBatcher.draw(gl, imageID, src, dest);
    }
  }
}
