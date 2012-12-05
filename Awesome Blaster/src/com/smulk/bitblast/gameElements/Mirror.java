package com.smulk.bitblast.gameElements;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.smulk.bitblast.R;
import com.smulk.bitblast.data.GameData;
import com.smulk.bitblast.data.PowerupType;
import com.smulk.bitblast.graphics.Images;
import com.smulk.bitblast.graphics.Shrapnel;
import com.smulk.bitblast.graphics.SpriteBatcher;

public class Mirror extends Block
{
  public Mirror(Context context, Images images)
  {
    super(context, images);
    imageID = R.drawable.block_sprites;
    image = BitmapFactory.decodeResource(context.getResources(), imageID);
    setDimensions(image.getHeight()/2, image.getWidth()/4);
    srcX = 3 * width;
    srcY = 1 * height;
    src = new Rect(srcX, srcY, width + srcX, height + srcY);
    
    shrapnel = new Shrapnel(GameData.leContext, GameData.SHRAPNEL_BLOWUP_SIZE, GameData.SHRAPNEL_BLOWUP_SIZE, Color.DKGRAY);
   
    damage = 1;
    //resetBlock();
    
    setSpawnChance(800);
  }
  
  private void resetMirror()
  {
    beenHit = false;
    resetMob();
  }
  
  public void update(GLSurfaceView surface, GameData gamedata, Hero hero)
  {
    {
      blockGapCounter++;
      
      if(!isMoving())
      {     
        //add mirror
        if(spawn() && !gamedata.getMirrorFreeze() && blockGapCounter >= GameData.MIRROR_GAP && gamedata.getState() != PowerupType.FREEZE_BLOCKS)
        {
          blockGapCounter = 0;
          startMoving();
          setXRandomly(surface.getWidth() - width);
          mY = 0;
        }
      }
      else //if it is moving
      {
        if(mY == 0)
        {
          setmY(1);
        }
        else if(mY >= surface.getHeight())
        {
          resetMirror();
        }
        else  //if it is moving normally
        {
          if(gamedata.getState() != PowerupType.FREEZE_BLOCKS)
             mY += GameData.MIRROR_SPEED;
          
          for(int k = 0; k < GameData.BULLET_NUM; k++)
          {
            //when mirror encounters a bullet
            if(hit(hero.bullets[k]))
            {
              if(hero.getState() == PowerupType.BREAK_MIRRORS)  //if mirror break powerup
              {
                shrapnel.setStart(mX + (width / 2), mY + (height / 2));
                //shrapnel.startShrapnel(gamedata, mX + (width / 2), mY + (height / 2), 7, 7, Color.DKGRAY);
                resetMirror();
                hero.bullets[k].resetBullet();
              }
              else
              {
                if(!hero.bullets[k].getHitMirror())
                  hero.bullets[k].setSpeed(-hero.bullets[k].getSpeed());
              }
            }
          }
          
          if(hit(hero))
          {
            if (!beenHit)
            {
              //When ship gets hit by block
              beenHit = true;
              hero.damageShip(gamedata, damage);
            }
          }
          
        }
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
