package com.smulk.bitblast.gameElements;

//TODO: Use rectangle size to store height and width

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

public class BaseSprite
{
  //location
  protected int mX = 0;
  protected int mY = 0;
  //hit box / location
  protected Rect dest = new Rect();
  
  protected Random rand = new Random();
  
  //image information
  protected int height;
  protected int width;
  protected Rect src = new Rect();
  protected Bitmap image = null;
  protected int imageID = 0;
  // for spritesheet
  protected int srcX = 0;
  protected int srcY = 0;
  protected int spriteSheetWidth = 0;
  protected int spriteSheetHeight = 0;
  
  public void setmY(int value)
  {
    mY = value;
  }
   
  public int getmY()
  {
    return mY; 
  }
  
  public void setmX(int value)
  {
    mX = value;
  }
   
  public int getmX()
  {
    return mX;
  }
  
  protected void setDimensions(int h, int w)
  {
    height = h;
    width = w;
  }
  
  public int getHeight()
  {
    return height; 
  }
   
  public int getWidth()
  {
    return width;
  }
  
  public void setImage(Bitmap newImage)
  {
    image = newImage;
  }
  
  public Bitmap getImage()
  {
    return image;
  }
  
  public void baseReset()
  {
    mX = mY = -1;
    dest = new Rect();
  }
  
  public boolean hit(BaseSprite rhs)
  {
    return dest.intersect(rhs.dest);
  }
}
