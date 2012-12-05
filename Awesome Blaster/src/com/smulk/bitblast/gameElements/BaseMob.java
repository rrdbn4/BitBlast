package com.smulk.bitblast.gameElements;

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
  
  public boolean isMoving()
  {
    return isMoving;
  }
  
  public void resetMob()
  {
    isMoving = false;
    baseReset();
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
