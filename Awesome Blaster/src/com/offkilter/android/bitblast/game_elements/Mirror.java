package com.offkilter.android.bitblast.game_elements;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.offkilter.android.bitblast.data.GameData;
import com.offkilter.android.bitblast.data.PowerupType;
import com.offkilter.android.bitblast.graphics.Images;
import com.offkilter.android.bitblast.graphics.Shrapnel;
import com.offkilter.android.bitblast.graphics.SpriteBatcher;
import com.offkilter.android.bitblast.R;

public class Mirror extends Block
{
  public Mirror(Context context, Images images)
  {
    super(context, images);
    imageID = R.drawable.block_sprites;
    image = BitmapFactory.decodeResource(context.getResources(), imageID);
    setDimensions(image.getHeight() / 2, image.getWidth() / 4);
    srcX = 3 * width;
    srcY = 1 * height;
    src = new Rect(srcX, srcY, width + srcX, height + srcY);

    shrapnel = new Shrapnel(GameData.leContext, GameData.SHRAPNEL_BLOWUP_SIZE, GameData.SHRAPNEL_BLOWUP_SIZE, Color.DKGRAY);

    health = 1;
    defaultDamage = 1;
    defaultSpeed = GameData.MIRROR_SPEED;
    setSpawnChance(800);
    resetMirror();
  }

  public void resetMirror()
  {
    resetMob();
    health = 1;
    damage = 1;
    beenHit = false;
    speed = defaultSpeed;
  }

  public void update(GLSurfaceView surface, GameData gamedata, Hero hero)
  {
    if (isMoving())
    {
      if(health > 0)
      {
        if (mY == 0)
        {
          setmY(1);
        }
        else if (mY >= surface.getHeight())
        {
          resetMirror();
        }
        else
        // if it is moving normally
        {
          if (gamedata.getState() != PowerupType.FREEZE_BLOCKS)
            mY += speed;

          if (hit(hero))
          {
            if (!beenHit)
            {
              // When ship gets hit by block
              beenHit = true;
              hero.damage(gamedata, damage);
            }
          }
        }
      }
      else
        resetMirror();
    }
    else
    {
      if (spawn() && !gamedata.getMirrorFreeze() && gamedata.getState() != PowerupType.FREEZE_BLOCKS)
      {
        startMoving();
        setXRandomly(surface.getWidth() - width);
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
