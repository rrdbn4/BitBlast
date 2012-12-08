// TODO List
// ======= Bugs =======
// Problem with shrapnel: Uses bitmap of large picture and cuts from that... Spritebatcher needs to take in an image, not an id for more flexibility
//
// ==Requires timer in new thread==
// - Powerups lasting forever
// ====================

// ======= New Features =======
// - Level system. Progressively gets harder
// - Upgrade screen. Can buy new weapons, etc. Money based on score
// - Ability to buy temporary powerups
// - Highscore table
// ============================

// ======= Powerups =======
// - Slow down should replace freeze
// ========================

package com.offkilter.android.bitblast;



import javax.microedition.khronos.opengles.GL10;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

import com.offkilter.android.bitblast.data.GameData;
import com.offkilter.android.bitblast.data.PrefKeys;
import com.offkilter.android.bitblast.game_elements.Block;
import com.offkilter.android.bitblast.game_elements.Hero;
import com.offkilter.android.bitblast.game_elements.Mirror;
import com.offkilter.android.bitblast.game_elements.Powerup;
import com.offkilter.android.bitblast.game_elements.bosses.Burrak;
import com.offkilter.android.bitblast.graphics.Drawer;
import com.offkilter.android.bitblast.graphics.GameBackground;
import com.offkilter.android.bitblast.graphics.HUD;
import com.offkilter.android.bitblast.graphics.Images;
import com.offkilter.android.bitblast.graphics.SpriteBatcher;
import com.offkilter.android.bitblast.sound.Sound;


public class BitBlast extends Activity implements SensorEventListener, OnTouchListener, Drawer
{
  GLSurfaceView surface;
  SensorManager accel;
  SharedPreferences data;
  int eventPos;

  private GameBackground background;
  private GameData gamedata;
  private Images images;
  private Sound sound;
  //private OKTimer timer;

  private Hero hero;
  private Block block[];
  private Burrak burrakBoss;
  private Mirror mirror[];
  private Powerup powerup;
  private HUD hud;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    initialize();

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
    if (!prefs.getBoolean(PrefKeys.PREFS_STATUSBAR_ENABLED_KEY, true))
    {
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    gamedata.setPlaySound((prefs.getBoolean(PrefKeys.PREFS_SOUND_FX_ENABLED_KEY, true)));
    gamedata.setPlayMusic((prefs.getBoolean(PrefKeys.PREFS_MUSIC_ENABLED_KEY, true)));
  }

  private void initialize()
  {
    background = new GameBackground();
    gamedata = new GameData(this);
    images = new Images(this);
    sound = new Sound();
    //timer = new OKTimer();

    hero = new Hero(this, images);

    block = new Block[GameData.BLOCK_NUM];
    for (int i = 0; i < GameData.BLOCK_NUM; i++)
      block[i] = new Block(this, images);

    burrakBoss = new Burrak(this);

    mirror = new Mirror[GameData.MIRROR_NUM];
    for (int i = 0; i < mirror.length; i++)
      mirror[i] = new Mirror(this, images);

    powerup = new Powerup(this);
    hud = new HUD(this);

    surface = new GLSurfaceView(this);
    setContentView(surface);
    surface.setRenderer(new SpriteBatcher(getResources(), images.getSprites(), this));
    surface.setOnTouchListener(this);
    
    //HOW TO USE TIMER
    //timer.setMethodToCall(this, "heroShoot", 0.2f);
    //timer.startTimer();
  }

  @Override
  protected void onPause()
  {
    super.onPause();
    // view.pause();
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
    // view.resume();
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy)
  {
    // do nothing
  }

  @Override
  public void onSensorChanged(SensorEvent event)
  {
    hero.setChange(event.values[eventPos]);
  }

  @Override
  public boolean onTouch(View arg0, MotionEvent touch)
  {
    if (touch.getAction() == MotionEvent.ACTION_DOWN)
    {
      if (gamedata.BBplayPause.isClicked(touch.getX(), touch.getY()))
      {
        if (gamedata.getIsRunning())
        {
          // view.pause();
        }
        else
        {
          // view.resume();
        }
      }
      else
        hero.shoot();
    }
    return true;
  }

  // called every frame
  @Override
  public void onDrawFrame(GL10 gl, SpriteBatcher spriteBatcher)
  {
    updateFrameInfo();
    drawResult(gl, spriteBatcher);
  }

  private void updateFrameInfo()
  {
    hero.update(surface, gamedata);
    burrakBoss.update(surface, gamedata, hero, images);
    
    for (int i = 0; i < hero.bullets.length; i++)
      hero.bullets[i].update(surface, gamedata, sound, burrakBoss, hero, block, mirror);

    for (int i = 0; i < burrakBoss.bullets.length; i++)
      burrakBoss.bullets[i].update(surface, gamedata, sound, burrakBoss, hero, block, mirror);

    for (int i = 0; i < block.length; i++)
      block[i].update(surface, gamedata, hero, sound);

    for (int i = 0; i < mirror.length; i++)
      mirror[i].update(surface, gamedata, hero);

    powerup.update(surface, gamedata, hero);
    gamedata.incrementGapCounter();
  }

  private void drawResult(GL10 gl, SpriteBatcher spriteBatcher)
  {
    background.draw(gl, spriteBatcher, surface, gamedata, hero);
    hero.draw(gl, spriteBatcher);
    burrakBoss.draw(gl, spriteBatcher);
    
    for (int i = 0; i < hero.bullets.length; i++)
      hero.bullets[i].draw(gl, spriteBatcher);

    for (int i = 0; i < burrakBoss.bullets.length; i++)
      burrakBoss.bullets[i].draw(gl, spriteBatcher);

    for (int i = 0; i < block.length; i++)
      block[i].draw(gl, spriteBatcher);

    for (int i = 0; i < mirror.length; i++)
      mirror[i].draw(gl, spriteBatcher);

    powerup.draw(gl, spriteBatcher, gamedata, images, hero);

    hud.displayHearts(surface, gl, spriteBatcher, gamedata, hero);
    // HUD.displayPauseButton(gl, spriteBatcher, gamedata, images);
  }
}
