package com.offkilter.android.bitblast.graphics;

import javax.microedition.khronos.opengles.GL10;

public interface Drawer {
  //An object implementing this interface must be handed to SpriteBatcher
  //when it is created. The onDrawFrame method of SpriteBatcher automatically
  //calls the method onDrawFrame() of the object implementing Drawer.
  
  //All draw calls onto SpriteBatcher should be made from within this method.
  
  public void onDrawFrame(GL10 gl, SpriteBatcher spriteBatcher);

}
