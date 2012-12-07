package com.offkilter.android.bitblast.graphics;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.util.Log;

import com.offkilter.android.bitblast.data.GameData;
import com.offkilter.android.bitblast.game_elements.Hero;

public class GameBackground
{
  private boolean initialize = true;

  private float dRed = 50;
  private float dGreen = 50;
  private float dBlue = 100;

  private int numColors = 255;
  private float alpha = 0.5f;

  private float tRed;
  // private float tGreen;
  private float tBlue;

  private Random rand = new Random();

  public void draw(GL10 gl, SpriteBatcher spriteBatcher, GLSurfaceView surface,
      GameData gamedata, Hero hero)
  {
    if (initialize)
    {
      gl.glClearColor(dRed / numColors, dGreen / numColors, dBlue / numColors,
          alpha);
      initialize = false;
    }

    if (hero.isWounded())
    {
      hero.setWounded(false);
      tRed = 255;
      tBlue = 20;
    }
    if (hero.getShowRed())
    {
      tRed -= 4;
      tBlue += 2;
      if (tRed <= 50)
        hero.setShowRed(false);
      gl.glClearColor(tRed / numColors, dGreen / numColors, tBlue / numColors,
          alpha);
    }

    // Decorate with shrapnel
    for (int i = 0; i < GameData.SHRAPNEL_DECOR_NUM; i++)
    {
      if (!gamedata.blasts[i].isAnimating())
      {
        if (rand.nextInt(10) == 0)
        {
          gamedata.blasts[i].setStart(rand.nextInt(surface.getWidth()),
              rand.nextInt(surface.getHeight()));
        }
      }
      else
        gamedata.blasts[i].draw(gl, spriteBatcher);
    }
  }
}
