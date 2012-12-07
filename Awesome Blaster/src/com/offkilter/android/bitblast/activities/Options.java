package com.offkilter.android.bitblast.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;

import com.offkilter.android.bitblast.data.PrefKeys;
import com.offkilter.android.bitblast.utils.BBButton;
import com.offkilter.android.bitblast.R;


public class Options extends Activity implements OnTouchListener
{
  ViewField view;
  BBButton musicSwitch, soundSwitch, statusbarSwitch, backButton;
  SharedPreferences prefs;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    view = new ViewField(this);
    view.setOnTouchListener(this);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(view);

    musicSwitch = new BBButton();
    soundSwitch = new BBButton();
    statusbarSwitch = new BBButton();
    backButton = new BBButton();

    prefs = getSharedPreferences(PrefKeys.PREFS_FILE_KEY,
        Context.MODE_WORLD_WRITEABLE);
    if (!prefs.getBoolean(PrefKeys.PREFS_STATUSBAR_ENABLED_KEY, true))
    {
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
          WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
  }

  @Override
  public boolean onTouch(View v, MotionEvent event)
  {
    int x = (int) event.getX();
    int y = (int) event.getY();
    if (event.getAction() == MotionEvent.ACTION_UP)
    {
      if (musicSwitch.isClicked(x, y))
      {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PrefKeys.PREFS_MUSIC_ENABLED_KEY,
            !prefs.getBoolean(PrefKeys.PREFS_MUSIC_ENABLED_KEY, true));
        editor.commit();
      } else if (soundSwitch.isClicked(x, y))
      {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PrefKeys.PREFS_SOUND_FX_ENABLED_KEY,
            !prefs.getBoolean(PrefKeys.PREFS_SOUND_FX_ENABLED_KEY, true));
        editor.commit();
      } else if (statusbarSwitch.isClicked(x, y))
      {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PrefKeys.PREFS_STATUSBAR_ENABLED_KEY,
            !prefs.getBoolean(PrefKeys.PREFS_STATUSBAR_ENABLED_KEY, true));
        editor.commit();

        if (!prefs.getBoolean(PrefKeys.PREFS_STATUSBAR_ENABLED_KEY, true))
        {
          getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
              WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
      } else if (backButton.isClicked(x, y))
      {
        finish();
      }
    }
    return true;
  }

  public class ViewField extends View
  {
    Bitmap back, offSelect, onSelect;
    Paint textattr;

    public ViewField(Context context)
    {
      super(context);

      back = BitmapFactory.decodeResource(getResources(),
          R.drawable.back_button);
      offSelect = BitmapFactory.decodeResource(getResources(),
          R.drawable.switch_off_select);
      onSelect = BitmapFactory.decodeResource(getResources(),
          R.drawable.switch_on_select);

      textattr = new Paint();
      textattr.setColor(Color.WHITE);
      textattr.setTextAlign(Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
      super.onDraw(canvas);
      canvas.drawARGB(155, 50, 100, 200);
      final int height = onSelect.getHeight();
      final int spacing = height / 8;
      textattr.setTextSize((int) (height * 0.8));

      backButton.create(0, getHeight() - back.getHeight(), back);
      musicSwitch.create(canvas.getWidth() / 2 - onSelect.getWidth() / 2,
          height * 2, onSelect);
      soundSwitch.create(canvas.getWidth() / 2 - onSelect.getWidth() / 2,
          height * 5, onSelect);
      statusbarSwitch.create(canvas.getWidth() / 2 - onSelect.getWidth() / 2,
          height * 8, onSelect);

      canvas.drawBitmap(back, 0, getHeight() - back.getHeight(), null);

      canvas.drawText("Music Enabled:", canvas.getWidth() / 2, height * 2
          - spacing, textattr);
      canvas.drawBitmap(prefs
          .getBoolean(PrefKeys.PREFS_MUSIC_ENABLED_KEY, true) ? onSelect
          : offSelect, canvas.getWidth() / 2 - onSelect.getWidth() / 2,
          height * 2, null);
      canvas.drawText("Sound Effects Enabled:", canvas.getWidth() / 2, height
          * 5 - spacing, textattr);
      canvas.drawBitmap(prefs.getBoolean(PrefKeys.PREFS_SOUND_FX_ENABLED_KEY,
          true) ? onSelect : offSelect,
          canvas.getWidth() / 2 - onSelect.getWidth() / 2, height * 5, null);
      canvas.drawText("Status Bar Enabled:", canvas.getWidth() / 2, height * 8
          - spacing, textattr);
      canvas.drawBitmap(prefs.getBoolean(PrefKeys.PREFS_STATUSBAR_ENABLED_KEY,
          true) ? onSelect : offSelect,
          canvas.getWidth() / 2 - onSelect.getWidth() / 2, height * 8, null);

      invalidate();
    }
  }
}
