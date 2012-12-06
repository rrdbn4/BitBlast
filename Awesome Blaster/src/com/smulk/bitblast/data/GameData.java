//TODO: Organize variables better

package com.smulk.bitblast.data;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.smulk.bitblast.R;
import com.smulk.bitblast.graphics.Images;
import com.smulk.bitblast.graphics.Shrapnel;
import com.smulk.bitblast.gameElements.Hero;
import com.smulk.bitblast.utils.BBButton;

public class GameData
{  
  private boolean resetState = false;
  private boolean deathscreenHit = false;
  private boolean playSound = true;
  private boolean playMusic = true;
  private boolean isRunning = false;
  private boolean isAllowBlockSpawn = false;

  private boolean mirrorFreeze = false;
  private boolean blockFreeze = false;
  
  private int score = 0;
  private int prevScore = 0;
  private int explosion = 0;
  private int blockExplosion = 0;
  private int gapCounter = 0;
  
  private static final String SCORE_KEY = "2n3odmwo";
  
  public static final int BLOCK_NUM = 40;
  public static final int BULLET_NUM = 5;
  public static final int GOOBLER_BULLET_NUM = 10;
  public static final int MIRROR_NUM = 10;
  public static final int MAX_LIVES = 10;
  public static final int MIRROR_GAP = 100;
	public static final int GAP_THRESHOLD = 40;
  public static final int SHRAPNEL_SIZE = 10;
  public static final int SHRAPNEL_DECOR_NUM = 10;
  public static final int SHRAPNEL_DECOR_SIZE = 3;
  public static final int SHRAPNEL_BLOWUP_SIZE = 10;
  public static final int HERO_BULLET_SPEED = -20; //negative to shoot up
  public static final int GOOBLER_BULLET_SPEED = 20;
  public static final int MIRROR_SPEED = 2;
  public static final int BLOCK_SPEED = 4;
  public static final int POWERUP_SPEED = 6;
  
  public static final String powerupNames[] =
  {
    "Freeze Blocks",
    "Goobler",
    "Invincuble",
    "Break Mirrors"
  };
  
  private PowerupType state;
  
  public BBButton BBplayPause = new BBButton();;
  
  public Thread thread = null;
  
  public static Context leContext;

  public Shrapnel blasts[];
  
  public GameData(Context context)
  {
    blasts = new Shrapnel[SHRAPNEL_DECOR_NUM];
    
    leContext = context;
    
    for(int i=0; i < blasts.length; i++)
      blasts[i] = new Shrapnel(leContext, SHRAPNEL_DECOR_SIZE, SHRAPNEL_DECOR_SIZE, Color.WHITE);
  }
  
  public void setResetState(Boolean value)
  {
    resetState = value;
  }
  
  public boolean getResetState()
  {
    return resetState;
  }
  
  public void setDeathscreenHit(Boolean value)
  {
    deathscreenHit = value;
  }
  
  public boolean getDeathscreenHit()
  {
    return deathscreenHit;
  }
  
  public void setPlaySound(Boolean value)
  {
    playSound = value;
  }
  
  public boolean getPlaySound()
  {
    return playSound;
  }
  
  public void setPlayMusic(Boolean value)
  {
    playMusic = value;
  }
  
  public boolean getPlayMusic()
  {
    return playMusic;
  }
  
  public void setIsRunning(Boolean value)
  {
    isRunning = value;
  }
  
  public boolean getIsRunning()
  {
    return isRunning;
  }
  
  public void setBlockFreeze(Boolean value)
  {
    blockFreeze = value;
  }
  
  public boolean getBlockFreeze()
  {
    return blockFreeze;
  }
  
  public void setMirrorFreeze(Boolean value)
  {
    mirrorFreeze = value;
  }
  
  public boolean getMirrorFreeze()
  {
    return mirrorFreeze;
  }
  
  public void addScore(int value)
  {
    score += value;
  }
  
  public int getScore()
  {
    return score;
  }
  
  public boolean isAllowBlockSpawn()
  {
  	return isAllowBlockSpawn;
  }

  public void setAllowBlockSpawn(boolean isAllowBlockSpawn)
  {
  	this.isAllowBlockSpawn = isAllowBlockSpawn;
  }
  
  public void setPrevScore(int value)
  {
    prevScore = value;
  }
  
  public int getPrevScore()
  {
    return prevScore;
  }
  
  public void setExplosion(int value)
  {
    explosion = value;
  }
  
  public int getExplosion()
  {
    return explosion;
  }
  
  public void setBlockExplosion(int value)
  {
    blockExplosion = value;
  }
  
  public boolean hasMetGapThreshold()
	{
		return gapCounter > GameData.GAP_THRESHOLD;
	}
	
	public void incrementGapCounter()
	{
		gapCounter++;
	}
	
	public void resetGapCounter()
	{
		gapCounter = 0;
	}
  
  public int getBlockExplosion()
  {
    return blockExplosion;
  }
  
  public static String getScoreKey()
  {
    return SCORE_KEY;
  }
  
  public void setState(PowerupType powerup)
  {
    state = powerup;
  }
  
  public PowerupType getState()
  {
    return state;
  }
  
  public void setAllBlockFreeze(Boolean value)
  {
    blockFreeze = value;
    mirrorFreeze = value;
  }
  
  public boolean getAllBlockFreeze()
  {
    return mirrorFreeze && blockFreeze;
  }
  
  public void setToNormal(float dt, GameData gamedata, Images images, Hero hero)
  {
    hero.setState(PowerupType.NONE);
    gamedata.setState(PowerupType.NONE);
    hero.setImage((BitmapFactory.decodeResource(leContext.getResources(), R.drawable.ship)));
    gamedata.setResetState(false);
  }
}
