package com.offkilter.android.bitblast.game_elements;

public class BaseMob extends BaseSprite
{
  private boolean isMoving = false;

  protected int health;

  private int spawnChance = 0;

  public void setXRandomly(int max)
  {
    mX = rand.nextInt(max);
  }

  public void startMoving()
  {
    isMoving = true;
  }
  
  public int getHealth()
  {
    return health;
  }

  public boolean isMoving()
  {
    return isMoving;
  }

  public void resetMob()
  {
    baseReset();
    isMoving = false;
    health = 0;
  }

  public void setSpawnChance(int value)
  {
    spawnChance = value;
  }

  public int getSpawnChance()
  {
    return spawnChance;
  }

  public boolean spawn()
  {
    return (rand.nextInt(spawnChance) == 1);
  }
}
