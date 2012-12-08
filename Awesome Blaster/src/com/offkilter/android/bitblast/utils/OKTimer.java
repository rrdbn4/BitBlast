package com.offkilter.android.bitblast.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.util.Log;

//OKScheduler (Off Kilter Scheduler)
//Author: Robert Dunn


public class OKTimer implements Runnable
{
  private Thread thread;
  private Object target;
  private String method;
  private Method invocation;
  private float interval, elapsed;
  private boolean isRunning = false;

  //Calls the specified method once then kills the timer
  public void setMethod(Object target, String selector, float seconds)
  {
    this.target = target;
    method = selector;
    interval = seconds;
    
    try
    {
      Class<?> cls = target.getClass();
      invocation = cls.getMethod(method);
    }
    catch (SecurityException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (NoSuchMethodException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public void start()
  {
    if(invocation != null)
    {
      thread = new Thread(this);
      thread.start();
      isRunning = true;
    }
    else
      Log.e("OKTimer.java", "Invocation has not been initialized.");
  }
  
  @Override
  public void run()
  {
    while(isRunning)
    {
      try
      {
        Thread.sleep(1);
      }
      catch (InterruptedException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
      elapsed += 0.001f;
      if(elapsed >= interval)
        invokeMethod();
    }  
  }
  
  private void invokeMethod()
  {
    elapsed = 0;
    interval = 0;
    isRunning = false;
    thread.stop();
    try
    {
      invocation.invoke(target);
    }
    catch (IllegalArgumentException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IllegalAccessException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (InvocationTargetException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
