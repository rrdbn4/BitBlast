package com.smulk.bitblast.activities;

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
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import com.smulk.bitblast.R;

import com.smulk.bitblast.data.GameData;
import com.smulk.bitblast.data.PrefKeys;
import com.smulk.bitblast.gameElements.Bullet;
import com.smulk.bitblast.gameElements.Goobler;
import com.smulk.bitblast.graphics.Shrapnel;
import com.smulk.bitblast.utils.BBButton;

public class DeathScreen extends Activity implements OnTouchListener, OnCompletionListener
{
	ArtWork view;
	private int endingScore;
	SharedPreferences highScoreData;
	int highScore;
	boolean isHighScore = false;
	boolean playMusic = false;
	boolean isVisible = false;
	boolean musicStarted = false;
	BBButton bbcontinue = new BBButton();
	BBButton bbChangeName = new BBButton();
	MediaPlayer mp = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		view = new ArtWork(this);
		view.setOnTouchListener(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(view);
		highScoreData = getSharedPreferences(PrefKeys.HIGH_SCORE_FILE_KEY, Context.MODE_WORLD_READABLE);
		
		Bundle extras = getIntent().getExtras();
		endingScore = extras.getInt(GameData.getScoreKey());
		highScore = highScoreData.getInt(PrefKeys.HIGH_SCORE_NUM_KEY, 0);
		
		if(endingScore >= highScore)
		{
			isHighScore = true;
			SharedPreferences.Editor editor = highScoreData.edit();
			editor.putInt(PrefKeys.HIGH_SCORE_NUM_KEY, endingScore);
			editor.commit();
		}
		
		SharedPreferences prefs = getSharedPreferences(PrefKeys.PREFS_FILE_KEY, Context.MODE_WORLD_WRITEABLE);
		playMusic = prefs.getBoolean(PrefKeys.PREFS_MUSIC_ENABLED_KEY, true);
		
		if(isHighScore && playMusic)
		{
			MediaPlayer mp = MediaPlayer.create(this, R.raw.tada);
			mp.start();
			mp.setOnCompletionListener(this);
		}
		else if(playMusic)
		{
			MediaPlayer mp = MediaPlayer.create(this, R.raw.wawa);
			mp.start();
			mp.setOnCompletionListener(this);
		}
		
		if(!prefs.getBoolean(PrefKeys.PREFS_STATUSBAR_ENABLED_KEY, true))
		{
			 getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}		
	}
	
	@Override
	public void onCompletion(MediaPlayer media)
	{
		if(playMusic && isVisible)
		{	
			stopMusic();
			startMusic();
			musicStarted = true;
		}	
	}
	
	public void startMusic()
	{
		if(playMusic)
		{	
			mp = MediaPlayer.create(this, R.raw.end_game);
			mp.start();
			mp.setLooping(true);
		}
	}
	public void stopMusic()
	{
		if(playMusic && mp != null)
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
		isVisible = false;
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		if(musicStarted)
			startMusic();
		isVisible = true;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		float x = event.getX();
		float y = event.getY();
		if(event.getAction() == MotionEvent.ACTION_UP)
		{	
			if(bbcontinue.isClicked(x, y))
			{	
				startActivity(new Intent(this, Begin.class));
				finish();
			}	
			else if(bbChangeName.isClicked(event.getX(), event.getY()))
			{
				AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		        final View view = View.inflate(this, R.layout.name_entry, null);
		        final TextView name = (TextView) view.findViewById(R.id.etScoreName);
		        dialog.setView(view);
		        dialog.setPositiveButton(
		                "Set Name",
		                new DialogInterface.OnClickListener() 
		                {
		                    @Override
		                    public void onClick(DialogInterface dialog, int which)
		                    {
		                        SharedPreferences data = getSharedPreferences(PrefKeys.HIGH_SCORE_FILE_KEY, Context.MODE_WORLD_READABLE);
		                        SharedPreferences.Editor editor = data.edit();
		                		editor.putString(PrefKeys.HIGH_SCORE_NAME_KEY, name.getText().toString());
		                		editor.commit();
		                    }
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
		}
		return true;
	}

	/*******NESTED CONTENT VIEW***********/
	public class ArtWork extends View
	{
		private final int BULLET_NUM = 3;
		private int counter = 0;
		private int counter2 = 0;
		private boolean countingDown, firstRun = true;
		private Bitmap ship, enemy, bullet;
		private Bitmap continueBut = BitmapFactory.decodeResource(getResources(), R.drawable.continue_button);
		private Goobler goobler;
		//private Shrapnel blasts[];
		private Bullet bullets[];
		
		
		public ArtWork(Context context)
		{
			super(context);
			
			countingDown = false;
			bullets = new Bullet[BULLET_NUM];
			for(int i=0; i < bullets.length; i++)
			  bullets[i] = new Bullet(context, GameData.GOOBLER_BULLET_SPEED, -1);
			
			/*
			blasts = new Shrapnel[2];
			
			for(int i = 0; i < 2; i++)
			{
				blasts[i] = new Shrapnel(Color.WHITE);
			}
			*/
			
			goobler = new Goobler(GameData.leContext);
			ship = BitmapFactory.decodeResource(getResources(), R.drawable.ship);
			enemy = BitmapFactory.decodeResource(getResources(), R.drawable.goobler_mad);
			bullet = BitmapFactory.decodeResource(getResources(), R.drawable.bullet);
		}

		@Override
		protected void onDraw(Canvas canvas)
		{
			if(firstRun)
			{	
				bbcontinue.create(canvas.getWidth() - continueBut.getWidth(), getHeight() - continueBut.getHeight(), continueBut);
				firstRun = false;
			}
			counter2++;
			counter += (countingDown? -4: 4);
			if (counter > 250)
				countingDown = true;
			if(counter <= 50)
				countingDown = false;
			
			super.onDraw(canvas);
			canvas.drawARGB(50, counter, 50, 50);
			Paint paint = new Paint(); //TODO fix
			paint.setColor(Color.WHITE);
			paint.setTextSize(50);
			paint.setTextAlign(Align.CENTER);
			
			for(int i=0; i < bullets.length; i++)
			{
				if(bullets[i].isMoving())
				{
					if(bullets[i].getmY() > (getHeight() - ship.getHeight() - 10))
					  bullets[i].resetBullet();
					else
					{	
					  bullets[i].setmY(bullets[i].getmY()+ 10);
						canvas.drawBitmap(bullet, bullets[i].getmX(), bullets[i].getmY(), null);
					}	
				}
				else
				{
					if(counter2 > 50)
					{
						counter2 = 0;
						bullets[i].startMoving();
						bullets[i].setmX((canvas.getWidth()/2 - bullet.getWidth()/2));
						bullets[i].setmY((goobler.getmY() + enemy.getHeight()));
					}
				}
			}
			
			if(isHighScore)
			{
			  /*
				if(blasts[0].isAnimating())
				{
					//blasts[0].draw(canvas);
				}
				else
				{
				  blasts[0].setColor(Color.RED);
					blasts[0].setStart(canvas.getWidth()/2 - 100, getHeight()/2, 10, 10);
				}
				
				if(blasts[1].isAnimating())
				{
					//blasts[1].draw(canvas);
				}
				else
				{
				  blasts[1].setColor(Color.RED);
					blasts[1].setStart(canvas.getWidth()/2 + 100, getHeight()/2, 10, 10);
				}
				*/
				
				canvas.drawText("NEW HIGHSCORE!", canvas.getWidth()/2, getHeight()/2, paint);
				canvas.drawText("by: " + highScoreData.getString(PrefKeys.HIGH_SCORE_NAME_KEY, "Enter Name"), canvas.getWidth()/2, 
						getHeight()/2 + paint.getTextSize(), paint);
				paint.setTextSize(20);
				canvas.drawText("(Tap to change)", canvas.getWidth()/2, getHeight()/2 + paint.getTextSize()*5, paint);
				paint.setTextSize(50);
				
				bbChangeName.create(0, canvas.getWidth(), getHeight()/2 - (int)paint.getTextSize()*3, getHeight()/2 + (int)paint.getTextSize()*3);
			}
			else
			{
				canvas.drawText("HighScore to beat: "+ highScore, canvas.getWidth()/2, getHeight()/2, paint);
				canvas.drawText("Set By: "+ highScoreData.getString(PrefKeys.HIGH_SCORE_NAME_KEY, "Nobody"), canvas.getWidth()/2, 
						getHeight()/2 + paint.getTextSize(), paint);
			}
			
			goobler.setmX((canvas.getWidth()/2) - (enemy.getWidth()/2));
			//TODO: Uncomment when implemented properly
			//goobler.bounce(canvas, enemy, 3, (int)(getHeight()*0.7), (int)(getHeight()*0.1));
			canvas.drawBitmap(ship, (canvas.getWidth()/2) - (ship.getWidth()/2), getHeight() - ship.getHeight() - 10, null);
			canvas.drawText("YOU DIED", canvas.getWidth()/2, paint.getTextSize(), paint);
			canvas.drawText("Final Score: "+endingScore, canvas.getWidth()/2, paint.getTextSize()*2, paint);
			canvas.drawBitmap(continueBut, canvas.getWidth() - continueBut.getWidth(), getHeight() - continueBut.getHeight(), null);
			
			invalidate();
		}
	}
}
