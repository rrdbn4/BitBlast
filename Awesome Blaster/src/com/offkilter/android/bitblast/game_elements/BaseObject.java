package com.offkilter.android.bitblast.game_elements;

public class BaseObject extends BaseSprite
{
  private boolean isMoving = false;
  private int spawnChance = 0;

  public boolean spawnChance()
  {
    return (rand.nextInt(spawnChance) == 1);
  }

  public void startMoving()
  {
    isMoving = true;
  }

  public void stopMoving()
  {
    isMoving = false;
  }

  public boolean isMoving()
  {
    return isMoving;
  }

  public void setXRandomly(int max)
  {
    mX = rand.nextInt(max);
  }

  public void resetObject()
  {
    isMoving = false;
    spawnChance = 0;
    baseReset();
  }
}
