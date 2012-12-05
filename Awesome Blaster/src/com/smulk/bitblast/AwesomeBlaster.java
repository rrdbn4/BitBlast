// TODO List
// Fix goobler staying red. Requires timer in new thread
// -Add stats
// -Add power-ups such as slow down
// -Add new weapons available for money
// -Make power-ups available for money
// -Add easter eggs
// High score table
// Problem with shrapnel: Uses bitmap of large picture and cuts from that... Spritebatcher needs to take in an image, not an id

package com.smulk.bitblast;

import javax.microedition.khronos.opengles.GL10;

import com.smulk.bitblast.data.GameData;
import com.smulk.bitblast.data.PrefKeys;
import com.smulk.bitblast.gameElements.Block;
import com.smulk.bitblast.gameElements.Goobler;
import com.smulk.bitblast.gameElements.Hero;
import com.smulk.bitblast.gameElements.Mirror;
import com.smulk.bitblast.gameElements.Powerup;
import com.smulk.bitblast.graphics.Drawer;
import com.smulk.bitblast.graphics.GameBackground;
import com.smulk.bitblast.graphics.HUD;
import com.smulk.bitblast.graphics.Images;
import com.smulk.bitblast.graphics.SpriteBatcher;
import com.smulk.bitblast.sound.Sound;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;

public class AwesomeBlaster extends Activity implements SensorEventListener, OnTouchListener, Drawer
{
  //ViewField view;
  GLSurfaceView surface;
  SensorManager accel;
  SharedPreferences data;
  int eventPos;
  
  private GameBackground background;
  private GameData gamedata;
  private Images images;
  private Sound sound;
  
  private Hero hero;
  private Block block[];
  private Goobler goobler;
  private Mirror mirror[];
  private Powerup powerup;
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    requestWindowFeature(Window.FEATURE_NO_TITLE);
	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN);
	getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
			WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    
    background = new GameBackground();
    images = new Images(this);
    gamedata = new GameData(this);
    sound = new Sound();
    
    hero = new Hero(this, images);
    
    block = new Block[GameData.BLOCK_NUM];
    
    for (int i = 0; i < GameData.BLOCK_NUM; i++)
    {
      block[i] = new Block(this, images);
    }
    
    goobler = new Goobler(this);
    
    mirror = new Mirror[GameData.MIRROR_NUM];
        
    for(int i = 0; i < mirror.length; i++)
      mirror[i] = new Mirror(this, images);
    
    
    powerup = new Powerup(this);
    
    
    
    surface = new GLSurfaceView(this);
    setContentView(surface);
    surface.setRenderer(new SpriteBatcher(getResources(), images.getSprites(), this));
    surface.setOnTouchListener(this);
    
    accel = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    if (accel.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0)
    {
      Sensor s = accel.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
      accel.registerListener(this, s, SensorManager.SENSOR_DELAY_GAME);
    }
    setVolumeControlStream(AudioManager.STREAM_MUSIC);
    
    data = getSharedPreferences(PrefKeys.PREFS_ACCEL_KEY, Context.MODE_WORLD_WRITEABLE);
    eventPos = data.getInt(PrefKeys.PREFS_ACCEL_KEY, 0);
    
    SharedPreferences prefs = getSharedPreferences(PrefKeys.PREFS_FILE_KEY, Context.MODE_WORLD_WRITEABLE);
    if(!prefs.getBoolean(PrefKeys.PREFS_STATUSBAR_ENABLED_KEY, true))
    {
       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    gamedata.setPlaySound((prefs.getBoolean(PrefKeys.PREFS_SOUND_FX_ENABLED_KEY, true)));
    gamedata.setPlayMusic((prefs.getBoolean(PrefKeys.PREFS_MUSIC_ENABLED_KEY, true)));
  }
  
  @Override
  protected void onPause()
  {
    super.onPause();
    //view.pause();
    sound.stopMusic(gamedata);
  }
  
  @Override
  protected void onStart()
  {
    super.onStart();
    sound.startMusic(gamedata);
  }
  
  @Override
  protected void onResume()
  {
    super.onResume();
    //view.resume();
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy)
  {
    //do nothing  
  }

  @Override
  public void onSensorChanged(SensorEvent event)
  {
    hero.setChange(event.values[eventPos]);
  }


  @Override
  public boolean onTouch(View arg0, MotionEvent touch)
  {
    if(touch.getAction() == MotionEvent.ACTION_DOWN)
    { 
      /*
      if(gamedata.BBplayPause.isClicked(touch.getX(), touch.getY()))
      {
        if(gamedata.getIsRunning())
          view.pause();
        else
          view.resume();  
      }
      else
      */
        hero.shoot();
    } 
    return true;
  }
  
  //called every frame
  @Override
  public void onDrawFrame(GL10 gl, SpriteBatcher spriteBatcher)
  {
    updateFrameInfo();
    drawResult(gl, spriteBatcher);
  }

  private void updateFrameInfo()
  { 
    hero.update(surface, gamedata, goobler);
    goobler.update(surface, gamedata, hero, images);
    
    for(int i = 0; i < hero.bullets.length; i++)
      hero.bullets[i].update(surface, gamedata, sound);
    
    for(int i = 0; i < goobler.bullets.length; i++)
      goobler.bullets[i].update(surface, gamedata, sound);
 
    for(int i = 0; i < block.length; i++)
      block[i].update(surface, gamedata, hero, sound);
    
    for(int i=0; i < mirror.length; i++)
      mirror[i].update(surface, gamedata, hero);
    
    powerup.update(surface, gamedata, hero);
  }
  
  private void drawResult(GL10 gl, SpriteBatcher spriteBatcher)
  {
    background.draw(gl, spriteBatcher, surface, gamedata, hero);
    hero.draw(gl, spriteBatcher);
    goobler.draw(gl, spriteBatcher);
    
    for(int i = 0; i < hero.bullets.length; i++)
      hero.bullets[i].draw(gl, spriteBatcher);
    
    for(int i = 0; i < goobler.bullets.length; i++)
      goobler.bullets[i].draw(gl, spriteBatcher);
    
    for(int i = 0; i < block.length; i++)
      block[i].draw(gl, spriteBatcher);
    
    for(int i=0; i < mirror.length; i++)
      mirror[i].draw(gl, spriteBatcher);
    
    powerup.draw(gl, spriteBatcher, gamedata, images, hero);
    
    HUD.displayHearts(surface, gl, spriteBatcher, gamedata, images, hero);
    //HUD.displayPauseButton(gl, spriteBatcher, gamedata, images);
  }
}
