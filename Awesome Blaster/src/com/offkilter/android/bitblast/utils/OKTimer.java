package com.offkilter.android.bitblast.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.util.Log;

public class OKTimer implements Runnable
{
  private Object target;
  private String selector;
  private Method invocation;
  private float interval = 0;
  private float counter = 0;
  private Thread thread;
  private boolean isRunning = false;

  
  public OKTimer(Object targ, String method, float seconds)
  {
    interval = seconds;
    selector = method;
    target = targ;
    
    try
    {
      Class<?> cls = target.getClass();
      invocation = cls.getMethod(selector);
    }
    catch (NoSuchMethodException e)
    {
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
      Log.e("OKScheduler.java", "Invocation has not been initialized.");
  }
  
  public void stop()
  {
    if(thread != null)
    {
      isRunning = false;
    }  
  }
  
  public void setInterval(float seconds)
  {
    interval = seconds;
  }
  
  public void setMethod(Object targ, String method)
  {
    selector = method;
    target = targ;
    
    try
    {
      Class<?> cls = target.getClass();
      invocation = cls.getMethod(selector);
    }
    catch (NoSuchMethodException e)
    {
      e.printStackTrace();
    }
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
        e.printStackTrace();
      }
      
      counter += 0.001f;
      if(counter >= interval)
      {
        counter = 0;
        invokeMethod();
      }
    }
  }
  
  private void invokeMethod()
  {
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
    stop();
  }
}
