package com.offkilter.android.bitblast.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.offkilter.android.bitblast.R;

public class Images
{
  private Bitmap enemy, enemyMad;

  private static final int allSprites[] =
  { 
    R.drawable.arrows, 
    R.drawable.back_button, 
    R.drawable.banner,
    R.drawable.begin, 
    R.drawable.block, 
    R.drawable.block_sprites,
    R.drawable.bullet, 
    R.drawable.buy_premium_button, 
    R.drawable.clear,
    R.drawable.continue_button, 
    R.drawable.feedback_button,
    R.drawable.goobler, 
    R.drawable.goobler_mad, 
    R.drawable.heart,
    R.drawable.how_to_button,
    R.drawable.ic_launcher, 
    R.drawable.ice_powerup,
    R.drawable.icon,
    R.drawable.invincability_powerup,
    R.drawable.invince_ship,
    R.drawable.logo,
    R.drawable.mirror,
    R.drawable.mirror_big,
    R.drawable.mirror_break_powerup,
    R.drawable.option1,
    R.drawable.option1_unselect,
    R.drawable.option2,
    R.drawable.option2_unselect,
    R.drawable.options_button, 
    R.drawable.pause,
    R.drawable.play,
    R.drawable.ship,
    R.drawable.test_ship,
    R.drawable.stats_button,
    R.drawable.switch_off_select,
    R.drawable.switch_on_select,
    R.drawable.view_website_button 
  };

  public Images(Context context)
  {
    enemy = BitmapFactory.decodeResource(context.getResources(), R.drawable.goobler);
    enemyMad = BitmapFactory.decodeResource(context.getResources(), R.drawable.goobler_mad);
  }

  public int[] getSprites()
  {
    return allSprites;
  }

  public Bitmap getEnemy()
  {
    return enemy;
  }

  public Bitmap getEnemyMad()
  {
    return enemyMad;
  }
}
