package com.smulk.bitblast.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.smulk.bitblast.R;
import com.smulk.bitblast.data.GameData;

public class Sound
{
  private SoundPool bang = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
  private int explosion;
  private SoundPool blowUp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
  private int blockExplosion;
  private MediaPlayer mediaplayer;
  
  public Sound()
  {
    explosion = bang.load(GameData.leContext, R.raw.bang, 1);
    blockExplosion = blowUp.load(GameData.leContext, R.raw.blow_up, 1);
  }
  
	public boolean play(Context context, int resId)
	{
		SoundPool pool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		int sound = pool.load(context, resId, 0);
		if(sound != 0)
			pool.play(sound, 1, 1, 0, 0, 1);
		else
			return false;
		return true;
	}
	
  public void startMusic(GameData gamedata)
  {
    if(gamedata.getPlayMusic())
    { 
      mediaplayer = MediaPlayer.create(GameData.leContext, R.raw.random_song);
      mediaplayer.start();
      mediaplayer.setLooping(true);
    }
  }
  public void stopMusic(GameData gamedata)
  {
    if(gamedata.getPlayMusic() && mediaplayer != null)
    {
      mediaplayer.stop();
      mediaplayer.release();
      mediaplayer = null;
    }
  }

  public int getBlockExplosion()
  {
    return blockExplosion;
  }
  public int getExplosion()
  {
    return explosion;
  }
  public SoundPool getBang()
  {
    return bang;
  }
  public SoundPool getBlowUp()
  {
    return blowUp;
  }
}
