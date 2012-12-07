package com.offkilter.android.bitblast.activities;

import com.offkilter.android.bitblast.data.PrefKeys;
import com.offkilter.android.bitblast.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends Activity
{
  SharedPreferences data;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.splash);

    SharedPreferences prefs = getSharedPreferences(PrefKeys.PREFS_FILE_KEY,
        Context.MODE_WORLD_WRITEABLE);
    if (!prefs.getBoolean(PrefKeys.PREFS_STATUSBAR_ENABLED_KEY, true))
    {
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
          WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    Thread thread = new Thread()
    {
      public void run()
      {
        // TODO: uncomment sleep statement when product is finalized
        /*
         * try { sleep(3000); } catch (InterruptedException e) {
         * e.printStackTrace(); }
         */
        data = getSharedPreferences(PrefKeys.FIRST_RUN_FILE_KEY,
            Context.MODE_PRIVATE);
        boolean beenRanBefore = data.getBoolean(PrefKeys.FIRST_RUN_AXIS_KEY,
            false);

        if (beenRanBefore)
        {
          startActivity(new Intent(SplashScreen.this, Begin.class));
          finish();
        }
        else
        {
          SharedPreferences.Editor editor = data.edit();
          editor.putBoolean(PrefKeys.FIRST_RUN_AXIS_KEY, true);
          editor.commit();

          // startActivity(new Intent(SplashScreen.this, AxisChooser.class));
          finish();
        }
      }
    };
    thread.start();

    // startActivity(new Intent(this, AxisChooser.class));
    // finish();
  }

}
