package com.offkilter.android.bitblast.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.offkilter.android.bitblast.data.PrefKeys;
import com.offkilter.android.bitblast.R;

public class HowToGuide extends Activity implements OnClickListener
{
  MediaPlayer mp = null;
  Button bReturn;
  boolean playSound = false;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.how_to_guide);

    bReturn = (Button) findViewById(R.id.bReturnToMenu);
    bReturn.setOnClickListener(this);

    SharedPreferences prefs = getSharedPreferences(PrefKeys.PREFS_FILE_KEY,
        Context.MODE_WORLD_WRITEABLE);
    if (!prefs.getBoolean(PrefKeys.PREFS_STATUSBAR_ENABLED_KEY, true))
    {
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
          WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    playSound = prefs.getBoolean(PrefKeys.PREFS_MUSIC_ENABLED_KEY, true);
  }

  public void startMusic()
  {
    if (playSound)
    {
      mp = MediaPlayer.create(this, R.raw.menu);
      mp.start();
      mp.setLooping(true);
    }
  }

  public void stopMusic()
  {
    if (playSound && mp != null)
    {
      mp.stop();
      mp.release();
      mp = null;
    }
  }

  @Override
  protected void onPause()
  {
    super.onPause();
    stopMusic();
  }

  @Override
  protected void onStart()
  {
    super.onStart();
    startMusic();
  }

  @Override
  public void onClick(View v)
  {
    startActivity(new Intent(this, Begin.class));
    finish();
  }

}
