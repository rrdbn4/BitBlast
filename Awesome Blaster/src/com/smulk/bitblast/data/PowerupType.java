package com.smulk.bitblast.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum PowerupType
{
  FREEZE_BLOCKS,
  GOOBLER,
  INVINCIBLE,
  BREAK_MIRRORS,
  NONE;
  
  private static final List<PowerupType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
  private static final int SIZE = VALUES.size();
  private static final Random RANDOM = new Random();
  private static int index;
  
  public static PowerupType randomPowerup()  
  {
    index = RANDOM.nextInt(SIZE-1);
    return VALUES.get(index);
  }
  
  public static int getSize()
  {
    return SIZE;
  }
  
  public static int getIndex()
  {
    return index;
  }
}
