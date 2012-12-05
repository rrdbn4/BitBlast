package com.smulk.bitblast.graphics;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import com.smulk.bitblast.R;
import com.smulk.bitblast.gameElements.BaseSprite;

public class Shrapnel extends BaseSprite
{
	private int tleftx, tlefty, trightx, trighty, bleftx, blefty, brightx, brighty;
	private int count;
	private final int SPEED = 2;
	private final int MAX_NUM_ITERATIONS = 20;
	private boolean going;
	
	private int color;
	
	public Shrapnel(Context context, int newWidth, int newHeight, int someColor)
	{
    spriteSheetWidth = 4;
    spriteSheetHeight = 2;
    imageID = R.drawable.block_sprites;
    //image = BitmapFactory.decodeResource(context.getResources(), imageID);
    setDimensions(newHeight, newWidth);
    image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.block_sprites), width*spriteSheetWidth, height*spriteSheetHeight, false);
    color = someColor;
    resetShrapnel();
	}
	
	private void resetShrapnel()
	{
		count = tleftx = tlefty = trightx = trighty = bleftx = blefty = brightx = brighty = 0;
		going = false;
		
    switch(color)
    {
      case Color.BLUE:
        srcX = 0 * width;
        srcY = 0 * height;
        break;
      case Color.CYAN:
        srcX = 1 * width;
        srcY = 0 * height;
        break;
      case Color.GREEN:
        srcX = 2 * width;
        srcY = 0 * height;
        break;
      case Color.MAGENTA:
        srcX = 3 * width;
        srcY = 0 * height;
        break;
      case Color.RED:
        srcX = 0 * width;
        srcY = 1 * height;
        break;
      case Color.WHITE:
        srcX = 1 * width;
        srcY = 1 * height;
        break;
      case Color.YELLOW:
        srcX = 2 * width;
        srcY = 1 * height;
        break;
      case Color.DKGRAY:
        srcX = 3 * width;
        srcY = 1 * height;
        break;
    }
    
    src = new Rect(srcX, srcY, width + srcX, height + srcY);
    //src = new Rect(0,0, image.getWidth(), image.getHeight());
	}
	
	public void setStart(int x, int y)
	{
    //src = new Rect(0, 0, width, height);
		tleftx = trightx = bleftx = brightx = x;
		tlefty = trighty = brighty = blefty = y;
		going = true;
	}
	
	public boolean isAnimating()
	{
		return going;
	}
	
	//TODO: this function should not exist. other functions exist to already do this, just without a color...
//  public void startShrapnel(GameData gamedata, int X, int Y, int height, int width, int someColor)
//  {
//    color = someColor;
//    setStart(X, Y, GameData.SHRAPNEL_SIZE, GameData.SHRAPNEL_SIZE);
//  }
//  
//  public void startDraw(GL10 gl, SpriteBatcher spriteBatcher, Block block)
//  {
//    if (block.shrapnel.isAnimating())
//      block.shrapnel.draw(gl, spriteBatcher);
//  }
  
  public void draw(GL10 gl, SpriteBatcher spriteBatcher)
  {
    if(going)
    { 
      tleftx -= SPEED;
      tlefty -= SPEED;
      
      trightx -= SPEED;
      trighty += SPEED;
      
      bleftx += SPEED;
      blefty -= SPEED;
      
      brightx += SPEED;
      brighty += SPEED;
      
      spriteBatcher.draw(gl, imageID, src, new Rect(tleftx, tlefty, tleftx + width, tlefty + height));
      spriteBatcher.draw(gl, imageID, src, new Rect(trightx, trighty, trightx + width, trighty + height));
      spriteBatcher.draw(gl, imageID, src, new Rect(bleftx, blefty, bleftx + width, blefty + height));
      spriteBatcher.draw(gl, imageID, src, new Rect(brightx, brighty, brightx + width, brighty + height));
      
      count++;
    }
    if (count > MAX_NUM_ITERATIONS)
      resetShrapnel();
  }
}
