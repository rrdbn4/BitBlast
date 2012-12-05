package com.smulk.bitblast.activities;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.smulk.bitblast.R;

import com.smulk.bitblast.AwesomeBlaster;
import com.smulk.bitblast.data.GameData;
import com.smulk.bitblast.data.PrefKeys;
import com.smulk.bitblast.gameElements.Bullet;
import com.smulk.bitblast.graphics.Shrapnel;
import com.smulk.bitblast.utils.BBButton;

public class Begin extends Activity implements OnTouchListener
{
	ArtWork view;
	private BBButton BBStart, BBOptions, BBFeedback, BBBanner, BBURL, BBHowTo;
	MediaPlayer mp;
	boolean playSound;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		view = new ArtWork(this);
		view.setOnTouchListener(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(view);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		BBStart = new BBButton();
		BBOptions = new BBButton();
		BBFeedback = new BBButton();
		BBBanner = new BBButton();
		BBURL = new BBButton();
		BBHowTo = new BBButton();
		
		SharedPreferences prefs = getSharedPreferences(PrefKeys.PREFS_FILE_KEY, Context.MODE_WORLD_WRITEABLE);
		if(!prefs.getBoolean(PrefKeys.PREFS_STATUSBAR_ENABLED_KEY, true))
		{
			 getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		playSound = prefs.getBoolean(PrefKeys.PREFS_MUSIC_ENABLED_KEY, true);
		
		SharedPreferences seenBefore = getSharedPreferences(PrefKeys.FIRST_RUN_FILE_KEY, Context.MODE_PRIVATE);
		if(!seenBefore.getBoolean(PrefKeys.FIRST_RUN_HELP_MESSAGE_KEY, false))
		{
			SharedPreferences.Editor editor = seenBefore.edit();
			editor.putBoolean(PrefKeys.FIRST_RUN_HELP_MESSAGE_KEY, true);
			editor.commit();
			showHelpDialog();
		}
	}
	
	public void startMusic()
	{
		if(playSound)
		{	
			mp = MediaPlayer.create(this, R.raw.menu);
			mp.start();
			mp.setLooping(true);
		}
	}
	public void stopMusic()
	{
		if(playSound && mp != null)
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
		view.pause();
		stopMusic();
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		startMusic();
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		if(event.getAction() == MotionEvent.ACTION_UP)
		{	
			if(BBStart.isClicked(event.getX(), event.getY()))
			{
				startActivity(new Intent(this, AwesomeBlaster.class));
				//TODO: Find out why is is a null pointer when going from options menu to in game
				//if(playSound)
					//mp.stop();
        finish();
			}
			else if (BBOptions.isClicked(event.getX(), event.getY()))
			{
				startActivity(new Intent(this, Options.class));
			}
			else if (BBFeedback.isClicked(event.getX(), event.getY()))
			{	
				startActivity(new Intent(this, Feedback.class));
			}
			else if(BBURL.isClicked(event.getX(), event.getY()))
			{
				String url = "http://www.dunnrightsoftware.com";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
			else if (BBHowTo.isClicked(event.getX(), event.getY()))
			{
				startActivity(new Intent(this, HowToGuide.class)); 
			}
		}
		return true;
	}

	

	@Override
	protected void onResume()
	{
		super.onResume();
		view.resume();
	}

	public void showHelpDialog()
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.help_dialog, null);
        TextView tv = (TextView) view.findViewById(R.id.tvHelp);
        String string = new String("BitBlast is made possible by your contributions.\nIf you experience any crashes, freezes, "
        		+ "misbehavior, or you just have a feature request, please don't hesitate to send feedback using the button on the menu."
        		+ " If BitBlast stops unexpectedly, please send the error report. Only you can make BitBlast great."
        		+ "\n\nThis message will not be shown again.");
        dialog.setView(view);
        tv.setText(string);
        dialog.setPositiveButton(
                "Dismiss",
                new DialogInterface.OnClickListener() 
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                    	dialog.dismiss();			                    }
	                });
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface dialog)
			{
				if (dialog != null)
				{
					dialog.cancel();
				}
			}
		});
		dialog.show();
	}

	/*******NESTED CONTENT VIEW***********/
	public class ArtWork extends SurfaceView implements Runnable
	{
		private final int BULLET_NUM = 3;
		private final int SPEED = 10;
		//Shrapnel blasts[];
		Random rand;
		private Bitmap button, ship, bullet, optionsButton, feedbackButton, banner, URLButton, howtoButton;
		private Bullet bullets[];
		private int counter = 0;
		public boolean isRunning = false;
		private Thread thread = null;
		private SurfaceHolder holder;
		
		public ArtWork(Context context)
		{
			super(context);
			
	    rand = new Random();
	    //Paint randomColor = new Paint();
			//blasts = new Shrapnel[GameData.SHRAPNEL_DECOR_NUM];
			
	     /*
			int menuBlastColors[] = 
			{
			  Color.RED,
			  Color.WHITE,
			  Color.BLUE
			};
			
			for(int i=0; i  < blasts.length; i++)
			{
				blasts[i] = new Shrapnel(menuBlastColors[rand.nextInt(menuBlastColors.length)]);
			}
			*/
			
			bullets = new Bullet[BULLET_NUM];
			for(int i=0; i < bullets.length; i++)
				bullets[i] = new Bullet(context, GameData.HERO_BULLET_SPEED, -1);
			
			button = BitmapFactory.decodeResource(getResources(), R.drawable.begin);
			ship = BitmapFactory.decodeResource(getResources(), R.drawable.ship);
			bullet = BitmapFactory.decodeResource(getResources(), R.drawable.bullet);
			optionsButton = BitmapFactory.decodeResource(getResources(), R.drawable.options_button);
			feedbackButton = BitmapFactory.decodeResource(getResources(), R.drawable.feedback_button);
			banner = BitmapFactory.decodeResource(getResources(), R.drawable.banner);
			URLButton = BitmapFactory.decodeResource(getResources(), R.drawable.view_website_button);
			howtoButton = BitmapFactory.decodeResource(getResources(), R.drawable.how_to_button);
			holder = getHolder();
		}

		public void pause()
		{
			isRunning = false;
			try
			{
				thread.join();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		public void resume()
		{
			isRunning = true;
			thread = new Thread(this);
			thread.start();
		}
		
		public void run()
		{
			while(isRunning)
			{
				if (!holder.getSurface().isValid())
					continue;
				Canvas canvas = holder.lockCanvas();
				final int buttonTop = (int)(getHeight()*0.3);
				final int masterHeight = button.getHeight();
				final int height = optionsButton.getHeight();
				final int shipTop = getHeight() - (ship.getHeight() + (ship.getHeight()/8));
				final int bannerTop = (int)(getHeight()*0.01);
				
				BBBanner.create(canvas.getWidth()/2 - banner.getWidth()/2, bannerTop, banner);
				BBStart.create((canvas.getWidth()/2)-(button.getWidth()/2), buttonTop, button);
				BBHowTo.create(getWidth()/2 - howtoButton.getWidth()/2, buttonTop + masterHeight, howtoButton);
				BBOptions.create(canvas.getWidth()/2 - optionsButton.getWidth()/2, buttonTop + masterHeight + height, optionsButton);
				BBFeedback.create(canvas.getWidth()/2 - feedbackButton.getWidth()/2, buttonTop + masterHeight + height*2, feedbackButton);
				BBURL.create(canvas.getWidth()/2 - URLButton.getWidth()/2, buttonTop + masterHeight + height*3, URLButton);
				
				canvas.drawRGB(0, 25, 100);
				counter++;	
				
				/*
				for(int i=0; i < SHRAPNEL_NUM; i++)
				{
					if(!blasts[i].isAnimating())
					{
						if(rand.nextInt(10)==0)
						{
							blasts[i].setStart(rand.nextInt(canvas.getWidth()), rand.nextInt(getHeight()), 7, 7);
						}
					}
					else
					{
						blasts[i].draw(canvas);
					}
				}
				*/
				
				for(int i=0; i < bullets.length; i++)
				{
					if(bullets[i].isMoving())
					{
						if(bullets[i].getmY() - SPEED < buttonTop)
						  bullets[i].resetBullet();
						else
						{	
						  bullets[i].setmY(bullets[i].getmY() - SPEED);
							canvas.drawBitmap(bullet, bullets[i].getmX(), bullets[i].getmY(), null);
						}	
					}
					else
					{
						if(counter > 20)
						{
							counter = 0;
							bullets[i].startMoving();
							bullets[i].setmY(shipTop);
							bullets[i].setmX((canvas.getWidth()/2)-(bullet.getWidth()/2));
						}
					}
				}	
				canvas.drawBitmap(ship, (canvas.getWidth()/2)-(ship.getWidth()/2), shipTop, null);
				canvas.drawBitmap(button, BBStart.getLeft(), BBStart.getTop(), null);
				canvas.drawBitmap(howtoButton, BBHowTo.getLeft(), BBHowTo.getTop(), null);
				canvas.drawBitmap(optionsButton, BBOptions.getLeft(), BBOptions.getTop(), null);
				canvas.drawBitmap(feedbackButton, BBFeedback.getLeft(), BBFeedback.getTop(), null);
				canvas.drawBitmap(URLButton, BBURL.getLeft(), BBURL.getTop(), null);
				canvas.drawBitmap(banner, BBBanner.getLeft(), BBBanner.getTop(), null);
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}
}	

